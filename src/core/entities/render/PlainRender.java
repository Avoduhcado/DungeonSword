package core.entities.render;

import core.render.SpriteList;
import core.render.transform.Transform;

public class PlainRender implements Render {

	private String sprite;
	private Transform transform;
	
	public PlainRender(String sprite) {
		this.sprite = sprite;
		this.transform = new Transform();
	}
	
	@Override
	public void draw(float x, float y) {
		transform.setX(x);
		transform.setY(y);
		SpriteList.get(sprite).draw(transform);
	}

}
