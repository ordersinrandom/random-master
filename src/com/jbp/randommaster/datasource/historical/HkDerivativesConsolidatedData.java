package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;

/**
 * Enrich the HkDerivativesConsolidatedTuple with a given timestamp.
 *
 */
public class HkDerivativesConsolidatedData implements HistoricalData<HkDerivativesConsolidatedTuple> {

	private static final long serialVersionUID = 3837406754718477022L;

	private LocalDateTime timestamp;
	private HkDerivativesConsolidatedTuple data;

	public HkDerivativesConsolidatedData(LocalDateTime timestamp, HkDerivativesConsolidatedTuple data) {
		this.timestamp = timestamp;
		this.data = data;
	}

	public HkDerivativesConsolidatedData(LocalDateTime timestamp, Iterable<HkDerivativesTRTuple> tuples) {
		this.timestamp = timestamp;
		this.data = HkDerivativesConsolidatedTuple.consolidate(tuples);
	}

	@Override
	public int compareTo(HistoricalData<? extends HistoricalDataTuple> obj) {
		if (equals(obj))
			return 0;
		else {
			return timestamp.compareTo(obj.getTimestamp());
		}
	}

	@Override
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public HkDerivativesConsolidatedTuple getData() {
		return data;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e1) {
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (obj instanceof HkDerivativesConsolidatedData) {
			HkDerivativesConsolidatedData d = (HkDerivativesConsolidatedData) obj;
			return this.timestamp.equals(d.timestamp) && this.data.equals(d.data);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return timestamp.hashCode() ^ data.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder buf=new StringBuilder(300);
		buf.append("HkDerivativesConsolidatedData { timestamp=");
		buf.append(timestamp);
		buf.append(", data=");
		buf.append(data);
		buf.append(" }");
		return buf.toString();
	}
	
}
