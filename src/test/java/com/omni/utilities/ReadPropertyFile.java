package com.omni.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ReadPropertyFile {
	FrameworkConstants frameworkConstants = new FrameworkConstants();
	private Properties property = new Properties();
	private final Map<String, String> CONFIGMAP = new HashMap<>();

	{
		FileInputStream file;
		try {
			file = new FileInputStream(frameworkConstants.getConfigFilePath());
			property.load(file);
			for (Map.Entry<Object, Object> entry : property.entrySet()) {
				CONFIGMAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()).trim());
				// trim() method will remove the trailing & leading spaces
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(String key) throws Exception {
		if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key))) {
			throw new Exception("Property name <" + key + "> is not found. Please check config.properties");
		}
		return CONFIGMAP.get(key);

	}
}