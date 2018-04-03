package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class StatusBuffDano extends StatusGUI{
	private static final BufferedImage img = Util.carregarImg("/Sprites/fist2.png");

	public StatusBuffDano(int dura�ao) {
		super(dura�ao, "Buff Dano");
	}

	@Override
	protected BufferedImage carregarImg() {
		return img;
	}

	@Override
	public String getDescri�ao() {
		return "BUFF DE DANO: Voc� possui alguma forma de magia em seu corpo que aumenta sua for�a.";
	}

}
