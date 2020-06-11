package com.stackpulse.automation.framework.utils;


import com.stackpulse.automation.framework.logger.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {


    private StringUtil(){ }

    public static String convert(InputStream ips , long timeoutMilliSec) {

        long timeEnd = System.currentTimeMillis() + timeoutMilliSec;
        long zeroBytesTimeoutMilli = (long) Math.ceil(((double) timeoutMilliSec/10));
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(ips);
             BufferedReader br = new BufferedReader(isr)){
            String line;
            while (timeEnd > System.currentTimeMillis() &&
                    !Thread.currentThread().isInterrupted() &&
                    zeroBytesTimeoutMilli > 0) {
                if(ips.available() > 0){
                    while (!Thread.currentThread().isInterrupted() &&
                            (line = br.readLine()) != null) {
                        builder.append(line)
                                .append("\n");
                        LoggerFactory.LOG.debug(line);
                    }
                    break;
                }else{
                    long current = System.currentTimeMillis();
                    Thread.sleep(200);
                    zeroBytesTimeoutMilli -= System.currentTimeMillis() - current;
                }
            }
        }catch (Exception e){
            LoggerFactory.LOG.w(e , "failed to read input stream");
        }
        return builder.toString();
    }

}


