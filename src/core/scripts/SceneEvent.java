package core.scripts;

import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;
import core.setups.GameSetup;
import core.ui.UIElement;

public abstract class SceneEvent {

	protected final GameSetup scene;
	
	public SceneEvent(GameSetup scene) {
		this.scene = scene;
	}
	
	public abstract void perform();
	
	public static class LoadNewSceneEvent extends SceneEvent {
		private String sceneName;
		
		public LoadNewSceneEvent(GameSetup scene, String sceneName) {
			super(scene);
			this.sceneName = sceneName;
		}
		
		@Override
		public void perform() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class DisplayUIEvent extends SceneEvent {
		private UIElement uiElement;

		public DisplayUIEvent(GameSetup scene, UIElement uiElement) {
			super(scene);
			this.uiElement = uiElement;
		}

		@Override
		public void perform() {
			scene.addUI(uiElement);
		}

	}
	
	public static class SetEntityEvent extends SceneEvent {
		private Entity entity;
		
		public SetEntityEvent(GameSetup scene, Entity entity) {
			super(scene);
			this.entity = entity;
		}
		
		public void perform() {
			// TODO Auto-generated method stub
		}
	}

	public static class MoveEntityEvent extends SceneEvent {
		private Entity entity;
		private Vector3f destination;
		
		public MoveEntityEvent(GameSetup scene, Entity entity, Vector3f destination) {
			super(scene);
			this.entity = entity;
			this.destination = destination;
		}
		
		public void perform() {
			// TODO Auto-generated method stub
		}
	}
	
}
