package wave.item.equip;

import java.awt.image.BufferedImage;

public class CouroLegs extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(64, 0, 32, 32);

	public CouroLegs(int x, int y) {
		super(x, y, 32, 32, Equip.PERNA);
		sombra.setWidthOff(-14);
		sombra.setYOff(-8);
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
		return "Calças de Couro";
	}

	@Override
	public String getText() {
		return "Uma calça que mais parece saia. Defesa +2.";
	}
	
	
	public int getDefesa() {
		return 2;
	}
}
