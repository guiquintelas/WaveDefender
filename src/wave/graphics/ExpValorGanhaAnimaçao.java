package wave.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class ExpValorGanhaAnimaçao {
	private int x;
	private int y;
	private int valor;
	
	private float alpha = 1;
	
	public static ArrayList<ExpValorGanhaAnimaçao> todasExps = new ArrayList<ExpValorGanhaAnimaçao>();
	
	public ExpValorGanhaAnimaçao(int x, int y, int valor) {
		this.x = x;
		this.y = y - 4;
		this.valor = valor;
				
		todasExps.add(this);				
	}
	
	public void update() {
		move();
		updateAlpha();
	}
	
	private void move() {
		y -= 0.5;
	}


	private void updateAlpha() {
		alpha -= 0.02f;
		if (alpha <= 0) {
			alpha = 0;
			todasExps.remove(this);
		}
	}
	
	public void pintar(Graphics2D g) {
		g.setFont(new Font("Bookman Old Style", Font.BOLD, 16));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.setColor(Color.BLACK);
		g.drawString("+" + valor, (int)x + 1, (int)y + 1);
;		g.setColor(new Color(255, 215, 0));
		g.drawString("+" + valor, (int)x, (int)y);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}
