package com.solenia.commons.logging;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

import java.net.PortUnreachableException;

public final class AppLogger {

   /* private final String module;

    private final String event;

    private final String succes;

    private final String message;

    private final String path;

    private final Map<String,Object> data;*/

    private AppLogger(){

    }


    public static void info(
            Logger logger,
            String event,
            String message
    ){
        info(logger,event,message,null);
    }

    public static void info(
            Logger logger,
            String event,
            String message,
            String detail
    ){
        write(logger, Level.INFO, event, message, detail);
    }

    public static void debug(
            Logger logger,
            String event
    ){
        debug(logger, event, null, null);
    }

    public static void debug(
            Logger logger,
            String event,
            String message
    ){
        debug(logger, event, message, null);
    }

    public static void debug(
            Logger logger,
            String event,
            String message,
            String detail
    ){
        write(logger, Level.DEBUG, event, message, detail);
    }

    private static void write(
            Logger logger,
            Level level,
            String event,
            String message,
            String detail
    ){
        LoggingEventBuilder builder = logger.atLevel(level)
                .addKeyValue("event", event);
        if(detail != null){
            builder.addKeyValue("detail", detail);
        }
        builder.log(message);
    }
}
