package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class PlateBoots extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(99, 67, 26, 28);
	private int danoBonus;
	
	private CriadorDeParticulas particulasBrilho;
	
	public PlateBoots(int x, int y) {
		super(x, y, 26, 28, Equip.PE);
		danoBonus = Util.randomInt(1, 3);
		
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
	
	@Override
	public int getDano() {
		return danoBonus;
	}
	
	public int getDefesa() {
		return 2;
	}

	@Override
	public String toString() {
		return "Plate Boots";
	}

	@Override
	public String getText() {
		return "Botas feitas para pisar na cara dos inimigos!        Defesa +2, Dano +" + getDano();
	}

}
