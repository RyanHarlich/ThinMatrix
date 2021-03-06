package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private final int RIGHT_MOUSE_BUTTON = 1;
	private final int LEFT_MOUSE_BUTTON = 0;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0.0f,0.0f,0.0f);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	/* TODO: delete this method */
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
	
	public void invertPitch() {
		pitch = -pitch;
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
	
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.y = -(distanceFromPlayer % 10 - 5) + player.getPosition().y + verticDistance;
		position.z = player.getPosition().z - offsetZ;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		
		if (distanceFromPlayer > 200)
			distanceFromPlayer = 200;
		else if (distanceFromPlayer < 1)
			distanceFromPlayer = 1;
	}
	
	private void calculatePitch() {
		if (Mouse.isButtonDown(RIGHT_MOUSE_BUTTON)) { // 1 is right mouse button
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
		
		if (pitch < 1)
			pitch = 1;
		else if (pitch > 90)
			pitch = 90;
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(LEFT_MOUSE_BUTTON)) { // 0 is left mouse button
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
