package core.ui.overlays;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.SoundStore;

import core.Camera;
import core.ui.Button;
import core.ui.CheckBox;
import core.ui.ElementGroup;
import core.ui.InputBox;
import core.ui.Label;
import core.ui.Slider;
import core.ui.UIElement;
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
		optionsLabel.setStill(true);
		add(optionsLabel);

		Slider musicSlider = new Slider(SoundStore.get().getMusicVolume());
		musicSlider.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.1667f));
		musicSlider.setVerticalAlign(VerticalAlign.CENTER);
		musicSlider.setStill(true);
		musicSlider.addValueChangeListener(e -> {
			if(SoundStore.get().getMusicVolume() != musicSlider.getValue()) {
				SoundStore.get().setMusicVolume((float) musicSlider.getValue());
				SoundStore.get().setCurrentMusicVolume((float) musicSlider.getValue());
			}
		});
		add(musicSlider);

		Label musicLabel = new Label("Music Volume ");
		musicLabel.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.1667f));
		musicLabel.setAlignments(VerticalAlign.CENTER, HorizontalAlign.RIGHT);
		musicLabel.setStill(true);
		add(musicLabel);

		Slider sfxSlider = new Slider(SoundStore.get().getSoundVolume());
		sfxSlider.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.215f));
		sfxSlider.setVerticalAlign(VerticalAlign.CENTER);
		sfxSlider.setStill(true);
		sfxSlider.addValueChangeListener(e -> {
			if(SoundStore.get().getSoundVolume() != sfxSlider.getValue()) {
				SoundStore.get().setSoundVolume((float) sfxSlider.getValue());
			}
		});
		add(sfxSlider);
		
		Label sfxLabel = new Label("Sound Volume ");
		sfxLabel.setPosition(() -> Camera.get().getDisplayWidth(0.35f), () -> Camera.get().getDisplayHeight(0.215f));
		sfxLabel.setAlignments(VerticalAlign.CENTER, HorizontalAlign.RIGHT);
		sfxLabel.setStill(true);
		add(sfxLabel);
		
		CheckBox fullscreenCheck = new CheckBox("Fullscreen");
		fullscreenCheck.setPosition(() -> Camera.get().getDisplayWidth(0.6f), () -> Camera.get().getDisplayHeight(0.15f));
		fullscreenCheck.setStill(true);
		fullscreenCheck.setChecked(Camera.get().isFullscreen());
		fullscreenCheck.addActionListener(e -> Camera.get().setFullscreen(fullscreenCheck.isChecked()));
		add(fullscreenCheck);
		
		CheckBox vsyncCheck = new CheckBox("VSync");
		vsyncCheck.setPosition(() -> Camera.get().getDisplayWidth(0.6f), () -> fullscreenCheck.getBounds().getMaxY());
		vsyncCheck.setStill(true);
		vsyncCheck.setChecked(Camera.get().isVSyncEnabled());
		vsyncCheck.addActionListener(e -> Camera.get().setVSync(vsyncCheck.isChecked()));
		add(vsyncCheck);
		
		Keybind keyValue;
		ElementGroup<UIElement> keyGroup;
		LinkedList<ElementGroup<UIElement>> keybinds = new LinkedList<ElementGroup<UIElement>>();
		for(int keyX = 0; keyX < 2; keyX++) {
			final int xOffset = keyX;
			for(int keyY = 0; keyY < 12; keyY++) {
				final int yOffset = keyY;
				try {
					keyValue = Keybind.values()[(keyX * 12) + keyY];
				} catch(IndexOutOfBoundsException e) {
					break;
				}
				keyGroup = new ElementGroup<UIElement>();
				
				Label keyLabel = new Label(keyValue.toString() + ": ");
				keyLabel.setPosition(() -> Camera.get().getDisplayWidth(0.333f) + (xOffset * Camera.get().getDisplayWidth(0.333f)),
						() -> Camera.get().getDisplayHeight(0.275f) + (yOffset * keyLabel.getBounds().getHeight()));
				keyLabel.setStill(true);
				keyLabel.setHorizontalAlign(HorizontalAlign.RIGHT);
				keyGroup.add(keyLabel);
				
				InputBox keyInput = new InputBox(keyValue.getKey(), InputStyle.KEYBINDS, 0);
				keyInput.setPosition(() -> Camera.get().getDisplayWidth(0.333f) + (xOffset * Camera.get().getDisplayWidth(0.333f)),
						() -> Camera.get().getDisplayHeight(0.275f) + (yOffset * keyLabel.getBounds().getHeight()));
				keyInput.setState(DISABLED);
				keyInput.setStill(true);
				keyInput.addActionListener(e -> {
					OptionsMenu.this.setEnabledAll(false);
					OptionsMenu.this.setFocus(keyInput);
				});
				keyInput.addValueChangeListener(e -> {
					Keybind.valueOf(keyLabel.getText().split(":")[0]).setKey(Keyboard.getKeyIndex(keyInput.getText()));
					keyInput.setState(DISABLED);
				});
				keyGroup.add(keyInput);
				
				keybinds.add(keyGroup);
				add(keybinds.getLast());
			}
		}
		
		Button close = new Button("Close");
		close.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.85f));
		close.setHorizontalAlign(HorizontalAlign.CENTER);
		close.setStill(true);
		close.addActionListener(e -> setState(KILL_FLAG));
		add(close);
		
		addKeybindListener(e -> {
			if(e.getKeybind().equals(Keybind.CANCEL)) {
				setState(KILL_FLAG);
			}
		});
		
		setFrame("Menu2");
		setBounds(() -> Camera.get().getDisplayWidth(0.2f), () -> Camera.get().getDisplayHeight(0.1f), 
				() -> Camera.get().getDisplayWidth(0.6f), () -> Camera.get().getDisplayHeight(0.8f));
	}

}
