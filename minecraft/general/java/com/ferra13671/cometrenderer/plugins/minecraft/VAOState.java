package com.ferra13671.cometrenderer.plugins.minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.Stack;

//TODO maybe do something with this
public class VAOState {
    private static final Stack<Integer> stack = new Stack<>();

    public static void push() {
        stack.push(GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING));
    }

    public static void pop() {
        GL30.glBindVertexArray(stack.peek());
        stack.pop();
    }
}
