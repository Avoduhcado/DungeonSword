package core.entities.bodies;

import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.entities.events.BodyEvent;
import core.entities.events.MoveEvent;

public class PlainBody extends Body {

	private Vector3f position;
	private float width, height;
	
	public PlainBody(Entity entity, Vector3f vec, float width, float height) {
		super(entity);
		this.position = vec;
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getCenter() {
		return Vector3f.add(position, new Vector3f(width * 0.5f, height * 0.5f, 0), null);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public void processEvent(BodyEvent event) {
		if(event instanceof MoveEvent) {
			processMoveEvent((MoveEvent) event);
		}
	}
	
	protected void processMoveEvent(MoveEvent event) {
		Vector3f.add(position, event.getMovement(), position);
	}

}
