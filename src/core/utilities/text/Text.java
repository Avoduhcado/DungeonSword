package core.utilities.text;

import java.util.HashMap;

import core.utilities.text.TextModifier.TextModValue;

public class Text {

	private static HashMap<String, GameFont> fonts = new HashMap<String, GameFont>();
	
	private static final TextModifier plainText = new TextModifier();
	public static final TextModifier DEBUG_TEXT = TextModifier.compile(
			TextModValue.SIZE + "=0.5", TextModValue.COLOR + "=white", TextModValue.SHADOW + "=false");
	
	/**
	 * Loads a font to be used. If no other fonts exist, will load given font under "DEFAULT" tag.
	 * @param key to identify given font. eg. "DEFAULT", "SYSTEM", "DIALOGUE"
	 * @param ref the name of the font to load
	 */
	public static void loadFont(String key, String ref) {
		if(fonts.get(ref) == null) {
			if(fonts.isEmpty())
				fonts.put("DEFAULT", new GameFont(ref));
			
			if(!key.matches("DEFAULT")) {
				fonts.put(key, new GameFont(ref));
			}
		}
	}
	
	/**
	 * Get a specific GameFont defined by font
	 * @param font key name to search for
	 * @return GameFont "font"
	 */
	public static GameFont getFont(String font) {
		if(fonts.containsKey(font))
			return fonts.get(font);
		
		return fonts.get("DEFAULT");
	}
	
	/**
	 * Get the Default game font
	 * @return GameFont "DEFAULT"
	 */
	public static GameFont getDefault() {
		return fonts.get("DEFAULT");
	}
	
	public static void drawString(String text, double x, double y) {
		getDefault().drawString(text, x, y, plainText);
	}
	
	public static void drawString(String text, double x, double y, TextModifier modifier) {
		getDefault().drawString(text, x, y, modifier);
	}
	
}
