package core.entities.components.interactions;

import core.entities.Entity;
import core.entities.events.InteractEvent;
import core.scripts.Script;

public class AutorunInteraction extends Interaction {

	public AutorunInteraction(Entity entity, Script script) {
		super(entity, script);
		
		this.interact(new InteractEvent(InteractEvent.Type.AUTORUN, entity));
	}

}
