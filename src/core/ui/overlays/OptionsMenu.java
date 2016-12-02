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
		fullscreenCheck.setPosition(() -> Camera.get().getDisplayWidth(0.6f), () -> Camera.get().getDisplayHeight(0.1667f));
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
		
		LinkedList<ElementGroup<UIElement>> keybinds = new LinkedList<ElementGroup<UIElement>>();
		double keyY = 0;
		for(int i = 0; i<Keybind.values().length; i++) {
			ElementGroup<UIElement> key = new ElementGroup<UIElement>();
			
			Label keyLabel = new Label(Keybind.values()[i].toString() + ": ");
			keyLabel.setPosition(() -> Camera.get().getDisplayWidth(0.25f), () -> Camera.get().getDisplayHeight(0.285f));
			keyLabel.setStill(true);
			keyLabel.setHorizontalAlign(HorizontalAlign.RIGHT);
			key.add(keyLabel);
			
			InputBox keyBox = new InputBox(Keybind.values()[i].getKey(), InputStyle.KEYBINDS, 0);
			keyBox.setPosition(() -> Camera.get().getDisplayWidth(0.25f), () -> Camera.get().getDisplayHeight(0.285f));
			keyBox.setState(DISABLED);
			keyBox.setStill(true);
			keyBox.setCentered(false);
			keyBox.addActionListener(e -> OptionsMenu.this.setFocus(keyBox));
			keyBox.addValueChangeListener(e -> {
				Keybind.valueOf(keyLabel.getText().split(":")[0]).setKey(Keyboard.getKeyIndex(keyBox.getText()));
				keyBox.setState(DISABLED);
			});
			key.add(keyBox);
			
			keybinds.add(key);
			add(keybinds.getLast());
			
			keyY += keyLabel.getBounds().getHeight();
			if(Camera.get().getDisplayHeight(0.285f) + keyY > Camera.get().getDisplayHeight(0.8f)) {
				// TODO Implement scrollpanes or fix this offset issue
				//xOffsett += 0.25f;
				keyY = 0;
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
	}

}
