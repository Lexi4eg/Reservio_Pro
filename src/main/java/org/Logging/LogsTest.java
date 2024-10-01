package org.Logging;

public class LogsTest {
    public static void main (String[] args) {
        LoggingService loggingService = new LoggingService();

        while (true) {
            loggingService.log("1","This is a log message");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
