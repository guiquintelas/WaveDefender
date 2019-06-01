package wave.gui.status;

import java.awt.Color;
import java.awt.Graphics2D;

public class BarraDuracaoStatus {
	private StatusGUI status;
	private static final int HEIGHT = 5;
	private double widthAtual;
	private int duracao;
	
	public BarraDuracaoStatus(int duracao, StatusGUI status) {
		this.status = status;
		this.widthAtual = StatusGUI.WIDTH;
		this.duracao = duracao;
	}
	
	public void update() {
		updateWidthAtual();
	}
	
	private void updateWidthAtual() {
		if (status.isSemDuracao()) return;
		
		widthAtual -= (double)StatusGUI.WIDTH / (double)duracao;
		
		if (widthAtual <= 0) {
			status.remove();
		}
	}

	public void reset() {
		
	}
	
	public void pintar(Graphics2D g) {
		if (status.isSemDuracao()) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.GRAY);
		}
		
		g.fillRect(status.getX(), StatusGUI.y + StatusGUI.HEIGHT + 1, (int)widthAtual - 1, HEIGHT);	
		g.setColor(Color.BLACK);
		g.drawRect(status.getX(), StatusGUI.y + StatusGUI.HEIGHT + 1, StatusGUI.WIDTH - 1, HEIGHT);		
	}

}
