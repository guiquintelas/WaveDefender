package wave.audio;

import java.util.ArrayList;

import wave.mob.Mob;
import wave.mob.Player;

public class SoundEffect2D extends SoundEffect {
	private Mob mob;

	public static float volumeMax = 1.0f;
	private float volume;
	private boolean loop;
	private int raio;

	public static ArrayList<SoundEffect2D> sfxs2d = new ArrayList<SoundEffect2D>();

	public SoundEffect2D(String path, Mob mob, int raio) {
		super(path);
		this.mob = mob;
		this.raio = raio;

		sfxs2d.add(this);
	}

	public void update() {
		updateVolume(getVolume());
		loop();
		if (!mob.isVivo()) sfxs2d.remove(this);
	}
	
	

	private void loop() {
		if (loop && !isPlaying()) {
			play(loop);
		}

	}

	public void play(boolean loop) {
		this.loop = loop;

		sf.playAsSoundEffect(1.0f, volume, false);
	}

	private float getVolume() {
		double dist = Math.sqrt(Math.pow(mob.getXCentro() - Player.getPlayer().getXCentro(), 2) + Math.pow(mob.getYCentro() - Player.getPlayer().getYCentro(), 2));

		if (dist >= raio) {
			return 0;
		}

		return (float) ((volumeMax * (raio - dist)) / (float)raio);
	}

	public void updateVolume(float vol) {
		float pos;
		
		if (this.isPlaying()) {
			pos = getPosition();
			playAt(pos, vol);
		}

	}

	public static void setVolume(float vol) {
		volumeMax = vol;
	}

	protected void playAt(float pos, float volume) {
		sf.stop();
		sf.playAsSoundEffect(1.0f, volume, false);
		sf.setPosition(pos);

	}

}
