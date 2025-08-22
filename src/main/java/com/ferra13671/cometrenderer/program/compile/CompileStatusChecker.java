package com.ferra13671.cometrenderer.program.compile;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL20;

/*
 * Класс для проверки статуса компиляции шейдера, либо программы.
 */
public class CompileStatusChecker {

    public static CompileResult checkShaderCompile(int shaderId) {
        int status = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
        String message = status == 0 ? StringUtils.trim(GlStateManager.glGetShaderInfoLog(shaderId, 32768)) : "";
        return new CompileResult(status, message);
    }

    public static CompileResult checkProgramCompile(int programId) {
        int status = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
        String message = status == 0 ? StringUtils.trim(GL20.glGetProgramInfoLog(status)) : "";
        return new CompileResult(status, message);
    }
}
