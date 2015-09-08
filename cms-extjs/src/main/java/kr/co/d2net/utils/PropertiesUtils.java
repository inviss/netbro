package kr.co.d2net.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

	final static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

	public static Map<String, String> loadAsMap(InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException("input stream is null.");
		}
		return toMap(load(input));
	}

	public static Properties load(InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException("input stream is null.");
		}
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (Exception e) {
			logger.error("Property load error", e);
			return null;
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.warn("Property close error", e);
			}
		}
		return properties;
	}

	public static Properties load(String resource, Class<?> callingClass) {
		InputStream input = ClassLoaderUtils.getResourceAsStream(resource, callingClass);
		return load(input);
	}

	public static Properties load(File file) {
		try {
			return load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public static Map<String, String> loadAsMap(File file) {
		try {
			return loadAsMap(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public static Map<String, String> loadAsMap(String resource,Class<?> callingClass) {
		InputStream input = ClassLoaderUtils.getResourceAsStream(resource, callingClass);
		return loadAsMap(input);
	}

	public static Map<String, String> toMap(Properties properties) {
		Map<String, String> map = new HashMap<String, String>();
		if (properties == null) {
			return map;
		}
		Enumeration<?> e = properties.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String val = properties.getProperty(key.toString(), "");
			map.put(key, val);
		}
		return map;
	}

}