package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.plugins.minecraft.program.DefaultPrograms;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.ShaderLibrariesPlugin;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.scissor.ScissorRect;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

public abstract class AbstractMinecraftPlugin {
    @Getter
    private static final GlProgramSnippet matrixSnippet = CometLoaders.IN_JAR.createProgramBuilder()
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX4)
            .buildSnippet();
    @Getter
    @Setter
    private static AbstractMinecraftPlugin instance;
    protected final Supplier<Integer> scaleGetter;
    @Getter
    private final DefaultPrograms programs;
    @Getter
    protected Framebuffer mainFrameBuffer;

    public AbstractMinecraftPlugin(Supplier<Integer> scaleGetter) {
        setInstance(this);

        initShaderLibraries();

        this.scaleGetter = scaleGetter;
        this.programs = new DefaultPrograms();
    }

    public abstract void setupUIProjection();

    public abstract void initMatrix();

    public abstract void bindMainFramebuffer(boolean setViewport);

    public ScissorRect fixScissorRect(ScissorRect scissorRect) {
        int scale = this.scaleGetter.get();
        int frameBufferHeight = this.mainFrameBuffer.getHeight();
        return new ScissorRect(
                scissorRect.x() * scale,
                frameBufferHeight - ((scissorRect.y() + scissorRect.height()) * scale),
                scissorRect.width() * scale,
                scissorRect.height() * scale
        );
    }

    private void initShaderLibraries() {
        ShaderLibrariesPlugin.registerShaderLibraries(
                DefaultShaderLibraries.MATRICES,
                DefaultShaderLibraries.SHADER_COLOR,
                DefaultShaderLibraries.ROUNDED
        );
    }
}
