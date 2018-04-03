package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.tools.Util;

public class StatusTrapPronta extends StatusGUI{
	
	public StatusTrapPronta() {
		super("trapPronta");
	}

	@Override
	protected BufferedImage carregarImg() {
		return Util.carregarImg("/Sprites/trap GUI.png");
	}

	@Override
	public String getDescriçao() {
		return "ARMADILHA PRONTA: Apertando 'E' você pode colocar uma poderosa trap, causando dano e envenenado quem a pisar.";
	}
}
