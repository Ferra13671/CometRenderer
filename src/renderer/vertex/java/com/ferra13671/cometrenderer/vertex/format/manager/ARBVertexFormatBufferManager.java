package com.ferra13671.cometrenderer.vertex.format.manager;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.exceptions.impl.WrongGpuBufferTargetException;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import org.lwjgl.opengl.ARBVertexAttribBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ARBVertexFormatBufferManager extends VertexFormatManager {
    private final boolean applyMesaWorkaround;

    public ARBVertexFormatBufferManager() {
        Mesa3DVersion mesaVersion = CometRenderer.getRegistry().get(CometTags.MESA_VERSION).orElseThrow().getValue();

        if (mesaVersion != Mesa3DVersion.NONE)
            this.applyMesaWorkaround = mesaVersion.version().contains("25.0.0") || mesaVersion.version().contains("25.0.1") || mesaVersion.version().contains("25.0.2");
        else
            this.applyMesaWorkaround = false;
    }

    @Override
    public void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            CometRenderer.getExceptionManager().manageException(new WrongGpuBufferTargetException(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId));

        VertexFormatBuffer vertexFormatBuffer = vertexFormat.getOrCreateBuffer(() -> createVertexFormatBuffer(vertexFormat));

        GL30.glBindVertexArray(vertexFormatBuffer.getGlId());
        if (vertexFormatBuffer.getBuffer() != vertexBuffer) {
            if (applyMesaWorkaround && vertexFormatBuffer.getBuffer() != null && vertexFormatBuffer.getBuffer().getId() == vertexBuffer.getId()) {
                ARBVertexAttribBinding.glBindVertexBuffer(0, 0, 0L, 0);
            }

            ARBVertexAttribBinding.glBindVertexBuffer(0, vertexBuffer.getId(), 0L, vertexFormat.getVertexSize());
            vertexFormatBuffer.setBuffer(vertexBuffer);
        }
    }

    @Override
    public VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat) {
        int vertBuffId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertBuffId);

        VertexElement[] elements = vertexFormat.getVertexElements();

        for (int i = 0; i < elements.length; i++) {
            VertexElement vertexElement = elements[i];
            GL30.glEnableVertexAttribArray(i);

            if (vertexElement.getType().glId() == GL11.GL_FLOAT) {
                ARBVertexAttribBinding.glVertexAttribFormat(
                        i, vertexElement.getCount(), vertexElement.getType().glId(), false, vertexFormat.getElementOffset(vertexElement)
                );
            } else {
                ARBVertexAttribBinding.glVertexAttribIFormat(
                        i, vertexElement.getCount(), vertexElement.getType().glId(), vertexFormat.getElementOffset(vertexElement)
                );
            }

            ARBVertexAttribBinding.glVertexAttribBinding(i, 0);
        }

        return new VertexFormatBuffer(vertBuffId, vertexFormat);
    }
}
