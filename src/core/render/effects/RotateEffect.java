package core.render.effects;

import org.lwjgl.util.vector.Vector4f;

import core.Camera;

public class RotateEffect extends ScreenEffect {

	public RotateEffect(Vector4f end, float duration, boolean moveTo, Tween tween) {
		super(Camera.get().getRotation(), end, duration, moveTo, tween);
	}

	@Override
	protected void applyEffect(Vector4f value) {
		Camera.get().setRotation(value);
	}
	
	@Override
	protected void processLoop() {
		startingValue = Camera.get().getRotation();
		super.processLoop();
	}

}
