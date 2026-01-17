package com.ferra13671.cometrenderer;

import org.joml.Vector4f;

import java.util.Stack;

public class ShaderColorStack {
    private static final Vector4f DEFAULT_COLOR = new Vector4f(1f, 1f, 1f, 1f);

    private final Stack<Vector4f> stack = new Stack<>();
    private Vector4f current = DEFAULT_COLOR;

    public void push() {
        this.stack.push(this.current);
    }

    public void setColor(Vector4f shaderColor) {
        this.current = shaderColor;
    }

    public Vector4f getColor() {
        return this.current;
    }

    public void resetColor() {
        this.current = DEFAULT_COLOR;
    }

    public void pop() {
        this.stack.pop();
        this.current = this.stack.isEmpty() ? DEFAULT_COLOR : this.stack.peek();
    }
}
