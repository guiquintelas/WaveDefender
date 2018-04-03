package wave.gui.status;

import java.awt.Color;
import java.awt.Graphics2D;

public class BarraDura�aoStatus {
	private StatusGUI status;
	private static final int HEIGHT = 5;
	private double widthAtual;
	private int dura�ao;
	
	public BarraDura�aoStatus(int dura�ao, StatusGUI status) {
		this.status = status;
		this.widthAtual = StatusGUI.WIDTH;
		this.dura�ao = dura�ao;
	}
	
	public void update() {
		updateWidthAtual();
	}
	
	private void updateWidthAtual() {
		if (status.isSemDura�ao()) return;
		
		widthAtual -= (double)StatusGUI.WIDTH / (double)dura�ao;
		
		if (widthAtual <= 0) {
			status.remove();
		}
	}

	public void reset() {
		
	}
	
	public void pintar(Graphics2D g) {
		if (status.isSemDura�ao()) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.GRAY);
		}
		
		g.fillRect(status.getX(), StatusGUI.y + StatusGUI.HEIGHT + 1, (int)widthAtual - 1, HEIGHT);	
		g.setColor(Color.BLACK);
		g.drawRect(status.getX(), StatusGUI.y + StatusGUI.HEIGHT + 1, StatusGUI.WIDTH - 1, HEIGHT);		
	}

}
