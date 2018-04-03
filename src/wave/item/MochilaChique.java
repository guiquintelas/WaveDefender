package wave.item;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class MochilaChique extends Item implements Container{
	private static final BufferedImage imgMochila = Util.carregarImg("/Sprites/bpChique.png");

	public MochilaChique(int x, int y) {
		super(x, y, imgMochila.getWidth(), imgMochila.getHeight());
	}

	@Override
	public void updateResto() {
	}

	public int getSize() {
		return 10;
	}

	@Override
	public String toString() {
		return "Mochila Chique[10]";
	}

	public BufferedImage getImg() {
		return imgMochila;
	}
	
	
	public String getText() {
		return "";
	}
}
