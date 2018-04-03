package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.mob.Player;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class ProjetilVeneno extends ProjetilPerseguidorMob{
	private Animaçao ani;
	private static ArrayList<BufferedImage> img = Util.carregarArrayBI(Animaçao.explosaoSrite, 64, 576, 64, 64, 30);
	
	private CriadorDeParticulas particulas;

	public ProjetilVeneno(int xCentro, int yCentro, int dano) {
		super(xCentro, yCentro, dano, 10, 10, 3.5);
		//3.5
		
		particulas = new CriadorDeParticulas(getX(), getY(), width, height, 2, 2, new Color(0, 100, 0), 40);
		particulas.setAlphaVar(25, 15);
		particulas.setSpeed(1);
		particulas.setProduzindo(true);
		particulas.setComLuz(7, new Color(0, 255, 0), 100, 5, 25);
		
		ani = new Animaçao(getXCentro(), getYCentro(), Animaçao.venenoGotaImgs, 4);
		ani.setScale(Util.randomInt(15, 20)/10f);
		ani.setLoop(true);
		ani.setSeguindo(this);
		ani.setAutoPaint(false);
		ani.start();
		
		new Luz(this, 30, 0, 255, 120, 70, 50);
	}
	
	public void updatePri() {
		super.updatePri();
		particulas.update(getX(), getY());
		particulas.setAngulo(angulo + 180);
	}

	protected void colisao() {
		Player.getPlayer().setEnvenenadoTrue(4, dano / 2);
		Player.getPlayer().setLentoTrue(125, 40);
		particulas.setProduzindo(false);
		
		new Animaçao(getXCentro(), getYCentro(), img, 2).start();
	}

	public void pintar(Graphics2D g) {
		//g.setColor(new Color(124, 252, 0));
		//g.fillOval(getX(), getY(), width, height);
		//g.setColor(new Color(0, 100, 0));
		//g.drawOval(getX(), getY(), width, height);
		ani.pintarManual(g, -angulo + 90, 0, -5);
	}
	
	protected void removeProjetil() {
		super.removeProjetil();
		ani.stop();
	}

}
