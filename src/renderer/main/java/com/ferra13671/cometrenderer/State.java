package com.ferra13671.cometrenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class State {
    public static BooleanState BLEND = new BooleanState() {

        @Override
        public void enable() {
            if (!this.state) {
                GL11.glEnable(GL11.GL_BLEND);
                this.state = true;
            }
        }

        @Override
        public void disable() {
            if (this.state) {
                GL11.glDisable(GL11.GL_BLEND);
                this.state = false;
            }
        }
    };
    public static BooleanState SCISSOR = new BooleanState() {

        @Override
        public void enable() {
            if (!this.state) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                this.state = true;
            }
        }

        @Override
        public void disable() {
            if (this.state) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                this.state = false;
            }
        }
    };
    public static TextureState TEXTURE = new TextureState() {
        @Override
        public void activeTexture(int activeTexture) {
            GL13.glActiveTexture(activeTexture);
        }

        @Override
        public void bindTexture(int texture) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        }
    };

    public static abstract class BooleanState {
        protected boolean state = false;

        public abstract void enable();

        public abstract void disable();
    }

    public static abstract class TextureState {

        public abstract void activeTexture(int activeTexture);

        public abstract void bindTexture(int texture);
    }
}
