package wave.item.equip;

import java.awt.image.BufferedImage;

public class MedievalHelmet extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(0, 40, 32, 18);

	public MedievalHelmet(int x, int y) {
		super(x, y, 32, 18, Equip.CABECA);
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
		return "Helmet Medieval";
	}

	@Override
	public String getText() {
		return "Um capacete tirado diretamente do jogo Skyrim. Defesa +2, Vida +20";
	}
	
	public int getDefesa() {
		return 2;
	}
	
	public int getVida() {
		return 20;
	}

}
