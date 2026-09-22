package com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import org.lwjgl.opengl.GL31;

public record UBOBindableBase(int bufferId) implements UBOBindable {

    @Override
    public void bind(int index) {
        GL31.glBindBufferBase(BufferTarget.UNIFORM_BUFFER.glId, index, bufferId());
    }
}
