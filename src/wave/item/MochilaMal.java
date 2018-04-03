package wave.item;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class MochilaMal extends Item implements Container{
	private static final BufferedImage imgMochila = Util.carregarImg("/Sprites/bpMal.png");

	public MochilaMal(int x, int y) {
		super(x, y, imgMochila.getWidth(), imgMochila.getHeight());
	}

	@Override
	public void updateResto() {
	}

	public int getSize() {
		return 15;
	}

	@Override
	public String toString() {
		return "Mochila do Mal[15]";
	}

	public BufferedImage getImg() {
		return imgMochila;
	}
	
	
	public String getText() {
		return "";
	}
}
