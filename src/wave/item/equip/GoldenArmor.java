package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.graphics.light.Luz;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class GoldenArmor extends Equip {
	private static final BufferedImage img = Equip.armorSheet.getSubimage(33, 133, 31, 22);
	private int randVida;
	private double randSpeed;
	
	private CriadorDeParticulas particulasBrilho;

	public GoldenArmor(int x, int y) {
		super(x, y, 31, 22, Equip.PEITO);
		
		randVida = Util.randomInt(5, 10) * 5;
		randSpeed = Util.randomInt(3, 6)/10.0; 
		
		particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
	}

	@Override
	public BufferedImage getImg() {
		return img;
	}
	
	protected Luz initLuz() {
		return new Luz(this, 30, 255, 255, 0, 50, 100, true, false, 5, 0);
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
		return "Golden Armor";
	}
	
	@Override
	public int getDefesa() {
		return 6;
	}
	
	@Override
	public int getVida() {
		return randVida;
	}
	
	@Override
	public double getSpeed() {
		return -randSpeed;
	}

	@Override
	public String getText() {
		return "Uma armadura de ouro que vai deixar você brilhando. Defesa +6, Speed " + getSpeed() + ", Vida +" + getVida();
	}

}
