package _3d;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Getter
@Setter
public class Camera {
    private Vector3f position = new Vector3f(0f, 0f, 0f);
    private Vector2f rotation = new Vector2f(0f, 0f);

    public void move(Vector3f delta) {
        this.position = this.position.add(delta);
    }

    public void rotate(float yawDelta, float pitchDelta) {
        this.rotation = new Vector2f(
                this.rotation.x() + yawDelta,
                Math.max(Math.min(this.rotation.y() + pitchDelta, 89.9f), -89.9f)
        );
    }

    public Matrix4f getViewMatrix() {
        Vector3f forward = getLookVector(this.rotation.x(), this.rotation.y());
        Vector3f center = new Vector3f(this.position).add(forward);

        return new Matrix4f().setLookAt(
                this.position,
                center,
                new Vector3f(0f, 1f, 0f)
        );
    }

    public Vector3f getLookVector(float yaw, float pitch) {
        float cosPitch = (float) Math.cos(Math.toRadians(pitch));
        float sinPitch = (float) Math.sin(Math.toRadians(pitch));
        float cosYaw = (float) Math.cos(Math.toRadians(yaw));
        float sinYaw = (float) Math.sin(Math.toRadians(yaw));

        return new Vector3f(
                -sinYaw * cosPitch,
                sinPitch,
                -cosYaw * cosPitch
        );
    }
}
