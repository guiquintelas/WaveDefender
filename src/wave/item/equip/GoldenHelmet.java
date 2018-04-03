package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.graphics.light.Luz;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class GoldenHelmet extends Equip {
	private static final BufferedImage img = Equip.armorSheet.getSubimage(7, 129, 18, 30);
	private int randDano;
	private int randVida;
	private double randSpeed;
	
	private CriadorDeParticulas particulasBrilho;

	public GoldenHelmet(int x, int y) {
		super(x, y, 18, 30, Equip.CABEÇA);
		
		randDano = Util.randomInt(1, 3);
		randVida = Util.randomInt(3, 7) * 5;
		randSpeed = -Util.randomInt(1, 3)/10.0;
		
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
		return "Golden Helmet";
	}
	
	public int getDefesa() {
		return 3;
	}
	
	@Override
	public int getVida() {
		return randVida;
	}
	
	@Override
	public int getDano() {
		return randDano;
	}
	
	@Override
	public double getSpeed() {
		return randSpeed;
	}

	@Override
	public String getText() {
		return "Um helmet de ouro que vai deixar você brilhando. Defesa +3, Speed " + getSpeed() + ",     Dano +" + getDano() + ", Vida +" + getVida();
	}

}
