package com.pixplicity.resourceconverter.builder;

import com.pixplicity.resourceconverter.file.IOSResourceFile;
import com.pixplicity.resourceconverter.file.ResourceFile;

public class IOSResourceBuilder extends ResourceBuilder {

	public IOSResourceBuilder(String[] languages) {
		super(languages);
	}

	@Override
	protected ResourceFile createResourceFile(String language, int column)
			throws Exception {
		return new IOSResourceFile(language, column);
	}

}
