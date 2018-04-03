package wave.item.equip;

import java.awt.image.BufferedImage;

public class MedievalBotas extends Equip {
	private static final BufferedImage img = Equip.armorSheet.getSubimage(96, 32, 32, 32);

	public MedievalBotas(int x, int y) {
		super(x, y, 32, 32, Equip.PE);
		sombra.setWidthOff(-4);
		sombra.setYOff(-3);
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
		return "Botas Medievais";
	}
	
	public int getDefesa() {
		return 3;
	}

	@Override
	public String getText() {
		return "Botas feitas de material resistente, capaz de receber muitos golpes. Defesa +2";
	}

}
