package wave.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.principal.Dimensional;

public class Sangue extends Dimensional{
	
	private float alpha;
	
	private Color cor;
	
	public static ArrayList<Sangue> sangueParaPintar = new ArrayList<Sangue>();
	
	public Sangue(int xCentro, int yCentro, Color cor, double danoMod) {
		random(danoMod);

		this.x = xCentro - width / 2 + (int)(8 * danoMod / 2) - (int)(Math.random() * (15 * danoMod));
		this.y = yCentro - height / 2 + (int)(8 * danoMod / 2) - (int)(Math.random() * (15 * danoMod));
		this.cor = cor;
		
		
		sangueParaPintar.add(this);
	}
	
	private void random(double danoMod) {
		width = 10 + (int)(Math.random() * 10 * danoMod * 0.75);
		height = width / 3;
		
		alpha = 0.2f + (float)(Math.random() * 0.3);
	}
	
	public void pintar(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.setColor(cor);
		g.fillOval(getX(), getY(), width, height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
		sangueParaPintar.remove(this);
	}

}
