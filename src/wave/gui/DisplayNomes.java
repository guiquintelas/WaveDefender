package wave.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import wave.item.Item;
import wave.mob.Mob;
import wave.mob.Monstro;
import wave.mob.Player;
import wave.principal.DimensionalObj;
import wave.principal.JanelaJogo;
import wave.tools.Util;

public class DisplayNomes {
	private static final Font font = new Font("Bookman Old Style", Font.PLAIN, 13);
	private static int x;
	private static int y;

	private static DimensionalObj dSelecionado;

	public static void update() {
		checaColisaoDimensionalObjMouse();
		updateXY();
	}

	private static void updateXY() {
		if (dSelecionado != null) {
			int fontWidth = Util.getStringWidh(dSelecionado.toString(), font);

			x = dSelecionado.getXCentro() - fontWidth / 2;
			y = dSelecionado.getY() + dSelecionado.getHeight() + 15;
		}

	}

	private synchronized static void checaColisaoDimensionalObjMouse() {
		for (int x = 0; x < DimensionalObj.todosDimensionalObjs.size(); x++) {
			DimensionalObj dAtual = DimensionalObj.todosDimensionalObjs.get(x);

			if (Math.abs(JanelaJogo.xMouse - dAtual.getXCentro()) < (dAtual.getWidth() / 2)) {
				if (dAtual instanceof Mob) {
					Mob dAtualM = (Mob)dAtual;
					if (Math.abs(JanelaJogo.yMouse - dAtualM.getYSpriteCentro()) < (dAtualM.heightSprite / 2)) {
						if (dAtualM.isInvisivel() && !(dAtual instanceof Player)) {
							return;
						}
						dSelecionado = dAtual;
						return;
					}
				} else {
					if (Math.abs(JanelaJogo.yMouse - dAtual.getYCentro()) < (dAtual.getHeight() / 2)) {
						dSelecionado = dAtual;
						return;
					}
				}

			}
		}

		dSelecionado = null;

	}

	public synchronized static void pintar(Graphics2D g) {
		if (dSelecionado != null) {
			
			g.setFont(font);
			if (dSelecionado instanceof Monstro) {
				g.setColor(Color.RED);
			} else if ( dSelecionado instanceof Item) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.LIGHT_GRAY);
			}
			
			g.drawString(dSelecionado.toString(), x-1, y-1);
			g.setColor(Color.BLACK);
			g.drawString(dSelecionado.toString(), x, y);
			
		}
	}
}
