package core;

import java.awt.geom.Rectangle2D;
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

import core.render.DrawUtils;
import core.render.effects.ScreenEffect;
import core.setups.GameSetup;
import core.utilities.text.Text;

public class Camera {
	
	/** Default Window width */
	public final int WIDTH = 1280;
	/** Default Window height */
	public final int HEIGHT = 720;
	/** Target FPS for application to run at */
	public static final int TARGET_FPS = 60;
	/** Window Icon */
	private final String icon = "AGDG Logo";
	
	/** Current Window width */
	public int displayWidth = WIDTH;
	/** Current Window height */
	public int displayHeight = HEIGHT;

	/** VSync status */
	private boolean vsync;
	
	/** View frame fixed to default size */
	public final Rectangle2D fixedFrame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	/** Current view frame */
	public Rectangle2D frame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	
	private Vector4f translation = new Vector4f();
	private Vector4f scale = new Vector4f(1f, 1f, 1f, 1f);
	private Vector4f rotation = new Vector4f();
	
	private Vector4f offset = new Vector4f();
	private Vector4f tint = new Vector4f(0f, 0f, 0f, 1f);
	
	private List<ScreenEffect> screenEffects = new ArrayList<ScreenEffect>();

	/** Determine whether window should upscale or increase view distance on resize */
	private boolean upscale = false;
	
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
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
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
		} catch (LWJGLException e) {
			System.err.println("Could not create display.");
		}
		
		frame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
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
		
		if(resized())
			resize();
	}
	
	public void updateHeader() {
		Display.setTitle(Theater.title + "  FPS: " + Theater.fps + " " + Theater.version);
	}
	
	public void draw(GameSetup setup) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		DrawUtils.fillScreen(0f, 0f, 0f, 1f);

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
			
			/*Text.drawString("Mouse", Mouse.getX() / ((float) Display.getWidth() / (float) WIDTH),
					(float) (frame.getHeight() - Mouse.getY()) / ((float) Display.getHeight() / (float) HEIGHT), "t+");*/
			Text.drawString("Mouse", Input.getMouseEventX(), Input.getMouseEventY());
		}
	}
	
	private void drawScreenTint() {
		DrawUtils.fillScreen(tint.x, tint.y, tint.z, tint.w);
	}

	private void positionCamera() {
		// Translation
		frame.setFrameFromCenter(translation.x, translation.y, translation.x - (fixedFrame.getWidth() / 2f), translation.y - (fixedFrame.getHeight() / 2f));

		// Scale
		GL11.glTranslated(frame.getWidth() / 2f, frame.getHeight() / 2f, 0);
		GL11.glScalef(scale.x, scale.y, scale.z);
		GL11.glTranslated(-frame.getWidth() / 2f, -frame.getHeight() / 2f, 0);
		
		// Rotation
		GL11.glTranslated(frame.getWidth() / 2f, frame.getHeight() / 2f, 0);
		// I don't recommend rotating on the y or z axis in 2D space
		GL11.glRotatef(rotation.x, 0, 0, 1f);
		GL11.glTranslated(-frame.getWidth() / 2f, -frame.getHeight() / 2f, 0);
	}
	
	private void processEffects() {
		screenEffects.stream().forEach(ScreenEffect::apply);
		screenEffects.removeIf(ScreenEffect::isComplete);
	}

	public boolean getUpscale() {
		return upscale;
	}
	
	public void setUpscale(boolean upscale) {
		this.upscale = upscale;
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
		
		frame = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
		
		if(upscale) {
			// Upscale
			GL11.glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
		} else {
			// Increase view window
			GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);
		}
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
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
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

	public float getFrameXScale() {
		if(upscale) {
			return 1f;//(float) (frame.getWidth() / fixedFrame.getWidth());
		} else {
			return 1f;
		}
	}
	
	public float getFrameYScale() {
		if(upscale) {
			return 1f;//(float) (frame.getHeight() / fixedFrame.getHeight());
		} else {
			return 1f;
		}
	}
	
	public double getDisplayWidth() {
		//return displayWidth / getFrameXScale();
		return displayWidth;
	}
	
	public double getDisplayHeight() {
		//return displayHeight / getFrameYScale();
		return displayHeight;
	}
	
	public double getDisplayWidth(float mod) {
		//return (displayWidth * mod) / getFrameXScale();
		return (displayWidth * mod);
	}
	
	public double getDisplayHeight(float mod) {
		//return (displayHeight * mod) / getFrameYScale();
		return (displayHeight * mod);
	}
	
	public boolean isFocus() {
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
