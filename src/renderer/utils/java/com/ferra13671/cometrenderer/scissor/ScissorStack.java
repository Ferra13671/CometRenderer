package com.ferra13671.cometrenderer.scissor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/*
 * Стек для областей ножниц OpenGL
 */
public class ScissorStack {
    private final Deque<ScissorRect> stack = new ArrayDeque<>();
    private ScissorRect current;

    /*
     * Увеличивает указатель стека на единицу и устанавливает новую область ножниц
     */
    public void push(ScissorRect rect) {
        rect = rect.fixRect();
        ScissorRect scissorRect = rect;
        if (current != null)
            scissorRect = Objects.requireNonNullElse(rect.intersection(current), new ScissorRect(0, 0, 0, 0));
        stack.addLast(scissorRect);
        current = scissorRect;
    }

    /*
     * Возвращает текущую область ножниц
     */
    public ScissorRect current() {
        return current;
    }

    /*
     * Уменьшает указатель стека на единиу и удаляет последнюю область ножниц
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
