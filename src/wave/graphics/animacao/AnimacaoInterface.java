package wave.graphics.animacao;

import java.awt.Graphics2D;
import java.util.ArrayList;


public interface AnimacaoInterface {
	public static final ArrayList<AnimacaoInterface> todasAni = new ArrayList<AnimacaoInterface>();
	
	public void update();
	public void pintar(Graphics2D g);
	
	public void start();
	public void stop();
}
