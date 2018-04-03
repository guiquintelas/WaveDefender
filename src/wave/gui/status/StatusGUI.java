package wave.gui.status;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.gui.Descritivo;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public abstract class StatusGUI extends Descritivo{
	private double x;
	public static final int y = 10;
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	private boolean semDura�ao = false;
	
	private float alpha = 1.0f;
	private String id;
	
	private BarraDura�aoStatus barraDura�ao;
	private BufferedImage img = carregarImg();
	
	private Timer timerCria�ao;
	
	public static ArrayList<StatusGUI> todosStatusGUI = new ArrayList<StatusGUI>();
	
	public StatusGUI(int dura�ao, String id) {
		barraDura�ao = new BarraDura�aoStatus(dura�ao, this);
		this.id = id;
		addArray(id);
		
		updateX();
		
	}
	
	public StatusGUI(String id) {
		this.semDura�ao = true;
		barraDura�ao = new BarraDura�aoStatus(0 , this);
		this.id = id;
		addArray(id);
		
		updateX();
		
	}
	
	private void addArray(String id) {
		for (int x = 0; x < todosStatusGUI.size(); x++) {
			if (this.id.equals(todosStatusGUI.get(x).id)) {
				todosDescritivos.remove(todosStatusGUI.get(x));
				todosStatusGUI.set(x, this);
				return;
			}
		}
		
		todosStatusGUI.add(this);
		cria�aoAni();
	}
	
	protected abstract BufferedImage carregarImg();
	
	public static void clear() {
		System.out.println(todosDescritivos.size());
		for (StatusGUI sGUI: todosStatusGUI) {
			todosDescritivos.remove(sGUI);
		}
		
		todosStatusGUI.clear();
		System.out.println(todosDescritivos.size() + "aqui");
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y + JanelaJogo.HEIGHT;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public abstract String getDescri�ao();

	public void update() {
		updateX();
		barraDura�ao.update();
	}

	private void updateX() {
		int index = todosStatusGUI.indexOf(this);
		x = 355 + (index * 50);	
	}
	
	private void cria�aoAni() {
		alpha = 0;
		
		timerCria�ao = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual) {
					alpha += 0.03f;
					tickAtual++;
					if (alpha >= 1) {
						alpha = 1;
						timerCria�ao.stop();
					}
				}
			}
			
		});
		timerCria�ao.start();
	}
	
	public void pintar(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));			
		if (img != null) {
			g.drawImage(img, getX(), y,WIDTH, HEIGHT, null);
		} else {
			g.fillRect(getX(), y, WIDTH, HEIGHT);
		}
		barraDura�ao.pintar(g);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public boolean isSemDura�ao() {
		return semDura�ao;
	}

	public void setSemDura�ao(boolean semDura�ao) {
		this.semDura�ao = semDura�ao;
		if (semDura�ao = false) {
			remove();
		}
	}
	
	public void remove() {
		todosStatusGUI.remove(this);
		todosDescritivos.remove(this);
	}
	
}
