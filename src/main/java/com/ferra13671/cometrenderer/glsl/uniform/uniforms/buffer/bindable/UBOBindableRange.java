package com.ferra13671.cometrenderer.glsl.uniform.uniforms.buffer.bindable;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import org.lwjgl.opengl.GL31;

public record UBOBindableRange(int bufferId, long offset, long size) implements UBOBindable {

    @Override
    public void bind(int index) {
        GL31.glBindBufferRange(
                BufferTarget.UNIFORM_BUFFER.glId,
                index,
                bufferId(),
                offset(),
                size()
        );
    }
}
