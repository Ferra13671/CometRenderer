package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.ARBVertexAttribBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ARBVertexFormatBufferUploader extends VertexFormatBufferUploader {
    private final boolean applyMesaWorkaround;

    protected ARBVertexFormatBufferUploader() {
        if ("Mesa".equals(GL30.glGetString(GL30.GL_VENDOR))) {
            String string = GL30.glGetString(GL30.GL_VERSION);
            applyMesaWorkaround = string.contains("25.0.0") || string.contains("25.0.1") || string.contains("25.0.2");
        } else {
            applyMesaWorkaround = false;
        }
    }

    @Override
    @OverriddenMethod
    public void applyFormatToBuffer(GlGpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        VertexFormatBuffer vertexFormatBuffer = vertexFormatBuffers.get(vertexFormat);
        if (vertexFormatBuffer == null) {
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

            ARBVertexAttribBinding.glBindVertexBuffer(0, CometRenderer.getBufferIdGetter().apply(vertexBuffer), 0L, vertexFormat.getVertexSize());
            VertexFormatBuffer vertexFormatBuffer2 = new VertexFormatBuffer(vertBuffId, vertexFormat, new AtomicReference<>(vertexBuffer));
            vertexFormatBuffers.put(vertexFormat, vertexFormatBuffer2);
        } else {
            GL30.glBindVertexArray(vertexFormatBuffer.glId());
            if (vertexFormatBuffer.buffer().get() != vertexBuffer) {
                if (applyMesaWorkaround && vertexFormatBuffer.buffer().get() != null && CometRenderer.getBufferIdGetter().apply(vertexFormatBuffer.buffer().get()).equals(CometRenderer.getBufferIdGetter().apply(vertexBuffer))) {
                    ARBVertexAttribBinding.glBindVertexBuffer(0, 0, 0L, 0);
                }

                ARBVertexAttribBinding.glBindVertexBuffer(0, CometRenderer.getBufferIdGetter().apply(vertexBuffer), 0L, vertexFormat.getVertexSize());
                vertexFormatBuffer.buffer().set(vertexBuffer);
            }
        }
    }
}
