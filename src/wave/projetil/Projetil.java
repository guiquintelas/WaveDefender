package wave.projetil;

import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.graphics.SombraDinamica;
import wave.principal.DimensionalObj;

public abstract class Projetil extends DimensionalObj{
	protected double angulo;
	
	protected double speed;
	protected int dano;
	protected SombraDinamica sombraD;
	
	public static ArrayList<Projetil> todosProjetils = new ArrayList<Projetil>();
	
	public Projetil(int xCentro, int yCentro, double angulo, int dano) {
		this.x = xCentro;
		this.y = yCentro;
		this.angulo = angulo;
		this.dano = dano;
		todosProjetils.add(this);
		prontoParaPintar();
	}
	
	protected synchronized void removeProjetil() {
		todosProjetils.remove(this);
		remove();
	}
	
	public final synchronized void update() {
		updatePri();
		colisaoParede();
	}
	
	protected abstract void updatePri();
	public abstract void pintar(Graphics2D g);
	
	protected void colisaoParede() {
		for (int i = 0; i < getTiles().size(); i++) {
			if (getTiles().get(i).isColliding(this)) {
				colisaoParedeRes();
			}
		}
	}
	
	
	
	protected abstract void colisaoParedeRes();
	
	public double getAngulo() {
		return angulo;
	}
	
	public String toString() {
		return "";
	}
	
}
