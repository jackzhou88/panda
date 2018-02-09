package com.panda.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: dave01.zhou  Time: 2018/2/9 11:18
 */
public class DynamicProperties {
    private static final Logger logger = LoggerFactory.getLogger(DynamicProperties.class);
    private static final String DEFAULT_RESOURCE = "application.properties";
    private static Properties properties;

    static {
        properties = loadFromResource(DEFAULT_RESOURCE);
    }

    public static Properties loadFromResource(String resource) {
        Properties props = new Properties();
        try (InputStream in = DynamicProperties.class.getResourceAsStream(resource)) {
            props.load(in);
        } catch (Exception e) {
            logger.error("Error load: " + resource, e);
        }
        return props;
    }

    public static Properties loadFromeFile(String fileName) {
        Properties props = new Properties();
        try (InputStream in = new BufferedInputStream(new FileInputStream(fileName))) {
            props.load(in);
            properties.putAll(props);
        } catch (Exception e) {
            logger.error("Error load file: {}" + fileName, e);
        }
        return props;
    }

    public static String getValue(String key) {
        return getValue(key, null);
    }

    public static String getValue(String key, String defaultValue) {
        String value = getSystem(key);
        if (value != null) {
            return value;
        }
        value = getEnv(key);
        if (value != null) {
            return value;
        }
        value = getProp(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    private static String getSystem(String key) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        String upperKey = key.toUpperCase();
        value = System.getProperty(upperKey);
        return value;
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        String upperKey = key.toUpperCase();
        value = System.getenv(upperKey);
        return value;
    }

    private static String getProp(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value;
        }
        String upperKey = key.toUpperCase();
        value = properties.getProperty(upperKey);
        return value;
    }
}
