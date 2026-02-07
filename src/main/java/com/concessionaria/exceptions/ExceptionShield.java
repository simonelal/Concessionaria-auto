package com.concessionaria.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.concessionaria.util.AppLogger;

public class ExceptionShield {

    private static final Logger LOGGER = AppLogger.getLogger(ExceptionShield.class.getName());

    private ExceptionShield() {}

    public static void runSafely(Runnable action) {
        try {
            action.run();
        } catch (AppException e) {
            LOGGER.log(Level.WARNING, "Errore applicativo: " + e.getMessage());
            System.out.println("ERRORE: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore imprevisto: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            System.out.println("ERRORE: Operazione non riuscita.");
        }
    }
}
