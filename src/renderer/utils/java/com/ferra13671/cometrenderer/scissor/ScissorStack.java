package com.ferra13671.cometrenderer.scissor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Стек для областей, используемых ножницами.
 */
public class ScissorStack {
    /** Стек. **/
    private final Deque<ScissorRect> stack = new ArrayDeque<>();
    /** Текущая область ножниц. **/
    private ScissorRect current;

    /**
     * Увеличивает указатель стека на единицу и устанавливает новую область ножниц.
     *
     * @param rect новая область ножниц.
     */
    public void push(ScissorRect rect) {
        rect = rect.fixRect();
        ScissorRect scissorRect = rect;
        if (current != null)
            scissorRect = Objects.requireNonNullElse(rect.intersection(current), new ScissorRect(0, 0, 0, 0));
        stack.addLast(scissorRect);
        current = scissorRect;
    }

    /**
     * Возвращает текущую область ножниц.
     *
     * @return текущая область ножниц.
     */
    public ScissorRect current() {
        return current;
    }

    /**
     * Уменьшает указатель стека на единицу и удаляет последнюю область ножниц.
     */
    public void pop() {
        //Если стек пустой, кидаем исключение
        if (stack.isEmpty()) {
            throw new IllegalStateException("Scissor stack underflow");
        } else {
            stack.removeLast();
            current = stack.peekLast();
        }
    }
}
