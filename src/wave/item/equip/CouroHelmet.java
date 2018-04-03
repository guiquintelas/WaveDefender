package wave.item.equip;

import java.awt.image.BufferedImage;

public class CouroHelmet extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(0, 0, 32, 32);

	public CouroHelmet(int x, int y) {
		super(x, y, 32, 32, Equip.CABE�A);
		sombra.setHeightOff(-8);
		sombra.setWidthOff(-9);
		sombra.setYOff(-5);
	}

	@Override
	public BufferedImage getImg() {
		return img;
	}

	@Override
	protected void updateResto() {	
	}

	@Override
	public String toString() {
		return "Chap�u de Couro";
	}

	@Override
	public String getText() {
		return "Um chap�u simples e confortavel. Defesa +2.";
	}
	
	public int getDefesa() {
		return 2;
	}
}
