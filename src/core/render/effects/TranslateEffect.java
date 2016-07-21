package core.render.effects;

import org.lwjgl.util.vector.Vector4f;

import core.Camera;

public class TranslateEffect extends ScreenEffect {

	public TranslateEffect(Vector4f end, float duration, boolean moveTo, Tween tween) {
		super(Camera.get().getTranslation(), end, duration, moveTo, tween);
	}

	@Override
	protected void applyEffect(Vector4f value) {
		Camera.get().setTranslation(value);
	}

	@Override
	protected void processLoop() {
		startingValue = Camera.get().getTranslation();
		super.processLoop();
	}
}
