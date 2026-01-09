package com.ferra13671.cometrenderer.utils.blend;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

/**
 * Множитель для уже имеющегося цвета в фреймбуффере в уравнении смешивания.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Blending">OpenGL blending wiki</a>
 */
@AllArgsConstructor
public enum DstFactor {
    CONSTANT_ALPHA(GL14.GL_CONSTANT_ALPHA),
    CONSTANT_COLOR(GL14.GL_CONSTANT_COLOR),
    DST_ALPHA(GL11.GL_DST_ALPHA),
    DST_COLOR(GL11.GL_DST_COLOR),
    ONE(GL11.GL_ONE),
    ONE_MINUS_CONSTANT_ALPHA(GL14.GL_ONE_MINUS_CONSTANT_ALPHA),
    ONE_MINUS_CONSTANT_COLOR(GL14.GL_ONE_MINUS_CONSTANT_COLOR),
    ONE_MINUS_DST_ALPHA(GL11.GL_ONE_MINUS_DST_ALPHA),
    ONE_MINUS_DST_COLOR(GL11.GL_ONE_MINUS_DST_COLOR),
    ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
    ONE_MINUS_SRC_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
    SRC_ALPHA(GL11.GL_SRC_ALPHA),
    SRC_COLOR(GL11.GL_SRC_COLOR),
    ZERO(GL11.GL_ZERO);

    /** Айди множителя смешивания. **/
    public final int glId;
}