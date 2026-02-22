package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import lombok.Getter;

abstract class AbstractCRMController {
    @Getter
    private final GlProgramSnippet matrixSnippet = loadMatrixSnippet();

    protected abstract GlProgramSnippet loadMatrixSnippet();

    protected abstract GlslFileEntry getMatricesShaderLib();

    protected abstract void setupUIMatrix(int scale);

    protected abstract void restoreUIMatrix();

    protected abstract void applyMatrixUniform();

    protected abstract Framebuffer createMainFramebuffer();

    public static void initMod() {
        CRM.initRender();
    }
}
