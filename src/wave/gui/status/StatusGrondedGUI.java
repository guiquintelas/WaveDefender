package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.mob.Player;
import wave.tools.Util;

public class StatusGrondedGUI extends StatusGUI{

	public StatusGrondedGUI(int duracao) {
		super(duracao, "Grounded");
	}

	public void update() {
		checaStatus();
		super.update();
	}

	private void checaStatus() {
		if (!Player.getPlayer().isGrounded()) {
			todosStatusGUI.remove(this);
		}
	}

	@Override
	protected BufferedImage carregarImg() {	
		return Util.carregarImg("/Sprites/groundedGUI.png");
	}

	@Override
	public String getDescricao() {
		return "GROUNDED: Voce foi atingido e nao pode se mexer.";
	}

}
