package core.entities.bodies;

import org.lwjgl.util.vector.Vector3f;

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

}
