package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferTargetException;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import org.lwjgl.opengl.GL;

/**
 * Менеджер установки формата вершины к буфферу вершин.
 */
public class VertexFormatManager {
    /** Установщик формата ввершины к буфферу вершин. В зависимости от функций поддерживаемого OpenGL будет выбран 1 из 2 типов привязывателя. **/
    private static final VertexFormatBufferUploader formatUploader =
            GL.getCapabilities().GL_ARB_vertex_attrib_binding && CometRenderer.getConfig().USE_ARB_ATTRIB_BINDING_IF_SUPPORT.getValue() ?
                    new ARBVertexFormatBufferUploader()
                    :
                    new DefaultVertexFormatBufferUploader();

    /**
     * Устанавливает формат вершины к буфферу вершин.
     *
     * @param vertexBuffer буффер вершин.
     * @param vertexFormat формат вершины.
     */
    public static void uploadFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            CometRenderer.manageException(new WrongGpuBufferTargetException(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId));

        formatUploader.applyFormatToBuffer(vertexBuffer, vertexFormat);
    }
}
