package com.ferra13671.cometrenderer.program.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/**
 * Тип шейдера. Отвечает за то, для какой части графической обработки предназначен шейдер при использовании.
 * На данный момент есть данные типы шейдеров:
 * <br><table>
 *     <tr><td>Vertex — обрабатывает каждую вершину, выполняя различные трансформации, определяя позицию и передавая данные в следующие шейдеры.</td></tr>
 *     <tr><td>Fragment — вычисляет цвет каждого пикселя после растеризации, является последним выполняющимся шейдером.</td></tr>
 *     <tr><td>Geometry — получает примитивы (точки/линии/треугольники), может генерировать новые примитивы или изменять существующие.</td></tr>
 *     <tr><td>TessellateEvaluation — один из двух теселяционных шейдеров. Отвечает за вычисление новых координат вершин после разбиения.</td></tr>
 *     <tr><td>TessellateControl — один из двух теселяционных шейдеров. Отвечает за уровень детализации.</td></tr>
 *     <tr><td>Compute — особый вид шейдера, являющийся общим вычислительным шейдером вне классических графических этапов. Используется для GPGPU‑задач (физика, постобработка, вычислительные шаги), обеспечивает произвольный доступ к памяти и синхронизацию на GPU.</td></tr>
 * </table>
 *
 * @see <a href="https://wikis.khronos.org/opengl/Shader">OpenGL shader wiki</a>
 * @see GlShader
 */
public enum ShaderType {
    Vertex(GL20.GL_VERTEX_SHADER),
    Fragment(GL20.GL_FRAGMENT_SHADER),
    Geometry(GL32.GL_GEOMETRY_SHADER),
    TessellateEvaluation(GL40.GL_TESS_EVALUATION_SHADER),
    TessellateControl(GL40.GL_TESS_CONTROL_SHADER),
    Compute(GL43.GL_COMPUTE_SHADER);

    /** Айди типа шейдера в OpenGL. **/
    public final int glId;

    /**
     * @param glId айди типа шейдера в OpenGL.
     */
    ShaderType(int glId) {
        this.glId = glId;
    }
}
