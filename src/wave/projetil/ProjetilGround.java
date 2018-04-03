package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.mob.Player;
import wave.particula.CriadorDeParticulas;

public class ProjetilGround extends ProjetilPerseguidorMob {
	private float anguloRotacao = 0;
	private Animaçao ani;
	private CriadorDeParticulas particulas;
	
	private Luz luz;
	
	public static ArrayList<ProjetilGround> todosGProjetils = new ArrayList<ProjetilGround>();

	public ProjetilGround(int xCentro, int yCentro, int dano) {
		super(xCentro, yCentro, dano / 2, 10, 10, Player.getPlayer().getSpeedMax() + .6);
		//Player.getPlayer().getSpeedMax() + 1
		
		particulas = new CriadorDeParticulas(getX(), getY(), width, width, 2, 2, new Color(139, 69, 19), 40);
		particulas.addColor(new Color(34, 139, 34));
		particulas.setProduzindo(true);
		
		ani = new Animaçao(getXCentro(), getYCentro(), Animaçao.groundProImgs, 3);
		ani.setVaiVolta(true);
		ani.setLoop(true);
		ani.setAutoPaint(false);
		ani.setSeguindo(this);
		ani.start();
		
		luz = new Luz(this, 35, 100, 100, 100, 100, 25);
		
		todosGProjetils.add(this);
	}

	public void updatePri() {
		super.updatePri();
		particulas.update(getX(), getY());
		updateAnguloRotacao();
	}


	private void updateAnguloRotacao() {	
		anguloRotacao += velRot();
		if (anguloRotacao >= 360) anguloRotacao -= 360;
		
	}
	
	private float velRot() {
		double dist = Math.sqrt(Math.pow(getXCentro() - Player.getPlayer().getXCentro(), 2) + Math.pow(getYCentro() - Player.getPlayer().getYCentro(), 2));
		if (dist < 200) {
			if (10 / (dist/100d) > 15) {
				return 15;
			} else {
				return (float)(10 / (dist/100d));
			}
			
		} else {
			return 2;
		}	
	}

	public void pintar(Graphics2D g) {
		ani.pintarManual(g, anguloRotacao -90);
	}

	protected void colisao() {
		Player.getPlayer().tomarHit(dano);
		Player.getPlayer().setGroundedTrue(75);	
		particulas.setProduzindo(false);
		todosGProjetils.remove(this);
		luz.desativar();
	}
	
	protected void autoDestruiçao() {
		super.autoDestruiçao();
		todosGProjetils.remove(this);
		luz.desativar();
	}
	
	protected void removeProjetil() {
		super.removeProjetil();
		ani.stop();
	}

	

}
