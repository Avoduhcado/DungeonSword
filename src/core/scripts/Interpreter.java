package core.scripts;

import com.google.gson.JsonObject;

import core.setups.GameSetup;
import core.setups.Stage;
import core.ui.UIElement;

public class Interpreter {

	/** TODO
	 * Custom exceptions probably?
	 */
	
	public static enum InterpretedEvent {
		LOAD_SCENE,
		DISPLAY_UI,
		SET_ENTITY,
		MOVE_ENTITY;
	}

	private static GameSetup setup;

	public static void registerSetup(GameSetup setup) {
		Interpreter.setup = setup;
	}
	
	public static boolean interpret(JsonObject json) {
		if(!json.has("ID")) {
			return false;
		}
		
		switch(InterpretedEvent.valueOf(json.get("ID").getAsString())) {
		case LOAD_SCENE:
			doLoadScene(json);
			return true;
		case DISPLAY_UI:
			doDisplayUI(json);
			return true;
		case SET_ENTITY:
			doSetEntity(json);
			return true;
		case MOVE_ENTITY:
			doMoveEntity(json);
			return true;
		default:
			System.out.println("Malformed event: " + json.get("ID"));
		}
		
		return false;
	}

	public static JsonObject interpretConditional(JsonObject json) {
		
		
		return null;
	}
	
	public static void doLoadScene(JsonObject json) {
		if(!currentlyOnStage()) {
			return;
		}
	}
	
	public static UIElement doDisplayUI(JsonObject json) {
		return null;
	}
	
	public static void doSetEntity(JsonObject json) {
		
	}
	
	public static void doMoveEntity(JsonObject json) {
		
	}
	
	private static boolean currentlyOnStage() {
		return setup instanceof Stage;
	}
	
	private static void processConditional(JsonObject json) {
		
	}
	
}
