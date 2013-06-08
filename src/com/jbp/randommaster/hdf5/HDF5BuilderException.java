package com.jbp.randommaster.hdf5;

public class HDF5BuilderException extends RuntimeException {

	private static final long serialVersionUID = 1834171420252219058L;

	
	public HDF5BuilderException(String msg) {
		super(msg);
	}
	
	public HDF5BuilderException(String msg, Throwable t) {
		super(msg,t);
	}
	
	
}
