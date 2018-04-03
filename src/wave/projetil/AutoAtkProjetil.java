package wave.projetil;

import java.awt.Graphics2D;

import wave.graphics.SombraDinamica;
import wave.graphics.animaçao.Animaçao;
import wave.mob.Player;
import wave.mob.Ranged;

public abstract class AutoAtkProjetil extends Projetil {

	protected int xInicial;
	protected int yInicial;
	protected int alcance;


	public AutoAtkProjetil(Ranged mob, double angulo) {
		super(mob.getXCentro(), mob.getYSpriteCentro(), angulo, mob.getDanoVar());
		this.xInicial = mob.getXCentro();
		this.yInicial = mob.getYSpriteCentro();
		this.alcance = mob.getAlcance();
		
		sombraD = new SombraDinamica(this, getXCentro(), getYCentro(), mob.getAlcance());
		sombra = sombraD.sombra;
		sombra.setHeightOff(7);
	}

	public void updatePri() {
		move();
		checaColisao();
		checaAlcance();
		if (sombraD != null) sombraD.update();
		updateResto();
	}

	protected abstract void updateResto();

	protected void checaColisao() {
		if (Math.abs(getXCentro() - Player.getPlayer().getXCentro()) < ((width / 2) + (Player.getPlayer().getWidth() / 2))) {
			if (Math.abs(getYCentro() - Player.getPlayer().getYCentro()) < ((height / 2) + (Player.getPlayer().getHeight() / 2))) {
				colisao();
				removeProjetil();
			}
		}

	}
	
	protected abstract void colisao();
	
	@Override
	protected void colisaoParedeRes() {
		removeProjetil();
	}

	private void move() {
		x += Math.cos(Math.toRadians(angulo)) * speed;
		y -= Math.sin(Math.toRadians(angulo)) * speed;
	}

	protected void checaAlcance() {
		double distanciaPercorrida = Math.sqrt(Math.pow(xInicial - getXCentro(), 2) + Math.pow(yInicial - getYCentro(), 2));
		if (distanciaPercorrida > alcance) {
			removeProjetil();
			Animaçao.aniFumaçaPadrao(getXCentro(), getYCentro());
		}

	}

	public abstract void pintar(Graphics2D g);

}
