package core.ui;

import core.Camera;
import core.render.SpriteList;
import core.render.transform.Transform;

public class Icon extends UIElement {

	private String image;
	
	private Transform transform = new Transform();
	
	public Icon(String icon) {
		this.setIcon(icon);
	}

	@Override
	public void draw() {
		transform.x = getX();
		transform.y = getY();
		transform.still = true;
		
		SpriteList.get(image).draw(transform);
	}
	
	public String getIcon() {
		return image;
	}

	public void setIcon(String icon) {
		this.image = icon;
	}

	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}
	
	public void setX(float x) {
		if(Float.isNaN(x)) {
			x = Camera.get().getDisplayWidth(0.5f) - (SpriteList.get(image).getWidth() / 2f);
		}
		
		setBounds(x, bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public void setY(float y) {
		if(Float.isNaN(y)) {
			y = Camera.get().getDisplayHeight(0.5f) - (SpriteList.get(image).getHeight() / 2f);
		}
		
		setBounds(bounds.getX(), y, bounds.getWidth(), bounds.getHeight());
	}
	
}
