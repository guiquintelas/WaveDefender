package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;

import wave.audio.RandomSFXGrupo;
import wave.graphics.animacao.Animacao;
import wave.graphics.light.Luz;
import wave.mob.Druida;
import wave.mob.Player;
import wave.particula.CriadorDeParticulas;

public class AtkDruida extends AutoAtkProjetil{
	private Animacao ani;
	
	private static RandomSFXGrupo sfx = new RandomSFXGrupo(new String[]{"/SFX/druidaAtk1.ogg", "/SFX/druidaAtk2.ogg" ,"/SFX/druidaAtk3.ogg"});
	
	private CriadorDeParticulas particulas;
	
	public AtkDruida(Druida druida, double angulo) {
		super(druida, angulo);

		this.width = 5;
		this.height = 5;
		this.speed = 3;

		particulas = new CriadorDeParticulas(getX(), getY(), width, height, 1, 1, new Color(0, 100, 0), 40);
		particulas.setProduzindo(true);
		particulas.setSpeed(0);
		particulas.addColor(new Color(139, 69, 19));
		if (druida.isFurioso()) {
			particulas.setSpeed(2);
			particulas.setAngulo(angulo - 180, 35);
			particulas.setAlphaVar(15, 5);
			particulas.setComLuz(4, Color.GREEN, 75, 5, 10);
			particulas.addColor(new Color(0, 100, 0));
			particulas.setSeguindo(true, this);
		}
		
		ani = new Animacao(getXCentro(), getYCentro(), Animacao.druidaAtkImgs, 3);
		ani.setLoop(true);
		ani.setSeguindo(this);
		ani.setAutoPaint(false);
		ani.start();
		
		sfx.play();
		
		new Luz(this, 30, 0, 200, 50, 100, 25);
	}

	protected void updateResto() {
		particulas.update(getX(), getY());
	}


	protected void colisao() {
		Player.getPlayer().tomarHit(dano);
		particulas.setProduzindo(false);
		ani.stop();
	}
	
	protected void removeProjetil() {
		super.removeProjetil();
		ani.stop();
	}

	public void pintar(Graphics2D g) {
//		g.setColor(new Color(0, 100, 0));
//		g.fillOval(getX() - width / 2, getY() - height / 2, width, height);
//		g.setColor(Color.BLACK);
//		g.drawOval(getX() - width / 2, getY() - height / 2, width, height);	
		if (ani != null) ani.pintarManual(g, -angulo + 90, 0, -5);
	}
}
