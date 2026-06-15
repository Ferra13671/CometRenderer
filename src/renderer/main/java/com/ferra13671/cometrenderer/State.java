package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.AlphaFunction;
import com.ferra13671.cometrenderer.utils.StencilOpAction;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

@API(status = API.Status.MAINTAINED, since = "2.0")
@UtilityClass
public class State {
    public BooleanState BLEND = new BooleanState() {
        private static boolean state = false;

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
        private static boolean state = false;

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
    public StencilState STENCIL = new StencilState() {
        private boolean state = false;
        private boolean maskState = false;
        private AlphaFunction function = AlphaFunction.ALWAYS;
        private int ref = 0;
        private int mask = 1;
        private StencilOpAction stencilFailed = StencilOpAction.KEEP;
        private StencilOpAction stencilPassedDepthFailed = StencilOpAction.KEEP;
        private StencilOpAction allPassed = StencilOpAction.KEEP;

        @Override
        public void enable() {
            if (!this.state) {
                GL11.glEnable(GL11.GL_STENCIL_TEST);
                this.state = true;
            }
        }

        @Override
        public void disable() {
            if (this.state) {
                GL11.glDisable(GL11.GL_STENCIL_TEST);
                this.state = false;
            }
        }

        @Override
        public void enableMask() {
            if (!this.maskState) {
                GL11.glStencilMask(0xFF);
                this.maskState = true;
            }
        }

        @Override
        public void disableMask() {
            if (this.maskState) {
                GL11.glStencilMask(0x00);
                this.maskState = false;
            }
        }

        @Override
        public void function(AlphaFunction function, int ref, int mask) {
            if (
                    this.function != function
                    || this.ref != ref
                    || this.mask != mask
            ) {
                GL11.glStencilFunc(function.glId, ref, mask);
                this.function = function;
                this.ref = ref;
                this.mask = mask;
            }
        }

        @Override
        public void op(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed) {
            if (
                    this.stencilFailed != stencilFailed
                    || this.stencilPassedDepthFailed != stencilPassedDepthFailed
                    || this.allPassed != allPassed
            ) {
                GL11.glStencilOp(stencilFailed.glId, stencilPassedDepthFailed.glId, allPassed.glId);
                this.stencilFailed = stencilFailed;
                this.stencilPassedDepthFailed = stencilPassedDepthFailed;
                this.allPassed = allPassed;
            }
        }
    };
    public ColorMaskState COLOR_MASK = new ColorMaskState() {
        boolean redState = true;
        boolean greenState = true;
        boolean blueState = true;
        boolean alphaState = true;

        @Override
        public void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
            if (
                    this.redState != red
                    || this.greenState != green
                    || this.blueState != blue
                    || this.alphaState != alpha
            ) {
                GL11.glColorMask(red, green, blue, alpha);
                this.redState = red;
                this.greenState = green;
                this.blueState = blue;
                this.alphaState = alpha;
            }
        }
    };
    public BooleanWithMaskState DEPTH_TEST = new BooleanWithMaskState() {
        private boolean state = false;
        private boolean maskState = true;

        @Override
        public void enable() {
            if (!this.state) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                this.state = true;
            }
        }

        @Override
        public void disable() {
            if (this.state) {
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                this.state = false;
            }
        }

        @Override
        public void enableMask() {
            if (!this.maskState) {
                GL11.glDepthMask(true);
                this.maskState = true;
            }
        }

        @Override
        public void disableMask() {
            if (this.maskState) {
                GL11.glDepthMask(false);
                this.maskState = false;
            }
        }
    };
    public ProgramState PROGRAM = GL20::glUseProgram;

    public interface BooleanState {

        void enable();

        void disable();
    }

    public interface TextureState {

        void activeTexture(int activeTexture);

        void bindTexture(int texture);
    }

    public interface FramebufferState {

        void bindFramebuffer(int id, boolean viewport, int width, int height);
    }

    public interface StencilState extends BooleanWithMaskState {

        void function(AlphaFunction function, int ref, int mask);

        void op(StencilOpAction stencilFailed, StencilOpAction stencilPassedDepthFailed, StencilOpAction allPassed);
    }

    public interface ColorMaskState {

        void colorMask(boolean red, boolean green, boolean blue, boolean alpha);
    }

    public interface BooleanWithMaskState extends BooleanState {

        void enableMask();

        void disableMask();
    }

    public interface ProgramState {

        void bind(int id);
    }
}
