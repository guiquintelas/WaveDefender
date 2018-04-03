package wave.gui.menus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import wave.gui.GUI;
import wave.mob.Player;
import wave.principal.Dimensional;
import wave.principal.Principal;
import wave.tools.Util;

public class MenuChar extends Dimensional{
	private static final Font font = new Font("Bookman Old Style", Font.PLAIN, 15);
	private static final Font font2 = new Font("Bookman Old Style", Font.PLAIN, 13);
	
	private static final int WIDTH = 130;
	private static final int HEIGHT = 135;
	
	private static final int x = 5;
	private static final int y = 180;
	
	public static void abrirMenu() {
		if (!Principal.menuChar) {
			
			Principal.menuChar = true;
		}
	}
	
	public static void fecharMenu() {
		if (Principal.menuChar) {
			
			Principal.menuChar = false;
		}
	}
	
	public static void pintar(Graphics2D g) {
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, WIDTH, HEIGHT, 10, 10);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Atributos", x + 30, y + 15);
		g.setFont(font2);
		
		if (Player.getPlayer().isBuffDano()) {
			g.setColor(Color.ORANGE);
		} else {
			g.setColor(Color.WHITE);
		}
		
		g.drawString("Level: ", x + 5, y + 40);
		g.drawString("" + Player.getPlayer().getLevel(), x + WIDTH - 3 - Util.getStringWidh("" + Player.getPlayer().getLevel(), font), y + 40);
		
		g.drawString("Exp Total: ", x + 5, y + 55);	
		g.drawString("" + Player.getPlayer().getExpTotal(), x + WIDTH - 3 - Util.getStringWidh("" + Player.getPlayer().getExpTotal(), font), y + 55);
		
		g.drawString("Dano: ", x + 5, y + 70);
		g.drawString("" + Player.getPlayer().getDano(), x + WIDTH - 3 - Util.getStringWidh("" + Player.getPlayer().getDano(), font), y + 70);
		g.setColor(Color.WHITE);
		
		g.drawString("Vel Atk: ", x + 5, y+ 85);	
		g.drawString("" + GUI.round(Player.getPlayer().getVelAtk(), 1), x + WIDTH - 3 - Util.getStringWidh("" + GUI.round(Player.getPlayer().getVelAtk(), 1), font), y + 85);
		
		g.setColor(Color.WHITE);
		if (Player.getPlayer().isLento()) {
			g.setColor(Color.RED);			
		}
		
		g.drawString("Speed: ", x + 5, y + 100);
		g.drawString("" + GUI.round(Player.getPlayer().getSpeed(), 2), x + WIDTH - 3 - Util.getStringWidh("" + GUI.round(Player.getPlayer().getSpeed(), 2), font), y + 100);
		
		g.drawString("Mana Regen: ", x + 5, y + 115);
		g.drawString("" + Player.getPlayer().getManaRegen(), x + WIDTH - 3 - Util.getStringWidh("" + Player.getPlayer().getManaRegen(), font), y + 115);
		
		g.drawString("Defesa: ", x + 5, y + 130);
		g.drawString("" + Player.getPlayer().getDefesa(), x + WIDTH - 3 - Util.getStringWidh("" + Player.getPlayer().getDefesa(), font), y + 130);
	}
	
}
