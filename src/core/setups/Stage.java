package core.setups;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import core.Camera;
import core.entities.Entity;
import core.entities.bodies.PlainBody;
import core.entities.controllers.PlayerController;
import core.entities.renders.PlainRender;

public class Stage extends GameSetup {
			
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Stage() {
		Entity ent = new Entity();
		ent.setRender(new PlainRender(ent, "AGDG Logo"));
		ent.setBody(new PlainBody(ent, new Vector3f(-16f, -16f, 0f), 32, 32));
		//ent.addComponent(AutorunInteraction.class, new AutorunInteraction(ent, new Script(ent, "Butts")));
		entities.add(ent);
		
		ent = new Entity();
		ent.setRender(new PlainRender(ent, "AGDG Logo"));
		ent.setBody(new PlainBody(ent, new Vector3f(0f, 0f, 0f), 32, 32));
		ent.setController(new PlayerController(ent));
		//ent.addComponent(AutorunInteraction.class, new AutorunInteraction(ent, new Script(ent, "Butts")));
		entities.add(ent);
		
		Camera.get().setFocus(ent.getBody());
		
		Camera.get().setClear(new Vector4f(1f, 1f, 0f, 1f));
	}
	
	@Override
	public void update() {
		entities.stream().forEach(Entity::update);
	}

	@Override
	public void draw() {
		entities.stream().sorted().forEach(Entity::draw);
	}

}
