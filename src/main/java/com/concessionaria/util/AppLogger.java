package com.concessionaria.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

    private static boolean initialized = false;

    private AppLogger() {}

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);

        if (!initialized) {
            initialized = true;

            Logger root = Logger.getLogger("");
            for (var h : root.getHandlers()) {
                root.removeHandler(h);
            }

            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter());

            root.addHandler(handler);
            root.setLevel(Level.INFO);
        }

        return logger;
    }
}
