package com.jbp.randommaster.datasource.historical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

import org.joda.time.LocalDateTime;

/**
 * 
 * Encapsulate the trade record data loading from a single HKEX TR file.
 *
 */
public class HkexTRFileSource implements HistoricalDataSource<HkexTRFileData> {

	private Reader inputReader;
	
	private LocalDateTime startRange, endRange;
	private String classCode;
	private String futuresOrOptions;
	
	/**
	 * Create an instance of HkexTRFileSource.
	 *  
	 * @param inputReader The source reader.
	 * @param startRange Filter only trades on or after start time will be included. Null means no filtering 
	 * @param endRange Filter only trades on or before end time will be included. Null means no filtering
	 * @param classCode Filter only trades of specific class code is included. Null means no filtering
	 * @param futuresOrOptions Filter only futures or options included. Null means no filtering
	 */
	public HkexTRFileSource(Reader inputReader, LocalDateTime startRange, LocalDateTime endRange, String classCode, String futuresOrOptions) {
		
		this.inputReader=inputReader;
		
		this.startRange=startRange;
		this.endRange=endRange;
		this.classCode=classCode;
	}
	
	/**
	 * Create an instance of HkexTRFileSource.
	 *  
	 * @param inputFile The source file path.
	 * @param startRange Filter only trades on or after start time will be included. Null means no filtering 
	 * @param endRange Filter only trades on or before end time will be included. Null means no filtering
	 * @param classCode Filter only trades of specific class code is included. Null means no filtering
	 * @param futuresOrOptions Filter only futures or options included. Null means no filtering
	 */
	public HkexTRFileSource(String inputFile, LocalDateTime startRange, LocalDateTime endRange, String classCode, String futuresOrOptions) throws FileNotFoundException {
		this(new FileReader(inputFile), startRange, endRange, classCode, futuresOrOptions);
	}	
	
	/**
	 * Create an instance of HkexTRFileSource with no filtering.
	 * 
	 * @param inputReader The input source reader.
	 */
	public HkexTRFileSource(Reader inputReader) {
		this(inputReader, null, null, null, null);
	}

	/**
	 * Create an instance of HkexTRFileSource with no filtering.
	 * 
	 * @param inputFile The input source file.
	 */
	public HkexTRFileSource(String inputFile) throws FileNotFoundException {
		this(new FileReader(inputFile), null, null, null, null);
	}
	
	
	/**
	 * Create an instance of HkexTRFileSource with only filtering on class code and futures/options flag.
	 * 
	 * @param inputReader The input source reader.
	 * @param classCode The class code such as HSI/MHI etc.
	 * @param futuresOrOptions The futures or options flag. F = Futures, O = Options.
	 */
	public HkexTRFileSource(Reader inputReader, String classCode, String futuresOrOptions) {
		this(inputReader, null, null, classCode, futuresOrOptions);
	}
	
	
	/**
	 * Create an instance of HkexTRFileSource with only filtering on class code and futures/options flag.
	 * 
	 * @param inputFile The input source file
	 * @param classCode The class code such as HSI/MHI etc.
	 * @param futuresOrOptions The futures or options flag. F = Futures, O = Options.
	 */	
	public HkexTRFileSource(String inputFile, String classCode, String futuresOrOptions) throws FileNotFoundException {
		this(new FileReader(inputFile), null, null, classCode, futuresOrOptions);
	}	
	
	@Override
	public Iterable<HkexTRFileData> getData() throws HistoricalDataSourceException {

		LinkedList<HkexTRFileData> result=new LinkedList<HkexTRFileData>();
		
		BufferedReader br=null;
		try {
			
			br=new BufferedReader(inputReader);
			
			String line=null;
			while ((line=br.readLine())!=null) {
				line=line.trim();
				// ignore blank lines.
				if (line.length()>0) {
					
					// parse the input line.
					HkexTRFileData d=new HkexTRFileData(line);
					
					// check time filter
					boolean timeFilterPass = false;
					if (startRange==null && endRange==null)
						timeFilterPass=true;
					else {
						boolean startPass = false;
						if (startRange==null || startRange.isEqual(d.getTimestamp()) || startRange.isBefore(d.getTimestamp()))
							startPass=true;
						boolean endPass = false;
						if (endRange==null || endRange.isEqual(d.getTimestamp()) || endRange.isAfter(d.getTimestamp()))
							endPass=true;
						
						timeFilterPass=(startPass && endPass);
					}
					
					// check class code filter
					boolean classCodePass = false;
					classCodePass= (classCode==null || classCode.equals(d.getData().getClassCode()));
					
					// check futures or options filter
					boolean futuresOrOptionsPass = false;
					futuresOrOptionsPass = (futuresOrOptions==null || futuresOrOptions.equals(d.getData().getFuturesOrOptions()));

					
					// aggregate all the filtering result
					boolean allPass = (timeFilterPass && classCodePass && futuresOrOptionsPass);
					
					// add only if all filtering passed.
					if (allPass)
						result.add(d);
				}
			}
			
		} catch (IOException e1) {
			// wrap up and re-throw.
			throw new HistoricalDataSourceException("unable to from input reader: "+inputReader, e1);
			
		} finally {
			
			if (inputReader!=null) {
				try {
					inputReader.close();
				} catch (IOException e2) {
					// ignore.
				}
			}
			
		}
		
		return result;
	}

	public LocalDateTime getStartRange() {
		return startRange;
	}

	public LocalDateTime getEndRange() {
		return endRange;
	}

	public String getClassCode() {
		return classCode;
	}

	public String getFuturesOrOptions() {
		return futuresOrOptions;
	}

}
