package wave.audio;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;

public class SoundEffect {
	public Audio sf;
	public static float volume = 1.0f;
	private static boolean mute = false;
	private boolean loop = false;

	public static ArrayList<SoundEffect> sfxs = new ArrayList<SoundEffect>();

	public SoundEffect(String path) {

		try {
			sf = AudioLoader.getAudio("OGG", getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		sfxs.add(this);
	}

	public void play() {
		if (mute) {
			sf.playAsSoundEffect(1.0f, 0, false);
		} else {
			try {
				sf.playAsSoundEffect(1.0f, volume, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void loop(boolean loop) {
		this.loop = loop;
	}
	
	public void update() {
		checaLoop();
	}

	private void checaLoop() {
		if (loop && !sf.isPlaying()) {
			play();
		}
	}

	public boolean isPlaying() {
		return sf.isPlaying();
	}

	public float getPosition() {
		return sf.getPosition();
	}

	public static boolean isMute() {
		return mute;
	}

	public static void setMute(boolean mute) {
		SoundEffect.mute = mute;

		for (int x = 0; x < sfxs.size(); x++) {
			if (sfxs.get(x).isPlaying()) {
				sfxs.get(x).playAt(sfxs.get(x).getPosition());
			}
		}
	}

	protected void playAt(float pos) {
		sf.stop();

		if (mute) {
			sf.playAsSoundEffect(1.0f, 0, false);
		} else {
			sf.playAsSoundEffect(1.0f, volume, false);
		}

		sf.setPosition(pos);

	}

	public static void setVolume(float vol) {
		float pos;
		volume = vol;

		for (int x = 0; x < sfxs.size(); x++) {
			if (sfxs.get(x).isPlaying()) {
				pos = sfxs.get(x).getPosition();
				sfxs.get(x).playAt(pos);
			}
		}

	}

}
