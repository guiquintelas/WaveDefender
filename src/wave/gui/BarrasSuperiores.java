package wave.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import wave.mob.Player;
import wave.principal.JanelaJogo;

public class BarrasSuperiores {
	public static final int TOPO = 1;
	public static final int DIREITA = 2;
	public static final int BAIXO = 3;
	public static final int ESQUERDA = 4;

	private static final Font font = new Font("Lucida Handwriting", Font.PLAIN, 12);

	private static int posicao = TOPO;

	public static boolean isInvertido = true;
	public static boolean isAtivo = true;

	private static Color corVida;

	public static void update() {
		if (isAtivo) {
			updateCor();
		}
	}

	public static int getPosicao() {
		return posicao;
	}

	private static int getXFixo(boolean pr) {
		if (pr) {
			if (posicao == TOPO || posicao == BAIXO || posicao == ESQUERDA) {
				return 5;
			} else {
				return JanelaJogo.WIDTH - getWidth() - 5;
			}
		} else {
			if (posicao == TOPO || posicao == BAIXO) {
				return 5 + JanelaJogo.WIDTH / 2;
			}

			if (posicao == ESQUERDA) {
				return 5;
			}

			return JanelaJogo.WIDTH - getWidth() - 5;
		}
	}

	private static int getYFixo(boolean pr) {
		if (posicao == TOPO) {
			return 5;
		}

		if (posicao == BAIXO) {
			return JanelaJogo.HEIGHT - getHeight() - 5;
		}

		if (pr) {
			return 5;
		} else {
			return JanelaJogo.HEIGHT / 2 + 5;
		}
	}

	private static int getWidth() {
		if (posicao == ESQUERDA || posicao == DIREITA) {
			return 30;
		} else {
			return JanelaJogo.WIDTH / 2 - 10;
		}
	}

	private static int getHeight() {
		if (posicao == ESQUERDA || posicao == DIREITA) {
			return JanelaJogo.HEIGHT / 2 - 10;
		} else {
			return 30;
		}
	}

	public static void setPosicao(int posicao) {
		if (posicao < 1 || posicao > 4) {
			BarrasSuperiores.posicao = TOPO;
			System.out.println("posicao invalida, topo usado como padrao");
		} else {
			BarrasSuperiores.posicao = posicao;
		}
	}

	private static int getWithAtualVida() {
		if (posicao == TOPO || posicao == BAIXO) {
			return (Player.getPlayer().getVida() * getWidth()) / Player.getPlayer().getVidaMax();
		} else {
			return getWidth();
		}
	}

	private static int getHeightAtualVida() {
		if (posicao == ESQUERDA || posicao == DIREITA) {
			return (Player.getPlayer().getVida() * getHeight()) / Player.getPlayer().getVidaMax();
		} else {
			return getHeight();
		}

	}

	private static int getWithAtualMana() {
		if (posicao == TOPO || posicao == BAIXO) {
			return ((int) Player.getPlayer().mana * getWidth()) / Player.getPlayer().getManaMax();
		} else {
			return getWidth();
		}

	}

	private static int getHeightAtualMana() {
		if (posicao == ESQUERDA || posicao == DIREITA) {
			return ((int) Player.getPlayer().mana * getHeight()) / Player.getPlayer().getManaMax();
		} else {
			return getHeight();
		}

	}

	private static int getXVida() {
		if (posicao == TOPO || posicao == BAIXO) {
			int x = 5;
			if (isInvertido) {
				x += getWidth() + 10 + (getWidth() - getWithAtualVida());
			}

			return x;
		}

		if (posicao == ESQUERDA) {
			return 5;
		}

		if (posicao == DIREITA) {
			return JanelaJogo.WIDTH - getWidth() - 5;
		}

		return 0;
	}

	private static int getXMana() {
		if (posicao == TOPO || posicao == BAIXO) {
			int x = 5 + JanelaJogo.WIDTH / 2 + (getWidth() - getWithAtualMana());
			if (isInvertido) {
				x = 5;
			}

			return x;
		}

		if (posicao == ESQUERDA) {
			return 5;
		}

		if (posicao == DIREITA) {
			return JanelaJogo.WIDTH - getWidth() - 5;
		}

		return 0;
	}

	private static int getYVida() {
		if (posicao == TOPO) {
			return 5;
		}

		if (posicao == BAIXO) {
			return JanelaJogo.HEIGHT - getHeight() - 5;
		}

		if (posicao == ESQUERDA || posicao == DIREITA) {
			int y = 5;

			if (isInvertido) {
				y += getHeight() + 10 + (getHeight() - getHeightAtualVida());
				;
			}

			return y;
		}

		return 0;
	}

	private static int getYMana() {
		if (posicao == TOPO) {
			return 5;
		}

		if (posicao == BAIXO) {
			return JanelaJogo.HEIGHT - getHeight() - 5;
		}

		if (posicao == ESQUERDA || posicao == DIREITA) {
			int y = JanelaJogo.HEIGHT / 2 + 5 + (getHeight() - getHeightAtualMana());

			if (isInvertido) {
				y = 5;
			}

			return y;
		}

		return 0;
	}

	private static void updateCor() {
		int vermelho = 255;
		int verde = 255;

		if (posicao == TOPO || posicao == BAIXO) {
			if (getWithAtualVida() >= (getWidth() / 2)) {
				int vermelhoFinal = (255 * (getWithAtualVida() - (getWidth() / 2))) / (getWidth() / 2);
				if (vermelhoFinal > 255)
					vermelhoFinal = 255;
				vermelho -= vermelhoFinal;
			} else {
				int verdeFinal = (255 * getWithAtualVida()) / (getWidth() / 2) - 255;
				if (verdeFinal < -255)
					verdeFinal = -255;
				verde += verdeFinal;
			}
		} else {
			if (getHeightAtualVida() >= (getHeight() / 2)) {
				int vermelhoFinal = (255 * (getHeightAtualVida() - (getHeight() / 2))) / (getHeight() / 2);
				if (vermelhoFinal > 255)
					vermelhoFinal = 255;
				vermelho -= vermelhoFinal;
			} else {
				int verdeFinal = (255 * getHeightAtualVida()) / (getHeight() / 2) - 255;
				if (verdeFinal < -255)
					verdeFinal = -255;
				verde += verdeFinal;
			}
		}

		corVida = new Color(vermelho, verde, 0);
	}

	private static int ajuste(int numero, int numeroIdeal, int tamanhoDigitoFonte) {
		int ajuste = 0;

		int diff = String.valueOf(numeroIdeal).length() - String.valueOf(numero).length();

		ajuste = tamanhoDigitoFonte * diff;

		return ajuste;
	}

	public static void pintar(Graphics2D g) {
		if (isAtivo) {
			g.setColor(corVida);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g.fillRect(getXVida(), getYVida(), getWithAtualVida(), getHeightAtualVida());
			g.setColor(new Color(0, 191, 255));
			g.fillRect(getXMana(), getYMana(), getWithAtualMana(), getHeightAtualMana());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			g.setColor(Color.BLACK);
			g.drawRect(getXFixo(true), getYFixo(true), getWidth(), getHeight());
			g.drawRect(getXFixo(false), getYFixo(false), getWidth(), getHeight());

			pintarTexto(g);

		}
	}

	private static void pintarTexto(Graphics2D g) {
		g.setFont(font);	

		if (posicao == TOPO || posicao == BAIXO) {
			if (isInvertido) {
				g.drawString("VIDA " + Player.getPlayer().getVida() + "/" + Player.getPlayer().getVidaMax(), 20 + getWidth(), getYVida() + 18);
				g.drawString((int) Player.getPlayer().mana + "/" + Player.getPlayer().getManaMax() + " MANA", 10 + getWidth() - 105 + ajuste((int) Player.getPlayer().mana, 100, 8), getYMana() + 18);
			} else {
				g.drawString(Player.getPlayer().getVida() + "/" + Player.getPlayer().getVidaMax() + " VIDA", 10 + getWidth() - 102 + ajuste(Player.getPlayer().getVida(), 100, 8), getYVida() + 18);
				g.drawString("MANA " + (int) Player.getPlayer().mana + "/" + Player.getPlayer().getManaMax(), 20 + getWidth(), getYMana() + 18);
			}
		} 
		
		if (posicao == ESQUERDA) {
			g.rotate(-Math.PI / 2);
			if (isInvertido) {
				g.drawString(Player.getPlayer().getVida() + "/" + Player.getPlayer().getVidaMax() + " VIDA", -(getHeight() + 115 - ajuste(Player.getPlayer().getVida(), 100, 8)), 25);
				g.drawString("MANA " + (int) Player.getPlayer().mana + "/" + Player.getPlayer().getManaMax(), -(getHeight()), 25);
			} else {
				g.drawString("VIDA " + Player.getPlayer().getVida() + "/" + Player.getPlayer().getVidaMax(), -(getHeight()), 25);
				g.drawString((int) Player.getPlayer().mana + "/" + Player.getPlayer().getManaMax() + " MANA", -(getHeight() + 120 - ajuste((int)Player.getPlayer().mana, 100, 8)), 25);
			}
			g.rotate(Math.PI / 2);
		}
		
		if (posicao == DIREITA) {
			g.rotate(Math.PI / 2);
			if (isInvertido) {
				g.drawString("VIDA " + Player.getPlayer().getVida() + "/" + Player.getPlayer().getVidaMax(), 20 + getHeight(), -(JanelaJogo.WIDTH - 5 - 18));
				g.drawString((int) Player.getPlayer().mana + "/" + Player.getPlayer().getManaMax() + " MANA", 10 + getHeight() - 105 + ajuste((int) Player.getPlayer().mana, 100, 8), -(JanelaJogo.WIDTH - 5 - 18));
			} else {
				g.drawString((int) Player.getPlayer().getVida() + "/" + Player.getPlayer().getVidaMax() + " VIDA", 10 + getHeight() - 105  + 5 + ajuste((int) Player.getPlayer().getVida(), 100, 8), -(JanelaJogo.WIDTH - 5 - 18));
				g.drawString("MANA " + (int)Player.getPlayer().mana + "/" + Player.getPlayer().getManaMax(), 20 + getHeight(), -(JanelaJogo.WIDTH - 5 - 18));
			}
			g.rotate(-Math.PI / 2);
		}

	}
}
