package core.entities.bodies;

import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.entities.components.EntityComponent;
import core.entities.listeners.BodyEventListener;

public abstract class AvoBody extends EntityComponent implements BodyEventListener {
	
	public AvoBody(Entity entity) {
		super(entity);
	}

	public abstract Vector3f getPosition();
	public abstract Vector3f getCenter();

}
