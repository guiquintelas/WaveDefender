package wave.gui;

import java.util.ArrayList;

import wave.principal.Dimensional;
import wave.principal.JanelaJogo;

public abstract class Descritivo extends Dimensional{
	public static final ArrayList<Descritivo> todosDescritivos = new ArrayList<Descritivo>();
	
	//pre requisitos
	// getX()
	// getY()
	// getWidth()
	// getHeight()
	
	public Descritivo() {
		todosDescritivos.add(this);
	}
	
	public abstract String getDescricao();
	
	
	public boolean checaColisao() {
		if (!isAtivo()) return false;
		
		if (JanelaJogo.xMouse > getX() && JanelaJogo.xMouse < getX() + getWidth()) {
			if (JanelaJogo.yMouse > getY() && JanelaJogo.yMouse < getY() + getHeight()) {
				DescricaoBox.criar(getDescricao());
				return true;
			}
		}
		
		return false;
	}
	
	public static void updateAll() {
		for (int i = 0; i < todosDescritivos.size(); i++) {
			if (todosDescritivos.get(i).checaColisao()) return;
		}
		
		DescricaoBox.setVisible(false);
	}
	
	public boolean isAtivo() {
		//padrao
		return true; 
	}
}
