package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.minecraft.program.DefaultPrograms;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.scissor.ScissorRect;
import com.ferra13671.cometrenderer.utils.Logger;
import lombok.Getter;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CRM {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("CometRenderer");
    static AbstractCRMController controller;
    @Getter
    private static DefaultPrograms programs;
    private static Framebuffer mainFramebuffer;

    static void initRender() {
        CometRenderer.setLogger(new Logger() {
            @Override
            public void log(String message) {
                logger.info(message);
            }

            @Override
            public void warn(String message) {
                logger.warn(message);
            }

            @Override
            public void error(String message) {
                logger.error(message);
            }
        });

        CometRenderer.init();

        CometRenderer.getLogger().log(String.format("Initialized CometRenderer %s", CometRenderer.getRegistry().get(CometTags.COMET_RENDERER_VERSION).orElseThrow().getValue()));
        CometRenderer.getLogger().log(String.format("OpenGL extensions: %s", Arrays.toString(CometRenderer.getRegistry().get(CometTags.GL_EXTENSIONS).orElseThrow().getValue())));

        BetterCompilerPlugin.init();
        controller = new CRMController();
        BetterCompilerPlugin.registerShaderLibraries(
                controller.getMatricesShaderLib(),
                DefaultShaderLibraries.SHADER_COLOR,
                DefaultShaderLibraries.ROUNDED
        );

        programs = new DefaultPrograms();
    }

    static ScissorRect fixScissorRect(ScissorRect scissorRect, int scale) {
        return new ScissorRect(
                scissorRect.x() * scale,
                getMainFramebuffer().getHeight() - ((scissorRect.y() + scissorRect.height()) * scale),
                scissorRect.width() * scale,
                scissorRect.height() * scale
        );
    }

    public static void applyMatrixUniform() {
        controller.applyMatrixUniform();
    }

    public static Framebuffer getMainFramebuffer() {
        if (mainFramebuffer == null)
            mainFramebuffer = controller.createMainFramebuffer();

        return mainFramebuffer;
    }
}
