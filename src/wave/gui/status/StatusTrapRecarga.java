package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class StatusTrapRecarga extends StatusGUI{

	public StatusTrapRecarga(int duracao) {
		super(duracao, "trapRecarga");
	}

	@Override
	protected BufferedImage carregarImg() {
		return Util.carregarImg("/Sprites/trap GUI.png");
	}

	@Override
	public String getDescricao() {
		return "RECARREGANDO ARMADLHA: Armadilha     usada recentemente. espere até que possa usa-la novamente.";
	}

}
