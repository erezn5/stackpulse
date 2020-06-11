package com.stackpulse.automation.framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.stackpulse.automation.framework.logger.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class FileUtil {

    private static final Gson GSON = new GsonBuilder().create();

    private FileUtil() { }
    public static <T> T readJsonFile(String relativePath , Class<T> clazz){
        try(InputStream ips = clazz.getClassLoader().getResourceAsStream(relativePath);
            InputStreamReader ipsr = new InputStreamReader(ips)){
            JsonReader reader = new JsonReader(ipsr);
            return  GSON.fromJson(reader , clazz);
        }catch (IOException e){
            System.err.println("Failed to convert resource file '" + relativePath + "' stream to properties, cause: " + e.getMessage());
            return null;
        }
    }

    public static Properties createPropertiesFromResource(Class clazz , String relativePath) {
        try(InputStream ips = clazz.getClassLoader().getResourceAsStream(relativePath)){
            Properties properties = new Properties();
            properties.load(ips);
            return properties;
        }catch (IOException e){
            System.err.println("Failed to convert resource'" + relativePath + "'stream to properties, cause: " + e.getMessage());
            return null;
        }
    }

    public static void createFolder(File folder , boolean recursive){
        if(folder.exists() && folder.isDirectory()){
            LoggerFactory.LOG.i(folder.getName() + " directory already exist");
        }else if((recursive ? folder.mkdirs() : folder.mkdir())){
            LoggerFactory.LOG.i(folder.getName() + " directory created successfully");
        }else{
            LoggerFactory.LOG.error("failed to create '" + folder.getName() + "' directory");
        }
    }

}

