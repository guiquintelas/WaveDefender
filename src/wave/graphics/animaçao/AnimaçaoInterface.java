package wave.graphics.animaçao;

import java.awt.Graphics2D;
import java.util.ArrayList;


public interface AnimaçaoInterface {
	public static final ArrayList<AnimaçaoInterface> todasAni = new ArrayList<AnimaçaoInterface>();
	
	public void update();
	public void pintar(Graphics2D g);
	
	public void start();
	public void stop();
}
