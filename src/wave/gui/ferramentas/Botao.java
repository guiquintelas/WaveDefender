package wave.gui.ferramentas;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import wave.input.KeyListener;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.principal.Dimensional;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.Util;

public class Botao extends Dimensional {
	private ActionListener al;
	private ActionListener alDireito;
	private BufferedImage img;
	private BufferedImage imgSel;
	private BufferedImage imgApertado;

	private MouseListener mousePressed;
	private MouseListener mouseReleased;
	private KeyListener keyPressed;
	private KeyListener keyReleased;

	private int textPosicao = CENTRO;
	public static final int CENTRO = 0;
	public static final int CIMA_ESQUERDA = 1;
	public static final int CIMA_DIREITA = 2;
	public static final int BAIXO_ESQUERDA = 3;
	public static final int BAIXO_DIREITA = 4;

	private float alpha = 0;

	private String text;
	private Font font;

	private boolean isSel = false;
	private boolean isApertado = false;
	private boolean isGui = false;
	private boolean isEnable = true;
	private boolean isTecla = false;
	private boolean isCor = false;
	private boolean hasText = false;
	private boolean hasBack = false;
	private boolean hasSombra = true;
	private boolean isPiscando = false;
	private boolean isCorSelApertadoAtivo = true;

	private Color cor;
	private Color corText;
	private Color corBack;
	private Color corSombra;
	private Color corPiscando = new Color(255, 215, 0);

	private Timer timerAlpha;

	private int teclaCod;

	public Botao(int x, int y, int width, int height, BufferedImage imgPadrao, BufferedImage imgSelecionado, BufferedImage imgApertado, ActionListener al) {
		this.x = x;

		if (y > JanelaJogo.HEIGHT) {
			isGui = true;
		}
		this.y = y;

		this.width = width;
		this.height = height;

		this.img = imgPadrao;
		this.imgSel = imgSelecionado;
		this.imgApertado = imgApertado;

		this.al = al;

		initMouseListener();
	}

	public Botao(int x, int y, int width, int height, BufferedImage imgPadrao, BufferedImage imgSelecionado, BufferedImage imgApertado, ActionListener al, char tecla) {
		this.x = x;

		if (y > JanelaJogo.HEIGHT) {
			isGui = true;
		}
		this.y = y;

		this.width = width;
		this.height = height;

		this.img = imgPadrao;
		this.imgSel = imgSelecionado;
		this.imgApertado = imgApertado;

		this.al = al;

		isTecla = true;
		teclaCod = Character.toUpperCase(tecla);
		initKeyListener();
		initMouseListener();
	}

	public Botao(int x, int y, int width, int height, Color corPadrao, ActionListener al, char tecla) {
		this.x = x;

		if (y > JanelaJogo.HEIGHT) {
			isGui = true;
		}
		this.y = y;

		this.width = width;
		this.height = height;

		isCor = true;

		cor = corPadrao;

		this.al = al;

		isTecla = true;
		teclaCod = Character.toUpperCase(tecla);
		initKeyListener();
		initMouseListener();
	}

	public Botao(int x, int y, int width, int height, Color corPadrao, ActionListener al) {
		this.x = x;

		if (y > JanelaJogo.HEIGHT) {
			isGui = true;
		}
		this.y = y;

		this.width = width;
		this.height = height;

		isCor = true;

		cor = corPadrao;

		this.al = al;
		initMouseListener();
	}

	private void initMouseListener() {
		mousePressed = new MouseListener() {
			public void acao(MouseEvent e) {
				if (JanelaJogo.xMouse > x && JanelaJogo.xMouse < x + width) {
					if (JanelaJogo.yMouse > y && JanelaJogo.yMouse < y + height) {
						isApertado = true;
						return;
					}
				}
			}
		};

		mouseReleased = new MouseListener() {
			public void acao(MouseEvent e) {
				if (JanelaJogo.xMouse > x && JanelaJogo.xMouse < x + width) {
					if (JanelaJogo.yMouse > y && JanelaJogo.yMouse < y + height && isApertado) {
						if (e.getButton() == 3 && alDireito != null) {
							alDireito.actionPerformed(null);
						} else {
							al.actionPerformed(null);
						}

					}
				}

				isApertado = false;

			}
		};

		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed);
		ListenerManager.addListener(ListenerManager.MOUSE_RELEASED, mouseReleased);
	}

	private void initKeyListener() {
		keyPressed = new KeyListener() {
			public void acao(KeyEvent e) {
				if (isTecla && e.getKeyCode() == teclaCod) {
					isApertado = true;
				}

			}
		};

		keyReleased = new KeyListener() {
			public void acao(KeyEvent e) {
				if (isTecla && e.getKeyCode() == teclaCod) {
					al.actionPerformed(null);
					isApertado = false;
				}

			}
		};
		
		ListenerManager.addListener(ListenerManager.KEY_PRESSED, keyPressed);
		ListenerManager.addListener(ListenerManager.KEY_RELEASED, keyReleased);
	}

	public void setActionListenerDireito(ActionListener al) {
		alDireito = al;
	}

	public void piscar(boolean piscar) {
		if (timerAlpha != null) {
			if (timerAlpha.isRunning()) {
				if (piscar) {
					return;
				} else {
					timerAlpha.stop();
				}
			}
		}

		if (!piscar) {
			isPiscando = false;
			return;
		}

		isPiscando = true;

		timerAlpha = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			boolean isAlphaUp = true;

			public void actionPerformed(ActionEvent e) {

				if (Principal.tickTotal >= tickAtual + 1 && isAlphaUp) {
					alpha += .025f;
					tickAtual++;
					if (alpha >= .7f) {
						alpha = .7f;
						isAlphaUp = false;
					}
				}

				if (Principal.tickTotal >= tickAtual + 1 && !isAlphaUp) {
					alpha -= .025f;
					tickAtual++;
					if (alpha <= 0) {
						alpha = 0;
						isAlphaUp = true;
					}
				}

			}
		});
		timerAlpha.start();
	}

	public int getY() {
		if (isGui) {
			return (int) y - JanelaJogo.HEIGHT;
		} else {
			return (int) y;
		}
	}

	public void setText(String texto, Font font, Color corText) {
		this.text = texto;
		this.font = font;
		this.corText = corText;
		hasText = true;
	}

	public void setText(String texto) {
		if (hasText) {
			this.text = texto;
		}
	}

	public void setTextPosicao(int pos) {
		textPosicao = pos;
	}

	public void setBG(Color cor) {
		corBack = cor;
		hasBack = true;
	}

	public void setAllImg(BufferedImage img) {
		this.img = img;
		this.imgSel = img;
		this.imgApertado = img;
	}

	public void setSombra(Color cor) {
		corSombra = cor;
		hasSombra = true;
	}

	public void setSombraFalse() {
		hasSombra = false;
	}

	public void setEnable(boolean enable) {
		if (enable && !isEnable) {
			isEnable = true;

			ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed);
			ListenerManager.addListener(ListenerManager.MOUSE_RELEASED, mouseReleased);
			
			if (isTecla) {
				ListenerManager.addListener(ListenerManager.KEY_PRESSED, keyPressed);
				ListenerManager.addListener(ListenerManager.KEY_RELEASED, keyReleased);
			}
			return;
		}

		if (!enable && isEnable) {
			isEnable = false;
			
			ListenerManager.removeListener(ListenerManager.MOUSE_PRESSED, mousePressed);
			ListenerManager.removeListener(ListenerManager.MOUSE_RELEASED, mouseReleased);
			
			if (isTecla) {
				ListenerManager.removeListener(ListenerManager.KEY_PRESSED, keyPressed);
				ListenerManager.removeListener(ListenerManager.KEY_RELEASED, keyReleased);
			}
			return;
		}
	}

	public void pintar(Graphics2D g) {
		pintarSombra(g);
		pintarBG(g);
		if (isCor) {
			if (isApertado) {
				g.setColor(cor);
				g.fillRect(getX() + 1, getY() + 1, width, height);
				pintarApertado(g);
				pintarPisca(g);
				pintarText(g);
				return;
			}

			if (isSel) {
				g.setColor(cor);
				g.fillRect(getX(), getY(), width, height);
				pintarSel(g);
				pintarPisca(g);
				pintarText(g);
				return;
			}

			g.setColor(cor);
			g.fillRect(getX(), getY(), width, height);
			pintarPisca(g);
			pintarText(g);

		} else {
			if (isApertado) {
				g.drawImage(imgApertado, getX() + 1, getY() + 1, null);
				pintarApertado(g);
				pintarPisca(g);
				pintarText(g);
				return;
			}

			if (isSel) {
				g.drawImage(imgSel, getX(), getY(), null);
				pintarSel(g);
				pintarPisca(g);
				pintarText(g);
				return;
			}

			g.drawImage(img, getX(), getY(), null);
			pintarPisca(g);
			pintarText(g);
		}

		if (!isEnable) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
			g.setColor(Color.BLACK);
			g.fillRect(getX(), getY(), width, height);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}

	}

	private void pintarSel(Graphics2D g) {
		if (isCorSelApertadoAtivo && isEnable) {
			if (isCorSelApertadoAtivo) {
				g.setColor(Color.WHITE);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
				g.fillRect(getX(), getY(), width, height);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			}
		}
	}

	private void pintarApertado(Graphics2D g) {
		if (isCorSelApertadoAtivo) {
			if (isCorSelApertadoAtivo) {
				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
				g.fillRect(getX() + 1, getY() + 1, width, height);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			}
		}
	}

	private void pintarText(Graphics2D g) {
		if (!hasText) return;
		g.setFont(font);
		g.setColor(corText);
		if (isApertado) {
			switch (textPosicao) {
				case CENTRO:
					g.drawString(text, getX() + width / 2 - Util.getStringWidh(text, font) / 2 + 1, getY() + height / 2 + Util.getStringHeight(text, font) / 2 - 1);
					break;
				case BAIXO_DIREITA:
					g.drawString(text, getX() + width - Util.getStringWidh(text, font) - 2 + 1, getY() + height - 2 - 1);
					break;
				default:
					g.drawString(text, getX() + width / 2 - Util.getStringWidh(text, font) / 2 + 1, getY() + height / 2 + Util.getStringHeight(text, font) / 2 - 1);
			}

		} else {
			switch (textPosicao) {
				case CENTRO:
					g.drawString(text, getX() + width / 2 - Util.getStringWidh(text, font) / 2, getY() + height / 2 + Util.getStringHeight(text, font) / 2 - 2);
					break;
				case BAIXO_DIREITA:
					g.drawString(text, getX() + width - Util.getStringWidh(text, font) - 2, getY() + height - 2 - 2);
					break;
				default:
					g.drawString(text, getX() + width / 2 - Util.getStringWidh(text, font) / 2, getY() + height / 2 + Util.getStringHeight(text, font) / 2 - 2);
			}

		}
	}

	private void pintarBG(Graphics2D g) {
		if (hasBack) {
			g.setColor(corBack);
			g.fillRect(getX(), getY(), width, height);
		}
	}

	private void pintarSombra(Graphics2D g) {
		if (hasSombra) {
			g.setColor(corSombra);
			g.fillRect(getX() + 1, getY() + 1, width, height);
		}
	}

	private void pintarPisca(Graphics2D g) {
		if (isPiscando) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g.setColor(corPiscando);
			g.fillRect(getX(), getY(), width, height);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
	}

	public void update() {
		checaSel();
	}

	private void checaSel() {
		if (JanelaJogo.xMouse > x && JanelaJogo.xMouse < x + width) {
			if (JanelaJogo.yMouse > y && JanelaJogo.yMouse < y + height) {
				isSel = true;
				return;
			}
		}
		isSel = false;

	}

}
