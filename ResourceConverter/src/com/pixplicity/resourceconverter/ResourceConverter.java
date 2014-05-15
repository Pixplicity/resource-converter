package com.pixplicity.resourceconverter;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.skife.csv.CSVReader;
import org.skife.csv.ReaderCallback;
import org.skife.csv.SimpleReader;

import com.pixplicity.resourceconverter.builder.AndroidResourceBuilder;
import com.pixplicity.resourceconverter.builder.IOSResourceBuilder;
import com.pixplicity.resourceconverter.builder.ResourceBuilder;

public class ResourceConverter {

	protected final static LinkedList<ResourceBuilder> builders = new LinkedList<ResourceBuilder>();

	public static void main(String[] args) {
		convert("Parkmobile Strings - Parkmobile.csv");

		System.out.println("Done!");
	}

	/**
	 * Convert a file. The file is expected to reside in the root of the
	 * project.
	 * 
	 * @param filename
	 */
	protected static void convert(String filename) {
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			CSVReader reader = new SimpleReader();
			reader.setQuoteCharacters(new char[] {
					'"'
			});
			// Disable character escaping, because '\' was causing '\n\n' to become 'nn'
			reader.setEscapeCharacter((char) 0);
			FileInputStream is = new FileInputStream(filename);
			InputStreamReader ir = new InputStreamReader(is, "UTF8");
			for (ResourceBuilder builder : builders) {
				builder.writeStart();
			}
			reader.parse(ir, new ReaderCallback() {

				int row = 0;

				@Override
				public void onRow(String[] columns) {
					row++;
					if (row == 1) {
						builders.add(new AndroidResourceBuilder(columns));
						builders.add(new IOSResourceBuilder(columns));
					} else {
						String key = columns[0];
						String comment = columns[1];
						for (ResourceBuilder builder : builders) {
							builder.write(row, key, columns, comment);
						}
					}
				}
			});
			for (ResourceBuilder builder : builders) {
				builder.writeEnd();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				for (ResourceBuilder builder : builders) {
					builder.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
