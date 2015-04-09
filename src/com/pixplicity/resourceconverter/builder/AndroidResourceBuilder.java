package com.pixplicity.resourceconverter.builder;

import com.pixplicity.resourceconverter.file.AndroidResourceFile;
import com.pixplicity.resourceconverter.file.XmlResourceFile;

public class AndroidResourceBuilder extends ResourceBuilder {

	public AndroidResourceBuilder(String[] languages) {
		super(languages);
	}

	@Override
	protected XmlResourceFile createResourceFile(String language, int column)
			throws Exception {
		return new AndroidResourceFile(language, column);
	}

}
