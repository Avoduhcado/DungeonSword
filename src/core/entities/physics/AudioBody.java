package core.entities.physics;

import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.OggData;
import org.newdawn.slick.openal.OggDecoder;
import org.newdawn.slick.util.ResourceLoader;

public class AudioBody implements Body {

	private Vector3f position;
	private Audio audio;
	
	public AudioBody(Vector3f position, String ref) {
		setPosition(position);
		audio = loadAudio(ref);
		
		// Audio file MUST be mono in order for attenuation to occur
		//AL10.alSourcei(audio.getBufferID(), AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
		
		AL10.alSourcei(audio.getBufferID(), AL10.AL_REFERENCE_DISTANCE, 5);
		AL10.alSourcei(audio.getBufferID(), AL10.AL_MAX_DISTANCE, 10);
	}

	private Audio loadAudio(String ref) {
		try {
			return AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(
							System.getProperty("resources") + "/soundeffects/" + ref + ".ogg"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void update() {
		if(!audio.isPlaying() && position.length() < 10) {
			audio.playAsSoundEffect(1f, 1f, false, position.x, position.y, position.z);
		}
		
		position.translate(0.01f, 0.01f, 0.01f);
		System.out.println(position.length());
		
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	
}
