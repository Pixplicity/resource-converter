package com.pixplicity.resourceconverter.file;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

public class AndroidResourceFile extends XmlResourceFile {

	private final int column;

	public AndroidResourceFile(String language, int column) throws IOException,
			XMLStreamException, TransformerConfigurationException {
		super(language);
		this.column = column;
	}

	@Override
	protected void createTransformer(Transformer transformer)
			throws TransformerConfigurationException {
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
	}

	@Override
	protected File getDirectory() {
		String path = "values";
		if (!language.equals("en")) {
			path += "-" + language;
		}
		File dir = new File("android/res/" + path);
		return dir;
	}

	@Override
	protected String getFilename() {
		return "strings.xml";
	}

	@Override
	protected void writeHeader(XMLStreamWriter writer)
			throws XMLStreamException {
		writer.writeStartDocument("utf-8", "1.0");
		writer.writeStartElement("resources");
	}

	@Override
	protected void write(XMLStreamWriter writer, String key, String[] fields,
			String comment) throws XMLStreamException {
		boolean isGroup = false;
		int pos = key.indexOf("|");
		if (pos > -1) {
			// Drop group name
			key = key.substring(pos + 1);
			isGroup = true;
		}
		// Drop conditionals from key
		key = key.replace("%@", "");
		// Fix formatting of value
		String value = "";
		if (column < fields.length) {
			value = fields[column];
		}
		if (value.contains("?nn")) {
			value = value.replace("?", "?");
		}
		value = value.replace("'", "\\'");
		value = value.replace("...", "…");
		// We can't use generic data types
		value = value.replaceAll("%@", "%s");
		// Java only uses '%d' for signed integers
		value = value.replaceAll("%i", "%d");
		value = value.replaceAll("%([0-9]+)\\$@", "%$1\\$s");
		writeComment(writer, comment + "  <pre>" + value + "</pre>");
		if (isGroup) {
			writer.writeStartElement("item");
			writer.writeAttribute("quantity", key);
		} else {
			writer.writeStartElement("string");
			writer.writeAttribute("name", key);
			if (value.startsWith("http://") || value.startsWith("https://")) {
				writer.writeAttribute("formatted", "false");
			}
		}
		writer.writeCharacters(value.trim());
		writer.writeEndElement();
	}

	@Override
	protected void writeComment(XMLStreamWriter writer, String comment) throws XMLStreamException {
		if (language.equals("en")) {
			if (comment != null && comment.length() > 0) {
				super.writeComment(writer, " " + comment.trim() + " ");
			}
		}
	}

	@Override
	protected void writeFooter(XMLStreamWriter writer2)
			throws XMLStreamException {
		// Do nothing
	}

	@Override
	protected String openGroup(XMLStreamWriter writer, String key, String comment)
			throws XMLStreamException {
		int pos = key.indexOf("|");
		if (pos > -1) {
			String group = key.substring(0, pos);
			if (!group.equals(getGroup())) {
				writeComment(writer, comment);
				writer.writeStartElement("plurals");
				writer.writeAttribute("name", group);
			}
			return group;
		}
		return null;
	}

	@Override
	protected void closeGroup(XMLStreamWriter writer, String group) throws XMLStreamException {
		writer.writeEndElement();
	}

}
