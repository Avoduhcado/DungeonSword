package core.ui.overlays;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.SoundStore;

import core.Camera;
import core.ui.Button;
import core.ui.CheckBox;
import core.ui.InputBox;
import core.ui.Label;
import core.ui.Slider;
import core.ui.utils.HorizontalAlign;
import core.ui.utils.InputStyle;
import core.ui.utils.VerticalAlign;
import core.utilities.keyboard.Keybind;

public class OptionsMenu extends MenuOverlay {

	public OptionsMenu() {
		super();
		/*try {
			displayModes = Display.getAvailableDisplayModes();
			for(DisplayMode d : displayModes)
				modes += d.toString() + "\n";
		} catch (LWJGLException e) {
			e.printStackTrace();
		}*/
		
		Label optionsLabel = new Label("Options");
		optionsLabel.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.1f));
		optionsLabel.setHorizontalAlign(HorizontalAlign.CENTER);
		addUI(optionsLabel);

		Slider musicSlider = new Slider(SoundStore.get().getMusicVolume());
		musicSlider.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.1667f));
		musicSlider.setVerticalAlign(VerticalAlign.CENTER);
		musicSlider.addValueChangeListener(e -> {
			if(SoundStore.get().getMusicVolume() != musicSlider.getValue()) {
				SoundStore.get().setMusicVolume((float) musicSlider.getValue());
				SoundStore.get().setCurrentMusicVolume((float) musicSlider.getValue());
			}
		});
		addUI(musicSlider);

		Label musicLabel = new Label("Music Volume ");
		musicLabel.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.1667f));
		musicLabel.setAlignments(VerticalAlign.CENTER, HorizontalAlign.RIGHT);
		addUI(musicLabel);

		Slider sfxSlider = new Slider(SoundStore.get().getSoundVolume());
		sfxSlider.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.215f));
		sfxSlider.setVerticalAlign(VerticalAlign.CENTER);
		sfxSlider.addValueChangeListener(e -> {
			if(SoundStore.get().getSoundVolume() != sfxSlider.getValue()) {
				SoundStore.get().setSoundVolume((float) sfxSlider.getValue());
			}
		});
		addUI(sfxSlider);
		
		Label sfxLabel = new Label("Sound Volume ");
		sfxLabel.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.215f));
		sfxLabel.setAlignments(VerticalAlign.CENTER, HorizontalAlign.RIGHT);
		addUI(sfxLabel);
		
		CheckBox fullscreenCheck = new CheckBox("Fullscreen");
		fullscreenCheck.setPosition(() -> Camera.get().getDisplayWidth(0.6f), () -> Camera.get().getDisplayHeight(0.15f));
		fullscreenCheck.setChecked(Camera.get().isFullscreen());
		fullscreenCheck.addActionListener(e -> Camera.get().setFullscreen(fullscreenCheck.isChecked()));
		addUI(fullscreenCheck);
		
		CheckBox vsyncCheck = new CheckBox("VSync");
		vsyncCheck.setPosition(() -> Camera.get().getDisplayWidth(0.6f), () -> fullscreenCheck.getBounds().getMaxY());
		vsyncCheck.setChecked(Camera.get().isVSyncEnabled());
		vsyncCheck.addActionListener(e -> Camera.get().setVSync(vsyncCheck.isChecked()));
		addUI(vsyncCheck);
		
		Keybind keyValue;
		for(int keyX = 0; keyX < 2; keyX++) {
			final int xOffset = keyX;
			for(int keyY = 0; keyY < 12; keyY++) {
				final int yOffset = keyY;
				try {
					keyValue = Keybind.values()[(keyX * 12) + keyY];
				} catch(IndexOutOfBoundsException e) {
					break;
				}
				
				Label keyLabel = new Label(keyValue.toString() + ": ");
				keyLabel.setPosition(() -> Camera.get().getDisplayWidth(0.333f) + (xOffset * Camera.get().getDisplayWidth(0.333f)),
						() -> Camera.get().getDisplayHeight(0.275f) + (yOffset * keyLabel.getBounds().getHeight()));
				keyLabel.setHorizontalAlign(HorizontalAlign.RIGHT);
				addUI(keyLabel);
				
				InputBox keyInput = new InputBox(keyValue.getKey(), InputStyle.KEYBINDS, 0);
				keyInput.setPosition(() -> Camera.get().getDisplayWidth(0.333f) + (xOffset * Camera.get().getDisplayWidth(0.333f)),
						() -> Camera.get().getDisplayHeight(0.275f) + (yOffset * keyLabel.getBounds().getHeight()));
				keyInput.addActionListener(e -> OptionsMenu.this.setFocus(keyInput));
				keyInput.addValueChangeListener(e -> {
					Keybind.valueOf(keyLabel.getText().split(":")[0]).setKey(Keyboard.getKeyIndex(keyInput.getText()));
				});
				addUI(keyInput);
			}
		}
		
		Button close = new Button("Close");
		close.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.85f));
		close.setHorizontalAlign(HorizontalAlign.CENTER);
		close.addActionListener(e -> setState(KILL_FLAG));
		addUI(close);
		
		/*addKeybindListener(e -> {
			if(e.getKeybind().equals(Keybind.CANCEL)) {
				setState(KILL_FLAG);
			}
		});*/
		
		setFrame("Menu2");
		setBounds(() -> Camera.get().getDisplayWidth(0.2f), () -> Camera.get().getDisplayHeight(0.1f), 
				() -> Camera.get().getDisplayWidth(0.6f), () -> Camera.get().getDisplayHeight(0.8f));
	}

}
