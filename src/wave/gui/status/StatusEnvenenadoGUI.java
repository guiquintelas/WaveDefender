package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.mob.Mob;
import wave.mob.Player;
import wave.tools.Util;

public class StatusEnvenenadoGUI extends StatusGUI{
	int duraçao;

	public StatusEnvenenadoGUI(int duraçao, Player player) {
		super(duraçao, "Envenenado");
		this.duraçao = duraçao;
	}

	public void update() {
		checaStatus();
		super.update();
	}
	
	public String getDescriçao() {
		return "ENVENENADO: Voce foi envenenado e vai sofrer dano " + (duraçao / Mob.DELAY_VENENO_TICK) + " vezes";
	}

	private void checaStatus() {
		if (!Player.getPlayer().isEnvenenado()) {
			todosStatusGUI.remove(this);
		}
	}

	@Override
	protected BufferedImage carregarImg() {
		return Util.carregarImg("/Sprites/veneno.png");
	}

}
