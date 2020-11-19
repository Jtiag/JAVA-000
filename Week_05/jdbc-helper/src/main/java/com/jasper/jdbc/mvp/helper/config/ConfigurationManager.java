package com.jasper.jdbc.mvp.helper.config;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author jasper
 */
@Slf4j
public class ConfigurationManager {
    private static final Properties prop = new Properties();

    static {
        try {
            InputStream in = ConfigurationManager.class
                    .getClassLoader().getResourceAsStream("my.properties");
            prop.load(in);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     * 获取整数类型的配置项
     *
     * @param key
     * @return value
     */
    public static Integer getInteger(String key) {
        String value = getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            log.error("", e);
        }
        return 0;
    }

    /**
     * 获取布尔类型的配置项
     *
     * @param key
     * @return value
     */
    public static Boolean getBoolean(String key) {
        String value = getProperty(key);
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 获取Long类型的配置项
     *
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        String value = getProperty(key);
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            log.error("", e);
        }
        return 0L;
    }

}
