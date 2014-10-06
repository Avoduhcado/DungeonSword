package core;

import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;

import core.setups.GameSetup;
import core.setups.Stage;
import core.utilities.keyboard.Keybinds;

public class Input {
	
	/** Global state of mouse press */
	public static boolean mousePressed;
	/** Global state of mouse hold */
	public static boolean mouseHeld;
	/** Location of mouse press */
	public static Point2D mouseClick;
	/** Location of mouse release */
	public static Point2D mouseRelease;
	
	/**
	 * Main processing of any and all input depending on current setup.
	 * @param setup The current setup of the game
	 */
	public static void checkInput(GameSetup setup) {
		// Refresh key bind presses
		Keybinds.update();
		
		// Enter debug mode
		if(Keybinds.DEBUG.clicked()) {
			Theater.get().debug = !Theater.get().debug;
		}
		
		// Refresh mouse clicks
		if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			mousePress();
		} else {
			mouseRelease();
		}
		
		// Setup specific processing
		if(setup instanceof Stage) {
			if(Keybinds.PAUSE.clicked()) {
				Theater.get().pause();
			}
		}
	}
	
	/**
	 * Detect whether or not the mouse was pressed.
	 */
	public static void mousePress() {
		if(!mousePressed) {
			mousePressed = true;
			mouseHeld = true;
			mouseClick = new Point2D.Double(Mouse.getX(), -(Mouse.getY() - Camera.get().displayHeight));
		}
	}
	
	/**
	 * Detect whether or not the mouse was released.
	 */
	public static void mouseRelease() {
		if(mousePressed) {
			mousePressed = false;
			mouseHeld = false;
			mouseRelease = new Point2D.Double(Mouse.getX(), -(Mouse.getY() - Camera.get().displayHeight));
		}
	}
	
	/**
	 * Clear the current mouse positions
	 */
	public static void clearMouse() {
		mouseClick = null;
		mouseRelease = null;
	}
	
	/**
	 * @return Location of mouse
	 */
	public static Point2D getCurrentMouse() {
		return new Point2D.Double(Mouse.getX(), -(Mouse.getY() - Camera.get().displayHeight));
	}
	
}
