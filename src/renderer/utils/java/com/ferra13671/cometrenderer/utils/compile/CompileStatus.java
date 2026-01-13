package com.ferra13671.cometrenderer.utils.compile;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL11;

/**
 * Статус компиляции чего-либо.
 * Имеет два типа:
 * <br><table>
 *     <tr><td>SUCCESSFUL — компиляция прошла успешно.</td></tr>
 *     <tr><td>FAILURE — при компиляции возникла ошибка.</td></tr>
 * </table>
 */
@AllArgsConstructor
public enum CompileStatus {
    SUCCESSFUL(GL11.GL_TRUE),
    FAILURE(GL11.GL_FALSE);

    /** Айди статуса компиляции. **/
    public final int id;

    /**
     * Возвращает статус компиляции по его айди.
     *
     * @param id айди статуса компиляции.
     * @return статус компиляции.
     */
    public static CompileStatus fromStatusId(int id) {
        for (CompileStatus compileStatus : values())
            if (compileStatus.id == id)
                return compileStatus;

        return null;
    }
}
