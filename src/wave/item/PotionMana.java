package wave.item;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class PotionMana extends Item implements Usavel{
	public static final BufferedImage imgPot = Util.carregarImg("/Sprites/potionMana.png");

	public PotionMana(int x, int y) {
		super(x, y, imgPot.getWidth(), imgPot.getHeight());
	}

	@Override
	public void updateResto() {
		
	}
	
	public String toString() {
		return "Potion de Mana";
	}
	
	public BufferedImage getImg() {
		return imgPot;
	}
	
	
	public String getText() {
		return "Uma poção bem forte que regenera uma parte de sua mana.";
	}

	@Override
	public void usar() {
		// TODO Auto-generated method stub
		
	}

}
