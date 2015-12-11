package core.entities.physics;

import org.lwjgl.util.vector.Vector2f;

public interface Body {
	
	public void update();
	
	public Vector2f getPosition();

}
