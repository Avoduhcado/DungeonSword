package core.entities.components.interactions;

import core.entities.Entity;
import core.scripts.Script;

public class TouchInteraction extends Interaction {

	public TouchInteraction(Entity entity, Script script) {
		super(entity, script);
		
		//entity.getBody().createFixture(createActivationRange());
	}

	/*private FixtureDef createActivationRange() {
		CircleShape bodyShape = new CircleShape();
		bodyShape.setRadius(20f / Stage.SCALE_FACTOR);

		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 0f;
		boxFixture.shape = bodyShape;
		boxFixture.isSensor = true;
		boxFixture.filter.categoryBits = 0b0001;
		boxFixture.filter.maskBits = 0b0110;
		boxFixture.userData = new SensorData(entity, "Toucher", SensorData.BODY);

		return boxFixture;
	}*/

}
