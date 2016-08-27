package core.entities.events;

import core.entities.Entity;

public class InteractEvent extends EntityEvent {

	public static enum Type {
		AUTORUN,
		ON_TOUCH,
		ON_ACTIVATE,
		INTERRUPT;
	}
	
	protected Type interactType;
	protected Entity interactor;
	
	public InteractEvent(Type interactType, Entity interactor) {
		this.interactType = interactType;
		this.interactor = interactor;
	}
	
	public Type getInteractType() {
		return interactType;
	}
	
	public void setInteractType(Type interactType) {
		this.interactType = interactType;
	}

	public Entity getInteractor() {
		return interactor;
	}
	
}
