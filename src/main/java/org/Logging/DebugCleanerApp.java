package org.Logging;

import org.Logging.DebugMessageCleaner;

public class DebugCleanerApp {
    public static void main(String[] args) {
        DebugMessageCleaner cleaner = new DebugMessageCleaner();
        cleaner.cleanDebugMessages();
    }
}