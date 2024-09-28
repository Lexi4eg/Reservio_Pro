package org.Application;

import org.Logging.LogConsumerService;

public class LoggingOutputService {
    public static void main(String[] args) {
        LogConsumerService logConsumerService = new LogConsumerService();
        logConsumerService.consumeLogs();
    }
}