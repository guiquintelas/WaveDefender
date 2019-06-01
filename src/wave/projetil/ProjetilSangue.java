package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.graphics.animacao.Animacao;
import wave.graphics.light.Luz;
import wave.mob.Player;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class ProjetilSangue extends ProjetilPerseguidorMob{
	
	private static final int WIDTH = 64;
	private static final int HEIGHT = 64;	
	private boolean isInvisivel = false;
	
	private static final RandomSFXGrupo sfxColisao = new RandomSFXGrupo(new String[]{"/SFX/projetilSangue1.ogg", "/SFX/projetilSangue2.ogg", "/SFX/projetilSangue3.ogg"});
	private static final ArrayList<BufferedImage> imgs = Util.carregarArrayBI(Animacao.explosaoSrite, 0, 1152, 64, 64, 26);
	
	private CriadorDeParticulas particulas;

	public ProjetilSangue(int xCentro, int yCentro, int dano, boolean isInvisivel) {
		super(xCentro, yCentro, dano, 7, 7, 4);
		
		sombra.setXOff(-3);
		
		this.isInvisivel = isInvisivel;
		
		particulas = new CriadorDeParticulas(getX() - 5, getY() - 5, 10, 10, 2, 2, new Color(178, 34, 34), 30);
		particulas.setProduzindo(true);
		particulas.setAlphaVar(50, 15);
		
		if (isInvisivel) {
			particulas.addColorLuz(new Color(255, 69, 0));
			particulas.setComLuz(7, null, 80, 5, 25);
		} else {
			particulas.setComLuz(5, null, 70, 5, 25);
		}
		
		
		particulas.addAllCoresLuz();
		
		new Luz(this, 35, 255, 80, 80, 70, 25);
		
		
	}
	
	public int getXCentro() {
		return getX();
	}
	
	public int getYCentro() {
		return getY();
	};

	@Override
	protected void colisao() {
		if (isInvisivel) {		
			Player.getPlayer().tomarHit(dano / 2);
			Player.getPlayer().setLentoTrue(50, 60);
			
		} else {
			Player.getPlayer().tomarHit(dano/ 4);
			Player.getPlayer().setLentoTrue(50, 40);
		}
		
		sfxColisao.play();
		particulas.setProduzindo(false);
		new Animacao(getX(), getY(), imgs, 1).start();;
		
	}
	
	public void updatePri() {
		super.updatePri();
		particulas.update(getX() - 5, getY() - 5);
		if (isInvisivel) {
			particulas.setAngulo(angulo - 180, 20);
		}
		
	}

	@Override
	public void pintar(Graphics2D g) {
		g.drawImage(imgs.get(0), getX() - WIDTH /2, getY() - HEIGHT / 2, null);
		
	}

}
