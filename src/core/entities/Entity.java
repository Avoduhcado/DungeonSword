package core.entities;

import java.io.Serializable;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import core.entities.bodies.Body;
import core.entities.components.EntityComponent;
import core.entities.controllers.Controller;
import core.entities.events.BodyEvent;
import core.entities.events.EntityEvent;
import core.entities.renders.Render;

public class Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Body body;
	private Render render;
	private Controller controller;
	
	private HashMap<Class<? extends EntityComponent>, EntityComponent> components = new HashMap<Class<? extends EntityComponent>, EntityComponent>();

	/**
	 * Poll entity's controller for updates. <br>
	 * Process any changes on entity's body.
	 */
	public void update() {
		if(controllable()) {
			controller.control();
		}
		
		if(hasBody()) {
			body.update();
		}
	}
	
	/**
	 * Draw the entity's render on the screen defined by the body's position.
	 */
	public void draw() {
		if(renderable()) {
			render.draw(getBodyPosition());
		}
	}
	
	public void fireEvent(EntityEvent event) {
		if(event instanceof BodyEvent) {
			if(hasBody()) {
				body.processEvent((BodyEvent) event);
			}
		}
	}
	
	public boolean hasBody() {
		return body != null;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	private Vector3f getBodyPosition() {
		if(hasBody()) {
			return body.getPosition();
		}
		return new Vector3f();
	}
	
	public boolean renderable() {
		return render != null;
	}
	
	public Render getRender() {
		return render;
	}
	
	public void setRender(Render render) {
		this.render = render;
	}
	
	public boolean controllable() {
		return controller != null;
	}
	
	public Controller getController() {
		return controller;
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}

	public HashMap<Class<? extends EntityComponent>, EntityComponent> getComponents() {
		return components;
	}

	public void setComponents(HashMap<Class<? extends EntityComponent>, EntityComponent> components) {
		this.components = components;
	}
	
	public EntityComponent getComponent(Class<? extends EntityComponent> clazz) {
		if(components.containsKey(clazz)) {
			return components.get(clazz);
		}
		return null;
	}
	
	public void addComponent(Class<? extends EntityComponent> clazz, EntityComponent component) {
		components.put(clazz, component);
	}
	
	public EntityComponent removeComponent(Class<? extends EntityComponent> clazz) {
		return components.remove(clazz);
	}

}
