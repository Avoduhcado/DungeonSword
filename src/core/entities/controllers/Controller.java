package core.entities.controllers;

import core.entities.Entity;
import core.entities.components.EntityComponent;

public abstract class Controller extends EntityComponent {
	
	public Controller(Entity entity) {
		super(entity);
	}

	public abstract void control();

}
