package core.entities.bodies;

import org.lwjgl.util.vector.Vector3f;

import core.entities.events.BodyEvent;
import core.entities.events.MoveEvent;

public class PlainBody implements Body {

	private Vector3f position;
	
	public PlainBody(Vector3f vec) {
		this.position = vec;
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
	public void processEvent(BodyEvent event) {
		if(event instanceof MoveEvent) {
			processMoveEvent((MoveEvent) event);
		}
	}
	
	protected void processMoveEvent(MoveEvent event) {
		Vector3f.add(position, event.getMovement(), position);
	}

}
