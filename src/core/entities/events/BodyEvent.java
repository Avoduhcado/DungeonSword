package core.entities.events;

import org.lwjgl.util.vector.Vector3f;

public class BodyEvent extends EntityEvent {

public static final int MOVE = 1;
	
	private final int type;
	private Vector3f vector;

	public BodyEvent(int type, Vector3f vector) {
		this.type = type;
		this.vector = vector;
	}

	public int getType() {
		return type;
	}

	public Vector3f getVector() {
		return vector;
	}

	public void setVector(Vector3f vector) {
		this.vector = vector;
	}
	
}
