package com.ferra13671.cometrenderer;

import org.apiguardian.api.API;
import org.joml.Vector4f;

import java.util.Stack;

@API(status = API.Status.MAINTAINED, since = "2.4")
public class ShaderColor {
    private static final Vector4f DEFAULT_COLOR = new Vector4f(1f, 1f, 1f, 1f);

    private final Stack<Vector4f> stack = new Stack<>();
    private final Stack<Vector4f> maskStack = new Stack<>();
    private Vector4f current = DEFAULT_COLOR;

    public void push() {
        this.stack.push(this.current);
    }

    public void setColor(Vector4f shaderColor) {
        this.current = shaderColor;
    }

    public Vector4f getColor() {
        return this.maskStack.isEmpty() ? this.current : new Vector4f(this.current).mul(this.maskStack.peek());
    }

    public void resetColor() {
        this.current = DEFAULT_COLOR;
    }

    public void pop() {
        this.current = this.stack.peek();
        this.stack.pop();
    }

    @API(status = API.Status.EXPERIMENTAL)
    public void pushMask(Vector4f mask) {
        this.maskStack.push(new Vector4f(mask).mul(this.maskStack.peek()));
    }

    @API(status = API.Status.EXPERIMENTAL)
    public void popMask() {
        this.maskStack.pop();
    }
}
