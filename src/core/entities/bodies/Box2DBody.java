package core.entities.bodies;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.entities.events.BodyEvent;
import core.entities.events.MoveEvent;
import core.generation.box2d.BodyBuilder;
import core.generation.box2d.WorldGeneratorBox2D;

public class Box2DBody extends AvoBody {

	private Body body;
	
	public Box2DBody(Entity entity, World world, Vec2 position, float radius) {
		super(entity);
		body = BodyBuilder.createCircle(world, position, radius);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector3f getPosition() {
		return new Vector3f((body.getPosition().x - body.getFixtureList().getShape().m_radius) * WorldGeneratorBox2D.SCALE_FACTOR,
				(body.getPosition().y - body.getFixtureList().getShape().m_radius) * WorldGeneratorBox2D.SCALE_FACTOR, 0);
	}

	@Override
	public Vector3f getCenter() {
		return new Vector3f((body.getPosition().x + body.getFixtureList().getShape().m_radius) * WorldGeneratorBox2D.SCALE_FACTOR,
				(body.getPosition().y + body.getFixtureList().getShape().m_radius) * WorldGeneratorBox2D.SCALE_FACTOR, 0);
	}
	
	public Body getBody() {
		return body;
	}

	@Override
	public void processEvent(BodyEvent event) {
		if(event instanceof MoveEvent) {
			processMoveEvent((MoveEvent) event);
		}
	}
	
	protected void processMoveEvent(MoveEvent event) {
		body.applyForceToCenter(new Vec2(event.getMovement().x, event.getMovement().y));
	}
}
