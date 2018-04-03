package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class StatusTrapRecarga extends StatusGUI{

	public StatusTrapRecarga(int dura�ao) {
		super(dura�ao, "trapRecarga");
	}

	@Override
	protected BufferedImage carregarImg() {
		return Util.carregarImg("/Sprites/trap GUI.png");
	}

	@Override
	public String getDescri�ao() {
		return "RECARREGANDO ARMADLHA: Armadilha     usada recentemente. espere at� que possa usa-la novamente.";
	}

}
