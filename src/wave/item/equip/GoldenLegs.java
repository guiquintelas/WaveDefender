package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.graphics.light.Luz;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class GoldenLegs extends Equip{
	private static final BufferedImage img = Equip.armorSheet.getSubimage(67, 134, 26, 21);
	private int randVida;
	private double randSpeed;
	
	private CriadorDeParticulas particulasBrilho;

	public GoldenLegs(int x, int y) {
		super(x, y, 26, 21, Equip.PERNA);
		
		randVida = Util.randomInt(3, 6) * 5;
		randSpeed = -Util.randomInt(1, 4)/10.0;
		
		particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
	}
	
	protected Luz initLuz() {
		return new Luz(this, 30, 255, 255, 0, 50, 100, true, false, 5, 0);
	}

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
	public int getDefesa() {
		return 3;
	}
	
	@Override
	public int getVida() {
		return randVida;
	}
	
	@Override
	public double getSpeed() {
		return randSpeed;
	}

	@Override
	public String toString() {
		return "Golden Legs";
	}

	@Override
	public String getText() {
		return "Uma legs feita de puro ouro. Defesa +3, Speed " + getSpeed() + ",      Vida +" + getVida();
	}

}
