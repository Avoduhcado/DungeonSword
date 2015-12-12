package core;

import org.lwjgl.input.Mouse;

import core.setups.GameSetup;
import core.setups.Stage;
import core.ui.UIElement;
import core.ui.event.MouseEvent;
import core.utilities.keyboard.Keybinds;

public class Input {
	
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
					processMouseUI(setup,
							new MouseEvent(MouseEvent.PRESSED,
									Mouse.getEventX(), Camera.get().displayHeight - Mouse.getEventY()));
					//System.out.println(Mouse.getEventButton() + " " + Mouse.getEventButtonState());
				} else {
					processMouseUI(setup,
							new MouseEvent(MouseEvent.CLICKED,
									Mouse.getEventX(), Camera.get().displayHeight - Mouse.getEventY()));

					//System.out.println(Mouse.getEventX() + " " + (Camera.get().displayHeight - Mouse.getEventY()));
					//System.out.println(Mouse.getEventButton() + " " + Mouse.getEventButtonState());
				}
			} else if(Mouse.getDX() != 0 || Mouse.getDY() != 0) {
				MouseEvent me = new MouseEvent(MouseEvent.MOVED,
						Mouse.getEventX(), Camera.get().displayHeight - Mouse.getEventY());
				me.setDx(Mouse.getEventDX());
				me.setDy(-Mouse.getEventDY());
				processMouseUI(setup, me);
				//System.out.println(Mouse.getEventDX() + " " + Mouse.getEventDY());
				
				if(Mouse.isButtonDown(0)) {
					MouseEvent med = new MouseEvent(MouseEvent.DRAGGED,
							Mouse.getEventX(), Camera.get().displayHeight - Mouse.getEventY());
					med.setDx(Mouse.getEventDX());
					med.setDy(-Mouse.getEventDY());
					processMouseUI(setup, med);
				}
			}
		}
		
		if(Mouse.hasWheel() && Mouse.getDWheel() != 0) {
			// TODO Implement mouseWheelListener
			System.out.println(Mouse.getEventDWheel());
		}
	}
	
	private static void processMouseUI(GameSetup setup, MouseEvent e) {
		for(UIElement ui : setup.getUI()) {
			switch(e.getEvent()) {
			case MouseEvent.CLICKED:
			case MouseEvent.RELEASED:
			case MouseEvent.PRESSED:
				if(ui.getBounds().contains(e.getPosition())) {
					ui.fireEvent(e);
				}
				break;
			case MouseEvent.MOVED:
			case MouseEvent.DRAGGED:
				if(ui.getBounds().contains(e.getPosition()) || ui.getBounds().contains(e.getPrevPosition())) {
					ui.fireEvent(e);
				}
				break;
			}
		}
	}
	
}
