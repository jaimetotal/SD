package tp_continua.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;

public class InternalLogger {

    private static Hashtable<Class, Logger> loggers;

    private Logger logger;

    static {
        loggers = new Hashtable<>();
    }

    private InternalLogger(Logger logger) {
        this.logger = logger;
    }


    public static InternalLogger getLogger(Class className) {
        if (!loggers.containsKey(className)) {
            loggers.put(className, LogManager.getLogger(className));
        }
        return new InternalLogger(loggers.get(className));
    }


    public void warn(String expression, Object... args) {
        warn(String.format(expression, args));
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String expression, Object... args) {
        info(String.format(expression, args));
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String expression, Object... args) {
        logger.error(expression, args);
    }

    public void error(Throwable error) {
        error(error, error.getMessage());
    }

    public void error(Throwable e, String message) {
        logger.error(message, e);
    }

    public void error(Throwable e, String expression, Object... args) {
        error(e, String.format(expression, args));
    }
}
