package wave.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import wave.mob.Player;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public class BarraExp extends Descritivo {
	public static final int X = 0;
	public static final int Y = 1;
	public static final int WIDTH = GUI.WIDTH;
	public static final int HEIGHT = 4;
	public static String descricao;
	
	private static boolean isPiscando;
	private static boolean isAlphaUp = true;
	
	private static float alpha = 0;
	
	private static Timer timerAlpha;
	
	private static int widthExp;
	
	public static void update() {
		updateDescricao();
		updateWidthExp();
	}
	
	public static void init() {
		new BarraExp();
	}
	
	public int getX() {
		return 0;
	}
	
	public int getY() {
		return JanelaJogo.HEIGHT - 2;
	}
	
	public int getWidth() {
		return BarraExp.WIDTH;
	}
	
	public int getHeight() {
		return 8;
	}
	
	public static void piscar(final int ticks) {
		if (timerAlpha != null) {
			if (timerAlpha.isRunning()) {
				return;
			}
		}
		
		isPiscando = true;
		
		timerAlpha = new Timer(5, new ActionListener() {
			int tickInicial = Principal.tickTotal;
			int tickAtual = Principal.tickTotal;
			float alphaMax = 0;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickInicial + ticks) {
					isPiscando = false;
					timerAlpha.stop();
					alpha = 0;
					isAlphaUp = true;
				}
				
				if (Principal.tickTotal >= tickInicial + ticks - (ticks/10)) {
					if (alphaMax == 0) alphaMax = alpha;
					alpha -= alphaMax/(ticks/10.f);
					if (alpha <= 0) alpha = 0;
					return;
				}
				
				if (Principal.tickTotal >= tickAtual + 1 && isAlphaUp) {
					alpha += .04f;
					tickAtual++;
					if (alpha >= 1) {
						alpha = 1;
						isAlphaUp = false;
					}
				}
				
				if (Principal.tickTotal >= tickAtual + 1 && !isAlphaUp) {
					alpha -= .04f;
					tickAtual++;
					if (alpha <= 0) {
						alpha = 0;
						isAlphaUp = true;
					}
				}
				
			}
		});
		timerAlpha.start();
	}
	
	private static void updateDescricao() {
		double porcentagemNivel = GUI.round(((double)Player.getPlayer().getExp() / (double)Player.getPlayer().getExpLevel()) * 100.0, 2);		
		descricao = "Barra de experiência. Voce precisa de " + (Player.getPlayer().getExpLevel() - Player.getPlayer().getExp()) + " de Exp para o proximo level. (" + porcentagemNivel + "%)";
	}
	
	private static void updateWidthExp() {
		widthExp = (Player.getPlayer().getExp() * WIDTH) / Player.getPlayer().getExpLevel();
	}
	
	public static void pintar(Graphics2D g) {
		g.setColor(new Color(255, 215, 0));
		g.fillRect(X, Y, widthExp, HEIGHT);
		
		if (isPiscando) {
			g.setColor(Color.WHITE);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g.fillRect(X, Y, WIDTH, HEIGHT);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
	}

	public String getDescriçao() {
		return descricao;
	}
}
