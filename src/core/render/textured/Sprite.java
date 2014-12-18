package core.render.textured;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;

import core.Camera;
import core.Theater;
import core.render.TextureLoader;

public class Sprite {
	
	protected Texture texture;

	protected float width;
	protected float height;
	protected float textureX;
	protected float textureY;
	protected float textureXWidth;
	protected float textureYHeight;
	
	protected Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
	protected Vector4f rotation = new Vector4f(0f, 0f, 0f, 0f);
	protected Vector3f scale = new Vector3f(1f, 1f, 1f);
	protected float rotateSpeed = 0f;
	
	protected boolean still;
	
	protected int maxDirection = 1;
	protected int maxFrame = 1;
	protected int direction = 0;
	protected int frame = 0;
	private float animStep;
	
	public Sprite(String ref) {
		setTexture(ref);
		
		if(ref.contains("^")) {
			String[] temp = ref.split("\\^");
			maxFrame = Integer.parseInt(temp[1]);
			if(temp.length > 2)
				maxDirection = Integer.parseInt(temp[2]);
		}
		
		width = texture.getWidth() / maxFrame;
		height = texture.getHeight() / maxDirection;
	}
	
	public void draw(float x, float y) {
		if(Float.isNaN(x))
			x = Camera.get().getDisplayWidth(0.5f) - (getWidth() * 0.5f);
		if(Float.isNaN(y))
			y = Camera.get().getDisplayHeight(0.5f) - (getHeight() * 0.5f);
		
		texture.bind();
		
		updateTextureOffsets();
		
		GL11.glPushMatrix();

		if(still) {
			// Static positioning
			GL11.glTranslatef((int) x, (int) y, 0f);
		} else {
			// Positioning relative to camera movement
			GL11.glTranslatef((int) (x - Camera.get().frame.getX()), (int) (y - Camera.get().frame.getY()), 0f);
		}
		GL11.glColor4f(color.x, color.y, color.z, color.w);
		if(rotation.w != 0f) {
			// Base rotation from center of object
			GL11.glTranslatef(getWidth() / 2f, getHeight() / 2f, 0f);
			GL11.glRotatef(rotation.w, rotation.x, rotation.y, rotation.z);
			GL11.glTranslatef(-getWidth() / 2f, -getHeight() / 2f, 0f);
		}
		GL11.glScalef(scale.x, scale.y, scale.z);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(textureX, textureY);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(textureXWidth, textureY);
			GL11.glVertex2f(getWidth(), 0);
			GL11.glTexCoord2f(textureXWidth, textureYHeight);
			GL11.glVertex2f(getWidth(), getHeight());
			GL11.glTexCoord2f(textureX, textureYHeight);
			GL11.glVertex2f(0, getHeight());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public void animate() {
		if(maxFrame > 1) {
			animStep += Theater.getDeltaSpeed(0.025f);
			if (animStep >= 0.16f) {
				animStep = 0f;
				frame++;
				if (frame >= maxFrame) {
					frame = 0;
				}
			}
		}
	}
	
	public void updateTextureOffsets() {
		textureX = width * frame;
		textureY = height * direction;
		textureXWidth = (width * frame) + width;
		textureYHeight = (height * direction) + height;
	}
	
	public void setTexture(String ref) {
		this.texture = TextureLoader.get().getSlickTexture(System.getProperty("resources") + "/textures/" + ref + ".png");
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public Vector4f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector4f rotation, float speed) {
		this.rotation = rotation;
		setRotateSpeed(speed);
	}
	
	public float getRotateSpeed() {
		return rotateSpeed;
	}
	
	public void setRotateSpeed(float speed) {
		this.rotateSpeed = speed;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public boolean isStill() {
		return still;
	}
	
	public void setStill(boolean still) {
		this.still = still;
	}
	
	public boolean isAnimated() {
		return maxFrame > 1;
	}
	
	public int getFrame() {
		return frame;
	}
	
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getMaxFrame() {
		return maxFrame;
	}

	/**
	 * Actual image width!!
	 * 
	 * @return Width of single frame of image
	 */
	public float getWidth() {
		return texture.getImageWidth() / maxFrame;
	}
	
	/**
	 * Actual image height!!
	 * 
	 * @return Height of single frame of image
	 */
	public float getHeight() {
		return texture.getImageHeight() / maxDirection;
	}
}