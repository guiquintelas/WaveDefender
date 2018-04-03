package wave.item;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class Mochila extends Item implements Container {
	
	private static final BufferedImage imgMochila = Util.carregarImg("/Sprites/backpack.png");
	public Mochila(int x, int y) {
		super(x, y, imgMochila.getWidth(), imgMochila.getHeight());
	}

	

	@Override
	public void updateResto() {
	}
	
	
	
	public int getSize() {
		return 5;
	}

	@Override
	public String toString() {
		return "Mochila[5]";
	}
	
	public String getText() {
		return "";
	}
	
	public BufferedImage getImg() {
		return imgMochila;
	}

}
