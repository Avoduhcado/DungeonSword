package core.setups;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import core.Camera;
import core.Theater;
import core.ui.Button;
import core.ui.ElementGroup;
import core.ui.Icon;
import core.ui.overlays.OptionsMenu;
import core.ui.utils.Align;

public class TitleMenu extends GameSetup {
	
	private Audio menuTheme;
	
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
		newGame.addActionListener(e -> Theater.get().setSetup(new Stage()));
		
		Button options = new Button(Float.NaN, (float) newGame.getBounds().getMaxY(), null, "Options");
		options.setStill(true);
		options.setAlign(Align.CENTER);
		options.addActionListener(e -> addUI(new OptionsMenu()));
		
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
				
		// Play title track
		try {
			menuTheme = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource(
					System.getProperty("resources") + "/music/" + "Menu" + ".ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menuTheme.playAsMusic(1f, 1f, true);
	}
	
	@Override
	public void update() {
	}
	
	@Override
	public void draw() {
	}

}
