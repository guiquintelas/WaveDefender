package wave.principal;

import java.awt.Graphics2D;

public abstract class GameEstate {
	
	public abstract void update();
	public abstract void pintar(Graphics2D g);
	public abstract void init();
	public abstract void close();
	
}
