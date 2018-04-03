package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class PlateLegs extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(71, 67, 19, 27);
	private int vidaBonus;
	
	private CriadorDeParticulas particulasBrilho;

	public PlateLegs(int x, int y) {
		super(x, y, 19, 27, Equip.PERNA);
		vidaBonus = Util.randomInt(1, 4) * 5;
		
		particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
	}

	@Override
	public BufferedImage getImg() {
		return img;
	}

	protected void updateResto() {
		particulasBrilho.update(getX(), getY());
	}
	
	public void removerDoChao() {
		super.removerDoChao();
		particulasBrilho.setProduzindo(false);
	}
	
	public void botarNoChao(int x, int y) {
		super.botarNoChao(x, y);
		particulasBrilho.setProduzindo(true);
	}
	
	public int getVida() {
		return vidaBonus;
	}
	
	public int getDefesa() {
		return 2;
	}

	@Override
	public String toString() {
		return "Plate Legs";
	}

	@Override
	public String getText() {
		return "Isso definitivamente não é uma legs. Defesa + 2, Vida +" + getVida();
	}

}
