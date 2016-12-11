package core.utilities.text;

import java.awt.Color;
import java.lang.reflect.Field;

public class TextModifier {

	public enum TextModValue {
		COLOR("COLOR"),
		SHADOW_COLOR("SHADOW_COLOR"),
		SHADOW("SHADOW"),
		SIZE("SIZE"),
		FONTFACE("FONTFACE"),
		ADD_IN("ADD_IN"),
		VARIABLE("VARIABLE");
		
		public final String valueName;
		
		TextModValue(String name) {
			this.valueName = name;
		}
	}

	/** TODO Add delimiter for accessing variables $variable somehow? */

	private Color color = GameFont.defaultColor;
	private Color shadowColor = GameFont.defaultShadow;
	private boolean shadow = true;
	private float size = GameFont.defaultSize;
	private String fontFace = "DEBUG";
	private String replacement = "";
	private Object variable = null;

	public void addModifier(TextModValue type, String value) {
		switch(type) {
		case COLOR:
			color = getValueAsColor(value, GameFont.defaultColor);
			break;
		case SHADOW_COLOR:
			shadowColor = getValueAsColor(value, GameFont.defaultShadow);
			break;
		case SHADOW:
			shadow = Boolean.valueOf(value);
			break;
		case SIZE:
			size = getValueAsFloat(value, GameFont.defaultSize);
			break;
		case FONTFACE:
			fontFace = value;
			break;
		case ADD_IN:
			replacement = value;
			break;
		case VARIABLE:
			break;
		default:
			break;
		}
	}
	
	public void addModifier(String type, String value) {
		addModifier(TextModValue.valueOf(type), value);
	}
	
	/**
	 * Converts a String value into a color, if the conversion fails it'll return the defaultColor.
	 * @param value Either a hex value beginning with a # or a plaintext Color value.
	 * @param defaultColor Default Color value to return if the conversion fails.
	 * @return The converted value or the default Color.
	 */
	private Color getValueAsColor(String value, Color defaultColor) {
		try {
			if(value.startsWith("#")) {
				return Color.decode(value.substring(1));
			} else {
				Field f = Color.class.getField(value);
				return (Color) f.get(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultColor;
	}
	
	private float getValueAsFloat(String value, float defaultFloat) {
		try {
			return Float.valueOf(value);
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return defaultFloat;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
	}

	public boolean hasShadow() {
		return shadow;
	}

	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public String getFontFace() {
		return fontFace;
	}

	public void setFontFace(String fontFace) {
		this.fontFace = fontFace;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public Object getVariable() {
		return variable;
	}

	public void setVariable(Object variable) {
		this.variable = variable;
	}

	/**
	 * Take in an array of Strings of key value pairs separated by '='.
	 * Presently no value should contain a '=' otherwise this method will not preserve the entire value.
	 * @param modifiers
	 * @return A new TextModifier object containing these modifiers.
	 */
	public static TextModifier compile(String...modifiers) {
		TextModifier modifier = new TextModifier();
		String[] typeValue;
		for(String m : modifiers) {
			if((typeValue = m.split("=")).length != 2) {
				continue;
			}
			modifier.addModifier(typeValue[0], typeValue[1]);
		}
		
		return modifier;
	}
}
