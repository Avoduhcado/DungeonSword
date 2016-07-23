package core.entities;

import java.io.Serializable;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import core.entities.bodies.Body;
import core.entities.components.EntityComponent;
import core.entities.controllers.Controller;
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
		if(controller != null) {
			controller.control();
		}
		
		if(body != null) {
			body.update();
		}
	}
	
	/**
	 * Draw the entity's render on the screen defined by the body's position.
	 */
	public void draw() {
		if(render != null && body != null) {
			render.draw(getBodyPosition());
		}
	}
	
	public Body getBody() {
		return body;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	private Vector3f getBodyPosition() {
		if(body != null) {
			return body.getPosition();
		}
		return new Vector3f();
	}
	
	public Render getRender() {
		return render;
	}
	
	public void setRender(Render render) {
		this.render = render;
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
