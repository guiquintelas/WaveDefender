package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class PlateHelmet extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(4, 65, 25, 29);
	private int danoBonus;
	
	private CriadorDeParticulas particulasBrilho;

	public PlateHelmet(int x, int y) {
		super(x, y, 25, 29, Equip.CABEÇA);
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
	public String toString() {
		return "Plate Helmet";
	}
	
	public int getDano() {
		return danoBonus;
	}
	
	@Override
	public int getDefesa() {
		return 3;
	}

	@Override
	public String getText() {
		return "Um helmet forjado com um forte metal. Resistente ele te trasmite uma aura de força. Defesa +3, Dano +" + getDano();
	}
	
	

}
