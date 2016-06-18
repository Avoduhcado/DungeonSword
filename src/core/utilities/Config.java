package core.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.newdawn.slick.openal.SoundStore;

import core.Camera;
import core.utilities.keyboard.Keybind;

public class Config {
	
	private static final Avoperties props = new Avoperties("config.properties");
	
	public static String getProperty(String property) {
		return props.getProperty(property);
	}
	
	public static String getProperty(String property, String defValue) {
		return props.getProperty(property, defValue);
	}
	
	private static void setProperty(String property, String value) {
		props.setProperty(property, value);
	}
	
	public static void loadConfig() {
		// Audio
		float musicVolume = new Float(getProperty("music", "1f"));
		SoundStore.get().setMusicVolume(musicVolume);
		SoundStore.get().setCurrentMusicVolume(musicVolume);
		float sfxVolume = new Float(getProperty("sfx", "1f"));
		SoundStore.get().setSoundVolume(sfxVolume);
		
		// Video
		boolean fullScreen = new Boolean(getProperty("fullscreen", "false"));
		Camera.get().setFullscreen(fullScreen);
		boolean vsync = new Boolean(getProperty("vsync", "false"));
		Camera.get().setVSync(vsync);
		
		// Keybinds
		int binding;
		for(Keybind keybind : Keybind.values()) {
			try {
				binding = new Integer(getProperty(keybind.name()));
			} catch(NumberFormatException e) {
				continue;
			}
			keybind.setKey(binding);
		}
	}
	
	public static void createConfig() {
		// Audio
		setProperty("music", "" + SoundStore.get().getMusicVolume());
		setProperty("sfx", "" + SoundStore.get().getSoundVolume());
		
		// Video
		setProperty("fullscreen", "" + Camera.get().isFullscreen());
		setProperty("vsync", "" + Camera.get().isVSyncEnabled());
		
		// Keybinds
		for(Keybind keybind : Keybind.values()) {
			setProperty(keybind.name(), "" + keybind.getKeyCode());
		}
		
		try {
			props.store(new BufferedWriter(new FileWriter(new File("properties/config.properties"))), null);
		} catch (IOException e) {
			System.out.println("Config file failed to save");
			e.printStackTrace();
		}
	}
	
}
