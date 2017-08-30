package toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import terrains.Terrain;

public class MousePicker {
	
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private Vector3f currentRay = new Vector3f();
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;
	
	// for multiple terrain it is Terrain[][] terrain
	public MousePicker(Camera cam, Matrix4f projection, Terrain terrain) {
		this.camera = cam;
		this.projectionMatrix = projection;
		this.viewMatrix = Maths.createViewMatrix(camera);
		this.terrain = terrain;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		}else {
			currentTerrainPoint = null;
		}
	}
	
	
	private Vector3f calculateMouseRay() {
		/* Viewport Space */
		// gets mouse point from bottom left corner
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		
		/* Normalized Device Space */
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		// z = -1
		/* Homogeneous Clip Space: projection matrix transform (frustum) */
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		/* Eye Space: view matrix transform */
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		/* World Space: model matrix transform */
		Vector3f worldRay = toWorldCoords(eyeCoords);
		/* Local Space */
		return worldRay;
	}
	
	// inverse view matrix
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	// inverse projection matrix
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null); // null is dest
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f); // z is set to point into the screen
	}
	
	// simple equation
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1;
		float y = (2f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	//**********************  COPIED AND PASTED CODE ************************************
	
		private Vector3f getPointOnRay(Vector3f ray, float distance) {
			Vector3f camPos = camera.getPosition();
			Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
			Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
			return Vector3f.add(start, scaledRay, null);
		}
		
		private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
			float half = start + ((finish - start) / 2f);
			if (count >= RECURSION_COUNT) {
				Vector3f endPoint = getPointOnRay(ray, half);
				Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
				if (terrain != null) {
					return endPoint;
				} else {
					return null;
				}
			}
			if (intersectionInRange(start, half, ray)) {
				return binarySearch(count + 1, start, half, ray);
			} else {
				return binarySearch(count + 1, half, finish, ray);
			}
		}

		private boolean intersectionInRange(float start, float finish, Vector3f ray) {
			Vector3f startPoint = getPointOnRay(ray, start);
			Vector3f endPoint = getPointOnRay(ray, finish);
			if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
				return true;
			} else {
				return false;
			}
		}

		private boolean isUnderGround(Vector3f testPoint) {
			Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
			float height = 0;
			if (terrain != null) {
				height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
			}
			if (testPoint.y < height) {
				return true;
			} else {
				return false;
			}
		}

		// if multiple terrain would have return the current terrain for the worldX and worldZ coords
		/*
		 * int x = worldX / Terrain.SIZE;
		 * int z = worldZ / Terrain.SIZE;
		 * return terrains[x][z];
		 */
		private Terrain getTerrain(float worldX, float worldZ) {
			return terrain;
		}

	
	
}
