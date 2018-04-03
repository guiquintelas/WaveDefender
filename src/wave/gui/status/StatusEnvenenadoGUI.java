package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.mob.Mob;
import wave.mob.Player;
import wave.tools.Util;

public class StatusEnvenenadoGUI extends StatusGUI{
	int dura�ao;

	public StatusEnvenenadoGUI(int dura�ao, Player player) {
		super(dura�ao, "Envenenado");
		this.dura�ao = dura�ao;
	}

	public void update() {
		checaStatus();
		super.update();
	}
	
	public String getDescri�ao() {
		return "ENVENENADO: Voce foi envenenado e vai sofrer dano " + (dura�ao / Mob.DELAY_VENENO_TICK) + " vezes";
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
