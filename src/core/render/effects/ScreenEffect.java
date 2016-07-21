package core.render.effects;

import org.lwjgl.util.vector.Vector4f;

import core.Theater;
import core.utilities.MathUtils;

public abstract class ScreenEffect {

	protected Vector4f startingValue;
	protected Vector4f endingValue;
	protected float duration;
	protected float currentTime;
	protected Tween tween;
	
	protected boolean loop;
	protected boolean reverse;
	protected boolean complete;
	
	public ScreenEffect(Vector4f start, Vector4f end, float duration, boolean moveTo, Tween tween) {
		if(moveTo) {
			Vector4f.sub(end, start, end);
		}
		this.startingValue = start;
		this.endingValue = end;
		// Using a duration of zero causes the effects to return NaN
		if(duration == 0) {
			duration = 0.001f;
		}
		this.duration = duration;
		this.currentTime = 0f;
		this.tween = tween;
	}
	
	public void apply() {
		incrementTime();
		
		applyEffect(calculateTween());
	}

	protected void incrementTime() {
		currentTime = MathUtils.clamp(currentTime + Theater.getDeltaSpeed(0.025f), 0, duration);

		if(currentTime >= duration) {
			if(loop) {
				processLoop();
				return;
			}
			complete();
		}
	}
	
	protected Vector4f calculateTween() {
		Vector4f value = new Vector4f();
		value.x = tween.getTweenedValue(currentTime, startingValue.x, endingValue.x, duration);
		value.y = tween.getTweenedValue(currentTime, startingValue.y, endingValue.y, duration);
		value.z = tween.getTweenedValue(currentTime, startingValue.z, endingValue.z, duration);
		value.w = tween.getTweenedValue(currentTime, startingValue.w, endingValue.w, duration);
		
		return value;
	}
	
	protected abstract void applyEffect(Vector4f value);

	protected void processLoop() {
		currentTime = 0f;
		if(reverse) {
			endingValue.negate(endingValue);
		}
	}
			
	public void complete() {
		this.complete = true;
	}
	
	public Vector4f getEndValue() {
		return endingValue;
	}

	public void setEndValue(Vector4f value) {
		this.endingValue = value;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}

	public Tween getTween() {
		return tween;
	}

	public void setTween(Tween tween) {
		this.tween = tween;
	}

	public boolean isLoop() {
		return loop;
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public boolean isReverse() {
		return reverse;
	}
	
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
