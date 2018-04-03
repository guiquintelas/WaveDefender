package wave.audio;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;

public class Musica {
	public Audio musica;
	public static float volume = 1.0f;
	protected static boolean mute = false;

	public Musica(String path) {

			try {
				musica = AudioLoader.getAudio("OGG", getClass().getResourceAsStream(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

	public void play() {
		musica.stop();
		if (mute) {
			musica.playAsSoundEffect(1.0f, 0, false);
		} else {
			musica.playAsSoundEffect(1.0f, volume, false);		
		}
	}
	
	public synchronized void playAt(float pos) {
		musica.stop();
		if (mute) {
			musica.playAsSoundEffect(1.0f, 0, false);
		} else {
			musica.playAsSoundEffect(1.0f, volume, false);
			
		}
		
		musica.setPosition(pos);

	}
	
	public boolean isPlaying() {
		return musica.isPlaying();
	}

	public float getPosition() {
		return musica.getPosition();
	}
	
	
	
}
