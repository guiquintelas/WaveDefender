package wave.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import wave.mob.Mob;
import wave.mob.Player;
import wave.principal.Dimensional;

public class Sombra extends Dimensional{
	
	private int xOff = 0;
	private int yOff = 0;
	private int widthOff = 0;
	private int heightOff = 0;
	private static final float ALPHA = 0.3f;
	
	private Dimensional d;
	
	public Sombra(Dimensional d) {
		this.d = d;	
		updateWH();
		updateXY();
	}
	
	public Dimensional getDimensional() {
		return d;
	}
	
	public void setWidthOff(int off) {
		this.widthOff = off;
	}
	
	public void setXOff(int off) {
		this.xOff = off;
	}
	
	public void setYOff(int off) {
		this.yOff = off;
	}
	
	public void setHeightOff(int off) {
		this.heightOff = off;
	}
	
	private void updateWH() {
		
		this.width = d.getWidth() + 10 + widthOff;
		
		if (d instanceof Mob) {
			Mob dM = (Mob)d;
			this.height = (dM.heightSprite + heightOff) / 4;
		} else {
			this.height = (d.getHeight() + heightOff) / 4;
		}
			
	}

	private void updateXY() {
		this.x = d.getXCentro() - (width)/2;
		if (d instanceof Mob) {
			this.y = ((Mob) d).getYSprite() + ((Mob) d).heightSprite - ((Mob) d).heightSprite / 7;
		} else {
			this.y = d.getY() +  d.getHeight() - d.getHeight() / 7;
		}
		
	}
	
	public void pintar(Graphics2D g) {
		if (d instanceof Mob  && ((Mob) d).isInvisivel() && !(d instanceof Player)) return;
		
		updateWH();
		updateXY();
		
		if (d instanceof Mob && ((Mob) d).isInvisivel()) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA / 2));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));
		}
		g.setColor(Color.BLACK);
		g.fillOval(getX() + xOff, getY() + yOff, width, height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
}
