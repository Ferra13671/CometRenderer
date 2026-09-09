package com.ferra13671.cometrenderer.device.vertexformat;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import org.lwjgl.opengl.ARBVertexAttribBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ARBVertexFormatManager implements VertexFormatManager {
    private final boolean applyMesaWorkaround;

    public ARBVertexFormatManager() {
        Mesa3DVersion mesaVersion = CometRenderer.getRegistry().get(CometTags.MESA_VERSION).orElseThrow();

        if (mesaVersion != Mesa3DVersion.NONE)
            this.applyMesaWorkaround = mesaVersion.version().contains("25.0.0") || mesaVersion.version().contains("25.0.1") || mesaVersion.version().contains("25.0.2");
        else
            this.applyMesaWorkaround = false;
    }

    @Override
    public void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);

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

    private VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat) {
        int vertBuffId = CometRenderer.getDevice().getDirectStateManager().createVertexArray();
        if (!GLCapabilities.supportsDirectStateAccess())
            GL30.glBindVertexArray(vertBuffId);

        VertexElement[] elements = vertexFormat.getVertexElements();

        for (int i = 0; i < elements.length; i++) {
            VertexElement vertexElement = elements[i];
            CometRenderer.getDevice().getDirectStateManager().enableVertexAttributeArray(vertBuffId, i);

            if (vertexElement.getType().glId() == GL11.GL_FLOAT) {
                CometRenderer.getDevice().getDirectStateManager().vertexAttributeFormat(
                        vertBuffId, i, vertexElement.getTypeCount(), vertexElement.getType().glId(), false, vertexFormat.getElementOffset(vertexElement)
                );
            } else {
                CometRenderer.getDevice().getDirectStateManager().vertexAttributeIntFormat(
                        vertBuffId, i, vertexElement.getTypeCount(), vertexElement.getType().glId(), vertexFormat.getElementOffset(vertexElement)
                );
            }

            CometRenderer.getDevice().getDirectStateManager().vertexAttributeBinding(vertBuffId, i, 0);
        }

        return new VertexFormatBuffer(vertBuffId, vertexFormat);
    }
}
