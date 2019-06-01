package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.mob.Player;
import wave.tools.Util;

public class StatusLentoGUI extends StatusGUI {

	public StatusLentoGUI(int duracao) {
		super(duracao, "Lento");
	}

	public void update() {
		checaStatus();
		super.update();
	}

	private void checaStatus() {
		if (!Player.getPlayer().isLento()) {
			todosStatusGUI.remove(this);
		}
	}

	@Override
	protected BufferedImage carregarImg() {
		return Util.carregarImg("/Sprites/lento.png");
	}

	@Override
	public String getDescricao() {
		return "LENTO: Voce foi atingido com uma magia, perdendo " + Player.getPlayer().getForcaPorcentagemLento() + "% de speed.";
	}
}
