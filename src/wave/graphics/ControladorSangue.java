package wave.graphics;

import java.awt.Color;

public class ControladorSangue {
	private int xCentro;
	private int yCentro;
	private Color cor;
	private double danoMod;
	
	public ControladorSangue(int xCentro, int yCentro, Color cor, int dano) {
		this.xCentro = xCentro;
		this.yCentro = yCentro;
		this.cor = cor;
		danoMod = dano/10.0;
		
		criarSangue();
	}
	
	private void criarSangue() {
		for (int x = 0; x < danoMod * 3; x++) {
			new Sangue(xCentro, yCentro, cor, danoMod);
		}
	}
}
