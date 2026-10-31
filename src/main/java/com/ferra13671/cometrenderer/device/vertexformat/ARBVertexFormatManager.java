package com.ferra13671.cometrenderer.device.vertexformat;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.buffer.BufferTarget;
import com.ferra13671.cometrenderer.device.directstate.DirectStateManager;
import com.ferra13671.cometrenderer.utils.GLCapabilities;
import com.ferra13671.cometrenderer.utils.Mesa3DVersion;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.format.VertexFormatBuffer;
import com.ferra13671.cometrenderer.vertex.format.instanced.InstancedVertexFormat;
import com.ferra13671.cometrenderer.vertex.format.instanced.InstancedVertexFormatBuffer;
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
    public void bindFormatBuffer(GpuBuffer vertexBuffer, VertexFormat vertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);

        VertexFormatBuffer vertexFormatBuffer = vertexFormat.getOrCreateBuffer(() -> createVertexFormatBuffer(vertexFormat));

        GL30.glBindVertexArray(vertexFormatBuffer.getGlId());
        if (vertexFormatBuffer.getBuffer() != vertexBuffer) {
            if (this.applyMesaWorkaround && vertexFormatBuffer.getBuffer() != null && vertexFormatBuffer.getBuffer().getId() == vertexBuffer.getId()) {
                ARBVertexAttribBinding.glBindVertexBuffer(0, 0, 0L, 0);
            }

            ARBVertexAttribBinding.glBindVertexBuffer(0, vertexBuffer.getId(), 0L, vertexFormat.getVertexSize());
            vertexFormatBuffer.setBuffer(vertexBuffer);
        }
    }

    @Override
    public void bindInstancedFormatBuffer(GpuBuffer vertexBuffer, GpuBuffer instanceBuffer, InstancedVertexFormat instancedVertexFormat) {
        if (vertexBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(vertexBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);
        if (instanceBuffer.getTarget() != BufferTarget.ARRAY_BUFFER)
            ErrorHandlers.onWrongBufferTarget(instanceBuffer.getTarget().glId, BufferTarget.ARRAY_BUFFER.glId);

        InstancedVertexFormatBuffer buffer = instancedVertexFormat.getOrCreateBuffer(() -> createInstancedVertexFormatBuffer(instancedVertexFormat));

        GL30.glBindVertexArray(buffer.getGlId());
        if (buffer.getBuffer() != vertexBuffer) {
            if (this.applyMesaWorkaround && buffer.getBuffer() != null && buffer.getBuffer().getId() == vertexBuffer.getId())
                ARBVertexAttribBinding.glBindVertexBuffer(0, 0, 0L, 0);

            ARBVertexAttribBinding.glBindVertexBuffer(0, vertexBuffer.getId(), 0L, instancedVertexFormat.getPerVertexFormat().getVertexSize());
            buffer.setBuffer(vertexBuffer);
        }
        if (buffer.getInstanceBuffer() != instanceBuffer) {
            ARBVertexAttribBinding.glBindVertexBuffer(1, instanceBuffer.getId(), 0L, instancedVertexFormat.getPerInstanceFormat().getVertexSize());
            buffer.setInstanceBuffer(instanceBuffer);
        }
    }

    private InstancedVertexFormatBuffer createInstancedVertexFormatBuffer(InstancedVertexFormat instancedVertexFormat) {
        int vaoId = CometRenderer.getDevice().createVertexArray();
        if (!GLCapabilities.supportsDirectStateAccess())
            GL30.glBindVertexArray(vaoId);

        int perVertexLocations = setupAttributes(vaoId, 0, instancedVertexFormat.getPerVertexFormat(), 0);
        setupAttributes(vaoId, 1, instancedVertexFormat.getPerInstanceFormat(), perVertexLocations);

        DirectStateManager dsm = CometRenderer.getDevice().getDirectStateManager();
        dsm.vertexBindingDivisor(vaoId, 0, 0);
        dsm.vertexBindingDivisor(vaoId, 1, 1);

        return new InstancedVertexFormatBuffer(vaoId);
    }

    private VertexFormatBuffer createVertexFormatBuffer(VertexFormat vertexFormat) {
        int vaoId = CometRenderer.getDevice().createVertexArray();
        if (!GLCapabilities.supportsDirectStateAccess())
            GL30.glBindVertexArray(vaoId);

        setupAttributes(vaoId, 0, vertexFormat, 0);

        return new VertexFormatBuffer(vaoId);
    }

    private int setupAttributes(int vaoId, int binding, VertexFormat format, int startAttribute) {
        VertexElement[] elements = format.getVertexElements();

        int attributeLocation = startAttribute;
        for (VertexElement element : elements) {
            int primitivesPerAttribute = element.getType().primitivesPerAttribute();

            for (int i = 0; i < element.getTypeCount(); i += primitivesPerAttribute) {
                int counts = Math.clamp(element.getTypeCount() - i, 1, primitivesPerAttribute);
                int offset = format.getElementOffset(element) + (i * element.getType().offset());

                CometRenderer.getDevice().getDirectStateManager().enableVertexAttributeArray(vaoId, attributeLocation);

                if (element.getType().glId() == GL11.GL_FLOAT) {
                    CometRenderer.getDevice().getDirectStateManager().vertexAttributeFormat(
                            vaoId, attributeLocation, counts, element.getType().glId(), false, offset
                    );
                } else {
                    CometRenderer.getDevice().getDirectStateManager().vertexAttributeIntFormat(
                            vaoId, attributeLocation, counts, element.getType().glId(), offset
                    );
                }

                CometRenderer.getDevice().getDirectStateManager().vertexAttributeBinding(vaoId, attributeLocation, binding);

                attributeLocation++;
            }
        }

        return attributeLocation;
    }
}
