package core.ui.utils;

import core.ui.UIElement;

public class MouseEvent extends UIEvent {

	public static final int CLICKED = 1;
	public static final int MOVED = 2;

	private int event;
	private float x, y;

	public MouseEvent(UIElement parent, int event, float x, float y) {
		super(parent);
		this.event = event;
		this.x = x;
		this.y = y;
	}
	
	public int getEvent() {
		return event;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
