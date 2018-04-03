package wave.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;

import wave.gui.ferramentas.Botao;
import wave.gui.menus.MenuChar;
import wave.gui.menus.MenuLevel;
import wave.gui.status.StatusGUI;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.item.PotionForça;
import wave.item.PotionVida;
import wave.mob.Monstro;
import wave.mob.Player;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.Util;

public class GUI {
	public static final BufferedImage guiSprite = Util.carregarImg("/Sprites/gui.png");
	private static final BufferedImage imgButao = Util.carregarImg("/Sprites/botaoChar.png");
	private static final BufferedImage imgButaoSel = Util.carregarImg("/Sprites/botaoCharSel.png");
	private static final BufferedImage imgButaoApertado = Util.carregarImg("/Sprites/botaoCharApertado.png");

	public static final int WIDTH = JanelaJogo.WIDTH;
	public static final int HEIGHT = 60;

	public static boolean barraSuperior = true;
	
	public static final GUI gui = new GUI();
	

	private static Monstro monstroSelecionado;
	private static Botao botaoChar = new Botao(286, JanelaJogo.HEIGHT + 8, imgButao.getWidth(), imgButao.getHeight(), imgButao, imgButaoSel, imgButaoApertado, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (Principal.menuChar) {
				MenuChar.fecharMenu();
			} else {
				MenuChar.abrirMenu();
			}

		}
	}, 'c');
	
	public static Botao botaoLevel = new Botao(200, JanelaJogo.HEIGHT +9, 65, 20, new Color(110, 110, 110), new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			botaoLevel.piscar(false);
			if (Principal.menuLevel) {
				MenuLevel.fecharMenu();
			} else {
				MenuLevel.abrirMenu();
			}
		}
	}, 'v');
	
	public static void init() {
		botaoLevel.setSombra(new Color(157, 157, 157));
		botaoChar.setSombraFalse();
		MenuLevel.init();
		BarraExp.init();
		
		MouseListener listener = new MouseListener() {
			public void acao(MouseEvent e) {
				Descritivo.updateAll();
			}
		};
		
		ListenerManager.addListener(ListenerManager.MOUSE_DRAG, listener);
		ListenerManager.addListener(ListenerManager.MOUSE_MOVED, listener);
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static void update() {
		checaColisaoMouseMonstro();
		if (!Principal.isPausado())updateStatus();
		BarraExp.update();
		botaoChar.update();
		botaoLevel.update();
		
		if (Principal.menuLevel)MenuLevel.update();
	}

	public static void reset() {
		StatusGUI.clear();
		MenuLevel.reset();
	}

	private static void updateStatus() {
		for (int x = 0; x < StatusGUI.todosStatusGUI.size(); x++) {
			StatusGUI.todosStatusGUI.get(x).update();
		}

	}

	private static void checaColisaoMouseMonstro() {
		for (int x = 0; x < Monstro.todosMontros.size(); x++) {
			Monstro monstroAtual = Monstro.todosMontros.get(x);

			if (monstroAtual.isInvisivel())
				return;

			if (Math.abs(JanelaJogo.xMouse - monstroAtual.getXCentro()) < (monstroAtual.getWidth() / 2)) {
				if (Math.abs(JanelaJogo.yMouse - monstroAtual.getYSpriteCentro()) < (monstroAtual.heightSprite / 2)) {
					monstroSelecionado = monstroAtual;
				}
			}
		}
	}


	public static void pintar(Graphics2D g) {
		BarraExp.pintar(g);
		g.setFont(new Font("Arial", Font.PLAIN, 15));

		if (!PotionVida.primeira) {
			
			g.setColor(Color.LIGHT_GRAY);
			g.drawImage(PotionVida.imgPot, 203, 32, null);
			g.drawString(Integer.toString(PotionVida.quantos), 220, 48);
			
			if (!PotionForça.primeira) {
				g.drawImage(PotionForça.imgPot, 238, 32, null);
				g.drawString("" + PotionForça.quantos, 253, 48);
			}
		}

		g.setColor(Color.WHITE);
		

		//level
		g.drawString("Fase: " + Principal.nivel, 10, 25);
		g.drawString("Monstros: " + Monstro.todosMontros.size(), 10, 50);

		//player
		g.setColor(Player.getPlayer().barraVida.getCor());
		g.drawString("Vida:  " + Player.getPlayer().getVida(), 120, 25);

		g.setColor(new Color(0, 191, 255));
		g.drawString("Mana: " + (int) Player.getPlayer().mana, 120, 50);

		if (Player.getPlayer().isBuffDano()) {
			g.setColor(Color.ORANGE);
		} else {
			g.setColor(Color.WHITE);
		}

		botaoLevel.setText("Level: " + Player.getPlayer().getLevel(), g.getFont(), Color.WHITE);
		botaoLevel.pintar(g);
		//g.setColor(Color.WHITE);
	//	g.drawString("Level: " + Player.getPlayer().getLevel(), 200, 25);

		botaoChar.pintar(g);

		//monstro
		if (monstroSelecionado != null) {
			if (monstroSelecionado.isVivo()) {

				g.setColor(monstroSelecionado.barraVida.getCor());
				g.drawString("Vida: " + monstroSelecionado.getVida(), JanelaJogo.WIDTH - 160, 25);
				g.setColor(Color.WHITE);
				g.drawString("Dano: " + monstroSelecionado.getDano(), JanelaJogo.WIDTH - 160, 50);
				g.drawString("Defesa: " + monstroSelecionado.getDefesa(), JanelaJogo.WIDTH - 80, 25);
				g.drawString("Speed: " + GUI.round(monstroSelecionado.getSpeed(), 2), JanelaJogo.WIDTH - 80, 50);
			}

		}

	}


}
