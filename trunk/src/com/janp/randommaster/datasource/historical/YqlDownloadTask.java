package com.janp.randommaster.datasource.historical;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class YqlDownloadTask implements Callable<Collection<YahooHistoricalData>> {

	private String yql;
	
	public YqlDownloadTask(String yql) {
		this.yql=yql;
	}

	@Override
	public Collection<YahooHistoricalData> call() throws Exception {
		
		//System.out.println("invoking....");
		Content content=Request.Get(yql).execute().returnContent();
		
		String resultXml=content.asString();
		
		//System.out.println("resultXml=\n"+resultXml);
		
		return parseYqlResponse(resultXml);
		
	}
	
	
	public static Collection<YahooHistoricalData> parseYqlResponse(String resultXml) throws XPathExpressionException {
		
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xPath = factory.newXPath();		

	    
	    NodeList quoteNodes = (NodeList) xPath.evaluate("/query/results/quote", 
	    		new InputSource(new StringReader(resultXml)), 
	    		XPathConstants.NODESET);	 
	    
	    TreeSet<YahooHistoricalData> result=new TreeSet<YahooHistoricalData>();
	    
	    
	    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	    
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
					fmt.parseLocalDate(dateStr), 
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
