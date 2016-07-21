package core.render.effects;

import core.utilities.MathUtils;

public enum Tween {
	IN, OUT, IN_OUT, LINEAR;
	
	public float getTweenedValue(float time, float start, float change, float duration) {
		switch(this) {
		case IN:
			return MathUtils.easeIn(time, start, change, duration);
		case OUT:
			return MathUtils.easeOut(time, start, change, duration);
		case IN_OUT:
			return MathUtils.easeInOut(time, start, change, duration);
		case LINEAR:
			return MathUtils.linearTween(time, start, change, duration);
		default:
			return -1;
		}
	}
}
