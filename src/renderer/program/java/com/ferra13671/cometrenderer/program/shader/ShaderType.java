package com.ferra13671.cometrenderer.program.shader;

import com.ferra13671.cometrenderer.utils.GLVersion;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public enum ShaderType {
    Vertex(GL20.GL_VERTEX_SHADER, GLVersion.GL20),
    Fragment(GL20.GL_FRAGMENT_SHADER, GLVersion.GL20),
    Geometry(GL32.GL_GEOMETRY_SHADER, GLVersion.GL32),
    TessellateEvaluation(GL40.GL_TESS_EVALUATION_SHADER, GLVersion.GL40),
    TessellateControl(GL40.GL_TESS_CONTROL_SHADER, GLVersion.GL40),
    Compute(GL43.GL_COMPUTE_SHADER, GLVersion.GL43);

    /** Айди типа шейдера в OpenGL. **/
    public final int glId;
    public final GLVersion glVersion;

    public boolean isSupportedOn(GLVersion glVersion) {
        return glVersion.id >= this.glVersion.id;
    }
}
