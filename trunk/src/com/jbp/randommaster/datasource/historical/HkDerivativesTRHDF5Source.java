package com.jbp.randommaster.datasource.historical;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

import com.jbp.randommaster.hdf5builders.HkDerivativesTRHDF5Builder;

/**
 * Data source to load Hk Derivatives TR data from HDF5.
 *
 */
public class HkDerivativesTRHDF5Source extends AutoCloseableHistoricalDataSource<HkDerivativesTR> {

	static Logger log = Logger.getLogger(HkDerivativesTRHDF5Source.class);

	private String hdf5Filename;
	private LocalDate tradeDate;
	private String instrumentType;
	private String instrumentName;

	/**
	 * Create an instance of HkDerivativesTRHDF5Source.
	 * 
	 * @param hdf5Filename The source hdf5 file.
	 * @param tradeDate The trade date of the data to be loaded.
	 * @param instrumentType The instrument type such as "Futures" or "Options" etc
	 * @param instrumentName The instrument name such as HSI or HHI etc
	 */
	public HkDerivativesTRHDF5Source(String hdf5Filename, LocalDate tradeDate, String instrumentType, String instrumentName) {
		this.hdf5Filename = hdf5Filename;
		this.tradeDate = tradeDate;
		this.instrumentType = instrumentType;
		this.instrumentName = instrumentName;
	}

	public String getHDF5LoadPath() {
		return instrumentType + "/" + instrumentName + "/" + tradeDate.toString("yyyy/MM/dd") + "/"
				+ HkDerivativesTRHDF5Builder.DEFAULT_DATASET_NAME;
	}

	public String getHDF5Filename() {
		return hdf5Filename;
	}

	public LocalDate getTradeDate() {
		return tradeDate;
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
	

	/**
	 * Helper class to implement the iterator of the HDF5 data.
	 */
	private class InputFileIterator implements AutoCloseableIterator<HkDerivativesTR> {

		private H5File h5ReadOnlyFile;

		private Iterator<HkDerivativesTR> dataBufIt;

		@SuppressWarnings("rawtypes")
		public InputFileIterator() {

			// TODO: change this HDF5 Source to really use iterator pattern.
			
			try {
				FileFormat format = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
				h5ReadOnlyFile = (H5File) format.createInstance(getHDF5Filename(), FileFormat.READ);

				// read the dataset
				HObject dataset = h5ReadOnlyFile.get(getHDF5LoadPath());

				// the buffer to store the data from the compound ds
				List<HkDerivativesTR> dataBuf = new LinkedList<HkDerivativesTR>();
				
				if (dataset instanceof H5CompoundDS) {
					H5CompoundDS compDS = (H5CompoundDS) dataset;

					Object data = compDS.getData();
					if (data instanceof Vector) {
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

							//dataBuf.add(new HkDerivativesTRData(tuple));
							dataBuf.add(tuple);

						}


					}
				}

				// in any case we need to create an iterator to avoid null pointer exception.
				dataBufIt = dataBuf.iterator();


			} catch (Exception e1) {
				throw new HistoricalDataSourceException("Unable to open HDF5 File: " + getHDF5Filename(), e1);
			} 

		}

		@Override
		public boolean hasNext() {
			boolean result = dataBufIt.hasNext();
			// do this to follow the same convention of the looping.
			if (result==false)
				close();
			return result;
		}

		@Override
		public HkDerivativesTR next() {
			return dataBufIt.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported for HkDerivativesTRHDF5Source");
		}


		@Override
		public boolean isClosed() {
			return h5ReadOnlyFile==null;
		}

		@Override
		public void close() {
			if (h5ReadOnlyFile != null) {
				try {
					h5ReadOnlyFile.close();
				} catch (Exception e2) {
					log.warn("Unable to close the HDF5 File: " + getHDF5Filename(), e2);
				} finally {
					h5ReadOnlyFile = null;
				}
			}			
		}
		
	}



}
