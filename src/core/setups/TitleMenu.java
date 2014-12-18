package core.setups;

import core.Camera;
import core.Theater;
import core.audio.Ensemble;
import core.audio.Track;
import core.render.textured.Sprite;
import core.ui.Button;
import core.ui.ButtonGroup;
import core.ui.overlays.OptionsMenu;

public class TitleMenu extends GameSetup {

	/** Title logo */
	private Sprite logo;
	/** A button group contain New Game, Options, and Exit */
	private ButtonGroup buttonGroup;
	/** The options menu */
	private OptionsMenu optionsMenu;
	
	/**
	 * Title Menu
	 * Set up buttons for game operation.
	 */
	public TitleMenu() {
		// Ensure fading has reset
		Camera.get().setFadeTimer(-0.1f);
		
		// Load title logo
		logo = new Sprite("Avogine Title");
		
		// Initialize game buttons
		buttonGroup = new ButtonGroup(Float.NaN, Camera.get().getDisplayHeight(0.667f), "Menu2");
		buttonGroup.addButton(new Button("New Game"));
		buttonGroup.addButton(new Button("Options"));
		buttonGroup.addButton(new Button("Exit"));
		buttonGroup.setCentered(true);
		
		// Play title track
		Ensemble.get().setBackground(new Track("Menu"));
		Ensemble.get().getBackground().play();
	}
	
	@Override
	public void update() {
		// Update options instead of main screen if it's open
		if(optionsMenu != null) {
			optionsMenu.update();
			// Close options if user chooses to close
			if(optionsMenu.isCloseRequest())
				optionsMenu = null;
		} else {
			// Update buttons
			buttonGroup.update();
			if(buttonGroup.getButton(0).isClicked()) {
				// Start game, proceed with state swap
				Theater.get().swapSetup(new Stage());
			} else if(buttonGroup.getButton(1).isClicked()) {
				// Open options menu
				optionsMenu = new OptionsMenu(20, 20, "Menu2");
			} else if(buttonGroup.getButton(2).isClicked()) {
				// Exit game
				Theater.get().close();
			}
		}
	}
	
	@Override
	public void draw() {
		// Draw logo
		logo.draw(Float.NaN, Camera.get().getDisplayHeight(0.1667f));
		
		// Draw buttons
		buttonGroup.draw();
		
		// If options menu is open, draw it
		if(optionsMenu != null)
			optionsMenu.draw();
	}

	@Override
	public void resizeRefresh() {
		// Reposition and center
		buttonGroup.setPosition(Float.NaN, Camera.get().getDisplayHeight(0.667f));
	}

}