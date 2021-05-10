package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {
	static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static final String INIT_FILE_PATH = "resources/config.properties";
	private static final Properties properties;
	
	static {
		properties = new Properties();
		
		try {
			properties.load(Files.newBufferedReader(Paths.get(INIT_FILE_PATH), StandardCharsets.UTF_8));
			
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	public static String getProperty(final String key) {
		return getProperty(key, "");
	}
	
    public static String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
