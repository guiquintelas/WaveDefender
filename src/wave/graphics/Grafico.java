package wave.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.principal.JanelaJogo;
import wave.principal.Principal;

public class Grafico {
	public static ArrayList<Integer> particulas = new ArrayList<Integer>();
	public static ArrayList<Integer> ani = new ArrayList<Integer>();
	public static ArrayList<Integer> vidaPlayer = new ArrayList<Integer>();
	public static ArrayList<Integer> exp = new ArrayList<Integer>();
	public static ArrayList<Integer> mobs = new ArrayList<Integer>();
	public static ArrayList<Integer> objs = new ArrayList<Integer>();
	public static ArrayList<Integer> fps = new ArrayList<Integer>();
	private static ArrayList<Integer> listaSel = particulas;
	

	public static void init() {
		particulas.add(0);
		ani.add(0); 
		vidaPlayer.add(0); 
		exp.add(0); 
		mobs.add(0); 
		objs.add(0); 
		fps.add(0);
	}
	
	public static void clearAll() {
		particulas.clear();
		ani.clear(); 
		vidaPlayer.clear();
		exp.clear();
		mobs.clear();
		objs.clear();
		fps.clear();
	}
	
	public static void pintar(Graphics2D g) {	
		Color cor;
		if (Principal.dia || !Principal.light) {
			cor = Color.BLACK;
		} else {
			cor = Color.WHITE;
		}
		g.setColor(cor);
		
		g.setFont(new Font("arial", Font.PLAIN, 10));
		
		int listaMaior = getMaior(listaSel);
		
		if (listaMaior < 50) {
			pintarGrafico(8, g);
		} else if (listaMaior >= 50 && listaMaior < 100) {
			pintarGrafico(4, g);
		} else if (listaMaior >= 100 && listaMaior < 200) {
			pintarGrafico(2, g);
		} else if (listaMaior >= 200 && listaMaior < 400) {
			pintarGrafico(1, g);
		} else if (listaMaior >= 400 && listaMaior < 800){
			pintarGrafico(.5f, g);
		} else if (listaMaior >= 800 && listaMaior < 1600){
			pintarGrafico(.25f, g);
		} else if (listaMaior >= 1600 && listaMaior < 3200){
			pintarGrafico(.125f, g);
		} else {
			pintarGrafico(.0625f, g);
		}
		
		g.drawLine(150, 50, 150, JanelaJogo.HEIGHT - 30);
		g.drawLine(150, JanelaJogo.HEIGHT - 30, JanelaJogo.WIDTH - 30, JanelaJogo.HEIGHT - 30);
		
		if (listaSel == fps) g.setColor(Color.YELLOW);
		g.drawString("Frames por Segundo", 30, JanelaJogo.HEIGHT/2 -120);
		g.setColor(cor);
		
		if (listaSel == mobs) g.setColor(Color.YELLOW);
		g.drawString("Mobs", 30, JanelaJogo.HEIGHT/2 - 80);
		g.setColor(cor);
		
		if (listaSel == objs) g.setColor(Color.YELLOW);
		g.drawString("Objetos", 30, JanelaJogo.HEIGHT/2 -40);
		g.setColor(cor);
		
		if (listaSel == ani) g.setColor(Color.YELLOW);
		g.drawString("Animações", 30, JanelaJogo.HEIGHT/2);
		g.setColor(cor);
		
		if (listaSel == exp) g.setColor(Color.YELLOW);
		g.drawString("Experência Total", 30, JanelaJogo.HEIGHT/2 + 40);
		g.setColor(cor);
		
		if (listaSel == vidaPlayer) g.setColor(Color.YELLOW);
		g.drawString("Vida Player", 30, JanelaJogo.HEIGHT/2 + 80);
		g.setColor(cor);
		
		if (listaSel == particulas) g.setColor(Color.YELLOW);
		g.drawString("Particulas", 30, JanelaJogo.HEIGHT/2 + 120);	
		g.setColor(cor);
		
		g.setFont(new Font("arial", Font.PLAIN, 14));
		g.drawString("Apertando X você limpa o grafico selecionado.", JanelaJogo.WIDTH/2 - 100, JanelaJogo.HEIGHT - 10);

	}

	public static synchronized void update() {
		if (JanelaJogo.xMouse > 150) return;
		
		if (JanelaJogo.yMouse < JanelaJogo.HEIGHT/2 -120) {
			listaSel = fps;
		} else if (JanelaJogo.yMouse > JanelaJogo.HEIGHT/2 -110 && JanelaJogo.yMouse < JanelaJogo.HEIGHT/2 -70) {
			listaSel = mobs;
		} else if (JanelaJogo.yMouse > JanelaJogo.HEIGHT/2 -70 && JanelaJogo.yMouse < JanelaJogo.HEIGHT/2 -30) {
			listaSel = objs;
		} else if (JanelaJogo.yMouse > JanelaJogo.HEIGHT/2 -30 && JanelaJogo.yMouse < JanelaJogo.HEIGHT/2 + 10) {
			listaSel = ani;
		} else if (JanelaJogo.yMouse > JanelaJogo.HEIGHT/2 + 10 && JanelaJogo.yMouse < JanelaJogo.HEIGHT/2 +50) {
			listaSel = exp;
		} else if (JanelaJogo.yMouse > JanelaJogo.HEIGHT/2 +50 && JanelaJogo.yMouse < JanelaJogo.HEIGHT/2 +90) {
			listaSel = vidaPlayer;
		} else if (JanelaJogo.yMouse > JanelaJogo.HEIGHT/2 +90) {
			listaSel = particulas;
		}
			
	}

	public static void clear() {
		listaSel.clear();
		listaSel.add(0);
	}
	
	private static int getMaior(ArrayList<Integer> lista) {
		int maior = 0;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i) > maior) maior = lista.get(i);
		}

		return maior;
	}
	
	private static synchronized void pintarGrafico(float scale, Graphics2D g) {
		if (listaSel.size() != 0) {
			float tam = listaSel.size();
			int index = 0;
			int xAnt = 0, yAnt = 0;
			
			for (float i = 150; i < JanelaJogo.WIDTH - 30 && listaSel.size() > index; i += (JanelaJogo.WIDTH - 30 - 150) / tam) {
				if (xAnt != 0) {
					g.drawLine((int) i, (int) (450 - listaSel.get(index) * scale), xAnt, yAnt);
					
				}
				
				xAnt = (int)i;
				yAnt = (int) (450 - listaSel.get(index) * scale);
				index++;

			}
		}

		for (int i = 450; i >= 50; i -= 20) {
			if (i % 50 == 0) {
				g.drawLine(140, i, 160, i);
				g.drawString((int)((450 - i) / scale) + "", 120, i + 4);
			} else {
				g.drawLine(145, i, 155, i);
			}

		}
	}
}
