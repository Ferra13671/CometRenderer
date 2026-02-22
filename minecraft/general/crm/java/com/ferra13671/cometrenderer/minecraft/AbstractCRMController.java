package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import lombok.Getter;
import lombok.SneakyThrows;

abstract class AbstractCRMController {
    private static final String IMPL_PATH = "com.ferra13671.cometrenderer.minecraft.CRMController";
    @Getter
    private final GlProgramSnippet matrixSnippet = loadMatrixSnippet();

    protected abstract GlProgramSnippet loadMatrixSnippet();

    protected abstract GlslFileEntry getMatricesShaderLib();

    protected abstract void setupUIProjection(int scale);

    protected abstract void restoreUIProjection();

    protected abstract void applyMatrixUniform();

    protected abstract Framebuffer createMainFramebuffer();


    @SneakyThrows
    static AbstractCRMController loadImpl() {
        return (AbstractCRMController) Class.forName(IMPL_PATH).getDeclaredConstructor().newInstance();
    }

    public static void initMod() {
        CRM.initRender();
    }
}
