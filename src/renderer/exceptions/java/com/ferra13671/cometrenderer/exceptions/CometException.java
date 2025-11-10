package com.ferra13671.cometrenderer.exceptions;

/**
 * Основной класс ошибки, который расширяется другими ошибками.
 * Хранит в себе описание исключения, его детали, возможные причины и варианты решения.
 *
 * @see ExceptionPrinter
 */
public class CometException extends RuntimeException {
    /** Описание ошибки. **/
    protected final String description;
    /** Детали ошибки. **/
    protected final String details;
    /** Возможные причины ошибки. **/
    protected final String[] reasons;
    /** Возможные решения ошибки. **/
    protected final String[] solutions;

    /**
     * @param description описание ошибки.
     * @param details детали ошибки.
     * @param reasons возможные причины ошибки.
     * @param solutions возможные решения ошибки.
     */
    public CometException(String description, String details, String[] reasons, String[] solutions) {
        this.description = description;
        this.details = details;
        this.reasons = reasons;
        this.solutions = solutions;
    }

    /**
     * Возвращает описание ошибки.
     *
     * @return описание ошибки.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает детали ошибки.
     *
     * @return детали ошибки.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Возвращает возможные причины ошибки.
     *
     * @return возможные причины ошибки.
     */
    public String[] getReasons() {
        return reasons;
    }

    /**
     * Возвращает возможные решения ошибки.
     *
     * @return возможные решения ошибки.
     */
    public String[] getSolutions() {
        return solutions;
    }
}
