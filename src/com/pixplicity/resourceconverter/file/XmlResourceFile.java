package com.pixplicity.resourceconverter.file;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public abstract class XmlResourceFile extends ResourceFile {

	private final StringWriter sw;
	private final XMLStreamWriter xmlWriter;

	private static Transformer transformer;

	public XmlResourceFile(String language) throws IOException,
			XMLStreamException, TransformerConfigurationException {
		super(language);
		if (transformer == null) {
			TransformerFactory factory = TransformerFactory.newInstance();
			transformer = factory.newTransformer();
			createTransformer(transformer);
		}
		XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
		sw = new StringWriter();
		xmlWriter = xmlof.createXMLStreamWriter(sw);
		writeHeader(xmlWriter);
	}

	@Override
	public final void writeKey(String key, String[] fields, String comment)
			throws Exception {
		write(xmlWriter, key, fields, comment);
	}

	protected abstract void createTransformer(Transformer transformer)
			throws TransformerConfigurationException;

	protected abstract void writeHeader(XMLStreamWriter writer2)
			throws XMLStreamException;

	protected abstract void write(XMLStreamWriter writer, String key,
			String[] fields, String comment) throws XMLStreamException;

	protected abstract void writeFooter(XMLStreamWriter writer2)
			throws XMLStreamException;

	protected void writeComment(XMLStreamWriter writer, String comment) throws XMLStreamException {
		writer.writeComment(comment);
	}

	@Override
	protected String openGroup(String key, String comment) throws Exception {
		return openGroup(xmlWriter, key, comment);
	}

	protected abstract String openGroup(XMLStreamWriter writer, String key, String comment)
			throws XMLStreamException;

	@Override
	protected void closeGroup(String group) throws Exception {
		closeGroup(xmlWriter, group);
	}

	protected abstract void closeGroup(XMLStreamWriter writer, String group)
			throws XMLStreamException;

	@Override
	public final void close() throws Exception {
		writeFooter(xmlWriter);
		xmlWriter.writeEndDocument();
		xmlWriter.flush();
		transformer.transform(
				new StreamSource(new StringReader(sw.toString())),
				new StreamResult(writer));
	}

	@Override
	public String toString() {
		return "ResourceFile{" + language + "}";
	}

}
