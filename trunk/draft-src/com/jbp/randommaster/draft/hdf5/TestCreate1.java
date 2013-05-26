package com.jbp.randommaster.draft.hdf5;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

public class TestCreate1 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		String tempFolder = System.getProperty("java.io.tmpdir");
		
		String filename = tempFolder+"test-hdf5.h5";
		
		File f=new File(filename);
		H5File 	h5File=(H5File) FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5).createInstance(filename, FileFormat.WRITE);
		System.out.println(filename+" opened");
		
		int count=480; // 1 day ticks

		Datatype[] memberDatatypes = {
			     new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, -1),
			     new H5Datatype(H5Datatype.CLASS_FLOAT, 8, H5Datatype.NATIVE, -1) };		
		
		
		H5Group g=(H5Group) h5File.get("/TestGroup");

		int datasetsCount = 250 * 10; // 10 years, stored daily
		System.out.println("number of datasets = "+datasetsCount);
		
		Random rand2=new Random();
		
		
		for (int x=0;x<datasetsCount;x++) {
			Date now=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String dsName="DS_"+df.format(now)+"."+rand2.nextInt();
			H5CompoundDS testDS=(H5CompoundDS) h5File.createCompoundDS(dsName,  
					g, new long[] { count }, null, new long[] {count}, 0, 
					new String[] { "time", "value" }, memberDatatypes, 
					new int[] { 1, 1 }, null);
					
			//System.out.println("Data set created: "+dsName);
			
			testDS.init();
			
			double[] time=new double [count];
			double[] val = new double [count];
			val[0]=50;
			time[0]=0;
			
			double mu=0.12;
			double sigma=0.2;
			double dt=1.0/count;
			
			Random rand=new Random();
			for (int i=1;i<count;i++) {
				time[i]=time[i-1]+dt;
				val[i]=val[i-1]*Math.exp((mu-0.5*sigma*sigma)*dt + sigma * rand.nextGaussian() * Math.sqrt(dt)); 
			}
			//System.out.println("data generated");
			
			
			@SuppressWarnings("rawtypes")
			Vector v=new Vector();
			v.add(time);
			v.add(val);
			
			testDS.write(v);
			if (x%1000==0) {
				double percent = ((double) x)/datasetsCount;
				DecimalFormat decF=new DecimalFormat("0.00%");
				System.out.println("data written for round "+x+"/"+datasetsCount+", "+decF.format(percent));
			}
		}
		
		h5File.close();
		System.out.println(filename+" closed");
		
		
	}

}

