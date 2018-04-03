package wave.gui.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import wave.item.equip.Equip;
import wave.mob.Player;
import wave.tools.Util;

public class MenuMochilaEquipSlot extends MenuMochilaSlot{
	private static final BufferedImage imgCabeça = Util.carregarImg("/Sprites/equipCabeça.png");
	private static final BufferedImage imgPeito = Util.carregarImg("/Sprites/equipPeito.png");
	private static final BufferedImage imgPerna = Util.carregarImg("/Sprites/equipPerna.png");
	private static final BufferedImage imgPe = Util.carregarImg("/Sprites/equipPe.png");
	
	private static final int LADO = 30;
	
	private static final HashMap<String, BufferedImage> imgs = carregarHashMap();
	private static int quantos = 0;
	public Equip equip;
	public String id;
	
	private static HashMap<String, BufferedImage> carregarHashMap() {
		HashMap<String, BufferedImage> hash = new HashMap<String, BufferedImage>();
		hash.put(Equip.CABEÇA, imgCabeça);
		hash.put(Equip.PEITO, imgPeito);
		hash.put(Equip.PERNA, imgPerna);
		hash.put(Equip.PE, imgPe);
		
		return hash;
	}
	
	
	
	public MenuMochilaEquipSlot(int x, int y, int index) {
		super(x, y, index);
		quantos++;
		this.x = x;
		this.y = y;
		
		switch (quantos) {
		case 1:
			this.id = Equip.CABEÇA;
			break;

		case 2:
			this.id = Equip.PEITO;
			break;
			
		case 3:
			this.id = Equip.PERNA;
			break;
			
		case 4:
			this.id = Equip.PE;
			break;
		}
		
		if (quantos == 4) {
			quantos = 0;
		}
		
	}
	
	public void setEquip(Equip equip) {
		if (this.equip == null && equip.getID() == id) {
			this.equip = equip;
			this.item = equip;
			Player.getPlayer().equipar(equip);
		}
	}
	
	public void removeEquip() {
		if (equip != null) {
			Player.getPlayer().desequipar(equip.getID());
			equip = null;
			item = null;
		}
	}
	
	public void pintar(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(getX(), getY() , LADO, LADO);
		
		
		if (equip != null) {
			g.drawImage(equip.getImg(),getX() + LADO/2 - equip.getImg().getWidth()/2,  getY() + LADO/2 - equip.getImg().getHeight()/2, null);
		} else {
			g.drawImage(imgs.get(id) ,getX() + LADO/2 - imgs.get(id).getWidth()/2,  getY() + LADO/2 - imgs.get(id).getHeight()/2, null);
		}
		
		g.setColor(Color.WHITE);
		g.drawRect(getX(), getY(), LADO, LADO);
		
	}
	
}
