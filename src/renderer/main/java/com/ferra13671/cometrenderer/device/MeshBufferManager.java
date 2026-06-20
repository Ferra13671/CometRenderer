package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.GpuBuffer;

import java.nio.ByteBuffer;

public interface MeshBufferManager {
    MeshBufferManager DEFAULT = (buffer, data) -> CometRenderer.getDevice().getDirectStateManager().bufferData(buffer, data);
    MeshBufferManager ARB = (buffer, data) -> CometRenderer.getDevice().getDirectStateManager().bufferStorage(buffer, data, 0);

    void allocateMeshBuffer(GpuBuffer buffer, ByteBuffer data);
}
