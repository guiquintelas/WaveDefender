package wave.principal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import wave.graphics.BalaoDeFala;
import wave.graphics.ExpValorGanhaAnimaçao;
import wave.graphics.Grafico;
import wave.graphics.ModValorAnimaçao;
import wave.graphics.NovoStatusAnimaçao;
import wave.graphics.Sangue;
import wave.graphics.animaçao.Animaçao;
import wave.graphics.animaçao.AnimaçaoInterface;
import wave.graphics.imgprocessing.Filter;
import wave.graphics.light.Luz;
import wave.graphics.light.LuzAmbiente;
import wave.graphics.light.LuzBruta;
import wave.gui.BarrasSuperiores;
import wave.gui.DescriçaoBox;
import wave.gui.DisplayNomes;
import wave.gui.GUI;
import wave.gui.menus.MenuChar;
import wave.gui.menus.MenuLevel;
import wave.gui.menus.MenuMochila;
import wave.gui.menus.MenuOpcoes;
import wave.gui.status.StatusGUI;
import wave.map.TileMap;
import wave.mob.Mob;
import wave.mob.Monstro;
import wave.particula.Particula;
import wave.pathfinding.PathFinding;
import wave.projetil.Projetil;
import wave.tools.Util;

@SuppressWarnings("serial")
public class Tela extends JPanel implements Runnable {
	private int FPS = 0;
	private static BufferedImage imgJogo;
	private static Graphics2D g;
	private BufferedImage imgJogoPadrao;
	private Graphics2D gP;
	private BufferedImage imgGUI;
	private Graphics2D gGUI;
	private static BufferedImage imgPausa;
	public boolean isInit = false;
	private Object estabilizador = new Object();
	private int fps;
	
	public static Thread thread;
	
	PathFinding pf= new PathFinding();

	public Tela() {
		this.setOpaque(false);
		this.setLayout(null);
		// this.setFocusable(true);
		setMaximumSize(new Dimension(JanelaJogo.WIDTH, JanelaJogo.HEIGHT + GUI.HEIGHT));
		setMinimumSize(new Dimension(JanelaJogo.WIDTH, JanelaJogo.HEIGHT + GUI.HEIGHT));
		setPreferredSize(new Dimension(JanelaJogo.WIDTH, JanelaJogo.HEIGHT + GUI.HEIGHT));

		imgJogo = new BufferedImage(JanelaJogo.WIDTH, JanelaJogo.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D) imgJogo.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		imgJogoPadrao = new BufferedImage(JanelaJogo.WIDTH, JanelaJogo.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		gP = (Graphics2D) imgJogoPadrao.getGraphics();
		gP.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		resetarImgPadrao();

		imgGUI = new BufferedImage(JanelaJogo.WIDTH, GUI.HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		gGUI = (Graphics2D) imgGUI.getGraphics();
		gGUI.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		BufferedImage img = Filter.pretoBranco(Util.carregarQuickImg("teste2"));
		Filter.pretoBranco(img);
	}

	private void updateTela() {
		LuzAmbiente.resetImgLuz();
		resetarImg();	
		pintarTutorial();
		pintarSangue();
		pintarTiles();	
		pintarSombras();				
		pintarProjetils();
		pintarEmOrdem();
		
		pintarParticulas();
		pintarAni();	
		DisplayNomes.pintar(g);
		pintarBarraVidas();	
		if (Principal.light && !Principal.dia) {
			pintarLuz();
			LuzAmbiente.pintarLuz(imgJogo);		
		}
		
		pintarLuzBruta();		
		BarrasSuperiores.pintar(g);
		pintarBaloes();
		pintarNovoStatusAni();
		pintarModAni();
		pintarExpGanho();
		
		if (Principal.debugPF) {
			Monstro.todosMontros.get(0).pf.pintarPF(g, gP);
			//pf.pathFind(Player.getPlayer().getXCentro(), Player.getPlayer().getYCentro(), g, gP, Monstro.todosMontros.get(0));
		}
		
	//	pf.pathFind((int)JanelaJogo.xMouse, (int)JanelaJogo.yMouse, Player.getPlayer().getXCentro(), Player.getPlayer().getYSpriteCentro(), g);
		pintarFimNivel();
	}

	private void updateGUI() {
		resetarGUI();
		GUI.pintar(gGUI);
		pintarStatus();
	}

	private void pintarBarraVidas() {
		for (int x = 0; x < Mob.todosMobs.size(); x++) {
			Mob mobAtual = Mob.todosMobs.get(x);
			mobAtual.barraVida.pintarBarra(g);
		}

	}

	private void pintarProjetils() {
		for (int x = 0; x < Projetil.todosProjetils.size(); x++) {
			try {
				Projetil.todosProjetils.get(x).pintar(g);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}

	public void init() {
		thread = new Thread(this);
		thread.start();
		isInit = true;
	}

	private void pintarTutorial() {
		if (Principal.nivel == 1) {
			g.setFont(new Font("Bookman Old Style", Font.BOLD, 20));
			g.setColor(new Color(245, 222, 149));
			g.drawString("WASD movimento", 141, 70);
			g.drawString("ESC menu/pause", JanelaJogo.WIDTH - 141 - 180, 70);
			g.drawString("MOUSE mira/atira", 141, JanelaJogo.HEIGHT - 60);
			g.drawString("E usa armadilha", JanelaJogo.WIDTH - 141 - 180, JanelaJogo.HEIGHT - 60);
		}
	}

	public void pintarGameOver() {
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.setColor(Color.GRAY);
		g.drawString("Game Over, aperte SPACE para um novo jogo.", 141, 221);
		g.setColor(Color.BLACK);
		g.drawString("Game Over, aperte SPACE para um novo jogo.", 140, 220);
		this.repaint();
	}

	public void pintarFimNivel() {
		if (Principal.isNivelFinalizado) {
			g.setFont(new Font("Arial", Font.BOLD, 29));
			g.setColor(Color.YELLOW);
			g.drawString("Parabens, fase completa! Precione SPACE para começar a fase " + Principal.nivel, 41, 221);
			g.setColor(Color.BLACK);
			g.drawString("Parabens, fase completa! Precione SPACE para começar a fase " + Principal.nivel, 40, 220);
		}

	}

	private void pintarPausado() {
		if (Principal.isPausado()) {
			g.drawImage(imgPausa, 0, 0, this);
			
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.setColor(Color.YELLOW);
			g.drawString("PAUSADO", 401, 101);
			g.setColor(Color.BLACK);
			g.drawString("PAUSADO", 400, 100);		
			
		}

	}
	
	public static void criarImgPausa() {
		imgPausa = Filter.gaussianBlur(imgJogo, 5, 1.1);
	}

	public int getFPS() {
		return FPS;
	}

	public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			synchronized (estabilizador) {
						
				if (Principal.isRodando && isInit) {				
					if (!Principal.isPausado())updateTela();
					updateGUI();
			
				}

				if (Principal.isPausado()) {
					pintarPausado();
				}
		
				if (Principal.menuOpçoes()) MenuOpcoes.pintar(Tela.g);
				if (Principal.menuMochila) MenuMochila.pintar(Tela.g);
				if (Principal.menuChar) MenuChar.pintar(Tela.g);
				if (Principal.menuLevel) MenuLevel.pintar(Tela.g);
				pintarDescriçaoBox();
				if (Principal.showFPS) pintarFPSTPS(Tela.g);
				if (Principal.grafico) Grafico.pintar(Tela.g);
			
				g.drawImage(imgJogo, 0, 0, this);
				g.drawImage(imgGUI, 0, JanelaJogo.HEIGHT, this);
				estabilizador.notify();	
				fps++;
				
				if (Principal.isRodando)this.repaint();
			}
	}
	
	public void run() {
		final int OPTICAL_FPS = 60;
		this.repaint();	
		fps = 0;
		long timer = System.currentTimeMillis();
		while (Principal.isRodando) {

			long now = System.currentTimeMillis();
			
				
			
//			synchronized (estabilizador) {
//				try {
//					estabilizador.wait(20);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}

			//fps++;
			
			if (System.currentTimeMillis() - timer >= 1000) {
				FPS = fps;
				Grafico.fps.add(fps);
				fps = 0;
				timer = System.currentTimeMillis();
			}

			long tempoDecorrido = System.currentTimeMillis() - now;
			if (tempoDecorrido > 1000 / OPTICAL_FPS) {
				tempoDecorrido = 1000 / OPTICAL_FPS;
			}
			try {
				// atualiza a 60 FPS
				Thread.sleep(600);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("RENDERER FINALIZADO");
	}

	// pinta os mobs ordenados pelos seus Y, o quanto menor primeiro ele vai ser
	// pintado
	private void pintarEmOrdem() {
		ArrayList<DimensionalObj> dsEmOrdem = new ArrayList<DimensionalObj>();
		ArrayList<DimensionalObj> dsNaoOrdem = new ArrayList<DimensionalObj>();
		//dsNaoOrdem.addAll(Mob.todosMobs);
		//dsNaoOrdem.addAll(Item.todosItens);
		dsNaoOrdem.addAll(DimensionalObj.todosDimensionalObjs);

		for (int y = 0; y < dsNaoOrdem.size(); y++) {
			for (int i = 0; i < dsNaoOrdem.size(); i++) {

				if (dsEmOrdem.contains(dsNaoOrdem.get(i))) {
					continue;
				}

				if (dsEmOrdem.size() == y) {
					dsEmOrdem.add(dsNaoOrdem.get(i));
					continue;
				}
				
				if (dsNaoOrdem.get(i).getY() + dsNaoOrdem.get(i).height < dsEmOrdem.get(y).getY() + dsEmOrdem.get(y).height) {
					dsEmOrdem.set(y, dsNaoOrdem.get(i));
				}
				
			}
		}

		for (int x = 0; x < dsEmOrdem.size(); x++) {
			dsEmOrdem.get(x).pintar(g);
		}
	}
	
	private void pintarFPSTPS(Graphics2D g) {
		g.setFont(new Font("Arial", Font.BOLD, 12));
		if (Principal.light) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.drawString("TPS: " + Principal.getTPS() + "   FPS: " + getFPS(), 5, JanelaJogo.HEIGHT - 10);
	}

	private void pintarModAni() {
		for (int x = 0; x < ModValorAnimaçao.todosModAni.size(); x++) {
			ModValorAnimaçao.todosModAni.get(x).pintar(g);
		}
	}

	private void pintarNovoStatusAni() {
		for (int x = 0; x < NovoStatusAnimaçao.todasStatusAni.size(); x++) {
			NovoStatusAnimaçao.todasStatusAni.get(x).pintar(g);
		}
	}

	private void resetarImg() {
		g.drawImage(imgJogoPadrao, 0, 0, this);
		if (Principal.editorTiles) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("Editor Mode: ON", 5, 15);
		}
		
		if (Principal.monstrosParados) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("Monstros Parados: ON", 5, 30);
		}
	}

	private void pintarStatus() {
		for (int x = 0; x < StatusGUI.todosStatusGUI.size(); x++) {
			StatusGUI.todosStatusGUI.get(x).pintar(gGUI);
		}
	}

	private void pintarDescriçaoBox() {
		
			DescriçaoBox.pintarNaGUI(gGUI);
		

		
			DescriçaoBox.pintarNoJogo(g);
		
	}

	private void pintarParticulas() {
		for (int x = 0; x < Particula.todasParticulas.size(); x++) {
			try {
				Particula.todasParticulas.get(x).pintar(g);
			} catch (NullPointerException e) {
				System.out.println("Erro particulas, particula removida na hora de ser pintada");
			}
			
		}
	}

	private void pintarSombras() {
		for (int x = 0; x < DimensionalObj.todosDimensionalObjs.size(); x++) {
			if (DimensionalObj.todosDimensionalObjs.get(x).sombra != null) {
				DimensionalObj.todosDimensionalObjs.get(x).sombra.pintar(g);
			}

		}
	}

	private void pintarSangue() {
		if (!Sangue.sangueParaPintar.isEmpty()) {
			for (int x = 0; x < Sangue.sangueParaPintar.size(); x++) {
				Sangue.sangueParaPintar.get(x).pintar(gP);
			}
		}
	}

	private void pintarExpGanho() {
		for (int x = 0; x < ExpValorGanhaAnimaçao.todasExps.size(); x++) {
			ExpValorGanhaAnimaçao.todasExps.get(x).pintar(g);
		}
	}

	private void pintarTiles() {
		for (int y = 0; y < TileMap.HEIGHT; y++) {
			for (int x = 0; x < TileMap.WIDTH; x++) {
				TileMap.tileMap[x][y].pintar(g);
			}
		}
	}
	
	private void pintarAni() {
		for (int x = 0; x < Animaçao.todasAni.size(); x++) {
			AnimaçaoInterface.todasAni.get(x).pintar(g);
		}
	}
	
	private void pintarBaloes() {
		for (int x = 0; x < Mob.todosMobs.size(); x++) {
			if (BalaoDeFala.todosBaloes.containsKey(Mob.todosMobs.get(x))) {
				BalaoDeFala.todosBaloes.get(Mob.todosMobs.get(x)).pintar(g);
			}
		}
	}
	
	private void pintarLuz() {
		for (int x = 0; x < Luz.todasLuz.size(); x++) {
			try {
				Luz.todasLuz.get(x).pintar(LuzAmbiente.imgLuz);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void pintarLuzBruta() {
		for (int x = 0; x < LuzBruta.todasLuzBruta.size(); x++) {
			try {
				LuzBruta.todasLuzBruta.get(x).pintar(imgJogo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	private void resetarGUI() {
		gGUI.setColor(Color.GRAY);
		gGUI.fillRect(0, 0, GUI.WIDTH, 5);
		gGUI.drawImage(GUI.guiSprite, 0, 5, this);

	}

	public void resetarImgPadrao() {
		//gP.setColor(Color.BLACK);
		gP.setColor(new Color(222, 184, 135));
		gP.fillRect(0, 0, JanelaJogo.WIDTH, JanelaJogo.HEIGHT);
	}
	
	public static Graphics2D getGraphics2d() {
		return Tela.g;
	}

}
