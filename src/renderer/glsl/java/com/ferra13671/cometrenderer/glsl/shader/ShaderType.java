package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.utils.GLCapabilities;
import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import java.util.function.Supplier;

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
@API(status = API.Status.STABLE, since = "1.1")
public enum ShaderType {
    Vertex("vertex", GL20.GL_VERTEX_SHADER, () -> true),
    Fragment("fragment", GL20.GL_FRAGMENT_SHADER, () -> true),
    Geometry("geometry", GL32.GL_GEOMETRY_SHADER, () -> true),
    TessellateEvaluation("tesselation", GL40.GL_TESS_EVALUATION_SHADER, GLCapabilities::supportsTesselationShader),
    TessellateControl("tesselation", GL40.GL_TESS_CONTROL_SHADER, GLCapabilities::supportsTesselationShader),
    Compute("compute", GL43.GL_COMPUTE_SHADER, GLCapabilities::supportsComputeShader);

    public final String name;
    public final int glId;
    public final Supplier<Boolean> supportConsumer;
}
