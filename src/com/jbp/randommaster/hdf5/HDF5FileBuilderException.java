package com.jbp.randommaster.hdf5;

public class HDF5FileBuilderException extends RuntimeException {

	private static final long serialVersionUID = 1834171420252219058L;

	
	public HDF5FileBuilderException(String msg) {
		super(msg);
	}
	
	public HDF5FileBuilderException(String msg, Throwable t) {
		super(msg,t);
	}
	
	
}
