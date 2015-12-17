package core.setups;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.entities.physics.Geometric;
import core.entities.physics.AudioBody;

public class Stage extends GameSetup {
			
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Stage() {
		Entity audioSource = new Entity();
		Geometric audioBody = new AudioBody(new Vector3f(0, 0, 0), "JLMG");
		audioSource.setBody(audioBody);
		entities.add(audioSource);
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
