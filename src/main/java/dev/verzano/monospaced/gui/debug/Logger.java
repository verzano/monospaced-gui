package dev.verzano.monospaced.gui.debug;

import java.time.ZonedDateTime;

public class Logger {
    private final String loggerName;

    public Logger(String loggerName) {
        this.loggerName = loggerName;
    }

    public void log(String message) {
        LoggerService.queueLog(loggerName, message, ZonedDateTime.now());
    }
}
