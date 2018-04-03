package wave.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import wave.mob.Mob;
import wave.principal.Dimensional;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.Util;

public class BalaoDeFala extends Dimensional{
	private static BufferedImage imgCompleta = Util.carregarImg("/Sprites/balaoDeFala.png");
	private static BufferedImage imgMeio = imgCompleta.getSubimage(0, 4, imgCompleta.getWidth(), 10);
	private static BufferedImage imgTopo = imgCompleta.getSubimage(0, 0, imgCompleta.getWidth(), 4);
	private static BufferedImage imgBaixo = imgCompleta.getSubimage(0, 16, imgCompleta.getWidth(), imgCompleta.getHeight() - 16);
	
	private static final Font font = new Font("Arial", Font.PLAIN, 10);
	
	private static final int MAX_TEXT_WIDTH = 105;
	
	public static HashMap<Mob, BalaoDeFala> todosBaloes = new HashMap<Mob, BalaoDeFala>();
	
	private String texto;
	private float alpha;
	
	private int duracao;
	private int tickAtual;
	private Mob mob;
	
	private boolean isAtivo = false;
	private boolean isMenorMax = false;
	private boolean isFadeOut = false;
	
	private Timer timerAlpha;
	
	private ArrayList<String> linhas = new ArrayList<String>();
	
	public BalaoDeFala(String texto, Mob mob, int tempoEmTicks) {
		
		this.texto = texto;
		this.mob = mob;
		this.duracao = tempoEmTicks;
		tickAtual = Principal.tickTotal;
		
		updateXY();
		
		if (Util.getStringWidh(this.texto, font) < MAX_TEXT_WIDTH) {
			isMenorMax = true;
		} else {
			criarLinhas();
		}
		
		isAtivo = true;
		fadeIn(tempoEmTicks/10);
		todosBaloes.put(mob, this);
		
		
	}
	
	private void fadeIn(final int ticks) {
		alpha = 0;
		
		timerAlpha  = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual+1) {
					alpha += 1/(float)ticks;
					tickAtual++;
					if (alpha >= 1) {
						alpha = 1;
						timerAlpha.stop();					
					}
				}
				
			}
		});
		timerAlpha.start();
	}
	
	private void fadeOut(final int ticks) {
		alpha = 1;
		
		timerAlpha  = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual+1) {
					alpha -= 1/(float)ticks;
					tickAtual++;
					if (alpha <= 0) {
						alpha = 0;
						timerAlpha.stop();					
					}
				}
				
			}
		});
		timerAlpha.start();
	}
	
	private void criarLinhas() {
		ArrayList<String> palavras = new ArrayList<String>();
		String linhaAtual = "";
		
		while (texto.length() > 0) {
			
			if (!texto.contains(" ")) {
				palavras.add(texto);
				texto = "";
				
			} else {
				palavras.add(texto.substring(0, texto.indexOf(" ") + 1));
				texto = texto.substring(texto.indexOf(" ") + 1, texto.length());
			}
			
		}
		
		
		while (!palavras.isEmpty()) {
			String linhaAtualTemp = "";
			
			while(true) {				
				if (palavras.isEmpty()) break;
				
				linhaAtualTemp = linhaAtualTemp.concat(palavras.get(0));

				if (Util.getStringWidh(linhaAtualTemp, font) <= MAX_TEXT_WIDTH) {
					linhaAtual = linhaAtualTemp;
				} else {
					break;
				}
				
				palavras.remove(0);
			}
			
			linhas.add(linhaAtual);			
		}
		
	}
	
	public void update() {
		checaAtivo();
		if (isAtivo) {
			updateXY();
			checaFadeOut();
		}
		
	}
	
	
	
	private void checaFadeOut() {
		if (!isFadeOut && Principal.tickTotal >= tickAtual + duracao - (duracao/10)) {
			fadeOut(duracao/10);
			isFadeOut = true;
		}
		
	}

	private void updateXY() {
		this.x = mob.getX() - 20;
		this.y = mob.getYSprite();
		
		if (x < 0) {
			x = 0;
		}
		
		if (y  < 0) {
			y = 0;
		}
		
		if (y - 4 - (linhas.size() * 10) < 0) {
			y = 4 + (linhas.size() * 10);
		}
		
		if (x > JanelaJogo.WIDTH - imgCompleta.getWidth()) {
			x = JanelaJogo.WIDTH - imgCompleta.getWidth();
		}
	}

	private void checaAtivo() {
		if (isAtivo) {
			if (Principal.tickTotal >= tickAtual + duracao) {
				isAtivo = false;
			}
			
			
		}
	}
	
	public void pintar(Graphics2D g) {
		if (!isAtivo) return;
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.setColor(Color.BLACK);
		g.setFont(font);
		
		if (isMenorMax) {
			g.drawImage(imgCompleta, getX(), getY() - 20, null);
			g.drawString(texto, getX() + 7, getY() - 20 + 13);
			
		} else {
			g.drawImage(imgTopo, getX(), getY() - 4 - (linhas.size() * 10), null);
			for (int i = 0; i < linhas.size(); i++) {
				g.drawImage(imgMeio, getX(), getY() - ((i + 1) * 10), null);
			}
			
			
			g.drawImage(imgBaixo, getX(), getY(), null);
			for (int x = 0; x < linhas.size(); x++) {
				g.drawString(linhas.get(x), getX() + 7,getY() + 9 + (x * 10) - (linhas.size() * 10) );
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
	
	}
}
