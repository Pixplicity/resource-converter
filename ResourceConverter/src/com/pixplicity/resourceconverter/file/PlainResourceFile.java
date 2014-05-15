package com.pixplicity.resourceconverter.file;

import java.io.IOException;
import java.io.Writer;

public abstract class PlainResourceFile extends ResourceFile {

	public PlainResourceFile(String language) throws IOException {
		super(language);
		writeHeader(writer);
	}

	@Override
	public void writeKey(String key, String[] fields, String comment)
			throws Exception {
		write(writer, key, fields, comment);
	}

	protected abstract void writeHeader(Writer writer) throws IOException;

	protected abstract void write(Writer writer, String key,
			String[] fields, String comment) throws IOException;

	protected abstract void writeFooter(Writer writer) throws IOException;

	@Override
	public void close() throws Exception {
		writer.flush();
		writeFooter(writer);
		writer.close();
	}

}
