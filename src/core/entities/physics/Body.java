package core.entities.physics;

import org.lwjgl.util.vector.Vector3f;

public class Body implements Geometric {

	private Vector3f position;
	
	public Body(Vector3f vec) {
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
