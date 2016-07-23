package core.entities.bodies;

import org.lwjgl.util.vector.Vector3f;

import core.entities.events.BodyEvent;

public interface Body {
	
	public void update();
	
	public Vector3f getPosition();

	public void processEvent(BodyEvent event);

}
