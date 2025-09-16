package com.ferra13671.cometrenderer.vertex.format.uploader;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import net.minecraft.client.gl.GlGpuBuffer;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultVertexFormatBufferUploader extends VertexFormatBufferUploader {

    @Override
    @OverriddenMethod
    public void applyFormatToBuffer(GlGpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        int vertexBufferId = CometRenderer.getBufferIdGetter().apply(vertexBuffer);

        VertexFormatBuffer formatBuffer = this.vertexFormatBuffers.get(vertexFormat);
        if (formatBuffer == null) {
            int i = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(i);
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexBufferId);
            setupBuffer(vertexFormat, true);
            VertexFormatBuffer formatBuffer2 = new VertexFormatBuffer(i, vertexFormat, new AtomicReference<>(vertexBuffer));
            this.vertexFormatBuffers.put(vertexFormat, formatBuffer2);
        } else {
            GL30.glBindVertexArray(formatBuffer.glId());
            if (formatBuffer.buffer().get() != vertexBuffer) {
                GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexBufferId);
                formatBuffer.buffer().set(vertexBuffer);
                setupBuffer(vertexFormat, false);
            }
        }
    }

    private void setupBuffer(VertexFormat format, boolean vbaIsNew) {
        int i = format.getVertexSize();
        List<VertexElement> list = format.getVertexElements();

        for (int j = 0; j < list.size(); j++) {
            VertexElement vertexElement = list.get(j);
            if (vbaIsNew)
                GL30.glEnableVertexAttribArray(j);

            if (vertexElement.getType() == VertexElementType.FLOAT) {
                GL30.glVertexAttribPointer(
                        j, vertexElement.getCount(), vertexElement.getType().glId(), false, i, format.getElementOffset(vertexElement)
                );
            } else {
                GL30.glVertexAttribIPointer(j, vertexElement.getCount(), vertexElement.getType().glId(), i, format.getElementOffset(vertexElement));
            }
        }
    }
}
