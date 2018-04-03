package wave.item;

import java.awt.image.BufferedImage;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.gui.menus.MenuMochila;
import wave.mob.Player;
import wave.tools.ActionQueue;
import wave.tools.Util;

public class PotionForça extends Item implements Usavel{
	public static boolean primeira = true;
	public static int quantos = 0;
	public static final BufferedImage imgPot = Util.carregarImg("/Sprites/potionForça.png");

	public PotionForça(int x, int y) {
		super(x, y, imgPot.getWidth(), imgPot.getHeight());
	}
	
	protected Luz initLuz() {
		return new Luz(this, 30, 255, 255, 0, 50, 100, true, true, 5, 0);
	} 

	@Override
	public void updateResto() {		
	}
	
	public String toString() {
		return "Potion de Força";
	}
	
	public BufferedImage getImg() {
		return imgPot;
	}
	
	public static void reset() {
		quantos = 0;
	}
	
	public void removerDoChao() {
		super.removerDoChao();
		if (primeira) {
			primeira = false;
			for (int x = 0; x <4; x++) {
				if (MenuMochila.hotkeys.get(x).isUsavelNull()) {
					MenuMochila.hotkeys.get(x).setUsavel(this);
					return;
				}
			}
		}
	}
	
	public void botarNoChao(int x, int y) {
		super.botarNoChao(x, y);
		quantos--;
	}
	
	public void usar() {
		quantos--;
		Item.drinkSFX.play();
		Player.getPlayer().setBuffDano(8 + (Player.getPlayer().getLevel() / 2), 500);
		Animaçao ani = new Animaçao(Player.getPlayer().getXCentro(), Player.getPlayer().getYCentro(), Animaçao.raiosImgs, 4);
		ani.setSeguindo(Player.getPlayer());
		ani.setScale(.45f);
		ani.start();
		
		Player.getPlayer().varGLuz.fadeOutSin(Player.getPlayer().getLuz().getGreen(), 100, 50);
		Player.getPlayer().varGLuz.variar(true);
		Player.getPlayer().varGLuz.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				if (!Player.getPlayer().isBuffDano()) {
					Player.getPlayer().varGLuz.fadeInSin(Player.getPlayer().getLuz().getGreen(), 223, 50);
					Player.getPlayer().varGLuz.variar(true);
					return true;
				}
				return false;
			}
		});
	}
	
	public String getText() {
		return "Uma poção mágica que aumenta seu dano, duração de 10 segundos.";
	}
	
	@Override
	protected void mousePressed() {
		super.mousePressed();
		if (!isNoChao) {
			quantos++;
		}
	}
}
