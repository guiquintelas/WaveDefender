package wave.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.principal.Dimensional;
import wave.principal.JanelaJogo;
import wave.tools.Util;

public class DescricaoBox extends Dimensional {
	
	private static final float ALPHA = 0.8f;
	private static final int MAX_WIDTH_LINHA = 300;
	private static final Color COR_FUNDO = new Color(238, 221, 130);
	private static final Font font = new Font("Bookman Old Style", Font.BOLD, 12);
	
	private static int x;
	private static int y;
	
	private static int width;
	private static int height;
	
	private static boolean isVisivel = false;
	
	private static ArrayList<String> linhas = new ArrayList<String>();
	public static String descricao;
	
	public static void criar(String descricao) {
		if (DescricaoBox.descricao == descricao) {
			updateXY();
		} else {
			DescricaoBox.descricao = descricao;		
			setWidthHeight();
			updateXY();
		}
		
		setVisible(true);
	}
	
	public static String getDescricao() {
		return descricao;
	}
	
	
	private synchronized static void updateXY() {
		x = (int)JanelaJogo.xMouse;
		y = (int)(JanelaJogo.yMouse - JanelaJogo.HEIGHT - height);	
	}

	private static void setWidthHeight() {
		linhas.clear();
		
		if (Util.getStringWidh(descricao, font) < MAX_WIDTH_LINHA) {
			width = Util.getStringWidh(descricao, font) + 10;
			height = 20;
			linhas.add(descricao);
			return;
		}
		
		
		ArrayList<String> palavras = new ArrayList<String>();
		String linhaAtual = "";
		
		while (descricao.length() > 0) {
			
			if (!descricao.contains(" ")) {
				palavras.add(descricao);
				descricao = "";
				
			} else {
				palavras.add(descricao.substring(0, descricao.indexOf(" ") + 1));
				descricao = descricao.substring(descricao.indexOf(" ") + 1, descricao.length());
				
			}
			
		}
		
		
		while (!palavras.isEmpty()) {
			String linhaAtualTemp = "";
			
			while(true) {				
				if (palavras.isEmpty()) break;
				
				linhaAtualTemp = linhaAtualTemp.concat(palavras.get(0));

				if (Util.getStringWidh(linhaAtualTemp, font) <= MAX_WIDTH_LINHA) {
					linhaAtual = linhaAtualTemp;
				} else {
					break;
				}
				
				palavras.remove(0);
			}
			
			linhas.add(linhaAtual);			
		}
		
		width = MAX_WIDTH_LINHA;	
		height = linhas.size() * 20;		
		
		if ((x + width) > GUI.WIDTH) {
			x -= width;
		}
	}
	
	public synchronized static void setVisible(boolean visivel) {
		DescricaoBox.isVisivel = visivel;
	}
	
	public static boolean isVisivel() {
		return isVisivel;
	}
	
	public synchronized static void pintarNaGUI(Graphics2D gGUI) {
		if (!isVisivel) return;
		gGUI.setColor(COR_FUNDO);
		gGUI.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));
		//gGUI.fillRect(x, y, width, height);
		gGUI.fillRoundRect(x, y, width, height, 7, 7);
		gGUI.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		gGUI.setColor(Color.BLACK);
		//gGUI.drawRect(x, y, width, height);
		gGUI.drawRoundRect(x, y, width, height, 7, 7);
		
		gGUI.setColor(Color.BLACK);
		gGUI.setFont(font);
		for (int i = 0; i < linhas.size(); i++) {
			gGUI.drawString(linhas.get(i), x + 5, y + (20 * (i + 1) - 5));
		}
 	}
	
	public synchronized static void pintarNoJogo(Graphics2D g) {		
		if (!isVisivel) return;
		
		g.setColor(COR_FUNDO);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));
		//g.fillRect(x,JanelaJogo.HEIGHT + y, width, height);
		g.fillRoundRect(x, JanelaJogo.HEIGHT + y, width, height, 7,7);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		g.setColor(Color.BLACK);
		//g.drawRect(x, JanelaJogo.HEIGHT + y, width, height);
		g.drawRoundRect(x, JanelaJogo.HEIGHT + y, width, height, 7, 7);
		
		g.setColor(Color.BLACK);
		g.setFont(font);
		for (int i = 0; i < linhas.size(); i++) {
			g.drawString(linhas.get(i), x + 5, JanelaJogo.HEIGHT + y + (20 * (i + 1) - 5));
		}
 	}
	
	
}
