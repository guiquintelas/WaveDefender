package wave.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import wave.graphics.Grafico;
import wave.graphics.light.Luz;
import wave.graphics.light.LuzBruta;
import wave.gui.menus.MenuLevel;
import wave.gui.menus.MenuMochila;
import wave.gui.menus.MenuOpcoes;
import wave.input.KeyListener;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.map.Tile;
import wave.map.TileMap;
import wave.mob.Player;
import wave.tools.Util;

@SuppressWarnings("unused")
public class JanelaJogo {
	public static JFrame janela;
	public static final int WIDTH = 960;
	public static final int HEIGHT = 480;
	public static double xMouse;
	public static double yMouse;
	public static Dimensional mouse;
	private Principal principal;

	public JanelaJogo(String titulo, JPanel tela, final Principal principal) {
		this.principal = principal;
		mouse = new Dimensional() {};
		//new Luz(mouse, 250, 255, 255, 255, 100, 50);
		//new LuzBruta(mouse, 150, 255, 255, 255, 100, 50);
		
		janela = new JFrame(titulo);

		janela.setResizable(false);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setContentPane(tela);
		janela.pack();
		janela.setVisible(true);
		janela.setLocationRelativeTo(null);
		janela.setFocusable(true);
		janela.setIconImage(Util.carregarImg("/Sprites/icon.png"));

	}

	public void init() {
		ListenerManager.addListener(ListenerManager.KEY_PRESSED, new KeyListener() {
			public void acao(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (!Principal.isRodando) {
						principal.novoJogo();
					} else if (Principal.isNivelFinalizado && !Principal.menuLevel) {
						principal.novoNivel();
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_B) {
					if (Principal.menuMochila) {
						MenuMochila.fecharMenu();
					} else {
						MenuMochila.abrirMenu();
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_T) {
					Principal.editorTiles = !Principal.editorTiles;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_M) {
					Principal.monstrosParados = !Principal.monstrosParados;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_L) {
					Principal.light = !Principal.light;
					Luz.setLuzesAtivas(Principal.light);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_F) {
					Principal.showFPS = !Principal.showFPS;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_G) {
					Principal.grafico = !Principal.grafico;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_X && Principal.grafico) {
					Grafico.clear();
				}
				
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (!Principal.menuOpçoes() && !Principal.menuChar && !Principal.menuMochila && !Principal.menuLevel){
						Principal.setMenuOpçoes(true);
					} else {
						if (Principal.menuChar)Principal.menuChar = false;
						if (Principal.menuMochila) MenuMochila.fecharMenu();
						if (Principal.menuLevel) MenuLevel.fecharMenu();
						if (Principal.menuOpçoes()) Principal.setMenuOpçoes(false);
					}		
				}
				
			}
		});
		
		ListenerManager.addListener(ListenerManager.MOUSE_MOVED, new MouseListener() {
			public void acao(MouseEvent e) {
				xMouse = e.getX();
				yMouse = e.getY();
				mouse.setX(xMouse);
				mouse.setY(yMouse);				
			}
		});
		
		ListenerManager.addListener(ListenerManager.MOUSE_DRAG, new MouseListener() {
			public void acao(MouseEvent e) {
				xMouse = e.getX();
				yMouse = e.getY();
				
				mouse.setX(xMouse);
				mouse.setY(yMouse);

				if (Principal.editorTiles && yMouse < HEIGHT) {
					TileMap.tileMap[(int) Math.floor(JanelaJogo.xMouse / Tile.WIDTH)][(int) Math.floor(JanelaJogo.yMouse / Tile.HEIGHT)].setWall();
				}
				
			}
		});
		
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, new MouseListener() {			
			public void acao(MouseEvent e) {
				//para corrigir o problema de abrir a janela e o keyListerner nao inicializar
				janela.requestFocus();
				
				if (Principal.editorTiles && yMouse < HEIGHT) {
					TileMap.tileMap[(int) Math.floor(JanelaJogo.xMouse / Tile.WIDTH)][(int) Math.floor(JanelaJogo.yMouse / Tile.HEIGHT)].setWall();
					if (e.getButton() == 3) {
						TileMap.tileMap[(int) Math.floor(JanelaJogo.xMouse / Tile.WIDTH)][(int) Math.floor(JanelaJogo.yMouse / Tile.HEIGHT)].setAir();
					}
				}

				
			}
		});
		
	}
}
