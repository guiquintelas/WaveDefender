package wave.item;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class MochilaAdv extends Item implements Container {

	private static final BufferedImage imgMochila = Util.carregarImg("/Sprites/mochilaAdv.png");

	public MochilaAdv(int x, int y) {
		super(x, y, imgMochila.getWidth(), imgMochila.getHeight());
	}

	@Override
	public void updateResto() {
	}

	public int getSize() {
		return 7;
	}

	@Override
	public String toString() {
		return "MochilaAdv[7]";
	}

	public BufferedImage getImg() {
		return imgMochila;
	}
	
	
	public String getText() {
		return "";
	}

}
