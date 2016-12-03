package core.ui;

import core.render.SpriteList;
import core.render.transform.Transform;
import core.utilities.ValueSupplier;

public class Icon extends UIElement {

	private String image;
	
	private Transform transform = new Transform();
	
	public Icon(String icon) {
		setIcon(icon);
	}
	
	@Override
	public void draw() {
		transform.x = getBounds().getX();
		transform.y = getBounds().getY();
		// TODO Likely shouldn't always be set to still
		transform.still = true;
		
		SpriteList.get(image).draw(transform);
	}
	
	public String getIcon() {
		return image;
	}

	public void setIcon(String icon) {
		this.image = icon;
	}
	
	public void setPosition(ValueSupplier<Double> x, ValueSupplier<Double> y) {
		setBounds(x, y, () -> (double) SpriteList.get(image).getWidth(), () -> (double) SpriteList.get(image).getHeight());
	}
}
