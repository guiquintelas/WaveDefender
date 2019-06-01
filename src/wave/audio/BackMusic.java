package wave.audio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.principal.Principal;


public class BackMusic {
	private static ArrayList<Musica> musicas = new ArrayList<Musica>();
	private static ArrayList<Boolean> jaTocou = new ArrayList<Boolean>();
	private static Musica atual;
	private int index;
	
	private static Timer timerReduzir;
	
	public BackMusic(String[] sfxs) {
		for (int x = 0; x < sfxs.length; x++) {
			BackMusic.musicas.add(new Musica(sfxs[x]));
			jaTocou.add(false);
		}
	}
	
	

	public void play() {
		if (atual != null && atual.isPlaying()) return;
		
		
		int indexNovo = (int)(Math.random() * musicas.size());
		while(jaTocou.get(indexNovo)) {
			indexNovo = (int)(Math.random() * musicas.size());
		}
		index = indexNovo;
		
		musicas.get(index).play();
		atual = musicas.get(index);
		jaTocou.set(index, true);
		
		for (int i = 0; i < jaTocou.size(); i++) {
			if (!jaTocou.get(i)) return;
		}
		
		jaTocou.clear();
		
		for (int i = 0; i < musicas.size(); i++) {
			jaTocou.add(false);
		}
		
	}
	
	public static void setVolume(float vol) {
		float pos;
		
		pos = atual.getPosition();
		Musica.volume = vol;
		atual.playAt(pos);
		
	}
	
	public synchronized static void reduzir(final int tickDuracao, final int duracaoFade) {	
		if (timerReduzir != null) {
			if (timerReduzir.isRunning()) {
				return;
			}
		}
		
		final float volumeTemp = Musica.volume;	
		Musica.volume -= 0.7f;
		if (Musica.volume < 0) Musica.volume = 0;
		final float diferVol = volumeTemp - Musica.volume;
		setVolume(Musica.volume);
		
		timerReduzir = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual + tickDuracao) {
					
					setVolume(Musica.volume + (diferVol / (float)duracaoFade));
					if (Musica.volume >= volumeTemp) {
						Musica.volume = volumeTemp;
						timerReduzir.stop();
					}
					tickAtual++;
				}
				
			}
		});
		timerReduzir.start();
	}
	
	public static boolean isMute() {
		return Musica.mute;
	}

	public static void setMute(boolean mute) {
		Musica.mute = mute;
		if (atual != null) {
			atual.playAt(atual.getPosition());
		}
		
	}
	
}
