package core.entities.controllers;

import core.entities.Entity;
import core.entities.components.EntityComponent;
import core.ui.event.KeybindEvent;

public abstract class Controller extends EntityComponent {
	
	public Controller(Entity entity) {
		super(entity);
	}

	public abstract void control();
	public abstract void control(KeybindEvent e);

}
