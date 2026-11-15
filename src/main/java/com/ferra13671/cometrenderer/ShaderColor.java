package com.ferra13671.cometrenderer;

import org.apiguardian.api.API;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

@API(status = API.Status.MAINTAINED, since = "2.4")
public final class ShaderColor {
    private static final Vector4fc DEFAULT_COLOR = new Vector4f(1f, 1f, 1f, 1f);

    private final Deque<float[]> stack = new ArrayDeque<>();
    private final Deque<float[]> maskStack = new ArrayDeque<>();
    private final Vector4f colorWithoutMask = new Vector4f(DEFAULT_COLOR);
    private final Vector4f colorWithMask = new Vector4f(DEFAULT_COLOR);

    public void push() {
        this.stack.push(createArray(this.colorWithoutMask));
    }

    public void setColor(Vector4fc shaderColor) {
        Objects.requireNonNull(shaderColor, "shaderColor");

        if (shaderColor == this.colorWithMask)
            return;

        this.colorWithoutMask.set(shaderColor);
        updateColorWithMask();
    }

    @API(status = API.Status.MAINTAINED, since = "3.1")
    public void setColor(float red, float green, float blue, float alpha) {
        this.colorWithoutMask.set(red, green, blue, alpha);
        updateColorWithMask();
    }

    @API(status = API.Status.DEPRECATED, since = "3.1")
    @Deprecated(forRemoval = true)
    public Vector4f getColor() {
        return getColor(new Vector4f());
    }

    @API(status = API.Status.MAINTAINED, since = "3.1")
    public Vector4f getColor(Vector4f dest) {
        Objects.requireNonNull(dest, "dest");

        return dest.set(getColorRef());
    }

    @API(status = API.Status.MAINTAINED, since = "3.1")
    public Vector4fc getColorRef() {
        return this.maskStack.isEmpty() ? this.colorWithoutMask : this.colorWithMask;
    }

    public void resetColor() {
        this.colorWithoutMask.set(DEFAULT_COLOR);
        updateColorWithMask();
    }

    public void pop() {
        if (this.stack.isEmpty())
            throw new IllegalStateException("Unbalanced ShaderColor.push()/pop()");

        this.colorWithoutMask.set(this.stack.pop());
        updateColorWithMask();
    }

    @API(status = API.Status.EXPERIMENTAL)
    public void pushMask(Vector4fc mask) {
        Objects.requireNonNull(mask, "mask");

        pushMask(mask.x(), mask.y(), mask.z(), mask.w());
    }

    @API(status = API.Status.EXPERIMENTAL)
    public void pushMask(float red, float green, float blue, float alpha) {
        float[] prevMask = this.maskStack.peek();

        float[] m = new float[]{
                red,
                green,
                blue,
                alpha
        };
        if (prevMask != null) {
            m[0] *= prevMask[0];
            m[1] *= prevMask[1];
            m[2] *= prevMask[2];
            m[3] *= prevMask[3];
        }

        this.maskStack.push(m);
        updateColorWithMask();
    }

    @API(status = API.Status.EXPERIMENTAL)
    public void popMask() {
        if (this.maskStack.isEmpty())
            throw new IllegalStateException("Unbalanced ShaderColor.pushMask()/popMask()");

        this.maskStack.pop();
        updateColorWithMask();
    }

    private void updateColorWithMask() {
        this.colorWithMask.set(this.colorWithoutMask);
        if (!this.maskStack.isEmpty())
            mul(this.colorWithMask, this.maskStack.peek());
    }

    private void mul(Vector4f color, float[] array) {
        color.x *= array[0];
        color.y *= array[1];
        color.z *= array[2];
        color.w *= array[3];
    }

    private float[] createArray(Vector4fc color) {
        return new float[]{
                color.x(),
                color.y(),
                color.z(),
                color.w()
        };
    }
}
