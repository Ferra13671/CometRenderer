package com.ferra13671.cometrenderer.plugins.glfw;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.plugins.glfw.callback.*;
import org.apiguardian.api.API;
import org.lwjgl.glfw.GLFWImage;

import java.io.Closeable;

@API(status = API.Status.STABLE, since = "3.0")
public interface Window extends Closeable {

    long getId();

    String getTitle();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getFramebufferWidth();

    int getFramebufferHeight();

    com.ferra13671.cometrenderer.plugins.glfw.SizeLimits getSizeLimits();

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

    void setLoopCallback(LoopCallback loopCallback);

    void setKeyboardCallback(KeyCallback callback);

    void setCharCallback(CharCallback callback);

    void setMouseCallback(KeyCallback callback);

    void setMousePosCallback(MousePosCallback callback);

    void setWindowPosCallback(WindowPosCallback callback);

    void setWindowSizeCallback(WindowSizeCallback callback);

    void setFramebufferSizeCallback(FramebufferSizeCallback callback);

    void setOpacity(float opacity);

    void setVisible(boolean visible);

    void attentionRequest();

    void loop();

    Framebuffer getFramebuffer();

    @API(status = API.Status.INTERNAL)
    @Override
    void close();
}
