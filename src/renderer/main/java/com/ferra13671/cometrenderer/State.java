package com.ferra13671.cometrenderer;

import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

@API(status = API.Status.MAINTAINED, since = "2.0")
@UtilityClass
public class State {
    public BooleanState BLEND = new BooleanState() {

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
    public BooleanState SCISSOR = new BooleanState() {

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
    public TextureState TEXTURE = new TextureState() {
        @Override
        public void activeTexture(int activeTexture) {
            GL13.glActiveTexture(activeTexture);
        }

        @Override
        public void bindTexture(int texture) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        }
    };
    public FramebufferState FRAMEBUFFER = (id, viewport, width, height) -> {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
        if (viewport)
            GL11.glViewport(0, 0, width, height);
    };

    public static abstract class BooleanState {
        protected boolean state = false;

        public abstract void enable();

        public abstract void disable();
    }

    public interface TextureState {

        void activeTexture(int activeTexture);

        void bindTexture(int texture);
    }

    public interface FramebufferState {

        void bindFramebuffer(int id, boolean viewport, int width, int height);
    }
}
