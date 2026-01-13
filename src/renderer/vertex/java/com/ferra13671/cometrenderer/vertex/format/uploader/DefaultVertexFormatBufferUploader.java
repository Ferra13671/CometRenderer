package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultVertexFormatBufferUploader extends VertexFormatBufferUploader {

    @Override
    public void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        VertexFormatBuffer vertexFormatBuffer = vertexFormat.getVertexFormatBufferOrCreate(() -> createVertexFormatBuffer(vertexFormat));

        GL30.glBindVertexArray(vertexFormatBuffer.glId());
        if (vertexFormatBuffer.buffer().get() != vertexBuffer) {
            vertexBuffer.bind();
            vertexFormatBuffer.buffer().set(vertexBuffer);
            setupBuffer(vertexFormat, false);
        }
    }

    @Override
    public VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat) {
        int i = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(i);
        setupBuffer(vertexFormat, true);
        return new VertexFormatBuffer(i, vertexFormat, new AtomicReference<>());
    }

    private void setupBuffer(VertexFormat format, boolean vbaIsNew) {
        int i = format.getVertexSize();
        List<VertexElement> list = format.getVertexElements();

        for (int j = 0; j < list.size(); j++) {
            VertexElement vertexElement = list.get(j);
            if (vbaIsNew)
                GL30.glEnableVertexAttribArray(j);

            if (vertexElement.getType().glId() == GL11.GL_FLOAT) {
                GL30.glVertexAttribPointer(
                        j, vertexElement.getCount(), vertexElement.getType().glId(), false, i, format.getElementOffset(vertexElement)
                );
            } else {
                GL30.glVertexAttribIPointer(j, vertexElement.getCount(), vertexElement.getType().glId(), i, format.getElementOffset(vertexElement));
            }
        }
    }
}
