package com.jbp.randommaster.draft.hdf5;

import java.util.LinkedList;
import java.util.List;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

public class TestLoadHDF5 {

	public static void main(String[] args) throws Exception {

		String filename = "D:\\dev\\quant.trading.data\\HkDerivativesTRHDF5\\201306_01_TR.csv.hdf5";

		FileFormat format = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
		H5File h5ReadOnlyFile = (H5File) format.createInstance(filename, FileFormat.READ);

		// read the dataset
		H5Group hsiGroup = (H5Group) h5ReadOnlyFile.get("Futures/HSI");
		
		List<String> allLeaves = getAllLeaves(h5ReadOnlyFile, hsiGroup);
		for (String s : allLeaves) {
			System.out.println(s);
		}
		
		h5ReadOnlyFile.close();
	}
	
	
	private static List<String> getAllLeaves(H5File h5ReadOnlyFile, H5Group g) throws Exception {
		
		// number of subgroups = 0 means terminal
		List<HObject> members = g.getMemberList();
		
		List<String> nestedPaths = new LinkedList<>();
		
		List<String> terminals = new LinkedList<>();
		
		if (members!=null) {
			for (HObject obj : members) {
				if (obj instanceof H5Group) {
					nestedPaths.add(((H5Group) obj).getFullName());
				}
				else {
					terminals.add(obj.getFullName());
				}
			}
		}
		
		List<String> result=new LinkedList<>();
		if (!terminals.isEmpty()) {
			result.addAll(terminals);
		}
		
		for (String p : nestedPaths) {
			H5Group subgroup = (H5Group) h5ReadOnlyFile.get(p);
			List<String> r = getAllLeaves(h5ReadOnlyFile, subgroup);
			result.addAll(r);
		}
		
		
		return result;
		
	}
	
	
}
