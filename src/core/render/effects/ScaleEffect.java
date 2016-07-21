package core.render.effects;

import org.lwjgl.util.vector.Vector4f;

import core.Camera;

public class ScaleEffect extends ScreenEffect {

	public ScaleEffect(Vector4f value, float duration, boolean addValueNotMove, Tween tween) {
		super(Camera.get().getScale(), value, duration, addValueNotMove, tween);
	}

	@Override
	protected void applyEffect(Vector4f value) {
		Camera.get().setScale(value);
	}

	@Override
	protected void processLoop() {
		startingValue = Camera.get().getScale();
		super.processLoop();
	}
}
