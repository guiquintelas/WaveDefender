package wave.gui.ferramentas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.principal.Dimensional;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public class Slider extends Dimensional implements MouseInputListener, MouseMotionListener {
	private int widthAtual;
	private SliderNumero numero;
	private double max;
	private boolean ativo = true;

	public Slider(int x, int y, int width, int height, SliderNumero numero, double max) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.numero = numero;
		this.max = max;

		this.widthAtual = (int) ((width * numero.getNumero()) / max);

		MouseListener listener = new MouseListener() {
			public void acao(MouseEvent e) {
				if (!ativo) return;

				if (JanelaJogo.xMouse > Slider.this.x && JanelaJogo.xMouse < (Slider.this.x + Slider.this.width) && Principal.isPausado()) {
					if (JanelaJogo.yMouse > Slider.this.y && JanelaJogo.yMouse < (Slider.this.y + Slider.this.height) && Principal.isPausado()) {
						widthAtual = (int) JanelaJogo.xMouse - getX();

						Slider.this.numero.setNumero((double) ((widthAtual * Slider.this.max) / Slider.this.width));
					}

				}

			}
		};

		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, listener);
		ListenerManager.addListener(ListenerManager.MOUSE_DRAG, listener);

	}

	private int getWidthAtual() {
		return (int) ((numero.getNumero() * width) / max);
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void pintar(Graphics2D g) {
		if (ativo) {
			g.setColor(new Color(238, 211, 130));
			g.fillRoundRect(getX(), getY(), width, height, 5, 5);
			g.setColor(new Color(255, 215, 0));
			g.fillRoundRect(getX(), getY(), getWidthAtual(), height, 5, 5);
		} else {
			g.setColor(new Color(193, 193, 193));
			g.fillRoundRect(getX(), getY(), width, height, 5, 5);
			g.setColor(new Color(235, 235, 235));
			g.fillRoundRect(getX(), getY(), getWidthAtual(), height, 5, 5);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
