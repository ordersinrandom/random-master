package com.jbp.randommaster.draft.yield;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.code.jyield.Generator;
import com.google.code.jyield.YieldUtils;
import com.google.code.jyield.Yieldable;

public class TestYield1 {

	public static class FileLineGenerator implements Generator<String>, AutoCloseable {

		private BufferedReader reader;

		public FileLineGenerator(File srcFile) throws FileNotFoundException {
			reader = new BufferedReader(new FileReader(srcFile));
		}

		@Override
		public void generate(Yieldable<String> y) {

			if (reader == null)
				return;

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					y.yield(line);
				}

			} catch (IOException ioe) {
				throw new RuntimeException("Catch exception when reading file", ioe);
			}

		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			if (reader != null)
				reader.close();
		}

	}

	public static void main(String[] args) throws IOException {

		// create temp file
		File tempFile = File.createTempFile("test-yield", "temp");
		tempFile.deleteOnExit();

		System.out.println("Generating temp file: " + tempFile.getAbsolutePath());
		int linesCount = 100000000;

		int displayLine = 20000000;

		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

		for (int i = 1; i <= linesCount; i++)
			pw.println("Generated Data Line " + i);
		pw.close();
		
		double sizeInMb = ((double) tempFile.length())/1000/1000; 

		System.out.println("File generated: " + tempFile.getAbsolutePath() + ", size(Mb) = "+sizeInMb);

		try (FileLineGenerator generator = new FileLineGenerator(tempFile)) {
			int counter = 1;
			for (String line : YieldUtils.toIterable(generator)) {
				if (counter % displayLine == 0) {
					System.out.println("At " + counter + ": result line = " + line);
				}
				counter++;
			}
		}
		
		System.out.println("Done");

	}

}
