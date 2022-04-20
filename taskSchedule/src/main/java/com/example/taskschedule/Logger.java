package com.example.taskschedule;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     *
     * @param text
     */
    public static void info(String text)
    {
        log.info(text);
    }

    /**
     * @param text
     */
    public static void error(String text)
    {
        log.error(text);
    }

    /**
     * @param text
     * @param o
     */
    public static void error(String text, Object o)
    {
        log.error(text, o);
    }

    /**
     * @param text
     */
    public static void warn(String text)
    {
        log.warn(text);
    }

    /**
     * @param text
     * @param o
     */
    public static void warn(String text, Object o)
    {
        log.warn(text, o);
    }
}