package com.jbp.randommaster.draft.csv;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class TestCsv1 {

	public static void main(String[] args) throws IOException {

		StringBuilder buf = new StringBuilder(1000);

		buf.append("Date,Open,High,Low,Close,Volume,Adj Close\n");
		buf.append("2013-06-11,21542.30,21632.01,21307.99,21354.66,2121919200,21354.66\n");
		buf.append("2013-06-10,21592.90,21723.86,21497.55,21615.09,1464580800,21615.09\n");
		buf.append("2013-06-07,21768.24,21820.43,21506.33,21575.26,1834956400,21575.26\n");
		buf.append("2013-06-06,21908.45,21949.81,21799.25,21838.43,1711966400,21838.43\n");
		buf.append("2013-06-05,22141.67,22174.69,21933.92,22069.24,1516765700,22069.24");

		try (CsvBeanReader beanReader = new CsvBeanReader(new StringReader(buf.toString()), CsvPreference.EXCEL_PREFERENCE);){
			beanReader.getHeader(true);
			CellProcessor[] processors = new CellProcessor[] { 
					new ParseDate("yyyy-MM-dd"), new ParseDouble(), new ParseDouble(), new ParseDouble(),
					new ParseDouble(), new ParseDouble(), new ParseDouble() };

			String[] propertyNames = new String[] { "Date", "Open", "High", "Low", "Close", "Volume", "AdjClose" };

			RowObject obj = null;
			while ((obj = beanReader.read(RowObject.class, propertyNames, processors)) != null) {

				System.out.println(obj);
			}
		} 
	}

	public static class RowObject {

		private Date date;
		private double open, high, low, close, volume, adjClose;

		public RowObject() {
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public void setOpen(double open) {
			this.open = open;
		}

		public void setHigh(double high) {
			this.high = high;
		}

		public void setLow(double low) {
			this.low = low;
		}

		public void setClose(double close) {
			this.close = close;
		}

		public void setVolume(double volume) {
			this.volume = volume;
		}

		public void setAdjClose(double adjClose) {
			this.adjClose = adjClose;
		}

		public Date getDate() {
			return date;
		}

		public double getOpen() {
			return open;
		}

		public double getHigh() {
			return high;
		}

		public double getLow() {
			return low;
		}

		public double getClose() {
			return close;
		}

		public double getVolume() {
			return volume;
		}

		public double getAdjClose() {
			return adjClose;
		}

		public String toString() {
			StringBuilder buf = new StringBuilder();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			buf.append("RowObject { date=");
			buf.append(df.format(date));
			buf.append(", open=");
			buf.append(open);
			buf.append(", high=");
			buf.append(high);
			buf.append(", low=");
			buf.append(low);
			buf.append(", close=");
			buf.append(close);
			buf.append(", volume=");
			buf.append(volume);
			buf.append(", adjClose=");
			buf.append(adjClose);
			buf.append(" }");
			return buf.toString();
		}

	}

}
