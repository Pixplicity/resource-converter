package com.pixplicity.resourceconverter.builder;

import java.util.Arrays;
import java.util.LinkedList;

import com.pixplicity.resourceconverter.file.ResourceFile;

public abstract class ResourceBuilder {

	protected final LinkedList<ResourceFile> resources = new LinkedList<ResourceFile>();

	protected static final String[] IGNORE_COLUMNS = new String[] {
			"",
			"KEY",
			"DESCRIPTION",
			"os",
			"Lengte NL",
			"Lengte FR",
			"Te lang",
	};

	public ResourceBuilder(String[] languages) {
		nextColumn: for (int i = 0; i < languages.length; i++) {
			String language = languages[i];
			for (String ignore : IGNORE_COLUMNS) {
				if (ignore.equalsIgnoreCase(language)) {
					continue nextColumn;
				}
			}
			try {
				resources.add(createResourceFile(language, i));
			} catch (Exception e) {
				System.err.println("Failed creating ResourceFile for " + language);
				e.printStackTrace();
			}
		}
	}

	protected abstract ResourceFile createResourceFile(String language, int i)
			throws Exception;

	public final void writeStart() throws Exception {
		for (ResourceFile resource : resources) {
			resource.writeStart();
		}
	}

	public final void write(int row, String key, String[] fields, String comment) {
		for (ResourceFile resource : resources) {
			try {
				resource.write(key, fields, comment);
			} catch (Exception e) {
				System.err.println("Error on CSV line " + row
						+ " for " + resource);
				System.err.println(fields.length + ": "
						+ Arrays.toString(fields));
				e.printStackTrace();
			}
		}
	}

	public final void writeEnd() throws Exception {
		for (ResourceFile resource : resources) {
			resource.writeEnd();
		}
	}

	public final void close() throws Exception {
		for (ResourceFile resource : resources) {
			resource.close();
		}
	}

}
