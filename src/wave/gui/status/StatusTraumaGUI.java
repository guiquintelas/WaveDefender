package wave.gui.status;

import java.awt.image.BufferedImage;

import wave.mob.Player;
import wave.tools.Util;

public class StatusTraumaGUI extends StatusGUI{

	public StatusTraumaGUI(int duraçao, Player player) {
		super(duraçao, "Trauma");
	}
	
	public void update() {
		checaStatus();
		super.update();
	}

	private void checaStatus() {
		if (!Player.getPlayer().isTrauma()) {
			todosStatusGUI.remove(this);
			
		}		
	}

	@Override
	protected BufferedImage carregarImg() {	
		return Util.carregarImg("/Sprites/traumaGUI.png");
	}

	@Override
	public String getDescriçao() {
		return "TRAUMATIZADO: Voce tomou dano recentemente, nao podendo assim regenerar passivamente.";
	}

}
