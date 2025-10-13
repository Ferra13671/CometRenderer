package com.ferra13671.cometrenderer.exceptions;

/*
 * Глобально исключение, которое расширяется другими исключениями. Хранит в себе описание исключения и варианты его решения.
 */
public class CometException extends RuntimeException {
    protected final String description;
    protected final String details;
    protected final String[] reasons;
    protected final String[] solutions;

    public CometException(String description, String details, String[] reasons, String[] solutions) {
        this.description = description;
        this.details = details;
        this.reasons = reasons;
        this.solutions = solutions;
    }

    /*
     * Возвращает краткое описание исключения
     */
    public String getDescription() {
        return description;
    }

    /*
     * Возвращает полное описание исключения
     */
    public String getDetails() {
        return details;
    }

    /*
     * Возвращает возможные причины происхождения исключения
     */
    public String[] getReasons() {
        return reasons;
    }

    /*
     * Возвращает возможные решения исключения
     */
    public String[] getSolutions() {
        return solutions;
    }
}
