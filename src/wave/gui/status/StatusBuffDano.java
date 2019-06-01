package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class StatusBuffDano extends StatusGUI{
	private static final BufferedImage img = Util.carregarImg("/Sprites/fist2.png");

	public StatusBuffDano(int duracao) {
		super(duracao, "Buff Dano");
	}

	@Override
	protected BufferedImage carregarImg() {
		return img;
	}

	@Override
	public String getDescricao() {
		return "BUFF DE DANO: Você possui alguma forma de magia em seu corpo que aumenta sua forca.";
	}

}
