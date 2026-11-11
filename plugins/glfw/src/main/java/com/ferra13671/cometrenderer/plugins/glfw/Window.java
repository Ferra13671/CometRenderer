package com.ferra13671.cometrenderer.plugins.glfw;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.plugins.glfw.callback.*;
import org.apiguardian.api.API;
import org.lwjgl.glfw.GLFWImage;

import java.io.Closeable;

@API(status = API.Status.MAINTAINED, since = "3.1")
public interface Window extends Closeable {

    long getId();

    String getTitle();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getFramebufferWidth();

    int getFramebufferHeight();

    SizeLimits getSizeLimits();

    boolean isFullScreen();

    GLFWImage getIcon();

    float getOpacity();

    boolean isVisible();

    WindowHints getHints();

    boolean isLooped();

    boolean isShouldCloseWindow();

    boolean isPressedKey(int key);

    boolean isPressedMouse(int button);

    void setTitle(String title);

    void setPosition(int x, int y);

    void setMonitorCenterPosition();

    void setSize(int width, int height);

    void setSizeLimits(SizeLimits sizeLimits);

    void setFullScreen(boolean fullScreen);

    void setIcon(GLFWImage icon);

    @API(status = API.Status.DEPRECATED, since = "3.1")
    @Deprecated(forRemoval = true)
    void setCursorGrabbed(boolean grabbed);

    void setCursorMode(CursorMode cursorMode);

    CursorMode getCursorMode();

    void setCursorType(CursorType cursorType);

    CursorType getCursorType();

    void setLoopCallback(LoopCallback loopCallback);

    void setKeyboardCallback(KeyCallback callback);

    void setCharCallback(CharCallback callback);

    void setMouseCallback(KeyCallback callback);

    void setMousePosCallback(MousePosCallback callback);

    void setMouseScrollCallback(MouseScrollCallback callback);

    void setMouseEnterCallback(MouseEnterCallback callback);

    void setWindowPosCallback(WindowPosCallback callback);

    void setWindowSizeCallback(WindowSizeCallback callback);

    void setWindowFocusCallback(WindowFocusCallback callback);

    void setWindowIconifyCallback(WindowIconifyCallback callback);

    void setFramebufferSizeCallback(FramebufferSizeCallback callback);

    void setFilesDropCallback(FilesDropCallback callback);

    void setOpacity(float opacity);

    void setVisible(boolean visible);

    void attentionRequest();

    void focus();

    void loop();

    Framebuffer getFramebuffer();

    @API(status = API.Status.INTERNAL)
    @Override
    void close();
}
