package wave.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.principal.Principal;

public class NovoStatusAnimaçao {
	private double x;
	private double y;
	private float alpha = 1.0f;
	private String status;

	private ArrayList<Color> cores = new ArrayList<Color>();
	private Font fonte = new Font("Bookman Old Style", Font.BOLD, 13);

	private static int fila = 0;
	private int ultimoIndex = -1;
	private boolean indexPronto = true;

	private static int DELAY = 5;

	public static final int PADRAO = 13;

	private Timer timerFilaEsprera;
	private Timer timerDelayPisco;

	public static ArrayList<NovoStatusAnimaçao> todasStatusAni = new ArrayList<NovoStatusAnimaçao>();

	public NovoStatusAnimaçao(int x, int y, String status) {
		this.status = status;
		this.x = x - (status.length() * 3);
		this.y = y - 28;
		addCor(Color.BLACK);
		addStatusAni();
	}

	public NovoStatusAnimaçao(int x, int y, String status, Color cor, int tamanho) {
		addCor(cor);
		this.status = status;
		this.x = x - (status.length() * 3);
		this.y = y - 28;
		fonte = new Font("Bookman Old Style", Font.BOLD, tamanho);
		addStatusAni();
	}

	public NovoStatusAnimaçao(int x, int y, String status, Color[] cores, int tamanho) {
		for (int i = 0; i < cores.length; i++) {
			addCor(cores[i]);
		}

		this.status = status;
		this.x = x - (status.length() * 3);
		this.y = y - 28;
		fonte = new Font("Bookman Old Style", Font.BOLD, tamanho);
		addStatusAni();
	}

	private void addCor(Color cor) {
		if (!cores.contains(cor)) {
			cores.add(cor);
		}
	}

	private Color getCor() {
		if (indexPronto) {
			indexPronto = false;

			if (ultimoIndex + 1 >= cores.size()) {
				ultimoIndex = 0;
			} else {
				ultimoIndex++;
			}

			timerDelayPisco = new Timer(5, new ActionListener() {
				int tickAtual = Principal.tickTotal;

				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= tickAtual + DELAY) {
						indexPronto = true;
						timerDelayPisco.stop();
					}
				}
			});
			timerDelayPisco.start();
		}

		return cores.get(ultimoIndex);
	}

	private void addStatusAni() {
		this.y -= fila * 15;
		todasStatusAni.add(this);
		fila++;

		final int tickAtual = Principal.tickTotal;
		timerFilaEsprera = new Timer(5, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual + 5) {
					fila--;
					timerFilaEsprera.stop();
				}

			}
		});
		timerFilaEsprera.start();
	}

	public void update() {
		y -= 0.5;
		alpha -= 0.01f;

		if (alpha <= 0) {
			alpha = 0;
			todasStatusAni.remove(this);
		}
	}

	public void pintar(Graphics2D g) {
		g.setFont(fonte);

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.setColor(Color.BLACK);
		g.drawString(status, (int) x + 1, (int) y + 1);
		g.setColor(getCor());
		g.drawString(status, (int) x, (int) y);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

}
