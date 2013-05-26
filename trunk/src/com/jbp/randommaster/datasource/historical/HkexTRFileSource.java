package com.jbp.randommaster.datasource.historical;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.joda.time.LocalDateTime;

public class HkexTRFileSource implements HistoricalDataSource<HkexTRFileData> {

	private String inputFile;
	
	private LocalDateTime startRange, endRange;
	private String classCode;
	private String futuresOrOptions;
	
	public HkexTRFileSource(String inputFile, LocalDateTime startRange, LocalDateTime endRange, String classCode, String futuresOrOptions) {
		
		this.inputFile=inputFile;
		
		this.startRange=startRange;
		this.endRange=endRange;
		this.classCode=classCode;
	}
	
	public HkexTRFileSource(String inputFile) {
		this(inputFile, null, null, null, null);
	}
	
	public HkexTRFileSource(String inputFile, String classCode, String futuresOrOptions) {
		this(inputFile, null, null, classCode, futuresOrOptions);
	}
	
	
	@Override
	public Collection<HkexTRFileData> getData() throws HistoricalDataSourceException {

		LinkedList<HkexTRFileData> result=new LinkedList<HkexTRFileData>();
		
		FileReader fin=null;
		BufferedReader br=null;
		try {
			
			fin=new FileReader(inputFile);
			br=new BufferedReader(fin);
			
			String line=null;
			while ((line=br.readLine())!=null) {
				line=line.trim();
				// ignore blank lines.
				if (line.length()>0) {
					HkexTRFileData d=new HkexTRFileData(line);
					
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
					
					boolean classCodePass = false;
					classCodePass= (classCode==null || classCode.equals(d.getData().getClassCode()));
					
					boolean futuresOrOptionsPass = false;
					futuresOrOptionsPass = (futuresOrOptions==null || futuresOrOptions.equals(d.getData().getFuturesOrOptions()));

					boolean allPass = (timeFilterPass && classCodePass && futuresOrOptionsPass);
					
					if (allPass)
						result.add(d);
				}
			}
			
		} catch (IOException e1) {
			// wrap up and re-throw.
			throw new HistoricalDataSourceException("unable to load input file "+inputFile, e1);
			
		} finally {
			
			if (fin!=null) {
				try {
					fin.close();
				} catch (IOException e2) {
					// ignore.
				}
			}
			
		}
		
		return result;
	}

}
