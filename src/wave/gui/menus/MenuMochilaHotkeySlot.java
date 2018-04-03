package wave.gui.menus;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import wave.graphics.BalaoDeFala;
import wave.item.Usavel;
import wave.mob.Player;
import wave.principal.Dimensional;

public class MenuMochilaHotkeySlot extends Dimensional {
	private static final Font font = new Font("Modern", Font.PLAIN, 13);
	private static final int LADO = 30;
	private Usavel usavel;
	public int keycode;
	
	public MenuMochilaHotkeySlot(int x, int y, int keycode) {
		this.x = x;
		this.y = y;
		this.keycode = keycode;
	}
	
	public void usar() {
		if (usavel != null && Player.getPlayer().mochila != null) {	
			for (int x = 0; x < Player.getPlayer().mochilaItens.size(); x++) {
				if (Player.getPlayer().mochilaItens.get(x) != null &&
						Player.getPlayer().mochilaItens.get(x).toString().equals(usavel.toString())) {
					((Usavel) Player.getPlayer().mochilaItens.get(x)).usar();
					Player.getPlayer().mochilaItens.put(x, null);
					return;
				}
			}
			
			new BalaoDeFala("Eu estou sem " + usavel.toString(), Player.getPlayer(), 100);
			Player.semMana.play();
		}
	}
	
	public void setUsavel(Usavel usavel) {
		this.usavel = usavel;
	}
	
	public boolean isUsavelNull() {
		return usavel == null;
	}
	
	public void pintar(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(getX(), getY() , LADO, LADO);
		g.setColor(Color.WHITE);
		g.drawRect(getX(), getY(), LADO, LADO);
		
		if (usavel != null) {
			g.drawImage(usavel.getImg(),getX() + LADO/2 - usavel.getImg().getWidth()/2,  getY() + LADO/2 - usavel.getImg().getHeight()/2, null);
		}
		g.setFont(font);
		g.drawString(Integer.toString(keycode - 48), getX() + LADO - 7, getY() + LADO - 2);
		
	}
	
}
