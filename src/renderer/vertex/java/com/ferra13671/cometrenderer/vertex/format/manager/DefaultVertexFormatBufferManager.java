package com.ferra13671.cometrenderer.vertex.format.manager;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferTargetException;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class DefaultVertexFormatBufferManager extends VertexFormatManager {

    @Override
    public void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            CometRenderer.getExceptionManager().manageException(new WrongGpuBufferTargetException(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId));

        VertexFormatBuffer vertexFormatBuffer = vertexFormat.getOrCreateBuffer(() -> createVertexFormatBuffer(vertexFormat));

        GL30.glBindVertexArray(vertexFormatBuffer.getGlId());
        if (vertexFormatBuffer.getBuffer() != vertexBuffer) {
            vertexBuffer.bind();
            vertexFormatBuffer.setBuffer(vertexBuffer);
            setupBuffer(vertexFormat, false);
        }
    }

    @Override
    public VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat) {
        int i = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(i);
        setupBuffer(vertexFormat, true);
        return new VertexFormatBuffer(i, vertexFormat);
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
