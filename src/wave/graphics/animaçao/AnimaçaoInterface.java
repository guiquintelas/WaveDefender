package wave.graphics.anima�ao;

import java.awt.Graphics2D;
import java.util.ArrayList;


public interface Anima�aoInterface {
	public static final ArrayList<Anima�aoInterface> todasAni = new ArrayList<Anima�aoInterface>();
	
	public void update();
	public void pintar(Graphics2D g);
	
	public void start();
	public void stop();
}
