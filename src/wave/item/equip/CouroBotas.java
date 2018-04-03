package wave.item.equip;

import java.awt.image.BufferedImage;

public class CouroBotas extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(96, 0, 32, 32);

	public CouroBotas(int x, int y) {
		super(x, y, 32, 32, Equip.PE);
		sombra.setWidthOff(-4);
		sombra.setYOff(-3);
	}

	protected void updateResto() {
	}
	
	public int getDefesa() {
		return 1;
	}

	@Override
	public String toString() {
		return "Botas de Couro";
	}

	@Override
	public String getText() {
		return "Botas de couro que protegem o seu pé. Defesa +1";
	}

	@Override
	public BufferedImage getImg() {
		return img;
	}

}
