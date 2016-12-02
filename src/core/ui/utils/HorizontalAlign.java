package core.ui.utils;

public enum HorizontalAlign {
	
	LEFT, CENTER, RIGHT;
	
	public double getX(UIBounds bounds) {
		switch(this) {
		case LEFT:
			return 0;
		case CENTER:
			return -bounds.getWidth() * 0.5;
		case RIGHT:
			return -bounds.getWidth();
		default:
			return 0;
		}
	}
}
