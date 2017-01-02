package core.setups;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import core.Camera;
import core.entities.Entity;
import core.entities.bodies.PlainBody;
import core.entities.controllers.PlayerController;
import core.generation.WorldGenerator;
import core.generation.box2d.WorldGeneratorBox2D;
import core.ui.Button;
import core.ui.CheckBox;
import core.ui.event.KeybindEvent;
import core.ui.event.KeybindListener;

public class TestWorldGenerator extends GameSetup {

	private WorldGenerator worldGen;
	private Entity player;
		
	private KeybindListener keybindListener = new StageKeybindListener();
	
	public TestWorldGenerator() {
		player = new Entity();
		player.setBody(new PlainBody(player, new Vector3f(), 32, 32));
		player.setController(new PlayerController(player, 5));
		Camera.get().setFocus(player.getBody());
		
		this.worldGen = new WorldGeneratorBox2D(null);
		Camera.get().setClear(new Vector4f(0f, 0f, 0f, 1f));
		
		Button regenWorld = new Button("Regenerate World");
		regenWorld.setPosition(15, 15);
		regenWorld.addActionListener(e -> this.worldGen = new WorldGeneratorBox2D(null));
		addUI(regenWorld);
		
		CheckBox hideInactive = new CheckBox("Hide Inactive Rooms");
		hideInactive.setPosition(15, regenWorld.getBounds().getMaxY());
		hideInactive.addActionListener(e -> WorldGenerator.hideInactiveRooms = !WorldGenerator.hideInactiveRooms);
		addUI(hideInactive);

		CheckBox hideConnections = new CheckBox("Hide Connections");
		hideConnections.setPosition(15, hideInactive.getBounds().getMaxY());
		hideConnections.addActionListener(e -> WorldGenerator.hideConnections = !WorldGenerator.hideConnections);
		addUI(hideConnections);

		CheckBox showNumbers = new CheckBox("Show IDs");
		showNumbers.setPosition(15, hideConnections.getBounds().getMaxY());
		showNumbers.addActionListener(e -> WorldGenerator.showRoomNumber = !WorldGenerator.showRoomNumber);
		addUI(showNumbers);
	}
	
	@Override
	public void update() {
		worldGen.generate();
	}

	@Override
	public void draw() {
		worldGen.draw();
	}
	
	@Override
	protected void processKeybindEvent(KeybindEvent e) {
		super.processKeybindEvent(e);
		
		if(keybindListener != null) {
			keybindListener.keybindClicked(e);
		}
	}
	
	private class StageKeybindListener implements KeybindListener {
		@Override
		public void keybindClicked(KeybindEvent e) {
			if(e.isConsumed()) {
				return;
			}
			
			switch(e.getKeybind()) {
			default:
				player.getController().control(e);
				break;
			}
		}
	}

}
