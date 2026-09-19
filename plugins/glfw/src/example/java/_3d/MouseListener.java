package _3d;

import com.ferra13671.cometrenderer.plugins.glfw.callback.MousePosCallback;
import org.joml.Vector2d;

public class MouseListener implements MousePosCallback {
    private Vector2d prevPos = null;

    @Override
    public void onPos(double mouseX, double mouseY) {
        if (this.prevPos == null) {
            this.prevPos = new Vector2d(mouseX, mouseY);
            return;
        }

        Hello3D.camera.rotate(
                (float) -(mouseX - this.prevPos.x()),
                (float) -(mouseY - this.prevPos.y())
        );
        this.prevPos = new Vector2d(mouseX, mouseY);
    }

    public void reset() {
        this.prevPos = null;
    }
}
