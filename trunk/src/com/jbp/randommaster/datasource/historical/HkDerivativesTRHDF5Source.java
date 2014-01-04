package com.jbp.randommaster.datasource.historical;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

import com.google.code.jyield.Generator;
import com.google.code.jyield.YieldUtils;
import com.google.code.jyield.Yieldable;
import com.jbp.randommaster.hdf5builders.HkDerivativesTRHDF5Builder;

/**
 * Data source to load Hk Derivatives TR data from HDF5.
 *
 */
public class HkDerivativesTRHDF5Source extends AutoCloseableHistoricalDataSource<HkDerivativesTR> {

	static Logger log = Logger.getLogger(HkDerivativesTRHDF5Source.class);

	private String[] hdf5Filenames;
	private LocalDate tradeDate;
	private String instrumentType;
	private String underlyingName;

	/**
	 * Create an instance of HkDerivativesTRHDF5Source.
	 * 
	 * @param hdf5Filename The source hdf5 file.
	 * @param tradeDate The trade date of the data to be loaded.
	 * @param instrumentType The instrument type such as "Futures" or "Options" etc
	 * @param underlyingName The underlying name such as HSI or HHI etc
	 */
	public HkDerivativesTRHDF5Source(String hdf5Filename, LocalDate tradeDate, String instrumentType, String underlyingName) {
		this.hdf5Filenames = new String[] { hdf5Filename };
		this.tradeDate = tradeDate;
		this.instrumentType = instrumentType;
		this.underlyingName = underlyingName;
	}
	
	/**
	 * Create an instance of HkDerivativesTRHDF5Source by an array of sorted hdf5 files.
	 * 
	 * @param hdf5Filenames An array of hdf5 files. Note that we assume the files are sorted by the trade timestamp and there is no overlapping of time between two files.
	 * @param instrumentType The instrument type that we are loading.
	 * @param underlyingName The underlying name such as HSI or HHI.
	 */
	public HkDerivativesTRHDF5Source(String[] hdf5Filenames, String instrumentType, String underlyingName) {
		this.hdf5Filenames = hdf5Filenames;
		this.instrumentType = instrumentType;
		this.underlyingName = underlyingName;
		this.tradeDate = null;
	}
	
	public String getInstrumentType() {
		return instrumentType;
	}

	/**
	 * Implementation of AutoCloseableHistoricalDataSource.
	 */
	@Override
	protected AutoCloseableIterator<HkDerivativesTR> getDataIterator() {
		return new InputFileIterator();
	}
	
	private class InputFileIterator implements AutoCloseableIterator<HkDerivativesTR> {

		private Map<String, H5File> filesMap;
		// use array here to improve the performance. should be exactly matching the size of the parent class hdf5Filenames array
		@SuppressWarnings("rawtypes")
		private Iterator[] iteratorsMap; 
		
		// to keep track of the current file that we are loading.
		private int currentFileIndex;
		
		public InputFileIterator() {

			filesMap = new HashMap<>();
			iteratorsMap = new Iterator[hdf5Filenames.length];
			currentFileIndex = 0;
			
			for (int i=0;i< hdf5Filenames.length;i++) {
			
				String hdf5Filename =hdf5Filenames[i];
				try {
					
					FileFormat format = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
					H5File h5ReadOnlyFile = (H5File) format.createInstance(hdf5Filename, FileFormat.READ);
					
					List<String> allDatasetsPaths = null;
					
					if (tradeDate==null) {
						String instrumentGroupPath = instrumentType + "/" + underlyingName;
						HObject obj = h5ReadOnlyFile.get(instrumentGroupPath);
						if (obj instanceof H5Group) {
							H5Group instrumentGroup = (H5Group) obj;
							List<String> paths = getAllLeafNodePaths(h5ReadOnlyFile, instrumentGroup);
							allDatasetsPaths = new LinkedList<>();
							for (String p : paths) {
								if (p.endsWith(HkDerivativesTRHDF5Builder.DEFAULT_DATASET_NAME))
									allDatasetsPaths.add(p);
							}
						}
						else throw new HistoricalDataSourceException("Unable to find the subgroup of "+instrumentGroupPath+" for HDF5 File: "+h5ReadOnlyFile.getAbsolutePath());
					}
					else {
						allDatasetsPaths=new LinkedList<>();
						allDatasetsPaths.add(instrumentType + "/" + underlyingName + "/" + tradeDate.toString("yyyy/MM/dd") + "/"
								+ HkDerivativesTRHDF5Builder.DEFAULT_DATASET_NAME);
					}
	
					// construct the iterable for each single file. (Note that this YieldUtils spawns a thread and a queue to handle its internal logic)
					Iterable<HkDerivativesTR> trIterable = YieldUtils.toIterable(new HkDerivativesTRYielder(h5ReadOnlyFile, allDatasetsPaths));
					Iterator<HkDerivativesTR> nestedIterator = trIterable.iterator();
					
					// save down the file and the iterator.
					filesMap.put(hdf5Filename, h5ReadOnlyFile);
					iteratorsMap[i]=nestedIterator;
					
				} catch (Exception e1) {
					throw new HistoricalDataSourceException("Unable to open HDF5 File: " + hdf5Filename, e1);
				} 
			}
		}
		
		
		@Override
		public boolean hasNext() {
			
			if (currentFileIndex>=hdf5Filenames.length)
				return false;
			
			String filename = hdf5Filenames[currentFileIndex];
			
			@SuppressWarnings("unchecked")
			Iterator<HkDerivativesTR> nestedIterator = iteratorsMap[currentFileIndex];
			
			boolean result = nestedIterator.hasNext();
			// do this to follow the same convention of the looping.
			if (result==false) {
				closeFile(filename);
				// move to next and try this function again.
				currentFileIndex++;
				return hasNext();
			}
			else return true;
			
		}

		@Override
		public HkDerivativesTR next() {
			if (currentFileIndex>=hdf5Filenames.length)
				return null;
			
			@SuppressWarnings("unchecked")
			Iterator<HkDerivativesTR> nestedIterator = iteratorsMap[currentFileIndex];
			
			HkDerivativesTR result = nestedIterator.next();
			if (result!=null)
				return result;
			else {
				String filename = hdf5Filenames[currentFileIndex];
				closeFile(filename);
				currentFileIndex++;
				return next();
			}
			
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported for HkDerivativesTRHDF5Source");
		}

		@Override
		public boolean isClosed() {
			return filesMap.isEmpty();
		}

		@Override
		public void close() {
			for (int i=0;i<hdf5Filenames.length;i++) {
				String filename = hdf5Filenames[i];
				closeFile(filename);
			}
		}
		
		private void closeFile(String filename) {
			
			H5File h5ReadOnlyFile = filesMap.get(filename);
			if (h5ReadOnlyFile != null) {
				try {
					h5ReadOnlyFile.close();
				} catch (Exception e2) {
					log.warn("Unable to close the HDF5 File: " + filename, e2);
				} finally {
					filesMap.remove(filename);
				}
			}				
		}
		
		
		// helper function to get the leaves Dataset for daily tick data.
		private List<String> getAllLeafNodePaths(H5File h5ReadOnlyFile, H5Group g) throws Exception {
			
			List<HObject> members = g.getMemberList();
			List<String> nestedPaths = new LinkedList<>();
			List<String> terminals = new LinkedList<>();
			
			if (members!=null) {
				for (HObject obj : members) {
					if (obj instanceof H5Group) {
						nestedPaths.add(((H5Group) obj).getFullName());
					}
					else {
						terminals.add(obj.getFullName());
					}
				}
			}
			
			List<String> result=new LinkedList<>();
			if (!terminals.isEmpty()) {
				result.addAll(terminals);
			}
			
			for (String p : nestedPaths) {
				H5Group subgroup = (H5Group) h5ReadOnlyFile.get(p);
				List<String> r = getAllLeafNodePaths(h5ReadOnlyFile, subgroup);
				result.addAll(r);
			}
			
			return result;
			
		}		
	}
	
	/**
	 * 
	 * HkDerivativesTRYielder is the actual yield iterator implementation that reads from a given HDF5 file
	 * and return the HkDerivativesTR object.
	 *
	 */
	private class HkDerivativesTRYielder implements Generator<HkDerivativesTR> {

		private H5File h5ReadOnlyFile;
		private Iterable<String> allDatasetsPaths;
		
		public HkDerivativesTRYielder(H5File h5ReadOnlyFile, Iterable<String> allDatasetsPaths) {
			this.h5ReadOnlyFile=h5ReadOnlyFile;
			this.allDatasetsPaths=allDatasetsPaths;
		}
		
		@Override
		public void generate(Yieldable<HkDerivativesTR> yieldable) {
			try {
				// one loop is one day of data
				for (String path : allDatasetsPaths) {
					
					// read the dataset for one day
					HObject dataset = h5ReadOnlyFile.get(path);
	
					if (dataset instanceof H5CompoundDS) {
						H5CompoundDS compDS = (H5CompoundDS) dataset;
	
						Object data = compDS.getData();
						if (data instanceof Vector) {
							@SuppressWarnings("rawtypes")
							Vector dataVector = (Vector) data;
							if (dataVector.size() != 9)
								throw new IllegalStateException("input file doesn't have exactly 9 columns");
	
							String[] classCode = (String[]) dataVector.get(0);
							String[] futuresOrOptions = (String[]) dataVector.get(1);
							long[] expiryMonth = (long[]) dataVector.get(2);
							double[] strikePrice = (double[]) dataVector.get(3);
							String[] callPut = (String[]) dataVector.get(4);
							long[] timestamp = (long[]) dataVector.get(5);
							double[] price = (double[]) dataVector.get(6);
							double[] quantity = (double[]) dataVector.get(7);
							String[] tradeType = (String[]) dataVector.get(8);
	
							int totalLength = classCode.length;
	
							for (int currentIndex = 0; currentIndex < totalLength; currentIndex++) {
	
								LocalDateTime expiryMonthDateTime = new LocalDateTime(expiryMonth[currentIndex]);
								YearMonth expiryMonthObj = new YearMonth(expiryMonthDateTime.getYear(),
										expiryMonthDateTime.getMonthOfYear());
	
								LocalDateTime tradeDateTime = new LocalDateTime(timestamp[currentIndex]);
	
								HkDerivativesTR tuple = new HkDerivativesTR(classCode[currentIndex],
										futuresOrOptions[currentIndex], 
										expiryMonthObj, strikePrice[currentIndex],
										callPut[currentIndex], 
										tradeDateTime, price[currentIndex],
										quantity[currentIndex], tradeType[currentIndex]);
								
								yieldable.yield(tuple);
							}
						}
					}				
					
					
				}
			} catch (Exception e1) {
				throw new RuntimeException("Caught exception when yield iterating HDF5 file: "+h5ReadOnlyFile.getName(), e1);
			}
		}
	
	}
	

}
