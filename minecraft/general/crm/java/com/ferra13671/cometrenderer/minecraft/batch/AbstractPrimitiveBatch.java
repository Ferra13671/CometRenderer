package com.ferra13671.cometrenderer.minecraft.batch;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;

public abstract class AbstractPrimitiveBatch implements IPrimitiveBatch {
    protected Runnable preDrawRunnable = () -> {};
    protected final MeshBuilder meshBuilder;
    protected Mesh mesh;
    protected boolean built = false;
    protected boolean closed = false;

    public AbstractPrimitiveBatch(MeshBuilder meshBuilder) {
        this.meshBuilder = meshBuilder;
    }

    @Override
    public IPrimitiveBatch tryDraw() {
        assertBuilt();
        assertNotClosed();

        if (this.mesh != null) {
            CometRenderer.getShaderColor().push();
            this.preDrawRunnable.run();

            draw();

            CometRenderer.getShaderColor().pop();
        }

        return this;
    }

    protected abstract void draw();

    @Override
    public boolean isBuilt() {
        return this.built;
    }

    @Override
    public IPrimitiveBatch build() {
        assertNotBuilt();
        assertNotClosed();

        this.mesh = this.meshBuilder.buildNullable();
        this.built = true;

        return this;
    }

    @Override
    public IPrimitiveBatch makeStandalone() {
        assertBuilt();
        assertNotClosed();

        if (this.mesh != null)
            this.mesh.makeStandalone();

        return this;
    }

    protected void assertBuilt() {
        if (!this.built)
            throw new IllegalStateException("Drawer not built");
    }

    protected void assertNotBuilt() {
        if (this.built)
            throw new IllegalStateException("Drawer already built");
    }

    protected void assertNotClosed() {
        if (this.closed)
            throw new IllegalStateException("Drawer already closed");
    }

    @Override
    public void close() {
        if (!this.closed) {

            if (this.mesh != null)
                this.mesh.close();
            this.closed = true;
        }
    }
}
