package com.jbp.randommaster.datasource.historical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;


/**
 * 
 * Encapsulate the trade record data loading from a single HKEX TR file.
 *
 */
public class HkDerivativesTRFileSource implements HistoricalDataSource<HkDerivativesTR>, AutoCloseable {

	static Logger log=Logger.getLogger(HkDerivativesTRFileSource.class);	
	
	private String inputFile;
	private LocalDateTime startRange, endRange;
	private String classCode;
	private String futuresOrOptions;
	
	// holding list of iterators to ensure it will be cleaned up finally.
	private List<InputFileIterator> finalCleanupList;
	
	/**
	 * Create an instance of HkexTRFileSource.
	 *  
	 * @param inputFile The source file name.
	 * @param startRange Filter only trades on or after start time will be included. Null means no filtering 
	 * @param endRange Filter only trades on or before end time will be included. Null means no filtering
	 * @param classCode Filter only trades of specific class code is included. Null means no filtering
	 * @param futuresOrOptions Filter only futures or options included. Null means no filtering
	 */
	public HkDerivativesTRFileSource(String inputFile, LocalDateTime startRange, LocalDateTime endRange, String classCode, String futuresOrOptions) {
		
		this.inputFile=inputFile;
		
		this.startRange=startRange;
		this.endRange=endRange;
		this.classCode=classCode;
		
		finalCleanupList=new ArrayList<InputFileIterator>(10);
	}
	


	/**
	 * Create an instance of HkexTRFileSource with no filtering.
	 * 
	 * @param inputFile The input source file.
	 */
	public HkDerivativesTRFileSource(String inputFile) throws FileNotFoundException {
		this(inputFile, null, null, null, null);
	}
	

	
	
	/**
	 * Create an instance of HkexTRFileSource with only filtering on class code and futures/options flag.
	 * 
	 * @param inputFile The input source file
	 * @param classCode The class code such as HSI/MHI etc.
	 * @param futuresOrOptions The futures or options flag. F = Futures, O = Options.
	 */	
	public HkDerivativesTRFileSource(String inputFile, String classCode, String futuresOrOptions) throws FileNotFoundException {
		this(inputFile, null, null, classCode, futuresOrOptions);
	}
	

	
	@Override
	public Iterable<HkDerivativesTR> getData() {
		
		return new Iterable<HkDerivativesTR>() {

			@Override
			public Iterator<HkDerivativesTR> iterator() {
				try {
					// return the iterator that truly runs through the input file line by line.
					InputFileIterator it= new InputFileIterator();
					
					// save down the iterator to ensure it will be in the final clean up
					finalCleanupList.add(it);
					
					return it;
					
				} catch (Exception e1) {
					throw new HistoricalDataSourceException("Unable to create iterator for getData() call in HkexTRFileSource("+inputFile+")", e1);
				}
			}
		};
		
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
	
	
	/**
	 * Remove the iterator from final clean up list.
	 */
	private void disposeIterator(InputFileIterator it) {
		finalCleanupList.remove(it);
	}

	@Override
	public void close() throws IOException {
		for (InputFileIterator it : finalCleanupList) {
			it.closeFileReader();
		}
		finalCleanupList.clear();
	}	
	
	/**
	 * Internal helper iterator class that actually carries out the file reading and parsing.
	 *
	 */
	private class InputFileIterator implements Iterator<HkDerivativesTR> {

		private FileReader fileReader;
		private BufferedReader bufReader;
		private Queue<String> lines;

		public InputFileIterator() throws FileNotFoundException {
			fileReader = new FileReader(inputFile);
			bufReader = new BufferedReader(fileReader);
			lines=new LinkedList<String>();
		}
		
		@Override
		public boolean hasNext() {
			if (lines.peek()!=null)
				return true;
			
			try {
				tryBuffing();
				return lines.peek()!=null;
			} catch(Exception e1) {
				return false;
			}
		}

		@Override
		public HkDerivativesTR next() {
			HkDerivativesTR data = null;
			try {
				String line=null;
				// if the data has been parsed we just leave
				while (data==null && hasNext()) {
					line=lines.poll();
					data = interpretOneLine(line);
				}
				return data;
			} catch (Exception e1) {
				log.fatal("Unable to read the next line for "+inputFile, e1);
				return null;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported for HkDerivativesTRFileSource");
		}
		
		public void closeFileReader() throws IOException {
			fileReader.close();
		}
		
		/**
		 * Helper function to fetch one line from the file.
		 */
		private void tryBuffing() throws IOException {
			String oneLine = null;
			if (lines.peek()==null) {
				try {
					oneLine = bufReader.readLine();
					if (oneLine!=null)
						lines.add(oneLine);
				} finally {
					// close if nothing can be read when we are required to read something.
					if (oneLine==null) {
						try {
							closeFileReader();
						} finally {
							// call the parent class to remove this iterator from final cleanup.
							disposeIterator(this);
						}
					}
				}
			}
		}
		

		/**
		 * Parse one row of input file.
		 * @param l One line in the input file
		 * @return A <code>HkexTRFileData</code> object if the input is in the correct format. null otherwise.
		 */
		private HkDerivativesTR interpretOneLine(String l) {
			String line=l.trim();
			// ignore blank lines.
			if (line.length()>0) {
				
				// parse the input line.
				//HkDerivativesTR d=new HkDerivativesTR(line);
				HkDerivativesTR d=HkDerivativesTR.parse(line);
				
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
				classCodePass= (classCode==null || classCode.equals(d.getUnderlying()));
				
				// check futures or options filter
				boolean futuresOrOptionsPass = false;
				futuresOrOptionsPass = (futuresOrOptions==null || futuresOrOptions.equals(d.getFuturesOrOptions()));

				
				// aggregate all the filtering result
				boolean allPass = (timeFilterPass && classCodePass && futuresOrOptionsPass);
				
				// add only if all filtering passed.
				if (allPass)
					return d;
				else return null;
			}
			else return null;
		}		
	}





		
}
