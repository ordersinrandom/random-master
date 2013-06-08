package com.jbp.randommaster.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utilities for zip files
 *
 */
public class ZipUtils {

	/**
	 * Helper function to unzip the entries in the zip file into a collection of files
	 * 
	 * @param zipFile The source zip file
	 * @param tempFilePrefix The output temp file prefix. If null this function will auto generate a prefix.
	 * @param deleteOnExit States whether the temp files will be cleaned on JVM exit.
	 * @return A Map from zip entry name to the temp file saved.
	 * @throws IOException If there is any IO error during the unzip process.
	 */
	public static Map<String, File> unzipToTempFiles(ZipFile zipFile, String tempFilePrefix, boolean deleteOnExit) throws IOException {
		
		String prefixToBeUsed = "randommaster_unzip_temp_";
		if (tempFilePrefix!=null)
			prefixToBeUsed=tempFilePrefix;
		
		Map<String, File> result=new TreeMap<String, File>();
		
		for (Enumeration<? extends ZipEntry> en=zipFile.entries();en.hasMoreElements();) {
			ZipEntry entry=en.nextElement();
			
			// get the input stream
			InputStream ins=zipFile.getInputStream(entry);
			
			String entryName=entry.getName();
			if (!entry.isDirectory()) {
				// unzip it to the temp file first.
				File tempUnzippedFile=File.createTempFile(prefixToBeUsed, null);
				String tempFilename = tempUnzippedFile.getAbsolutePath();
				byte[] outBuf = new byte [1024*1000]; // 1M buffer
				FileOutputStream outs=new FileOutputStream(tempFilename);
				int count=-1;
				while ((count=ins.read(outBuf))!=-1) {
					outs.write(outBuf, 0, count);
				}
				ins.close();
				outs.close();
				// finished unzip
				
				if (deleteOnExit)
					tempUnzippedFile.deleteOnExit();
				
				result.put(entryName, tempUnzippedFile);
			}
		}
		
		return result;
	}	
	
}
