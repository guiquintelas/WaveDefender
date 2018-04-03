package wave.item.equip;

import java.awt.image.BufferedImage;

public class MedievalLegs extends Equip {
	private static final BufferedImage img = Equip.armorSheet.getSubimage(70, 34, 21, 28);

	public MedievalLegs(int x, int y) {
		super(x, y, 21, 28, Equip.PERNA);
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
		return "Calças Medievais";
	}

	@Override
	public String getText() {
		return "Na verdade é uma saia que protege pouco, mas tem um tecido ótimo para correr. Defesa +1. Speed +0.2";
	}
	
	public int getDefesa() {
		return 1;
	}
	
	public double getSpeed() {
		return 0.2;
	}

}
