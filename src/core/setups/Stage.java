package core.setups;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector3f;

import core.Camera;
import core.entities.Entity;
import core.entities.controllers.PlayerController;
import core.generation.box2d.BodyBuilder;
import core.generation.box2d.WorldGeneratorBox2D;
import core.render.DrawUtils;
import core.ui.event.KeybindEvent;
import core.ui.event.KeybindListener;
import core.ui.overlays.GameMenu;
import core.utilities.keyboard.Keybind;
import core.utilities.text.Text;

public class Stage extends GameSetup {
			
	private boolean pause;
	
	private World world;
	private ArrayList<Body> walls = new ArrayList<Body>();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private KeybindListener keybindListener = new StageKeybindListener();
	
	public Stage() {
		world = new World(new Vec2(0, 0));
	}
	
	@Override
	public void update() {
		if(pause) {
			return;
		}
		
		world.step(1 / 60f, 8, 3);
		entities.stream().forEach(Entity::update);
	}

	@Override
	public void draw() {
		walls.stream().forEach(e -> {
			DrawUtils.setColor(new Vector3f(0, 1, 0));
			EdgeShape edge = (EdgeShape) e.getFixtureList().getShape();
			DrawUtils.drawBox2DShape(e, edge);
		});
		
		entities.stream().sorted().forEach(Entity::draw);
	}
	
	@Override
	public void drawUI() {
		super.drawUI();
		
		for(Body body = world.getBodyList(); body.getNext() != null; body = body.getNext()) {
			switch(body.getFixtureList().getShape().getType()) {
			case CHAIN:
				break;
			case CIRCLE:
				DrawUtils.setColor(new Vector3f(1, 0, 1));
				CircleShape circle = (CircleShape) body.getFixtureList().getShape();
				DrawUtils.drawBox2DCircle(body, circle, 1);
				break;
			case EDGE:
				DrawUtils.setColor(new Vector3f(1, 0, 0));
				EdgeShape edge = (EdgeShape) body.getFixtureList().getShape();
				DrawUtils.drawBox2DEdge(body, edge, 1);
				break;
			case POLYGON:
				break;
			default:
				break;
			}
		}
		
		if(pause) {
			DrawUtils.fillScreen(0, 0, 0, 0.65f);
			Text.drawString("Paused", Camera.get().getDisplayWidth(0.5f), Camera.get().getDisplayHeight(0.5f));
		}
	}

	public World getWorld() {
		return world;
	}
	
	public void setGrid(boolean[][] gridArray) {
		if(!walls.isEmpty()) {
			walls.stream().forEach(e -> world.destroyBody(e));
			walls.clear();
		}
		
		for(int x = 0; x<gridArray.length; x++) {
			for(int y = 0; y<gridArray[0].length; y++) {
				if(gridArray[x][y]) {
					if(x == 0 || (x > 0 && !gridArray[x - 1][y])) {
						walls.add(BodyBuilder.createEdge(world, new Vec2(x * WorldGeneratorBox2D.SCALE_FACTOR, y * WorldGeneratorBox2D.SCALE_FACTOR),
								new Vec2(0, WorldGeneratorBox2D.SCALE_FACTOR)));
					}
					if(x == gridArray.length - 1 || (x < gridArray.length - 1 && !gridArray[x + 1][y])) {
						walls.add(BodyBuilder.createEdge(world, new Vec2((x + 1) * WorldGeneratorBox2D.SCALE_FACTOR, y * WorldGeneratorBox2D.SCALE_FACTOR),
								new Vec2(0, WorldGeneratorBox2D.SCALE_FACTOR)));
					}
					if(y == 0 || (y > 0 && !gridArray[x][y - 1])) {
						walls.add(BodyBuilder.createEdge(world, new Vec2(x * WorldGeneratorBox2D.SCALE_FACTOR, y * WorldGeneratorBox2D.SCALE_FACTOR),
								new Vec2(WorldGeneratorBox2D.SCALE_FACTOR, 0)));
					}
					if(y == gridArray[0].length - 1 || (y < gridArray[0].length - 1 && !gridArray[x][y + 1])) {
						walls.add(BodyBuilder.createEdge(world, new Vec2(x * WorldGeneratorBox2D.SCALE_FACTOR, (y + 1) * WorldGeneratorBox2D.SCALE_FACTOR),
								new Vec2(WorldGeneratorBox2D.SCALE_FACTOR, 0)));
					}
				}
			}
		}
	}
	
	public void addEntity(Entity entity, boolean focus) {
		entities.add(entity);
		if(focus && entity.hasBody()) {
			Camera.get().setFocus(entity.getBody());
		}
	}
	
	public void addEntity(Entity entity) {
		addEntity(entity, false);
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
			if(e.isConsumed() || (pause && e.getKeybind() != Keybind.PAUSE)) {
				return;
			}
			
			switch(e.getKeybind()) {
			case PAUSE:
				e.consume();
				if(e.getKeybind().clicked()) {
					pause = !pause;
				}
				break;
			case EXIT:
				e.consume();
				if(e.getKeybind().clicked()) {
					GameMenu gameMenu = new GameMenu();
					addUI(gameMenu);
					setFocus(gameMenu);
				}
				break;
			default:
				entities.stream()
				.filter(ent -> ent.controllable() && ent.getController() instanceof PlayerController)
				.forEach(ent -> ent.getController().control(e));
				break;
			}
		}
	}

}
