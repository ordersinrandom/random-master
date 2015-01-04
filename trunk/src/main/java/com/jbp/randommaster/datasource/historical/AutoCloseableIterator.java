package com.jbp.randommaster.datasource.historical;

import java.util.Iterator;

public interface AutoCloseableIterator<T> extends Iterator<T>, AutoCloseable {

	public boolean isClosed();
	
	public void close();
}
