package wave.audio;

import java.util.ArrayList;


public class RandomSFXGrupo {
	private ArrayList<SoundEffect> sfxs = new ArrayList<SoundEffect>();
	private int index;
	
	
	public RandomSFXGrupo(String[] sfxs) {
		for (int x = 0; x < sfxs.length; x++) {
			this.sfxs.add(new SoundEffect(sfxs[x]));
		}
	}

	public void play() {
		int indexNovo = (int)(Math.random() * sfxs.size());
		while(indexNovo == index) {
			indexNovo = (int)(Math.random() * sfxs.size());
		}
		index = indexNovo;
		
		sfxs.get(index).play();
	}
}
