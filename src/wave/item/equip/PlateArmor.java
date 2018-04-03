package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class PlateArmor extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(33, 67, 30, 28);
	private int vidaBonus;
	private double speedDebuff;
	
	private CriadorDeParticulas particulasBrilho;

	public PlateArmor(int x, int y) {
		super(x, y, 30, 28, Equip.PEITO);
		sombra.setWidthOff(-10);
		
		vidaBonus = Util.randomInt(1, 3) * 10;
		speedDebuff = Util.randomInt(2, 5) / 10.0;
		
		particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
	}

	@Override
	public BufferedImage getImg() {
		return img;
	}

	@Override
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
	

	@Override
	public String toString() {
		return "Plate Armor";
	}
	
	public int getVida() {
		return vidaBonus;
	}
	
	public double getSpeed() {
		return -speedDebuff;
	}
	
	public int getDefesa() {
		return 5;
	}

	@Override
	public String getText() {
		return "Armadura pesada mas muito resistente. Defesa +5,     Speed " + getSpeed() + ", Vida +" + getVida();
	}

}
