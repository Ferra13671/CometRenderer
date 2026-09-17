package com.ferra13671.cometrenderer.plugins.glfw;

import lombok.Setter;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL, since = "3.0")
@Setter
public class ScreenSize {
    private int currentWidth;
    private int currentHeight;
    private int windowedWidth;
    private int windowedHeight;

    public ScreenSize(int width, int height) {
        this.currentWidth = this.windowedWidth = width;
        this.currentHeight = this.windowedHeight = height;
    }

    public void setSize(int width, int height, boolean fullScreen) {
        this.currentWidth = width;
        this.currentHeight = height;

        if (!fullScreen) {
            this.windowedWidth = width;
            this.windowedHeight = height;
        }
    }

    public int getWidth(boolean fullScreen) {
        return fullScreen ? this.currentWidth : this.windowedWidth;
    }

    public int getHeight(boolean fullScreen) {
        return fullScreen ? this.currentHeight : this.windowedHeight;
    }
}
