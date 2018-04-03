package wave.item;

import java.awt.image.BufferedImage;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.gui.menus.MenuMochila;
import wave.mob.Player;
import wave.tools.ActionQueue;
import wave.tools.Util;

public class PotionVida extends Item implements Usavel{
	public static boolean primeira = true;
	public static int quantos = 0;
	public static final BufferedImage imgPot = Util.carregarImg("/Sprites/potion2.png");

	public PotionVida(int x, int y) {
		super(x, y, imgPot.getWidth(), imgPot.getHeight());
	}
	
	protected Luz initLuz() {
		return new Luz(this, 30, 255, 50, 0, 50, 100, true, false, 5, 0);
	}

	@Override
	public void updateResto() {		
	}
	
	public static void reset() {
		quantos = 0;
	}
	
	public String toString() {
		return "Potion de Vida";
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
	
	public BufferedImage getImg() {
		return imgPot;
	}
	
	
	public void usar() {
		Item.drinkSFX.play();
		Player.getPlayer().curar(Player.getPlayer().getVidaMax() / 3);
		Animaçao ani = new Animaçao(Player.getPlayer().getXCentro(), Player.getPlayer().getYCentro(), Animaçao.particulasHealImgs, 3);
		ani.setSeguindo(Player.getPlayer());
		ani.setScale(.45f);
		ani.start();
		
		final Luz luzAni = new Luz(ani, ani.getWidth() - 20, 0, 255, 0, 100, 15, true, true, 0, 0);
		luzAni.forçaVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				luzAni.desativar(15);
				return true;
			}
		});
		
		quantos--;
	}
	
	public String getText() {
		return "Uma poção mágica capaz de regenerar parte de sua vida. Você pode usa-la apertando a tecla 1(Padrão)";
	}
	
	protected void mousePressed() {
		super.mousePressed();
		if (!isNoChao) {
			quantos++;
		}
	}

}
