package wave.principal;

import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.graphics.Grafico;
import wave.graphics.Sombra;

public abstract class DimensionalObj extends Dimensional {
	public Sombra sombra;
	public static ArrayList<DimensionalObj> todosDimensionalObjs = new ArrayList<DimensionalObj>();
	
	public void prontoParaPintar() {
		if (!todosDimensionalObjs.contains(this)) {
			todosDimensionalObjs.add(this);
			Grafico.objs.add(todosDimensionalObjs.size());
		}	
	}
	
	public void remove() {
		todosDimensionalObjs.remove(this);
	}
	
	public abstract String toString();
	
	public abstract void pintar(Graphics2D g);
	
	public abstract void update();
}
