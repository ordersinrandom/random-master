package com.jbp.randommaster.datasource.historical;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * The callable object that implements the asynchronous operation of getData() in <code>YahooHistoricalDataSource</code>.
 *
 */
public class YqlHistoricalDownloadTask implements Callable<Iterable<YahooHistoricalData>> {

	private String yql;
	
	public YqlHistoricalDownloadTask(String yql) {
		this.yql=yql;
	}

	/**
	 * Invoke the http request of the given yql and parse the response XML.
	 * 
	 * @return A collection of <code>YahooHistoricalData</code>
	 */
	@Override
	public Iterable<YahooHistoricalData> call() throws Exception {
		
		//System.out.println("invoking....");
		Content content=Request.Get(yql).execute().returnContent();
		
		String resultXml=content.asString();
		
		//System.out.println("resultXml=\n"+resultXml);
		
		return parseYqlResponse(resultXml);
		
	}
	
	
	public static Iterable<YahooHistoricalData> parseYqlResponse(String resultXml) throws XPathExpressionException {
		
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xPath = factory.newXPath();		

	    
	    NodeList quoteNodes = (NodeList) xPath.evaluate("/query/results/quote", 
	    		new InputSource(new StringReader(resultXml)), 
	    		XPathConstants.NODESET);	 
	    
	    TreeSet<YahooHistoricalData> result=new TreeSet<YahooHistoricalData>();
	    
	    
	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    
	    for (int i=0;i<quoteNodes.getLength();i++) {
	    	Node quoteNode=quoteNodes.item(i);
	    	String dateStr=xPath.evaluate("./Date/text()", quoteNode);
	    	String openStr=xPath.evaluate("./Open/text()", quoteNode);
	    	String highStr=xPath.evaluate("./High/text()", quoteNode);
	    	String lowStr=xPath.evaluate("./Low/text()", quoteNode);
	    	String closeStr=xPath.evaluate("./Close/text()", quoteNode);
	    	String volumeStr=xPath.evaluate("./Volume/text()", quoteNode);
	    	String adjCloseStr=xPath.evaluate("./Adj_Close/text()", quoteNode);
	    	
			YahooHistoricalData d = new YahooHistoricalData(
					LocalDate.parse(dateStr, fmt).atTime(LocalTime.MIDNIGHT), 
					Double.valueOf(openStr).doubleValue(), 
					Double.valueOf(highStr).doubleValue(), 
					Double.valueOf(lowStr).doubleValue(), 
					Double.valueOf(closeStr).doubleValue(), 
					Double.valueOf(volumeStr).doubleValue(), 
					Double.valueOf(adjCloseStr).doubleValue());
			
			result.add(d);
	    }
	    
	    
	    return result;
	}
	
	
}
