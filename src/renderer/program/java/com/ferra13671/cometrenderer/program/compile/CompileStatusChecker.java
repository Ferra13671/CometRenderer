package com.ferra13671.cometrenderer.program.compile;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL20;

/*
 * Класс для проверки статуса компиляции шейдера, либо программы.
 */
public class CompileStatusChecker {

    public static CompileResult checkShaderCompile(int shaderId) {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS));
        String message = status == CompileStatus.FAILURE ? StringUtils.trim(GlStateManager.glGetShaderInfoLog(shaderId, 32768)) : "";
        return new CompileResult(status, message);
    }

    public static CompileResult checkProgramCompile(int programId) {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS));
        String message = status == CompileStatus.FAILURE ? StringUtils.trim(GL20.glGetProgramInfoLog(programId)) : "";
        return new CompileResult(status, message);
    }
}
