package core.setups;

import core.Camera;
import core.Theater;
import core.audio.Ensemble;
import core.audio.Track;
import core.ui.Button;
import core.ui.ElementGroup;
import core.ui.Icon;
import core.ui.overlays.OptionsMenu;
import core.ui.utils.Align;
import core.ui.utils.ClickEvent;
import core.ui.utils.MouseAdapter;
import core.ui.utils.MouseEvent;

public class TitleMenu implements GameSetup {

	/** Title logo */
	private Icon logo;
	/** A button group containing New Game, Options, and Exit */
	private ElementGroup<Button> buttons;
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
		logo = new Icon("Avogine Title");
		logo.setPosition(Float.NaN, Camera.get().getDisplayHeight(0.1667f));
		
		// Initialize game buttons
		Button newGame = new Button("New Game", Float.NaN, Camera.get().getDisplayHeight(0.55f), 0, null);
		newGame.setStill(true);
		newGame.setAlign(Align.CENTER);
		newGame.addEvent(new ClickEvent(newGame) {
			public void click() {
				Theater.get().swapSetup(new Stage());
			}
		});
		
		Button options = new Button("Options", Float.NaN, (float) newGame.getBounds().getMaxY(), 0, null);
		options.setStill(true);
		options.setAlign(Align.CENTER);
		options.addEvent(new ClickEvent(options) {
			public void click() {
				optionsMenu = new OptionsMenu("Menu2");
			}
		});
		
		Button exit = new Button("Exit", Float.NaN, (float) options.getBounds().getMaxY(), 0, null);
		exit.setStill(true);
		exit.setAlign(Align.CENTER);
		exit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("SDFSDFSDF");
				Theater.get().close();	
			}
		});
		/*exit.addEvent(new ClickEvent(exit) {
			public void click() {
				Theater.get().close();
			}
		});*/
		
		// Initialize game buttons
		newGame.setSurrounding(3, options);
		options.setSurrounding(3, exit);
		exit.setSurrounding(3, newGame);
		
		buttons = new ElementGroup<Button>();
		buttons.add(newGame);
		buttons.add(options);
		buttons.add(exit);
		buttons.setKeyboardNavigable(true, newGame);
		//buttons.setSelectionPointer("screen ui/Pointer");
		buttons.addFrame("Menu2");
		
		addUI(exit);
				
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
			for(Button b : buttons) {
				b.update();
			}
		}
	}
	
	@Override
	public void draw() {
		// Draw logo
		logo.draw();
		
		// Draw buttons
		buttons.draw();
		
		// If options menu is open, draw it
		if(optionsMenu != null)
			optionsMenu.draw();
	}

	@Override
	public void drawUI() {
		// TODO Auto-generated method stub
		
	}

}
