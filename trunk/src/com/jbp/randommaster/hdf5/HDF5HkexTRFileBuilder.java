package com.jbp.randommaster.hdf5;

import java.util.Collection;
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
	
	public void createCompoundDSForTRData(Collection<HkexTRFileData> rawData) {
		
		// group by instrument code and then trade date and then the list of raw data.
		TreeMap<String, Map<LocalDate,List<HkexTRFileData>>> instrumentClassCodes=new TreeMap<String, Map<LocalDate,List<HkexTRFileData>>>();
		for (HkexTRFileData d : rawData) {
			
			String classCode = d.getData().getClassCode();
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
				
				log.info("Creating group "+instrumentCode+", "+tradeDate);
				H5Group h5Group=super.createInstrumentAndDateGroups(instrumentCode, tradeDate);
				log.info("Group "+instrumentCode+", "+tradeDate+" created");
				
				String dsName = "TRData";
				
				// create the compound DS and then attach the data.
				createCoupoundDSForInstrument(instrumentCode, h5Group, dsName, dataList);
				
				log.info("Compound DS for "+instrumentCode+" created");
			}
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
	protected H5CompoundDS createCoupoundDSForInstrument(String instrumentCode, H5Group parentGroup, String dsName, List<HkexTRFileData> dataForOneDay) {
		
		if (dataForOneDay==null || dataForOneDay.isEmpty())
			throw new IllegalArgumentException("Input raw data is empty for instrument: "+instrumentCode);
		
		H5File h5f=super.getHDF5File();
		
		int rowCount = dataForOneDay.size();
		
		H5CompoundDS resultDS =null;
		
		for (HObject obj : parentGroup.getMemberList()) {
			if (dsName.equals(obj.getName())) {
				resultDS=(H5CompoundDS) obj;
				break;
			}
		}

		// if no previous setup for the DS.
		if (resultDS==null) {
			String[] colHeader = new String[] {
					"ClassCode",
					"FuturesOrOptions",
					"ExpiryMonth",
					"StrikePrice",
					"CallPut",
					"Timestamp",
					"Price",
					"Quantity"
			};
			
			int[] memberDataSize=new int[] {
				1, 1, 1, 1, 1, 1, 1, 1	
			};
			
			Datatype[] memberDatatypes=new Datatype[] {
					new H5Datatype(H5Datatype.CLASS_STRING, 10, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_STRING, 1, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_INTEGER, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_STRING, 1, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_INTEGER, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, H5Datatype.NATIVE),
					new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, H5Datatype.NATIVE)
			};
			

			try {
				resultDS=(H5CompoundDS) h5f.createCompoundDS(dsName,  
						parentGroup, new long[] { rowCount }, null, new long[] {rowCount}, 0, 
						colHeader, memberDatatypes, 
						memberDataSize, null);
			} catch (Exception e1) {
				log.fatal("Unable to create compound DS "+dsName, e1);
				throw new HDF5FileBuilderException("Unable to create compound DS "+dsName, e1);
			}
		}
		
		// now we have a DS ready
		@SuppressWarnings("rawtypes")
		Vector dataVect=new Vector();
		// copy all the raw data to the arrays
		String[] classCode=new String [rowCount];
		String[] futuresOrOptions = new String [rowCount];
		long[] expiryMonth = new long [rowCount];
		double[] strike = new double [rowCount];
		String[] callPut = new String [rowCount];
		long[] timestamp = new long [rowCount];
		double[] price = new double [rowCount];
		double[] quantity = new double [rowCount];
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
		
		try {
			resultDS.write(dataVect);
		} catch (Exception e1) {
			log.fatal("unable to write data vector to "+super.getTargetFilename(), e1);
			throw new HDF5FileBuilderException("unable to write data vector to "+super.getTargetFilename(), e1);
		}
		
		return resultDS;
	}

}
