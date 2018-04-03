package wave.item.equip;

import java.awt.image.BufferedImage;


public class CouroArmor extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(32, 0, 32, 32);

	public CouroArmor(int x, int y) {
		super(x, y, 32, 32, Equip.PEITO);
		sombra.setHeightOff(-5);
		sombra.setWidthOff(-6);
		sombra.setYOff(-2);
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
		return "Armadura de Couro";
	}

	@Override
	public String getText() {
		return "Uma armadura feita da pele de um forte animal. Defesa +3. Speed -0.1";
	}
	
	public double getSpeed() {
		return -.1;
	}
	
	public int getDefesa() {
		return 3;
	}

}
