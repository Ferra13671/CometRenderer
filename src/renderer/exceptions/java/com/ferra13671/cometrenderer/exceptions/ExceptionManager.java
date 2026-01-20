package com.ferra13671.cometrenderer.exceptions;

import com.ferra13671.cometrenderer.CometRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Менеджер, отвечающий за управление возникающими ошибками.
 * Позволяет изменить тип действия для каждой ошибки по отдельности, либо изменить сам метод, вызываемый для определённого типа действия.
 */
public class ExceptionManager {
    private final HashMap<Class<? extends CometException>, ExceptionAction> actionHashMap = new HashMap<>();
    @Getter
    @Setter
    private ExceptionAction defaultAction = ExceptionAction.Throw;
    @Getter
    @Setter
    private Consumer<CometException> onThrowException = exception -> {
        throw exception;
    };
    @Getter
    @Setter
    private Consumer<CometException> onLogException = exception -> CometRenderer.getLogger().error(
            String.format(
                    "CometRenderer error occurred: class='%s' message='%s'",
                    exception.getClass().getName(),
                    exception.getMessage()
            )
    );

    public void manageException(CometException exception) {
        switch (this.actionHashMap.computeIfAbsent(exception.getClass(), clazz -> this.defaultAction)) {
            case Throw -> this.onThrowException.accept(exception);
            case Log -> this.onLogException.accept(exception);
        }
    }

    public void setExceptionAction(Class<? extends CometException> clazz, ExceptionAction exceptionAction) {
        this.actionHashMap.put(clazz, exceptionAction);
    }
}
