package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 2560; //= 1280;
	private static final int HEIGHT = 1440; //= 720;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Our First Display!");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {	
		Display.sync(FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f; // milliseconds converted to seconds
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		// from player movement tutorial 18
		return Sys.getTime() * 1000 /Sys.getTimerResolution(); // current time in ticks * (1000 / the time in seconds) // divide by the number of ticks per second multiple by 1000 to get milliseconds
	}
}
