package core.entities.renders;

import org.lwjgl.util.vector.Vector3f;

import core.Camera;
import core.Theater;
import core.entities.Entity;
import core.render.SpriteList;
import core.render.transform.Transform;
import core.utilities.text.Text;

public class PlainRender extends Render {

	private String sprite;
	private Transform transform;
	
	public PlainRender(Entity entity, String sprite) {
		super(entity);
		this.sprite = sprite;
		this.transform = new Transform();
	}
	
	@Override
	public void draw(Vector3f position) {
		transform.setX(position.x);
		transform.setY(position.y);
		SpriteList.get(sprite).draw(transform);
		
		if(Theater.debug) {
			String mods = "cwhite";
			if(this.entity.hasBody() && this.entity.getBody() == Camera.get().getFocus()) {
				mods = "cgreen";
			}
			Text.drawString(sprite, transform.x, transform.y, mods);
		}
	}

}
