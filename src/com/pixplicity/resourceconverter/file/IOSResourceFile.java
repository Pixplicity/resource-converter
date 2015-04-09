package com.pixplicity.resourceconverter.file;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;

public class IOSResourceFile extends PlainResourceFile {

	private final int column;

	public IOSResourceFile(String language, int column) throws IOException,
			XMLStreamException, TransformerConfigurationException {
		super(language);
		this.column = column;
		// TODO set additional fields
	}

	@Override
	protected File getDirectory() {
		// TODO specify the correct path
		String path = "values-" + language;
		return new File("ios/" + path);
	}

	@Override
	protected String getFilename() {
		// TODO specify the correct filename
		return "Localizable.strings";
	}

	@Override
	protected void writeHeader(Writer writer) throws IOException {
		writer.write("/* Auto-generated iOS string resource */\n");
	}

	@Override
	protected void write(Writer writer, String key, String[] fields,
			String comment) throws IOException {
		writer.write("\n");
		if (comment != null) {
			writer.write("/* " + comment.trim() + " */\n");
		}
		String value = "";
		if (column < fields.length) {
			value = fields[column];
		}
		value = value.replace("\"", "\\\"");
		writer.write("\"" + key + "\" = \"" + value + "\";\n");
	}

	@Override
	protected void writeFooter(Writer writer) throws IOException {
	}

	@Override
	protected String openGroup(String key, String comment) throws Exception {
		// TODO Not implemented
		return null;
	}

	@Override
	protected void closeGroup(String group) throws Exception {
		// TODO Not implemented
	}

}
