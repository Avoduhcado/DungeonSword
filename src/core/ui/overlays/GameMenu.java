package core.ui.overlays;

import core.Camera;
import core.Theater;
import core.setups.TitleMenu;
import core.ui.Button;
import core.ui.utils.HorizontalAlign;

public class GameMenu extends MenuOverlay {
	
	public GameMenu() {
		super();
		
		Button toGame = new Button("Return to Game");
		toGame.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.2f));
		toGame.setHorizontalAlign(HorizontalAlign.CENTER);
		toGame.addActionListener(e -> setState(KILL_FLAG));
		
		Button openOptions = new Button("Options");
		openOptions.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> toGame.getBounds().getMaxY());
		openOptions.setHorizontalAlign(HorizontalAlign.CENTER);
		openOptions.addActionListener(e -> {
			OptionsMenu optionsMenu = new OptionsMenu();
			getContainer().addUI(optionsMenu);
			getContainer().setFocus(openOptions);
		});
		
		Button toTitle = new Button("Quit to Title");
		toTitle.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> openOptions.getBounds().getMaxY());
		toTitle.setHorizontalAlign(HorizontalAlign.CENTER);
		toTitle.addActionListener(e -> Theater.get().setSetup(new TitleMenu()));
		
		Button toDesktop = new Button("Quit to Desktop");
		toDesktop.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> toTitle.getBounds().getMaxY());
		toDesktop.setHorizontalAlign(HorizontalAlign.CENTER);
		toDesktop.addActionListener(e -> Theater.get().close());
		
		toGame.setSurrounding(0, toDesktop);
		openOptions.setSurrounding(0, toGame);
		toTitle.setSurrounding(0, openOptions);
		toDesktop.setSurrounding(0, toTitle);
		
		addUI(toGame);
		addUI(openOptions);
		addUI(toTitle);
		addUI(toDesktop);
		setHorizontalAlign(HorizontalAlign.CENTER);
		setFrame("Menu2");
		setKeyboardNavigable(true, toGame);
	}

}
