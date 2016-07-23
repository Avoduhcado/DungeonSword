package core.entities.events;

import org.lwjgl.util.vector.Vector3f;

public class MoveEvent extends BodyEvent {

	private Vector3f movement;

	public MoveEvent(Vector3f movement) {
		this.setMovement(movement);
	}

	public Vector3f getMovement() {
		return movement;
	}

	public void setMovement(Vector3f movement) {
		this.movement = movement;
	}
}
