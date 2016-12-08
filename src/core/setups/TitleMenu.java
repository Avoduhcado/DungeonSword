package core.setups;

import java.io.IOException;

import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import core.Camera;
import core.Theater;
import core.render.effects.TintEffect;
import core.render.effects.Tween;
import core.ui.Button;
import core.ui.ElementGroup;
import core.ui.Icon;
import core.ui.overlays.OptionsMenu;
import core.ui.utils.HorizontalAlign;
import core.ui.utils.VerticalAlign;

public class TitleMenu extends GameSetup {
	
	private Audio menuTheme;
	
	/**
	 * Title Menu
	 * Set up buttons for game operation.
	 */
	public TitleMenu() {
		// Ensure fading has reset
		Camera.get().addScreenEffect(new TintEffect(new Vector4f(0f, 0f, 0f, 0f), 0f, true, Tween.IN));
		
		// Load title logo
		Icon logo = new Icon("Avogine Title");
		logo.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.2667f));
		logo.setAlignments(VerticalAlign.CENTER, HorizontalAlign.CENTER);
		addUI(logo);
				
		// Initialize game buttons
		Button newGame = new Button("New Game");
		newGame.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.55f));
		newGame.setStill(true);
		newGame.setHorizontalAlign(HorizontalAlign.CENTER);
		newGame.addActionListener(e -> Theater.get().setSetup(new Stage()));
		
		Button options = new Button("Options");
		options.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> newGame.getBounds().getMaxY());
		options.setStill(true);
		options.setHorizontalAlign(HorizontalAlign.CENTER);
		options.addActionListener(e -> addUI(new OptionsMenu()));
		
		Button exit = new Button("Exit");
		exit.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> options.getBounds().getMaxY());
		exit.setStill(true);
		exit.setHorizontalAlign(HorizontalAlign.CENTER);
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
