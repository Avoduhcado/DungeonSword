package core.entities.renders;

import org.lwjgl.util.vector.Vector3f;

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
	public void draw(Vector3f position) {
		transform.setX(position.x);
		transform.setY(position.y);
		SpriteList.get(sprite).draw(transform);
	}

}
