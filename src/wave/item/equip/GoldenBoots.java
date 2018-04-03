package wave.item.equip;

import java.awt.image.BufferedImage;

import wave.graphics.light.Luz;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.tools.Util;

public class GoldenBoots extends Equip {
	private static final BufferedImage img = Equip.armorSheet.getSubimage(97, 131, 30, 27);
	private int randVida;
	private int randDano;
	private double randSpeed;
	
	private CriadorDeParticulas particulasBrilho;

	public GoldenBoots(int x, int y) {
		super(x, y, 30, 27, Equip.PE);

		randVida = Util.randomInt(2, 5) * 5;
		randSpeed = -Util.randomInt(1, 3)/10.0;
		randDano = Util.randomInt(2, 4);
		
		particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
	}
	
	protected Luz initLuz() {
		return new Luz(this, 30, 255, 255, 0, 50, 100, true, false, 5, 0);
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
	public int getDefesa() {
		return 3;
	}
	
	@Override
	public double getSpeed() {
		return randSpeed;
	}
	
	@Override
	public int getDano() {
		return randDano;
	}
	
	@Override
	public int getVida() {
		return randVida;
	}

	@Override
	public String toString() {
		return "Golden Boots";
	}

	@Override
	public String getText() {
		return "Você é tão foda que pisa em ouro puro. Defesa +3, Speed " + getSpeed() + ", Vida +" + getVida() + ", Dano +" + getDano();
	}

}
