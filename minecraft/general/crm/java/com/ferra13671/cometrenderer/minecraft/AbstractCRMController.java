package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.glsl.GLProgramSnippet;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import lombok.Getter;
import org.apiguardian.api.API;
import org.joml.Vector2f;

@API(status = API.Status.INTERNAL, since = "2.6")
abstract class AbstractCRMController {
    @Getter
    private final GLProgramSnippet matrixSnippet = loadMatrixSnippet();

    protected abstract GLProgramSnippet loadMatrixSnippet();

    protected abstract GLSLFileEntry getMatricesShaderLib();

    protected abstract void setupUIMatrix(int scale);

    protected abstract void restoreUIMatrix();

    protected abstract Vector2f getScaledMousePos(int scale);

    protected abstract void applyMatrixUniform();

    protected abstract Framebuffer createMainFramebuffer();

    protected abstract RenderColor getColorFromMinecraftCode(char code);

    public static void initMod() {
        CRM.initRender();
    }
}
