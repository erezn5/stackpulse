package com.stackpulse.automation.framework.logger;

import com.stackpulse.automation.framework.utils.FileUtil;
import com.stackpulse.automation.framework.conf.EnvConf;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class LoggerFactory {

    private static final Properties log4jProperties;
    public static final LoggerFormat LOG = new LoggerFormat();

    static {
        String log4jPath = EnvConf.getProperty("conf.log4j");
        if(log4jPath == null){
            log4jProperties = null;
            System.err.println("log4j.properties file don't exist!");
        }else{
            Properties properties = FileUtil.createPropertiesFromResource(LoggerFactory.class , log4jPath);
            if(properties == null){
                log4jProperties = null;
                throw new IllegalStateException("Failed to load '" + log4jPath + "' file");
            }else{
                log4jProperties = properties;
                PropertyConfigurator.configure(log4jProperties);
            }
        }
    }

    private LoggerFactory() {
    }


}


