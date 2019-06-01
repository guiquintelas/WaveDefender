package wave.mob;

import java.awt.Color;
import java.awt.Graphics2D;

import wave.audio.RandomSFXGrupo;
import wave.particula.CriadorDeParticulas;
import wave.principal.Principal;
import wave.projetil.ProjetilGround;
import wave.tools.Util;

public class Urso extends Monstro{
	private Druida druida;
	
	private CriadorDeParticulas particulasInvocar;
	
	private RandomSFXGrupo tomarHitSFX = new RandomSFXGrupo(new String[]{"/SFX/ursoHit1.ogg", "/SFX/ursoHit2.ogg", "/SFX/ursoHit3.ogg"});
	
	public Urso(Druida druida) {
		super(Principal.nivel * 5, 100, (int)(druida.getDano() * 1.5), 0, Util.randomDouble(1.4, 1.6), Util.randomDouble(0.8, 1.0), 0, null);
		this.druida = druida;	
		
		x = druida.getX() - 50 + (int)(Math.random() * 100);
		y = druida.getY() - 50 + (int)(Math.random() * 100);
		
		barraVida.setAjuste(7);
		
		particulasInvocar = new CriadorDeParticulas(getX(), getY() + height, width, 5, 2, 2, Color.BLUE, 100);
		particulasInvocar.setSpeed(2);
		particulasInvocar.setAngulo(90, 30);
		particulasInvocar.addColor(Color.MAGENTA);
		particulasInvocar.addColor(Color.YELLOW);
		particulasInvocar.addColor(Color.RED);
		particulasInvocar.addColor(Color.GREEN);
		particulasInvocar.setComLuz(5, null, 50, 5, 10);
		particulasInvocar.addAllCoresLuz();
		particulasInvocar.setAlphaVar(30, 10);
		particulasInvocar.setProduzindo(true, 40);
		
		prontoParaPintar();
	}
	
	protected RandomSFXGrupo getTomarHitSFX() {
		return tomarHitSFX;
	}
	
	protected void updateResto() {
		atiraGround();
		particulasInvocar.update(getX(), getY() + height);
		particulasInvocar.update(druida.getX(), druida.getY() + druida.getHeight());
	}
	
	public String toString() {
		return "Urso";
	}
	
	protected int expValor() {
		return 5;
	}
	
	protected void setRandomXY() {
		//nao precisa
	}
	
	protected void droppar() {
		//nao droppa nada
	}
	
	protected void moveIdle() {
		double distancia = Math.sqrt(Math.pow(getXCentro() - druida.getXCentro(), 2) + Math.pow(getYCentro() - druida.getYCentro(), 2));
		if (distancia > 100) {	
			angulo = Math.toDegrees(Math.atan2(druida.getXCentro() - getXCentro(), druida.getYCentro() - getYCentro())) - 90;
			isAndando = true;
		}
		
		super.moveIdle();
	}
	
//	protected void updateIdleStatus() {
//		double distancia = Math.sqrt(Math.pow(getXCentro() - druida.getXCentro(), 2) + Math.pow(getYCentro() - druida.getYCentro(), 2));
//		if (distancia > 100) {	
//			isAndando = true;
//		}
//		
//		super.updateIdleStatus();
//	}
//	
	public Color getCor() {
		return new Color(205, 133 , 63);
	}
	
	private void atiraGround() {
		int random = 1 + (int)(Math.random() * 1000);
		if (random <= 2 && isFurioso  && ProjetilGround.todosGProjetils.size() < 2) {
			new ProjetilGround(getXCentro(), getYSpriteCentro(), getDanoVar());
			lancaProjetilSFX.play();
		}
	}
	
	
	public void pintarMob(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawOval(getXCentro() - width/3 - 6, getYSpriteCentro() - height / 2 - 6, 11, 11);
		g.drawOval(getXCentro() + width/3 - 6, getYSpriteCentro() - height / 2 - 6, 11, 11);
		
		super.pintarMob(g);
		g.setColor(getCor());
		g.fillOval(getXCentro() - width/3 - 5, getYSpriteCentro() - height / 2 - 5, 10, 10);
		g.fillOval(getXCentro() + width/3 - 5, getYSpriteCentro() - height / 2 - 5, 10, 10);
		
	}
}
