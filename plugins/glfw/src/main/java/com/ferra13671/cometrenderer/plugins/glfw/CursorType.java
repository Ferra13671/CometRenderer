package com.ferra13671.cometrenderer.plugins.glfw;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.glfw.GLFW;

@API(status = API.Status.MAINTAINED, since = "3.1")
@AllArgsConstructor
public enum CursorType {
    Default(GLFW.GLFW_ARROW_CURSOR, 0),
    IBeam(GLFW.GLFW_IBEAM_CURSOR, GLFW.GLFW_ARROW_CURSOR),
    Crosshair(GLFW.GLFW_CROSSHAIR_CURSOR, GLFW.GLFW_ARROW_CURSOR),
    Hand(GLFW.GLFW_POINTING_HAND_CURSOR, GLFW.GLFW_ARROW_CURSOR),
    ResizeHorizontal(GLFW.GLFW_RESIZE_EW_CURSOR, GLFW.GLFW_ARROW_CURSOR),
    ResizeVertical(GLFW.GLFW_RESIZE_NS_CURSOR, GLFW.GLFW_ARROW_CURSOR),
    ResizeTopLeftBottomRight(GLFW.GLFW_RESIZE_NWSE_CURSOR, GLFW.GLFW_RESIZE_NS_CURSOR),
    ResizeTopRightBottomLeft(GLFW.GLFW_RESIZE_NESW_CURSOR, GLFW.GLFW_RESIZE_NS_CURSOR),
    ResizeAll(GLFW.GLFW_RESIZE_ALL_CURSOR, GLFW.GLFW_ARROW_CURSOR);

    public final int id;
    public final int alternative;
}
