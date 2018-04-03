package wave.item;

import java.awt.image.BufferedImage;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.animaçao.AnimaçaoListener;
import wave.graphics.light.Luz;
import wave.mob.Player;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;

public class ChestPadrao extends Chest{
	
	private int exp = 0;
	
	private Animaçao ani;
	
	private CriadorDeParticulas particulasBrilho = null;

	public ChestPadrao(int x, int y, String tipo) {
		super(x, y, img.getWidth(), img.getHeight(), tipo);
		
		switch (tipo) {
		case AZUL:
			ani = new Animaçao(getXCentro(), getYCentro(), bauAzImgs, 10);
			particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
			break;
		case PADRAO:
			ani = new Animaçao(getXCentro(), getYCentro(), bauImgs, 10);
			break;
		case VERMELHO:
			ani = new Animaçao(getXCentro(), getYCentro(), bauVerImgs, 10);
			particulasBrilho = CDPProntos.brilho(this, getX(), getY(), width, height);
			break;
		default:
			ani = new Animaçao(getXCentro(), getYCentro(), bauImgs, 10);
			break;
		}
		
		ani.setVoid(3, new AnimaçaoListener() {
			public void metodo() {
				remove();
				dropper.droppar();	
				if (particulasBrilho != null) particulasBrilho.setProduzindo(false);
			}
		});
		ani.setFadeOut(70);
		
		new Luz(this, 35, 255, 255, 255, 70, 50, true, true, 5, 10);
	}
	
	protected void updateResto() {
		if (particulasBrilho != null) particulasBrilho.update(getX(), getY());
	}
	
	public void setExp(int exp) {
		this.exp = exp;
	}

	@Override
	public String toString() {
		return "Baú";
	}

	@Override
	public BufferedImage getImg() {
		if (tipo == null) tipo = PADRAO;
		switch (tipo) {
		case AZUL:
			return imgAz;
		case PADRAO:
			return img;
		case VERMELHO:
			return imgVer;
		default:
			return img;
		}
	}

	@Override
	public void abrir() {
		super.abrir();
		abrirSfx.play();
		if (exp > 0) Player.getPlayer().ganhaExp(exp);
		ani.start();
	}


}
