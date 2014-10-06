package core.setups;

import core.ui.overlays.OptionsMenu;
import core.utilities.keyboard.Keybinds;

public class Stage extends GameSetup {
	
	private OptionsMenu options;
	
	public Stage() {

	}
	
	@Override
	public void update() {
		if(options != null) {
			options.update();
			if(options.isCloseRequest())
				options = null;
		} else {
			if(Keybinds.EXIT.clicked()) {
				options = new OptionsMenu(20, 20, "Menu2");
			}
		}
	}

	@Override
	public void draw() {
		if(options != null)
			options.draw();
	}

	@Override
	public void resizeRefresh() {
		
	}

}
