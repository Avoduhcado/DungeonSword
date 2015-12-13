package core.ui.overlays;

import core.Camera;
import core.Theater;
import core.render.DrawUtils;
import core.setups.TitleMenu;
import core.ui.Button;
import core.ui.utils.Align;

public class GameMenu extends MenuOverlay {

	private OptionsMenu options;
	
	public GameMenu() {
		Button toGame = new Button(Float.NaN, Camera.get().getDisplayHeight(0.2f), null, "Return to Game");
		toGame.setAlign(Align.CENTER);
		toGame.setStill(true);
		toGame.addActionListener(e -> toClose = true);
		
		Button openOptions = new Button(Float.NaN, (float) toGame.getBounds().getMaxY(), null, "Options");
		openOptions.setAlign(Align.CENTER);
		openOptions.setStill(true);
		openOptions.addActionListener(e -> options = new OptionsMenu());
		
		Button toTitle = new Button(Float.NaN, (float) openOptions.getBounds().getMaxY(), null, "Quit to Title");
		toTitle.setAlign(Align.CENTER);
		toTitle.setStill(true);
		toTitle.addActionListener(e -> Theater.get().swapSetup(new TitleMenu()));
		
		Button toDesktop = new Button(Float.NaN, (float) toTitle.getBounds().getMaxY(), null, "Quit to Desktop");
		toDesktop.setAlign(Align.CENTER);
		toDesktop.setStill(true);
		toDesktop.addActionListener(e -> Theater.get().close());
		
		toGame.setSurrounding(0, toDesktop);
		add(toGame);
		openOptions.setSurrounding(0, toGame);
		add(openOptions);
		toTitle.setSurrounding(0, openOptions);
		add(toTitle);
		toDesktop.setSurrounding(0, toTitle);
		add(toDesktop);
		
		setKeyboardNavigable(true, toGame);
		
		setFrame("Menu2");
	}
	
	@Override
	public void update() {
		if(options != null) {
			options.update();
			if(options.isCloseRequest())
				options = null;
		} else {
			super.update();
		}
	}
	
	@Override
	public void draw() {
		DrawUtils.fillColor(0f, 0f, 0f, 0.65f);
		
		if(options != null) {
			options.draw();
		} else {
			super.draw();
		}
	}

	@Override
	public boolean isCloseRequest() {
		return super.isCloseRequest() && options == null;
	}

}
