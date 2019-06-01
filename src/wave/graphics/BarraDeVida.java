package wave.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import wave.gui.BarrasSuperiores;
import wave.mob.Mob;
import wave.mob.Player;
import wave.principal.Dimensional;
import wave.principal.JanelaJogo;

public class BarraDeVida extends Dimensional {
	private static final int ESPAcO = 2;
	private Mob mob;
	private Color cor;
	private int ajuste = 0;
	private boolean isMouseDentro = false;

	public BarraDeVida(Mob mob) {
		this.mob = mob;
		this.width = mob.getWidth() + 10;
		this.height = 7;
	}

	public Color getCor() {
		return cor;
	}

	private void checaColisaoMouse() {
		if (Math.abs(JanelaJogo.xMouse - mob.getXCentro()) < (mob.getWidth() / 2)) {
			if (Math.abs(JanelaJogo.yMouse - mob.getYSpriteCentro()) < (mob.heightSprite / 2)) {
				isMouseDentro = true;
				return;
			}
		}
		
		isMouseDentro = false;
	}

	public void setAjuste(int ajuste) {
		this.ajuste = ajuste;
	}

	private void updateXY() {
		x = mob.getX() - 5;
		y = mob.getYSprite() - (ESPAcO + height) - ajuste;
	}

	private void updateCor() {
		int vermelho = 255;
		int verde = 255;

		if (getWithAtual() >= (width / 2)) {
			int vermelhoFinal = (255 * (getWithAtual() - (width / 2))) / (width / 2);
			if (vermelhoFinal > 255)
				vermelhoFinal = 255;
			vermelho -= vermelhoFinal;
		} else {
			int verdeFinal = (255 * getWithAtual()) / (width / 2) - 255;
			if (verdeFinal < -255)
				verdeFinal = -255;
			verde += verdeFinal;
		}

		cor = new Color(vermelho, verde, 0);
	}

	public void update() {
		this.width = mob.getWidth() + 10;
		updateXY();
		updateCor();
		checaColisaoMouse();
		if (mob instanceof Player) {
			BarrasSuperiores.update();
		}
	}

	private int getWithAtual() {
		return (mob.getVida() * width) / mob.getVidaMax();
	}
	
	private boolean isAtiva() {
		return (!(getWithAtual() == width) || isMouseDentro) && !(mob.isInvisivel() && !(mob instanceof Player));
	}

	public void pintarBarra(Graphics2D g) {	
		if (!isAtiva()) return;
		g.setColor(cor);
		g.fillRect(getX(), getY(), getWithAtual(), height);
		g.setColor(Color.GRAY);
		g.drawRect(getX(), getY(), width, height);
	}

}
