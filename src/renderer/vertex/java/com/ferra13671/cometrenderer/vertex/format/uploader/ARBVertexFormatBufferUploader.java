package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import org.lwjgl.opengl.ARBVertexAttribBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ARBVertexFormatBufferUploader extends VertexFormatBufferUploader {
    private final boolean applyMesaWorkaround;

    protected ARBVertexFormatBufferUploader() {
        Mesa3DVersion mesaVersion = CometRenderer.getRegistry().get(CometTags.MESA_VERSION).orElseThrow().getValue();

        if (mesaVersion != Mesa3DVersion.NONE)
            this.applyMesaWorkaround = mesaVersion.version().contains("25.0.0") || mesaVersion.version().contains("25.0.1") || mesaVersion.version().contains("25.0.2");
        else
            this.applyMesaWorkaround = false;
    }

    @Override
    public void applyFormatToBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        VertexFormatBuffer vertexFormatBuffer = vertexFormat.getVertexFormatBufferOrCreate(() -> createVertexFormatBuffer(vertexFormat));

        GL30.glBindVertexArray(vertexFormatBuffer.glId());
        if (vertexFormatBuffer.buffer().get() != vertexBuffer) {
            if (applyMesaWorkaround && vertexFormatBuffer.buffer().get() != null && vertexFormatBuffer.buffer().get().getId() == vertexBuffer.getId()) {
                ARBVertexAttribBinding.glBindVertexBuffer(0, 0, 0L, 0);
            }

            ARBVertexAttribBinding.glBindVertexBuffer(0, vertexBuffer.getId(), 0L, vertexFormat.getVertexSize());
            vertexFormatBuffer.buffer().set(vertexBuffer);
        }
    }

    @Override
    public VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat) {
        int vertBuffId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertBuffId);

        List<VertexElement> vertexElements = vertexFormat.getVertexElements();

        for (int i = 0; i < vertexElements.size(); i++) {
            VertexElement vertexElement = vertexElements.get(i);
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

        return new VertexFormatBuffer(vertBuffId, vertexFormat, new AtomicReference<>());
    }
}
