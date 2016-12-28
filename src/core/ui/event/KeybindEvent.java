package core.ui.event;

import core.utilities.keyboard.Keybind;

public class KeybindEvent extends UIEvent {

	private final Keybind keybind;
	
	public KeybindEvent(Keybind keybind) {
		this.keybind = keybind;
	}

	public Keybind getKeybind() {
		return keybind;
	}
	
}
