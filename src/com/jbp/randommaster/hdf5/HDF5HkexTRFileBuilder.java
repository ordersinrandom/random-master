package com.jbp.randommaster.hdf5;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import com.jbp.randommaster.datasource.historical.HkexTRFileData;

/**
 * 
 * Helper class to create/modify the HDF5 file appending HkexTRFileData objects
 *
 */
public class HDF5HkexTRFileBuilder extends HDF5FileBuilder {

	static Logger log=Logger.getLogger(HDF5HkexTRFileBuilder.class);	
	
	public HDF5HkexTRFileBuilder(String targetFilename) {
		super(targetFilename);
	}
	
	/**
	 * Create various compound datasets for the given large list of HKEX TR File data.
	 * 
	 * @param rawData The raw data of the whole HKEX TR File.
	 */
	public void createCompoundDatasetsForTRData(Iterable<HkexTRFileData> rawData) {
		// group by instrument code and then trade date and then the list of raw data.
		TreeMap<String, Map<LocalDate,List<HkexTRFileData>>> instrumentClassCodes=new TreeMap<String, Map<LocalDate,List<HkexTRFileData>>>();
		for (HkexTRFileData d : rawData) {
			
			String classCode = d.getData().getClassCode();
			// data grouped by trade date.
			Map<LocalDate,List<HkexTRFileData>> groupedData=instrumentClassCodes.get(classCode);
			if (groupedData==null) {
				groupedData=new TreeMap<LocalDate, List<HkexTRFileData>>();
				instrumentClassCodes.put(classCode, groupedData);
			}

			LocalDate tradeDate=d.getTimestamp().toLocalDate();
			List<HkexTRFileData> dataList=groupedData.get(tradeDate);
			if (dataList==null) {
				dataList=new LinkedList<HkexTRFileData>();
				groupedData.put(tradeDate, dataList);
			}
			dataList.add(d);
		}
		
		// create all the relevant groups
		for (Map.Entry<String, Map<LocalDate,List<HkexTRFileData>>> en : instrumentClassCodes.entrySet()) {
			
			String instrumentCode = en.getKey();
			
			log.info("Creating compound DS for "+instrumentCode);
			
			Map<LocalDate,List<HkexTRFileData>> relevantData = en.getValue();
			
			for (Map.Entry<LocalDate,List<HkexTRFileData>> en2 : relevantData.entrySet()) {
				LocalDate tradeDate=en2.getKey();
				List<HkexTRFileData> dataList = en2.getValue();
				// split into options and futures
				List<HkexTRFileData> optionsList = new LinkedList<HkexTRFileData>();
				List<HkexTRFileData> futuresList = new LinkedList<HkexTRFileData>();
				for (HkexTRFileData d : dataList) {
					if (d.getData().isFutures()) 
						futuresList.add(d);
					else if (d.getData().isOptions())
						optionsList.add(d);
				}

				String dsName = "TRData";
				
				createGroupAndCompoundDS("Futures", instrumentCode, tradeDate, dsName, futuresList);
				createGroupAndCompoundDS("Options", instrumentCode, tradeDate, dsName, optionsList);
				
			}
		}
	}
	
	
	/**
	 * Helper function to create the groups path and the compound DS for some particular instrument type and date
	 * 
	 * @param instrumentType For example "Futures", "Options"
	 * @param underlying e.g. HSI
	 * @param tradeDate The trade date of the group
	 * @param dsName The dataset name to be used.
	 * @param dataList The list of data grouped by instrument type.
	 */
	private void createGroupAndCompoundDS(String instrumentType, String underlying, LocalDate tradeDate, String dsName, List<HkexTRFileData> dataList) {

		if (dataList!=null && !dataList.isEmpty()) {
			// create the instrument and date group for futures or options
			log.info("Creating group "+instrumentType+", "+underlying+", "+tradeDate);
			H5Group h5FuturesGroup=super.createOrGetInstrumentAndDateGroup(instrumentType, underlying, tradeDate);
			log.info("Group "+instrumentType+", "+underlying+", "+tradeDate+" created");
			
			// create the compound DS and then attach the data.
			createCoupoundDSForInstrument(underlying, h5FuturesGroup, dsName, dataList);
			log.info("Compound DS for "+instrumentType+", "+underlying+" created");
		}
		else {
			log.info("Group "+instrumentType+", "+underlying+", "+tradeDate+" NOT created as the input data list is empty or null");
		}
		
	}
	
	
	/**
	 * Helper class to create CompoundDS for instrument data.
	 * @param instrumentCode The instrument code of the data
	 * @param parentGroup The group that the dataset going to be attached to
	 * @param dsName The Dataset name to be used.
	 * @param dataForOneDay The data to be added. Supposed to be in a single trade date.
	 * @return The created compound DS.
	 */
	@SuppressWarnings("unchecked")
	private H5CompoundDS createCoupoundDSForInstrument(String instrumentCode, H5Group parentGroup, String dsName, List<HkexTRFileData> dataForOneDay) {
		
		if (dataForOneDay==null || dataForOneDay.isEmpty())
			throw new IllegalArgumentException("Input raw data is empty for instrument: "+instrumentCode);
		
		H5File h5f=super.getHDF5File();
		
		int rowCount = dataForOneDay.size();
		
		
		try {
			HObject obj = super.getHDF5File().get(parentGroup.getFullName()+"/"+dsName);
			if (obj!=null && obj instanceof H5CompoundDS) {
				// delete it. we are going to re-create
				super.getHDF5File().delete(obj);
			}
		} catch (Exception e1) {
		}

		H5CompoundDS resultDS =null;

		String[] colHeader = new String[] {
				"ClassCode",
				"FuturesOrOptions",
				"ExpiryMonth",
				"StrikePrice",
				"CallPut",
				"Timestamp",
				"Price",
				"Quantity",
				"TradeType"
		};
		
		int[] memberDataSize=new int[] {
			1, 1, 1, 1, 1, 1, 1, 1,1	
		};
		
		Datatype[] memberDatatypes=new Datatype[] {
				new H5Datatype(H5Datatype.CLASS_STRING, 10, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_STRING, 1, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_INTEGER, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_STRING, 1, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_INTEGER, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
				new H5Datatype(H5Datatype.CLASS_STRING, 4, H5Datatype.NATIVE, H5Datatype.NATIVE),
		};
		

		try {
			int compressionLevel = 9;
			
			resultDS=(H5CompoundDS) h5f.createCompoundDS(dsName,  
					parentGroup, new long[] { rowCount }, null, new long[] {rowCount}, compressionLevel, 
					colHeader, memberDatatypes, 
					memberDataSize, null);
			
		} catch (Exception e1) {
			log.fatal("Unable to create compound DS "+dsName, e1);
			throw new HDF5FileBuilderException("Unable to create compound DS "+dsName, e1);
		}


		resultDS.init();
		
		// now we have a DS ready
		@SuppressWarnings("rawtypes")
		Vector dataVect=new Vector();
		// copy all the raw data to the arrays
		String[] classCode = new String[rowCount];
		String[] futuresOrOptions = new String[rowCount];
		long[] expiryMonth = new long[rowCount];
		double[] strike = new double[rowCount];
		String[] callPut = new String[rowCount];
		long[] timestamp = new long[rowCount];
		double[] price = new double[rowCount];
		double[] quantity = new double[rowCount];
		String[] tradeType = new String[rowCount];
		int i=0;
		for (HkexTRFileData d : dataForOneDay) {
			classCode[i]=d.getData().getClassCode();
			futuresOrOptions[i]=d.getData().getFuturesOrOptions();
			expiryMonth[i]=d.getData().getExpiryMonth().toLocalDate(1).toDateMidnight().getMillis();
			strike[i]=d.getData().getStrikePrice();
			callPut[i]=d.getData().getCallPut();
			timestamp[i]=d.getTimestamp().toDateTime().getMillis();
			price[i]=d.getData().getPrice();
			quantity[i]=d.getData().getQuantity();
			tradeType[i]=d.getData().getTradeType();
			i++;
		}
		dataVect.add(classCode);
		dataVect.add(futuresOrOptions);
		dataVect.add(expiryMonth);
		dataVect.add(strike);
		dataVect.add(callPut);
		dataVect.add(timestamp);
		dataVect.add(price);
		dataVect.add(quantity);
		dataVect.add(tradeType);
		
		try {
			resultDS.write(dataVect);
		} catch (Exception e1) {
			log.fatal("unable to write data vector to "+super.getTargetFilename(), e1);
			throw new HDF5FileBuilderException("unable to write data vector to "+super.getTargetFilename(), e1);
		}
		
		return resultDS;
	}

}
