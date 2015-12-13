package core.setups;

import core.Camera;
import core.Theater;
import core.audio.Ensemble;
import core.audio.Track;
import core.ui.Button;
import core.ui.CheckBox;
import core.ui.ElementGroup;
import core.ui.Icon;
import core.ui.InputBox;
import core.ui.Slider;
import core.ui.TextBox;
import core.ui.overlays.OptionsMenu;
import core.ui.utils.Align;

public class TitleMenu extends GameSetup {

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
		Icon logo = new Icon(Float.NaN, Camera.get().getDisplayHeight(0.1667f), "Avogine Title");
		addUI(logo);
		
		// Initialize game buttons
		Button newGame = new Button(Float.NaN, Camera.get().getDisplayHeight(0.55f), null, "New Game");
		newGame.setStill(true);
		newGame.setAlign(Align.CENTER);
		newGame.addActionListener(e -> Theater.get().swapSetup(new Stage()));
		
		Button options = new Button(Float.NaN, (float) newGame.getBounds().getMaxY(), null, "Options");
		options.setStill(true);
		options.setAlign(Align.CENTER);
		options.addActionListener(e -> addUI(optionsMenu = new OptionsMenu()));
		
		Button exit = new Button(Float.NaN, (float) options.getBounds().getMaxY(), null, "Exit");
		exit.setStill(true);
		exit.setAlign(Align.CENTER);
		exit.addActionListener(e -> Theater.get().close());
		
		// Initialize game buttons
		newGame.setSurrounding(3, options);
		options.setSurrounding(3, exit);
		exit.setSurrounding(3, newGame);
		
		ElementGroup<Button> buttons = new ElementGroup<Button>();
		buttons.add(newGame);
		buttons.add(options);
		buttons.add(exit);
		buttons.setKeyboardNavigable(true, newGame);
		//buttons.setSelectionPointer("screen ui/Pointer");
		buttons.setFrame("Menu2");
		addUI(buttons);
		
		TextBox text = new TextBox(100, 500, "Menu2", "Hello World; How are you doing today??", true);
		addUI(text);
		
		Slider slider = new Slider(100, 200, 0.5f);
		slider.addValueChangeListener(e -> Ensemble.get().setMasterVolume((float) e.getValue()));
		addUI(slider);
		
		CheckBox check = new CheckBox(400, 200, "Menu2", "Click me");
		check.addActionListener(e -> Ensemble.get().mute());
		//addUI(check);
		
		InputBox input = new InputBox(500, 200, "Menu2", null, 0, 10);
		input.addValueChangeListener(e -> System.out.println(e.getValue()));
		addUI(input);
				
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
		}
	}
	
	@Override
	public void draw() {
		// If options menu is open, draw it
		if(optionsMenu != null)
			optionsMenu.draw();
	}

}
