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
	
	private boolean semDuraçao = false;
	
	private float alpha = 1.0f;
	private String id;
	
	private BarraDuraçaoStatus barraDuraçao;
	private BufferedImage img = carregarImg();
	
	private Timer timerCriaçao;
	
	public static ArrayList<StatusGUI> todosStatusGUI = new ArrayList<StatusGUI>();
	
	public StatusGUI(int duraçao, String id) {
		barraDuraçao = new BarraDuraçaoStatus(duraçao, this);
		this.id = id;
		addArray(id);
		
		updateX();
		
	}
	
	public StatusGUI(String id) {
		this.semDuraçao = true;
		barraDuraçao = new BarraDuraçaoStatus(0 , this);
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
		criaçaoAni();
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
	
	public abstract String getDescriçao();

	public void update() {
		updateX();
		barraDuraçao.update();
	}

	private void updateX() {
		int index = todosStatusGUI.indexOf(this);
		x = 355 + (index * 50);	
	}
	
	private void criaçaoAni() {
		alpha = 0;
		
		timerCriaçao = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual) {
					alpha += 0.03f;
					tickAtual++;
					if (alpha >= 1) {
						alpha = 1;
						timerCriaçao.stop();
					}
				}
			}
			
		});
		timerCriaçao.start();
	}
	
	public void pintar(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));			
		if (img != null) {
			g.drawImage(img, getX(), y,WIDTH, HEIGHT, null);
		} else {
			g.fillRect(getX(), y, WIDTH, HEIGHT);
		}
		barraDuraçao.pintar(g);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public boolean isSemDuraçao() {
		return semDuraçao;
	}

	public void setSemDuraçao(boolean semDuraçao) {
		this.semDuraçao = semDuraçao;
		if (semDuraçao = false) {
			remove();
		}
	}
	
	public void remove() {
		todosStatusGUI.remove(this);
		todosDescritivos.remove(this);
	}
	
}
