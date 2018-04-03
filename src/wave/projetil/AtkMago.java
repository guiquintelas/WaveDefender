package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.mob.Mago;
import wave.mob.Player;
import wave.particula.CriadorDeParticulas;

public class AtkMago extends AutoAtkProjetil {
	private Animaçao ani;

	private CriadorDeParticulas particulas;	

	public AtkMago(Mago mago, double angulo, boolean isFurioso) {
		super(mago, angulo);

		if (isFurioso) {
			this.speed = 6;
			this.width = 8;
			this.height = 8;
			particulas = new CriadorDeParticulas(getX(), getY(), width, height, 1, 1, Color.MAGENTA, 60);
			particulas.setAlphaVar(15);
			particulas.setComLuz(4, new Color(160, 32, 240), 100, 5, 10);
		} else {
			this.speed = 3;
			this.width = 5;
			this.height = 5;
			particulas = new CriadorDeParticulas(getX(), getY(), width, height, 1, 1, Color.MAGENTA, 40);
			particulas.setAlphaVar(20, 5);
			particulas.setComLuz(4, new Color(160, 32, 240), 50, 5, 10);
		}

		
		particulas.setProduzindo(true);
		particulas.setSpeed(0);
		particulas.addColor(new Color(160, 32, 240));
		
		
		ani = new Animaçao(getXCentro(), getYCentro(), Animaçao.particulaEsfericaImgs, 1);
		if (isFurioso) {
			ani.setScale(.225f);
		} else {
			ani.setScale(.2f);
		}
		
		ani.setLoop(true);
		ani.setAutoPaint(false);
		ani.setSeguindo(this);
		ani.start();
		
		new Luz(this, 40, 255, 0, 255, 50, 25);

	}

	protected void updateResto() {
		particulas.update(getX(), getY());
	}


	protected void colisao() {
		Player.getPlayer().tomarHit(dano);
		Player.getPlayer().setLentoTrue(100, 25);
		particulas.setProduzindo(false);
	}
	
	protected void removeProjetil() {
		super.removeProjetil();
		ani.stop();
	}

	public void pintar(Graphics2D g) {
		if (ani != null) ani.pintarManual(g);
	}

}
