package com.jbp.randommaster.datasource.historical;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	 * @param hdf5Filename
	 *            The source hdf5 file.
	 * @param tradeDate
	 *            The trade date of the data to be loaded.
	 * @param instrumentType
	 *            The instrument type such as "Futures" or "Options" etc
	 * @param underlyingName
	 *            The underlying name such as HSI or HHI etc
	 */
	public HkDerivativesTRHDF5Source(String hdf5Filename, LocalDate tradeDate, String instrumentType, String underlyingName) {
		this.hdf5Filenames = new String[] { hdf5Filename };
		this.tradeDate = tradeDate;
		this.instrumentType = instrumentType;
		this.underlyingName = underlyingName;
	}

	/**
	 * Create an instance of HkDerivativesTRHDF5Source by an array of sorted
	 * hdf5 files.
	 * 
	 * @param hdf5Filenames
	 *            An array of hdf5 files. Note that we assume the files are
	 *            sorted by the trade timestamp and there is no overlapping of
	 *            time between two files.
	 * @param instrumentType
	 *            The instrument type that we are loading.
	 * @param underlyingName
	 *            The underlying name such as HSI or HHI.
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
		// use array here to improve the performance. should be exactly matching
		// the size of the parent class hdf5Filenames array
		@SuppressWarnings("rawtypes")
		private Iterator[] iteratorsMap;

		// to keep track of the current file that we are loading.
		private int currentFileIndex;

		public InputFileIterator() {

			filesMap = new HashMap<>();
			iteratorsMap = new Iterator[hdf5Filenames.length];
			currentFileIndex = 0;

			for (int i = 0; i < hdf5Filenames.length; i++) {

				String hdf5Filename = hdf5Filenames[i];
				try {

					FileFormat format = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
					H5File h5ReadOnlyFile = (H5File) format.createInstance(hdf5Filename, FileFormat.READ);

					List<String> allDatasetsPaths = null;

					if (tradeDate == null) {
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
						} else
							throw new HistoricalDataSourceException("Unable to find the subgroup of " + instrumentGroupPath + " for HDF5 File: "
									+ h5ReadOnlyFile.getAbsolutePath());
					} else {
						allDatasetsPaths = new LinkedList<>();
						allDatasetsPaths.add(instrumentType + "/" + underlyingName + "/" + tradeDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/"
								+ HkDerivativesTRHDF5Builder.DEFAULT_DATASET_NAME);
					}

					Iterator<HkDerivativesTR> nestedIterator = new SingleHkDerivativesTRFileIterator(h5ReadOnlyFile, allDatasetsPaths);

					// save down the file and the iterator.
					filesMap.put(hdf5Filename, h5ReadOnlyFile);
					iteratorsMap[i] = nestedIterator;

				} catch (Exception e1) {
					throw new HistoricalDataSourceException("Unable to open HDF5 File: " + hdf5Filename, e1);
				}
			}
		}

		@Override
		public boolean hasNext() {

			if (currentFileIndex >= hdf5Filenames.length)
				return false;

			String filename = hdf5Filenames[currentFileIndex];

			@SuppressWarnings("unchecked")
			Iterator<HkDerivativesTR> nestedIterator = iteratorsMap[currentFileIndex];

			boolean result = nestedIterator.hasNext();
			// do this to follow the same convention of the looping.
			if (result == false) {
				closeFile(filename);
				// move to next and try this function again.
				currentFileIndex++;
				return hasNext();
			} else
				return true;

		}

		@Override
		public HkDerivativesTR next() {
			if (currentFileIndex >= hdf5Filenames.length)
				return null;

			@SuppressWarnings("unchecked")
			Iterator<HkDerivativesTR> nestedIterator = iteratorsMap[currentFileIndex];

			HkDerivativesTR result = nestedIterator.next();
			if (result != null)
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
			for (int i = 0; i < hdf5Filenames.length; i++) {
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

			if (members != null) {
				for (HObject obj : members) {
					if (obj instanceof H5Group) {
						nestedPaths.add(((H5Group) obj).getFullName());
					} else {
						terminals.add(obj.getFullName());
					}
				}
			}

			List<String> result = new LinkedList<>();
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
	 * SingleHkDerivativesTRFileIterator provides on-demand iteration on an
	 * "opened" HDF5 file and a set of dataset paths.
	 * 
	 * It loads the path one by one and provides the dataset rows line by line.
	 * When one dataset is iterated it moves to the next dataset and continues.
	 * 
	 * It does not opened or close the given HDF5 file and leaves it for
	 * external caller to handle the open and close operation.
	 * 
	 */
	private class SingleHkDerivativesTRFileIterator implements Iterator<HkDerivativesTR> {

		private H5File h5ReadOnlyFile;
		private Iterator<String> datasetPathsIt;
		private int currentPathDatasetSize;
		private int currentDatasetRow;

		// these arrays are the storage for current dataset data.
		private String[] classCode;
		private String[] futuresOrOptions;
		private long[] expiryMonth;
		private double[] strikePrice;
		private String[] callPut;
		private long[] timestamp;
		private double[] price;
		private double[] quantity;
		private String[] tradeType;

		public SingleHkDerivativesTRFileIterator(H5File h5ReadOnlyFile, Iterable<String> allDatasetsPaths) {
			this.h5ReadOnlyFile = h5ReadOnlyFile;
			this.datasetPathsIt = allDatasetsPaths.iterator();
			this.currentDatasetRow = -1;
			this.currentPathDatasetSize = -1;
		}

		@Override
		public boolean hasNext() {

			String newPath = null;
			try {
				if (currentDatasetRow >= currentPathDatasetSize) {

					// no more data

					// if we don't have more paths... we don't have next
					if (!datasetPathsIt.hasNext())
						return false;
					else {

						// try move on to next path (until there is an non empty
						// one).
						boolean nextPathSuccess = false;
						while (!nextPathSuccess && datasetPathsIt.hasNext()) {
							newPath = datasetPathsIt.next();
							nextPathSuccess = loadDataset(newPath);
						}

						return nextPathSuccess;

					}
				} else
					return true; // still has some items to iterate from the
									// current dataset.

			} catch (Exception e1) {
				throw new RuntimeException("Caught exception when doing hasNext() check with loadDataset(" + newPath + ") call on "
						+ h5ReadOnlyFile.getAbsolutePath(), e1);
			}
		}

		@Override
		public HkDerivativesTR next() {
			boolean check = hasNext();
			if (!check)
				return null;
			else {
				LocalDateTime expiryMonthDateTime = LocalDateTime.ofInstant(new java.util.Date(expiryMonth[currentDatasetRow]).toInstant(), ZoneId.systemDefault());
				
				YearMonth expiryMonthObj = YearMonth.of(expiryMonthDateTime.getYear(), expiryMonthDateTime.getMonthValue());

				LocalDateTime tradeDateTime = LocalDateTime.ofInstant(new java.util.Date(timestamp[currentDatasetRow]).toInstant(), ZoneId.systemDefault());

				HkDerivativesTR tuple = new HkDerivativesTR(classCode[currentDatasetRow], futuresOrOptions[currentDatasetRow], expiryMonthObj,
						strikePrice[currentDatasetRow], callPut[currentDatasetRow], tradeDateTime, price[currentDatasetRow],
						quantity[currentDatasetRow], tradeType[currentDatasetRow]);

				currentDatasetRow++;

				return tuple;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported for HkDerivativesTRHDF5Source.SingleHkDerivativesTRFileIterator");
		}

		/**
		 * Helper function to load a dataset from a given path
		 * 
		 * @param newPath
		 *            The path to load the dataset from the HDF5 file.
		 * @return true means successful loading on an non empty dataset.
		 * @throws Exception
		 *             propagated from the HDF5 native calls.
		 */
		private boolean loadDataset(String newPath) throws Exception {
			// read the dataset for one day
			HObject dataset = h5ReadOnlyFile.get(newPath);

			if (dataset instanceof H5CompoundDS) {
				H5CompoundDS compDS = (H5CompoundDS) dataset;

				Object data = compDS.getData();
				if (data instanceof Vector) {
					@SuppressWarnings("rawtypes")
					Vector dataVector = (Vector) data;
					if (dataVector.size() != 9)
						throw new IllegalStateException("input file " + h5ReadOnlyFile.getAbsolutePath() + " dataset path " + newPath
								+ " does not have exactly 9 columns");

					classCode = (String[]) dataVector.get(0);
					futuresOrOptions = (String[]) dataVector.get(1);
					expiryMonth = (long[]) dataVector.get(2);
					strikePrice = (double[]) dataVector.get(3);
					callPut = (String[]) dataVector.get(4);
					timestamp = (long[]) dataVector.get(5);
					price = (double[]) dataVector.get(6);
					quantity = (double[]) dataVector.get(7);
					tradeType = (String[]) dataVector.get(8);

					currentDatasetRow = 0;
					currentPathDatasetSize = classCode.length;

					if (currentDatasetRow < currentPathDatasetSize)
						return true; // successful case
				}
			}

			// failed case
			currentDatasetRow = -1;
			currentPathDatasetSize = -1;
			return false;
		}

	}

}
