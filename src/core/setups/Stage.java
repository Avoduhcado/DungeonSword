package core.setups;

import java.util.ArrayList;

import core.entities.Entity;

public class Stage extends GameSetup {
			
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Stage() {
		
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
