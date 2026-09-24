package com.ferra13671.cometrenderer.plugins.glfw;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferInfo;
import com.ferra13671.cometrenderer.plugins.glfw.callback.*;
import com.ferra13671.cometrenderer.plugins.glfw.key.KeyHandler;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

final class WindowImpl implements Window {
    @Getter
    private final long id;

    @Getter
    private String title = "Window";
    @Getter
    private int x;
    @Getter
    private int y;
    private final ScreenSize screenSize = new ScreenSize(800, 600);
    @Getter
    private SizeLimits sizeLimits;
    @Getter
    private boolean fullScreen = false;
    @Getter
    private final WindowHints hints;
    @Getter
    private GLFWImage icon;
    private GLFWImage.Buffer iconBuffer;
    @Getter
    private float opacity;
    @Getter
    private boolean visible;

    private final KeyHandler keyboardHandler;
    private final KeyHandler mouseHandler;
    private final FullScreenSwitchCombination fullScreenSwitchCombination;
    private final boolean clearFramebuffer;

    @Getter
    private Framebuffer framebuffer;

    @Setter
    private LoopCallback loopCallback;
    @Setter
    private CharCallback charCallback;
    @Setter
    private MousePosCallback mousePosCallback;
    @Setter
    private WindowPosCallback windowPosCallback;
    @Setter
    private WindowSizeCallback windowSizeCallback;
    @Setter
    private FramebufferSizeCallback framebufferSizeCallback;

    @Getter
    private boolean looped = false;

    WindowImpl(WindowHints hints, KeyHandler keyboardHandler, KeyHandler mouseHandler, FullScreenSwitchCombination fullScreenSwitchCombination, boolean clearFramebuffer, boolean debugContext) {
        this.hints = hints;
        this.keyboardHandler = keyboardHandler;
        this.mouseHandler = mouseHandler;
        this.fullScreenSwitchCombination = fullScreenSwitchCombination;
        this.clearFramebuffer = clearFramebuffer;

        setupHints(debugContext);
        this.id = glfwCreateWindow(getWidth(), getHeight(), this.title, MemoryUtil.NULL, MemoryUtil.NULL);

        int[] xa = new int[1], ya = new int[1];
        glfwGetWindowPos(getId(), xa, ya);
        this.x = xa[0];
        this.y = ya[0];

        this.opacity = glfwGetWindowOpacity(getId());

        setupCallbacks();
    }

    @Override
    public void focus() {
        glfwFocusWindow(getId());
    }

    @Override
    public void loop() {
        if (!isShouldCloseWindow()) {

            this.looped = true;

            int[] widtha = new int[1], heighta = new int[1];
            glfwGetFramebufferSize(getId(), widtha, heighta);
            this.framebuffer = new FramebufferImpl(
                    FramebufferInfo.builder()
                            .name("CometGLFW main framebuffer")
                            .width(widtha[0])
                            .height(heighta[0])
                            .clearColor(new Color(0f, 0f, 0f, 0f))
                            .build()
            );

            while (!isShouldCloseWindow()) {
                if (this.clearFramebuffer) {
                    glClear(GL_COLOR_BUFFER_BIT);
                    getFramebuffer().clearAll();
                }

                if (this.loopCallback != null)
                    this.loopCallback.onLoop();

                Framebuffer framebuffer = getFramebuffer();
                framebuffer.blit(0, framebuffer.getWidth(), framebuffer.getHeight(), false, false);

                glfwSwapBuffers(getId());
                glfwPollEvents();
            }

            closeWindow();
        }
    }

    @Override
    public int getWidth() {
        return this.screenSize.getWidth(isFullScreen());
    }

    @Override
    public int getHeight() {
        return this.screenSize.getHeight(isFullScreen());
    }

    @Override
    public int getFramebufferWidth() {
        return getFramebuffer().getWidth();
    }

    @Override
    public int getFramebufferHeight() {
        return getFramebuffer().getHeight();
    }

    @Override
    public boolean isShouldCloseWindow() {
        return glfwWindowShouldClose(getId());
    }

    @Override
    public boolean isPressedKey(int key) {
        return this.keyboardHandler.isPressed(key);
    }

    @Override
    public boolean isPressedMouse(int button) {
        return this.mouseHandler.isPressed(button);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;

        glfwSetWindowTitle(getId(), title);
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;

        glfwSetWindowPos(getId(), x, y);
    }

    @Override
    public void setMonitorCenterPosition() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        setPosition(
                (vidMode.width() - getWidth()) / 2,
                (vidMode.height() - getHeight()) / 2
        );
    }

    @Override
    public void setSize(int width, int height) {
        this.screenSize.setSize(width, height, isFullScreen());

        glfwSetWindowSize(getId(), width, height);
    }

    @Override
    public void setSizeLimits(SizeLimits sizeLimits) {
        this.sizeLimits = sizeLimits;

        if (sizeLimits != null) {
            glfwSetWindowSizeLimits(
                    getId(),
                    sizeLimits.minWidth(),
                    sizeLimits.minHeight(),
                    sizeLimits.maxWidth(),
                    sizeLimits.maxHeight()
            );
        } else {
            glfwSetWindowSizeLimits(
                    getId(),
                    GLFW_DONT_CARE,
                    GLFW_DONT_CARE,
                    GLFW_DONT_CARE,
                    GLFW_DONT_CARE
            );
        }
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;

        if (fullScreen) {
            long monitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);

            setSize(vidMode.width(), vidMode.height());
            glfwSetWindowMonitor(getId(), monitor, 0, 0, vidMode.width(), vidMode.height(), GLFW_DONT_CARE);
        } else {
            glfwSetWindowMonitor(getId(), MemoryUtil.NULL, getX(), getY(), getWidth(), getHeight(), GLFW_DONT_CARE);
            setMonitorCenterPosition();
        }
    }

    @Override
    public void setIcon(GLFWImage icon) {
        if (!this.icon.equals(icon)) {
            this.icon.close();
            this.icon = icon;

            if (this.iconBuffer != null)
                this.iconBuffer.close();

            this.iconBuffer = GLFWImage.malloc(1);
            this.iconBuffer.put(0, this.icon);

            glfwSetWindowIcon(getId(), this.iconBuffer);
        }
    }

    @Override
    public void setCursorGrabbed(boolean grabbed) {
        glfwSetInputMode(getId(), GLFW_CURSOR, grabbed ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    @Override
    public void setKeyboardCallback(KeyCallback callback) {
        this.keyboardHandler.setCallback(callback);
    }

    @Override
    public void setMouseCallback(KeyCallback callback) {
        this.mouseHandler.setCallback(callback);
    }

    @Override
    public void setOpacity(float opacity) {
        this.opacity = opacity;

        glfwSetWindowOpacity(getId(), opacity);
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;

            if (visible)
                glfwShowWindow(getId());
            else
                glfwHideWindow(getId());
        }
    }

    @Override
    public void attentionRequest() {
        glfwRequestWindowAttention(getId());
    }

    @Override
    public void close() {
        glfwSetWindowShouldClose(getId(), true);
    }

    void closeWindow() {
        glfwFreeCallbacks(getId());
        glfwDestroyWindow(getId());
        this.looped = false;
    }

    void setupHints(boolean debugContext) {
        glfwDefaultWindowHints();

        if (this.hints.getGlVersion() != null) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, (int) (this.hints.getGlVersion().id / 10f));
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, this.hints.getGlVersion().id % 10);
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, this.hints.isDebugContext() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, this.hints.getGlProfile().glfwId);

        glfwWindowHint(GLFW_RESIZABLE, this.hints.isResizable() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, this.hints.isDecorated() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_AUTO_ICONIFY, this.hints.isAutoIconify() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, this.hints.isTransparent() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, this.hints.isFocusOnShow() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, debugContext ? GLFW_TRUE : GLFW_FALSE);
    }

    void setupCallbacks() {
        glfwSetKeyCallback(getId(), (window, key, scancode, action, mods) -> {
            if (window == getId()) {
                this.keyboardHandler.onKey(key, action);

                if (action == GLFW_PRESS && this.fullScreenSwitchCombination.isCombinationPressed.apply(this.keyboardHandler))
                    setFullScreen(!isFullScreen());
            }
        });
        glfwSetMouseButtonCallback(getId(), (window, button, action, mods) -> {
            if (window == getId())
                this.mouseHandler.onKey(button, action);
        });
        glfwSetCharModsCallback(getId(), (window, codePoint, modifiers) -> {
            if (window == getId() && this.charCallback != null)
                if (Character.charCount(codePoint) == 1) {
                    this.charCallback.onChar((char) codePoint);
                } else {
                    char[] chars = Character.toChars(codePoint);

                    for (char c : chars)
                        this.charCallback.onChar(c);
                }
        });
        glfwSetCursorPosCallback(getId(), (window, mouseX, mouseY) -> {
            if (window == getId() && this.mousePosCallback != null)
                this.mousePosCallback.onPos(mouseX, mouseY);
        });
        glfwSetWindowPosCallback(getId(), (window, x, y) -> {
            if (window == getId()) {
                setPosition(x, y);

                if (this.windowPosCallback != null)
                    this.windowPosCallback.onPos(x, y);
            }
        });
        glfwSetWindowSizeCallback(getId(), (window, width, height) -> {
            if (window == getId()) {
                setSize(width, height);

                if (this.windowSizeCallback != null)
                    this.windowSizeCallback.onSize(width, height);
            }
        });
        glfwSetFramebufferSizeCallback(getId(), (window, width, height) -> {
            if (window == getId()) {
                if (getFramebuffer() != null)
                    getFramebuffer().resize(width, height);

                if (this.framebufferSizeCallback != null)
                    this.framebufferSizeCallback.onSize(width, height);
            }
        });
    }
}
