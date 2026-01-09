package com.ferra13671.cometrenderer.plugins.minecraft.drawer;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;

public abstract class AbstractDrawer implements IDrawer {
    protected Runnable preDrawRunnable = () -> {};
    protected final GlProgram program;
    protected final MeshBuilder meshBuilder;
    protected Mesh mesh;
    protected boolean built = false;

    public AbstractDrawer(GlProgram program, MeshBuilder meshBuilder) {
        this.program = program;
        this.meshBuilder = meshBuilder;
    }

    @Override
    public IDrawer tryDraw() {
        if (this.mesh != null) {
            this.preDrawRunnable.run();

            CometRenderer.setGlobalProgram(this.program);
            draw();

            CometRenderer.resetShaderColor();
        }

        return this;
    }

    protected abstract void draw();

    @Override
    public boolean isBuilt() {
        return this.built;
    }

    @Override
    public IDrawer build() {
        assertNotBuilt();

        this.mesh = this.meshBuilder.buildNullable();
        this.built = true;

        return this;
    }

    @Override
    public IDrawer makeStandalone() {
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

    @Override
    public void close() {
        if (this.mesh != null)
            this.mesh.close();
    }
}
