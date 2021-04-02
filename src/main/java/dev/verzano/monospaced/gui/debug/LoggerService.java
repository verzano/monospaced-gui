package dev.verzano.monospaced.gui.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class LoggerService {
    private LoggerService() {
    }

    private static final Queue<LogCommand> logQueue = new LinkedBlockingQueue<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss.SSSZ");

    private static boolean enabled = false;

    public static void enable() {
        try {
            File logFile = new File(System.getProperty("java.io.tmpdir")
                    + File.separator + "log"
                    + File.separator + "log.txt");
            logFile.createNewFile();
            enable(new FileOutputStream(logFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void enable(OutputStream os) {
        new Thread(() -> {
            try (PrintWriter pw = new PrintWriter(os)) {
                while (true) {
                    if (!logQueue.isEmpty()) {
                        printLog(pw, logQueue.remove());
                        pw.flush();
                    } else {
                        Thread.sleep(50);
                    }
                }
            } catch (InterruptedException e) {
                // TODO where do you log when your logger dies?
                e.printStackTrace();
            }
        }).start();
        enabled = true;
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName());
    }

    protected static void queueLog(String loggerName, String message, ZonedDateTime time) {
        if (enabled) {
            logQueue.add(new LogCommand(loggerName, message, time));
        }
    }

    protected static void printLog(PrintWriter pw, LogCommand logCommand) {
        pw.printf("%s -- %s -- %s\n", formatter.format(logCommand.time), logCommand.loggerName, logCommand.message);
    }

    private static class LogCommand {
        private final String loggerName;
        private final String message;
        private final ZonedDateTime time;

        private LogCommand(String loggerName, String message, ZonedDateTime time) {
            this.loggerName = loggerName;
            this.message = message;
            this.time = time;
        }
    }
}
