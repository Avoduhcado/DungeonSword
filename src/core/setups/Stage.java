package core.setups;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.entities.bodies.PlainBody;
import core.entities.controllers.PlayerController;
import core.entities.renders.PlainRender;
import core.render.DrawUtils;

public class Stage extends GameSetup {
			
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Stage() {
		Entity ent = new Entity();
		ent.setRender(new PlainRender("AGDG Logo"));
		ent.setBody(new PlainBody(new Vector3f(-16f, -16f, 0f)));
		ent.setController(new PlayerController(ent));
		//ent.addComponent(AutorunInteraction.class, new AutorunInteraction(ent, new Script(ent, "Butts")));
		entities.add(ent);
	}
	
	@Override
	public void update() {
		entities.stream().forEach(Entity::update);
	}

	@Override
	public void draw() {
		DrawUtils.fillScreen(1, 1, 0, 1);
		
		entities.stream().forEach(Entity::draw);
	}

}
