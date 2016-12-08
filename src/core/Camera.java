package core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.util.ResourceLoader;

import core.entities.bodies.Body;
import core.render.DrawUtils;
import core.render.effects.ScreenEffect;
import core.setups.GameSetup;
import core.utilities.text.Text;

public class Camera {
	/** Target FPS for application to run at */
	public static final int TARGET_FPS = 60;
	/** Window Icon */
	private final String icon = "AGDG Logo";
	/** Determine whether window should upscale or increase view distance on resize */
	private final boolean upscale = false;
	/** VSync status */
	private boolean vsync;
	
	/** Default Window width and current viewport width */
	private int viewWidth = 1280;
	/** Default Window height and current viewport height */
	private int viewHeight = 720;
	
	/** Current Window width */
	private int displayWidth = viewWidth;
	/** Current Window height */
	private int displayHeight = viewHeight;

	/** Target for the camera to "look at" and always be centered in the screen */
	private Body focus;
	
	private Vector4f translation = new Vector4f();
	private Vector4f scale = new Vector4f(1f, 1f, 1f, 1f);
	private Vector4f rotation = new Vector4f();
	
	private Vector4f clearColor = new Vector4f(0f, 0f, 0f, 1f);
	private Vector4f offset = new Vector4f();
	private Vector4f tint = new Vector4f(0f, 0f, 0f, 1f);
	
	private List<ScreenEffect> screenEffects = new ArrayList<ScreenEffect>();

	/** Screen singleton */
	private static Camera camera;
	
	/** Initialize Screen singleton */
	public static void init() {
		camera = new Camera();
	}
	
	/** Return Screen singleton */
	public static Camera get() {
		return camera;
	}
	
	public Camera() {
		try {
			Display.setDisplayMode(new DisplayMode(viewWidth, viewHeight));
			updateHeader();
			try {
				Display.setIcon(loadIcon(System.getProperty("resources") + "/sprites/" + icon + ".png"));
			} catch (IOException e) {
				System.out.println("Failed to load icon");
			}
			Display.setResizable(true);
			Display.create();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);
			GL11.glViewport(0, 0, displayWidth, displayHeight);
			GL11.glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
		} catch (LWJGLException e) {
			System.err.println("Could not create display.");
		}
	}
	
	public static ByteBuffer[] loadIcon(String ref) throws IOException {
        InputStream fis = ResourceLoader.getResourceAsStream(ref);
        try {
            PNGDecoder decoder = new PNGDecoder(fis);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.RGBA);
            bb.flip();
            ByteBuffer[] buffer = new ByteBuffer[1];
            buffer[0] = bb;
            return buffer;
        } finally {
            fis.close();
        }
    }
	
	public void update() {
		Display.update();
		Display.sync(TARGET_FPS);
		
		if(resized()) {
			resize();
		}
	}
	
	public void updateHeader() {
		Display.setTitle(Theater.title + "  FPS: " + Theater.fps + " " + Theater.version);
	}
	
	public void draw(GameSetup setup) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		processEffects();
		positionCamera();

		// Draw current game setup
		setup.draw();
		
		// Reload identity to draw UI
		GL11.glLoadIdentity();

		setup.drawUI();
		
		if(Theater.get().isPaused()) {
			DrawUtils.fillScreen(0, 0, 0, 0.65f);
			Text.drawString("Paused", getDisplayWidth(0.5f), getDisplayHeight(0.5f), "t+,cwhite");
		}
		
		drawScreenTint();
		
		// Draw debug info
		if(Theater.debug) {
			int y = 15;
			Text.drawString("Current Setup: " + Theater.get().getSetup().getClass().getName(), 15, y, "t+,s0.4,cwhite,d-");
			Text.drawString("Avogine v" + Theater.AVOGINE_VERSION, 15, y += 25, "t+,s0.4,cwhite,d-");
			Text.drawString("Position: " + translation.toString(), 15, y += 40, "t+,s0.4,cwhite,d-");
			Text.drawString("Scale: " + scale.toString(), 15, y += 30, "t+,s0.4,cwhite,d-");
			Text.drawString("Rotation: " + rotation.toString(), 15, y += 30, "t+,s0.4,cwhite,d-");
			Text.drawString("Mouse: " + Input.getMouseEventX() + " " + Input.getMouseEventY(), 15, y += 30, "t+,s0.4,cwhite,d-");
			
			Text.drawString("Mouse", Input.getMouseEventX(), Input.getMouseEventY());
		}
	}
	
	private void drawScreenTint() {
		DrawUtils.fillScreen(tint.x, tint.y, tint.z, tint.w);
	}

	private void positionCamera() {
		// Translation
		GL11.glTranslated(translation.x, translation.y, translation.z);

		// Perform scaling/rotation from the center of the screen to avoid weird offsets
		// Scale
		GL11.glTranslated(viewWidth * 0.5, viewHeight * 0.5, 0);
		GL11.glScalef(scale.x, scale.y, scale.z);
		GL11.glTranslated(-viewWidth * 0.5, -viewHeight * 0.5, 0);
		
		// Rotation
		GL11.glTranslated(viewWidth * 0.5, viewHeight * 0.5, 0);
		// I don't recommend rotating on the y or z axis in 2D space
		GL11.glRotatef(rotation.x, 0, 0, 1f);
		GL11.glTranslated(-viewWidth * 0.5, -viewHeight * 0.5, 0);

		// Place the focus target in the center of the screen
		if(focus != null) {
			GL11.glTranslated(-focus.getCenter().getX() + (viewWidth / 2), -focus.getCenter().getY() + (viewHeight / 2), 0);
		}
	}

	private void processEffects() {
		screenEffects.stream().forEach(ScreenEffect::apply);
		screenEffects.removeIf(ScreenEffect::isComplete);
	}
	
	public Body getFocus() {
		return focus;
	}
	
	public void setFocus(Body focus) {
		this.focus = focus;
	}

	public boolean getUpscale() {
		return upscale;
	}
	
	public boolean resized() {
		if(Display.getWidth() != displayWidth || Display.getHeight() != displayHeight) {
			return true;
		}
		
		return false;
	}
	
	public void resize() {
		displayWidth = Display.getWidth();
		displayHeight = Display.getHeight();
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		if(!upscale) {
			viewWidth = displayWidth;
			viewHeight = displayHeight;
		}
		GL11.glOrtho(0, viewWidth, viewHeight, 0, -1, 1);
	}

	/**
	 * @return If Display is in fullscreen mode
	 */
	public boolean isFullscreen() {
		return Display.isFullscreen();
	}
	
	/**
	 * Attempt to toggle display's fullscreen mode
	 * @param fullscreen
	 * @return True if toggle succeeded
	 */
	public boolean setFullscreen(boolean fullscreen) {
		try {
			Display.setFullscreen(fullscreen);
			if(fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
			} else if(!fullscreen && Display.isFullscreen()){
				Display.setDisplayMode(new DisplayMode(viewWidth, viewHeight));
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean isVSyncEnabled() {
		return vsync;
	}
	
	public void setVSync(boolean vsync) {
		this.vsync = vsync;
		Display.setVSyncEnabled(vsync);
	}
	
	public double getFrameXScale() {
		return (double) displayWidth / (double) viewWidth;
	}
	
	public double getFrameYScale() {
		return (double) displayHeight / (double) viewHeight;
	}
	
	public double getDisplayWidth() {
		return (double) displayWidth / getFrameXScale();
	}
	
	public double getDisplayHeight() {
		return (double) displayHeight / getFrameYScale();
	}
	
	public double getDisplayWidth(float mod) {
		return ((double) displayWidth * mod) / getFrameXScale();
	}
	
	public double getDisplayHeight(float mod) {
		return ((double) displayHeight * mod) / getFrameYScale();
	}
	
	public boolean isActive() {
		return Display.isActive();
	}
	
	public boolean toBeClosed() {
		if(Display.isCloseRequested()) {
			return true;
		}
		
		return false;
	}
	
	public void close() {
		Display.destroy();
	}
	
	public Vector4f getTranslation() {
		return translation;
	}
	
	public void setTranslation(Vector4f translation) {
		this.translation = translation;
	}

	public Vector4f getScale() {
		return scale;
	}
	
	public void setScale(Vector4f scale) {
		this.scale = scale;
	}

	public Vector4f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector4f rotation) {
		this.rotation = rotation;
		if(this.rotation.x >= 360) {
			this.rotation.x -= 360;
		} else if(this.rotation.x < 0) {
			this.rotation.x += 360;
		}
	}

	public Vector4f getClear() {
		return clearColor;
	}
	
	public void setClear(Vector4f clear) {
		this.clearColor = clear;
		GL11.glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
	}
	
	public Vector4f getTint() {
		return tint;
	}
	
	public void setTint(Vector4f tint) {
		this.tint = tint;
	}
	
	public void addScreenEffect(ScreenEffect effect) {
		screenEffects.add(effect);
	}

	public void cancelAllEffects() {
		screenEffects.clear();
	}
	
	public void cancelEffect(ScreenEffect effect) {
		screenEffects.remove(effect);
	}
}
