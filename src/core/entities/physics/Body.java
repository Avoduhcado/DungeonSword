package core.entities.physics;

import org.lwjgl.util.vector.Vector3f;

public interface Body {
	
	public void update();
	
	public Vector3f getPosition();

}
