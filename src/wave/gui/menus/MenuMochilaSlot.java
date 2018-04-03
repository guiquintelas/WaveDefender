package wave.gui.menus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import wave.item.Item;
import wave.principal.Dimensional;

public class MenuMochilaSlot extends Dimensional{
	public static final int LADO = 30;
	public int index;
	
	public Item item;
	public boolean isSelecionado = false;
	
	public MenuMochilaSlot(int x, int y, int index) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.width = LADO;
		this.height = LADO;
	}
	
	public void pintar(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(getX(), getY() , LADO, LADO);
		g.setColor(Color.WHITE);
		g.drawRect(getX(), getY(), LADO, LADO);
		
		if (item != null) {
			if (isSelecionado) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				g.drawImage(item.getImg(),getX() + LADO/2 - item.getImg().getWidth()/2,  getY() + LADO/2 - item.getImg().getHeight()/2, null);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			} else {
				g.drawImage(item.getImg(),getX() + LADO/2 - item.getImg().getWidth()/2,  getY() + LADO/2 - item.getImg().getHeight()/2, null);
			}
			
		}
		
	}

}
