package core.ui.utils;

public enum VerticalAlign {

	TOP, CENTER, BOTTOM;
	
	public double getY(UIBounds bounds) {
		switch(this) {
		case TOP:
			return 0;
		case CENTER:
			return -bounds.getHeight() * 0.5;
		case BOTTOM:
			return -bounds.getHeight();
		default:
			return 0;
		}
	}
	
}
