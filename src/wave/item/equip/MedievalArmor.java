package wave.item.equip;

import java.awt.image.BufferedImage;

public class MedievalArmor extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(35, 36, 28, 25);

	public MedievalArmor(int x, int y) {
		super(x, y, 28, 25, Equip.PEITO);
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
		return "Armadura Medieval";
	}

	@Override
	public String getText() {
		return "Feita de material mais resistente, mas também mais pesado. Defesa +4. Speed -0.2. Vida +10";
	}
	
	public int getVida() {
		return 10;
	}
	
	public int getDefesa() {
		return 4;
	}
	
	public double getSpeed() {
		return -.2;
	}

}
