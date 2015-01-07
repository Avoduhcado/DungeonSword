package core.setups;

import core.ui.overlays.GameMenu;
import core.utilities.keyboard.Keybinds;

public class Stage extends GameSetup {
	
	private GameMenu gameMenu;
	
	public Stage() {

	}
	
	@Override
	public void update() {
		if(gameMenu != null) {
			gameMenu.update();
			if(gameMenu.isCloseRequest())
				gameMenu = null;
		} else {
			if(Keybinds.EXIT.clicked()) {
				gameMenu = new GameMenu(20f, 20f, "Menu2");
			}
		}
	}

	@Override
	public void draw() {
		if(gameMenu != null)
			gameMenu.draw();
	}

	@Override
	public void resizeRefresh() {
		
	}

}
