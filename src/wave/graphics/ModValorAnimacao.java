package wave.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class ModValorAnimacao {
	private double x;
	private double y;
	private float alpha = 1.0f;
	private double angulo = 90;
	private double gravidade = .2;
	private String valor;
	private Color cor;
	public static final Color COR_DANO = new Color(255, 140, 0);
	public static ArrayList<ModValorAnimacao> todosModAni = new ArrayList<ModValorAnimacao>();
	
	public ModValorAnimacao(int x, int y, int valor, Color cor) {
		this.x = x;
		this.y = y - 8;
		this.valor = Integer.toString(valor);
		this.cor = cor;
		
		angulo += 80 - (int)(Math.random() * 160);
				
		todosModAni.add(this);
		
		
	}
	
	public void update() {
		move();
		gravidade();
		updateAlpha();
	}
	
	private void move() {
		x += Math.cos(Math.toRadians(angulo)) * 1.5;	
		y -= Math.sin(Math.toRadians(angulo)) * 1.5;	
	}

	private void gravidade() {
		gravidade += 0.09;
		y += gravidade;
	}

	private void updateAlpha() {
		alpha -= 0.02f;
		if (alpha <= 0) {
			alpha = 0;
			todosModAni.remove(this);
		}
	}
	
	public void pintar(Graphics2D g) {
		g.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.setColor(Color.BLACK);
		g.drawString(valor, (int)x + 1, (int)y + 1);
;		g.setColor(cor);
		g.drawString(valor, (int)x, (int)y);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}
