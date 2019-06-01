package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;

import wave.graphics.animacao.Animacao;
import wave.graphics.light.Luz;
import wave.mob.Druida;
import wave.mob.Monstro;
import wave.mob.Ogro;
import wave.particula.CriadorDeParticulas;
import wave.tools.ActionQueue;

public class ProjetilCuraDruida extends ProjetilPerseguidorMob{
	private Monstro monstro;
	
	private CriadorDeParticulas particulas;

	public ProjetilCuraDruida(Druida druida, Monstro monstro) {
		super(druida.getXCentro(), druida.getYSpriteCentro(), druida.getDanoVar(), 10, 10, 5);
		this.monstro = monstro;
		
		particulas = new CriadorDeParticulas(getX(), getY(), width, height, 2, 3, Color.GREEN, 100);
		particulas.setAlphaVar(50, 25);
		particulas.setProduzindo(true);
		particulas.setSpeed(0);
		particulas.setComLuz(6, Color.GREEN, 50, 5, 35);
		
	}
	
	protected void init() {
		return;
	}
	
	public void updatePri() {
		super.updatePri();
		particulas.update(getX(), getY());
	}

	protected void colisao() {
		if (monstro.isVivo()) {
			monstro.curar(dano * 2);
			
			Animacao ani = new Animacao(0, 0, Animacao.particulasHealImgs, 4);
			ani.setScale(monstro.getWidth() / 90f);
			
			if (monstro instanceof Ogro) {
				ani.setSeguindo(monstro, 0, 5);
			} else {
				ani.setSeguindo(monstro);
			}
			
			ani.start();
			
			final Luz luzCura = new Luz(monstro, 50, 0, 255, 0, 100, 15, true, true, 0, 0);
			luzCura.forcaVar.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					luzCura.desativar(25);
					return true;
				}
			});
		}
		
		particulas.setProduzindo(false);
		
	}

	public void pintar(Graphics2D g) {
		return;		
	}
	
	protected void checaColisaoPlayer() {
		if (Math.abs(getXCentro() - monstro.getXCentro()) < ((width / 2) + (monstro.getWidth() / 2))) {
			if (Math.abs(getYCentro() - monstro.getYCentro()) < ((height / 2) + (monstro.getHeight() / 2))) {
				colisao();
				removeProjetil();
			}
		}
	}
	
	protected void updateAngulo() {
		// - 90 para acertar com as cordenadas do campo, 0 para direita, 90 para cima, 180 para direita e 270 para baixo
		double anguloNovo = Math.toDegrees(Math.atan2(monstro.getXCentro() - getXCentro(), monstro.getYCentro() - getYCentro())) - 90;
		if (anguloNovo < 0) {
			anguloNovo += 360;
		}
		
		angulo = anguloNovo;
		
	}
	
	protected void checaAutoDestruicao() {
		return;
	}

}
