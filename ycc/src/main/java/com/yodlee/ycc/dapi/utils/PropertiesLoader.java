package com.yodlee.ycc.dapi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {

	static Properties prop = null;
	/**
	 * load properties file
	 */
	public static final Properties load(String propFile) {
		InputStream in = null;
		if (prop == null || prop.isEmpty()) {
			prop = new Properties();
			try {
				in = PropertiesLoader.class.getResourceAsStream(propFile);
				if (in == null)
					throw new RuntimeException("Unable to locate " + propFile);
				prop.load(in);
				in.close();
			} catch (IOException e) {
				throw new RuntimeException("Error Loading  " + propFile, e);
			}
		}
		return prop;
	}

	public static Properties getInstance() {
		if (prop == null || prop.isEmpty()) {
			prop = load("Config.properties");
		}
		return prop;
	}
	
}
