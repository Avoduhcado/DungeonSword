package core.setups;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.entities.physics.Body;
import core.entities.render.PlainRender;

public class Stage extends GameSetup {
			
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Stage() {
		Entity ent = new Entity();
		ent.setRender(new PlainRender("AGDG Logo"));
		ent.setBody(new Body(new Vector3f(-16f, -16f, 0f)));
		entities.add(ent);
	}
	
	@Override
	public void update() {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	@Override
	public void draw() {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).draw();
		}
	}

}
