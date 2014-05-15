package com.pixplicity.resourceconverter.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public abstract class ResourceFile {

	// protected final BufferedOutputStream out;
	protected final OutputStreamWriter writer;
	protected final String language;

	private String mLastGroup;


	public ResourceFile(String language) throws FileNotFoundException {
		this.language = language;
		File dir = getDirectory();
		dir.mkdirs();
		File file = new File(dir, getFilename());
		System.out.println("Writing " + file + "...");
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file), "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		// out = new BufferedOutputStream(new FileOutputStream(file));
	}

	protected abstract String getFilename();

	protected abstract File getDirectory();

	public void writeStart() {
		// By default, do nothing
	}

	/**
	 * Writes the current key to the resource file.
	 * 
	 * @param key
	 * @param fields
	 * @param comment
	 * @throws Exception
	 */
	public final void write(String key, String[] fields, String comment) throws Exception {
		String group = openGroup(key, comment);
		if ((group == null || !group.equals(mLastGroup)) && mLastGroup != null) {
			closeGroup(mLastGroup);
		}
		mLastGroup = group;
		writeKey(key, fields, comment);
	}

	public void writeEnd() throws Exception {
		if (mLastGroup != null) {
			closeGroup(mLastGroup);
		}
	}

	public String getGroup() {
		return mLastGroup;
	}

	/**
	 * Writes the current key to the resource file.
	 * 
	 * @param key
	 * @param fields
	 * @param comment
	 * @throws Exception
	 */
	protected abstract void writeKey(String key, String[] fields, String comment)
			throws Exception;

	/**
	 * Opens a key group based on the current key name, returning the name of the group when
	 * applicable.
	 * 
	 * @param key
	 * @param comment
	 */
	protected abstract String openGroup(String key, String comment) throws Exception;

	/**
	 * Closes a key group previously opened through {@link ResourceFile#openGroup(String)}.
	 * 
	 * @param key
	 */
	protected abstract void closeGroup(String group) throws Exception;

	/**
	 * Closes the resource file.
	 * 
	 * @throws Exception
	 */
	public abstract void close() throws Exception;

}
