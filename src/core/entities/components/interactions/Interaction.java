package core.entities.components.interactions;

import core.entities.Entity;
import core.entities.components.EntityComponent;
import core.entities.events.InteractEvent;
import core.scripts.Script;

public abstract class Interaction extends EntityComponent {

	/** The actual container of the events for the interaction */
	protected Script script;
	
	public Interaction(Entity entity, Script script) {
		super(entity);
		this.script = script;
	}
	
	public void interact(InteractEvent e) {
		if(!script.isBusyReading() && e.getInteractType() != InteractEvent.Type.INTERRUPT) {
			script.startReading(e.getInteractor());
		} else if(script.isBusyReading() && e.getInteractType() == InteractEvent.Type.INTERRUPT) {
			script.interrupt();
		} else if(script.isBusyReading()) {
			script.read();
		} 
	}
	
}
