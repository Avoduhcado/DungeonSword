package core.setups;

import org.newdawn.slick.openal.SoundStore;

import core.Camera;
import core.Theater;
import core.audio.Ensemble;
import core.audio.Track;
import core.ui.Button;
import core.ui.ElementGroup;
import core.ui.Icon;
import core.ui.Slider;
import core.ui.TextBox;
import core.ui.event.ActionEvent;
import core.ui.event.ActionListener;
import core.ui.event.ValueChangeEvent;
import core.ui.event.ValueChangeListener;
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
		Icon logo = new Icon("Avogine Title");
		logo.setPosition(Float.NaN, Camera.get().getDisplayHeight(0.1667f));
		addUI(logo);
		
		// Initialize game buttons
		Button newGame = new Button("New Game", Float.NaN, Camera.get().getDisplayHeight(0.55f), 0, null);
		newGame.setStill(true);
		newGame.setAlign(Align.CENTER);
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Theater.get().swapSetup(new Stage());
			}
		});
		
		Button options = new Button("Options", Float.NaN, (float) newGame.getBounds().getMaxY(), 0, null);
		options.setStill(true);
		options.setAlign(Align.CENTER);
		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionsMenu = new OptionsMenu("Menu2");
			}
		});
		
		Button exit = new Button("Exit", Float.NaN, (float) options.getBounds().getMaxY(), 0, null);
		exit.setStill(true);
		exit.setAlign(Align.CENTER);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Theater.get().close();	
			}
		});
		
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
		buttons.addFrame("Menu2");
		addUI(buttons);
		
		TextBox text = new TextBox("Hello World; How are you doing today??", 100, 500, "Menu2", true);
		addUI(text);
		
		Slider slider = new Slider(100, 200, 0.5f);
		slider.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent e) {
				// TODO Remove Ensemble
				Ensemble.get().setMasterVolume((float) e.getValue());
				//SoundStore.get().setMusicVolume((float) e.getValue());
				//SoundStore.get().setCurrentMusicVolume((float) e.getValue());
			}
		});
		addUI(slider);
				
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
