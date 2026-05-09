package com.concessionaria.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.concessionaria.util.AppLogger;

public class ExceptionShield {

    private static final Logger LOGGER = AppLogger.getLogger(ExceptionShield.class.getName());

    private ExceptionShield() {}

    @FunctionalInterface
    public interface SafeRunnable {
        void run() throws Exception;
    }

    public static void runSafely(SafeRunnable action) {
        try {
            action.run();
        } catch (AppException e) {
            LOGGER.log(Level.WARNING, "Errore applicativo: {0}", e.getMessage());
            System.out.println("ERRORE: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore imprevisto: {0}", e.getClass().getSimpleName());
            System.out.println("ERRORE: Operazione non riuscita.");
        }
    }
}