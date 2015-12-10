package core;

import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;

import core.setups.GameSetup;
import core.setups.Stage;
import core.ui.UIElement;
import core.ui.utils.MouseEvent;
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
		processMouse(setup);
		
		// Refresh key bind presses
		Keybinds.update();
		
		// Enter debug mode
		if(Keybinds.DEBUG.clicked()) {
			Theater.get().debug = !Theater.get().debug;
		}
		
		// Setup specific processing
		if(setup instanceof Stage) {
			if(Keybinds.PAUSE.clicked()) {
				Theater.get().pause();
			}
		}
	}
	
	private static void processMouse(GameSetup setup) {
		while(Mouse.next()) {
			if(Mouse.getEventButton() != -1) {
				if(Mouse.getEventButtonState()) {
					System.out.println(Mouse.getEventButton() + " " + Mouse.getEventButtonState());
				} else {
					processMouseUI(setup,
							new MouseEvent(null,
									MouseEvent.CLICKED,
									Mouse.getEventX(), Camera.get().displayHeight - Mouse.getEventY()));

					System.out.println(Mouse.getEventX() + " " + (Camera.get().displayHeight - Mouse.getEventY()));
					System.out.println(Mouse.getEventButton() + " " + Mouse.getEventButtonState());
				}
			} else if(Mouse.getDX() != 0 || Mouse.getDY() != 0) {
				System.out.println(Mouse.getEventDX() + " " + Mouse.getEventDY());
			}
		}
		
		if(Mouse.hasWheel() && Mouse.getDWheel() != 0) {
			System.out.println(Mouse.getEventDWheel());
		}
	}
	
	private static void processMouseUI(GameSetup setup, MouseEvent e) {
		for(UIElement ui : setup.getUI()) {
			if(ui.getBounds().contains(e.getX(), e.getY())) {
				System.out.println("SDfsdfsdf");
				ui.fireEvent(e);
			}
		}
	}
	
}
