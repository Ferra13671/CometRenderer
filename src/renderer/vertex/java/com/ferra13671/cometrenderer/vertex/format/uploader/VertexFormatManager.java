package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferUsageException;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import org.lwjgl.opengl.GL;

/*
 * Менеджер привязки структуры вершин к буфферу вершин
 */
public class VertexFormatManager {
    //Сам привязыватель. В зависимости от функций поддерживаемого OpenGL будет выбран 1 из 2 типов привязывателя.
    private static final VertexFormatBufferUploader formatUploader =
            GL.getCapabilities().GL_ARB_vertex_attrib_binding ?
                    new ARBVertexFormatBufferUploader()
                    :
                    new DefaultVertexFormatBufferUploader();


    /*
     * Привязывает структуру вершин к буфферу вершин
     */
    public static void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ExceptionPrinter.printAndExit(new WrongGpuBufferUsageException(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId));

        formatUploader.applyFormatToBuffer(vertexBuffer, vertexFormat);
    }
}
