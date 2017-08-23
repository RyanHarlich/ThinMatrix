package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0.0f,6.0f,0.0f);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {
		
	}
	
	public void move() {

	}
	
	public void debugMove() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 2.02f;
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y += 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y -= 2.02f;
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			roll += 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			pitch += 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			yaw += 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			roll -= 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			pitch -= 2.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			yaw -= 2.02f;
		}

	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}