package com.ferra13671.cometrenderer.device.vertexformat;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import com.ferra13671.cometrenderer.vertex.format.instanced.InstancedVertexFormat;
import com.ferra13671.cometrenderer.vertex.format.instanced.InstancedVertexFormatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class DefaultVertexFormatManager implements VertexFormatManager {

    @Override
    public void bindFormatBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);

        VertexFormatBuffer vfb = vertexFormat.getOrCreateBuffer(
                () -> new VertexFormatBuffer(CometRenderer.getDevice().createVertexArray()));

        GL30.glBindVertexArray(vfb.getGlId());
        if (vfb.getBuffer() != vertexBuffer) {
            boolean firstSetup = vfb.getBuffer() == null;
            vertexBuffer.bind();
            vfb.setBuffer(vertexBuffer);

            setupAttributes(
                    0,
                    vertexFormat,
                    vertexFormat.getVertexSize(),
                    firstSetup
            );
        }
    }

    @Override
    public void bindInstancedFormatBuffer(GpuBuffer vertexBuffer, GpuBuffer instanceBuffer, InstancedVertexFormat instancedVertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);
        if (instanceBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(instanceBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);

        InstancedVertexFormatBuffer buf = instancedVertexFormat.getOrCreateBuffer(
                () -> new InstancedVertexFormatBuffer(CometRenderer.getDevice().createVertexArray()));

        GL30.glBindVertexArray(buf.getGlId());

        if (buf.getBuffer() != vertexBuffer || buf.getInstanceBuffer() != instanceBuffer) {
            setupInstancedAttributes(
                    instancedVertexFormat,
                    vertexBuffer,
                    instanceBuffer,
                    buf.getBuffer() == null && buf.getInstanceBuffer() == null
            );

            buf.setBuffer(vertexBuffer);
            buf.setInstanceBuffer(instanceBuffer);
        }
    }

    private void setupInstancedAttributes(InstancedVertexFormat format, GpuBuffer vertexBuffer, GpuBuffer instanceBuffer, boolean firstSetup) {
        vertexBuffer.bind();
        int perVertexNext = setupAttributes(0, format.getPerVertexFormat(),
                format.getPerVertexFormat().getVertexSize(), firstSetup);

        instanceBuffer.bind();
        int totalNext = setupAttributes(perVertexNext, format.getPerInstanceFormat(),
                format.getPerInstanceFormat().getVertexSize(), firstSetup);

        if (firstSetup) {
            for (int i = 0; i < perVertexNext; i++)
                GL33.glVertexAttribDivisor(i, 0);
            for (int i = perVertexNext; i < totalNext; i++)
                GL33.glVertexAttribDivisor(i, 1);
        }
    }

    private int setupAttributes(int startLocation, VertexFormat format, int stride, boolean enableArrays) {
        int location = startLocation;

        for (VertexElement element : format.getVertexElements()) {
            int primitivesPerAttribute = element.getType().primitivesPerAttribute();

            for (int i = 0; i < element.getTypeCount(); i += primitivesPerAttribute) {
                int counts = Math.clamp(element.getTypeCount() - i, 1, primitivesPerAttribute);
                int offset = format.getElementOffset(element) + (i * element.getType().offset());

                if (enableArrays)
                    GL30.glEnableVertexAttribArray(location);

                if (element.getType().glId() == GL11.GL_FLOAT) {
                    GL30.glVertexAttribPointer(
                            location, counts, element.getType().glId(),
                            false, stride, offset);
                } else {
                    GL30.glVertexAttribIPointer(
                            location, counts, element.getType().glId(),
                            stride, offset);
                }

                location++;
            }
        }

        return location;
    }
}