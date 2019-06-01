package wave.gui.menus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import wave.graphics.BalaoDeFala;
import wave.gui.GUI;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.item.Item;
import wave.item.Usavel;
import wave.item.equip.Equip;
import wave.mob.Player;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.Util;

public class MenuMochila {

	private static int width;
	private static int height;

	private static final int LADO = 30;
	private static final int HEIGHT_TEXT_BOX = 60;

	private static final Font font = new Font("Bookman Old Style", Font.BOLD, 14);
	private static final Font fontDescricao = new Font("Bookman Old Style", Font.PLAIN, 12);

	private static int x;
	private static int y;

	public static ArrayList<MenuMochilaHotkeySlot> hotkeys = new ArrayList<MenuMochilaHotkeySlot>();
	public static ArrayList<MenuMochilaEquipSlot> equips = new ArrayList<MenuMochilaEquipSlot>();
	private static ArrayList<MenuMochilaSlot> slots = new ArrayList<MenuMochilaSlot>();
	private static ArrayList<String> linhas = new ArrayList<String>();

	private static boolean primeiraVez = true;
	private static boolean isDragging = false;

	// para mover
	private static MenuMochilaSlot slotDrag;
	//para a descricao
	private static MenuMochilaSlot mouseSelected;
	
	//listeners
	private static MouseListener mousePressed;
	private static MouseListener mouseReleased;
	private static MouseListener mouseMoved;
	private static MouseListener mouseDragged;
	

	private MenuMochila() {
		for (int i = 0; i < 4; i++) {
			hotkeys.add(new MenuMochilaHotkeySlot(x + width + 10, y + 5 + (i * 35), KeyEvent.VK_1 + i));
		}

		for (int i = 0; i < 4; i++) {
			equips.add(new MenuMochilaEquipSlot(x - LADO - 10, y + 5 + (i * 35), i + 2000));
		}
	}

	public static void init() {
		new MenuMochila();
		
		mousePressed = new MouseListener() {
			public void acao(MouseEvent e) {
				for (int i = 0; i < slots.size(); i++) {
					if (e.getX() > slots.get(i).getX() && e.getX() < slots.get(i).getX() + LADO) {
						if (e.getY() > slots.get(i).getY() && e.getY() < slots.get(i).getY() + LADO) {
							slotDrag = slots.get(i);
							slotDrag.isSelecionado = true;
							break;
						}
					}
				}

				for (int i = 0; i < equips.size(); i++) {
					if (e.getX() > equips.get(i).getX() && e.getX() < equips.get(i).getX() + LADO) {
						if (e.getY() > equips.get(i).getY() && e.getY() < equips.get(i).getY() + LADO) {
							slotDrag = equips.get(i);
							slotDrag.isSelecionado = true;

							break;
						}
					}
				}		
			}
		};
		
		mouseReleased = new MouseListener() {
			public void acao(MouseEvent e) {
				isDragging = false;

				if (slotDrag != null && slotDrag.item != null) {
					slotDrag.isSelecionado = false;

					if (e.getX() > x + width) {
						for (int i = 0; i < 4; i++) {
							if (e.getX() > hotkeys.get(i).getX() && e.getX() < hotkeys.get(i).getX() + LADO) {
								if (e.getY() > hotkeys.get(i).getY() && e.getY() < hotkeys.get(i).getY() + LADO) {
									if (slotDrag.item instanceof Usavel) {
										hotkeys.get(i).setUsavel((Usavel) slotDrag.item);
									}
									slotDrag = null;
									return;
								}
							}
						}
					} else if (e.getX() > x) {
						for (int i = 0; i < slots.size(); i++) {
							if (e.getX() > slots.get(i).getX() && e.getX() < slots.get(i).getX() + LADO) {
								if (e.getY() > slots.get(i).getY() && e.getY() < slots.get(i).getY() + LADO) {
									if (slots.get(i).isSelecionado)
										return;

									if (slotDrag instanceof MenuMochilaEquipSlot) {
										if (slots.get(i).item == null) {
											slots.get(i).item = slotDrag.item;
											((MenuMochilaEquipSlot) slotDrag).removeEquip();
											slotDrag = null;
										} else if (slots.get(i).item instanceof Equip && ((MenuMochilaEquipSlot) slotDrag).id == ((Equip) slots.get(i).item).getID()) {
											Object temp = slots.get(i).item;
											slots.get(i).item = slotDrag.item;
											((MenuMochilaEquipSlot) slotDrag).removeEquip();
											((MenuMochilaEquipSlot) slotDrag).setEquip((Equip) temp);
											slotDrag = null;
										}
									} else {
										if (slots.get(i).item == null) {
											slots.get(i).item = slotDrag.item;
											slotDrag.item = null;
											slotDrag = null;
										} else {
											Object temp = slots.get(i).item;
											slots.get(i).item = slotDrag.item;
											slotDrag.item = (Item) temp;
											slotDrag = null;
										}
									}

									updatePlayerMochila();
									return;
								}
							}
						}
					} else {
						if (!(slotDrag.item instanceof Equip)) {
							slotDrag = null;
							return;
						}

						for (int i = 0; i < 4; i++) {
							if (e.getX() > equips.get(i).getX() && e.getX() < equips.get(i).getX() + LADO) {
								if (e.getY() > equips.get(i).getY() && e.getY() < equips.get(i).getY() + LADO) {
									if (slotDrag instanceof MenuMochilaEquipSlot)
										return;

									if (equips.get(i).equip == null && (equips.get(i).id == ((Equip) slotDrag.item).getID())) {
										equips.get(i).setEquip(((Equip) slotDrag.item));
										slotDrag.item = null;
										slotDrag = null;
										updatePlayerMochila();
										return;
									} else if ((equips.get(i).id != ((Equip) slotDrag.item).getID()))
										return;

									if (equips.get(i).equip != null && (equips.get(i).id == ((Equip) slotDrag.item).getID())) {
										Object temp = equips.get(i).item;
										equips.get(i).removeEquip();
										equips.get(i).setEquip((Equip) slotDrag.item);
										slotDrag.item = (Item) temp;
										slotDrag = null;
										updatePlayerMochila();
										return;
									}
								}
							}
						}

					}

					if (slotDrag instanceof MenuMochilaEquipSlot) {
						if (e.getX() > x + width || e.getX() < x - 40 || e.getY() > y + height || e.getY() < y) {
							slotDrag.item.botarNoChao(Player.getPlayer().getXCentro() + Util.randomInt(-10, 10), Player.getPlayer().getYCentro() + Util.randomInt(-10, 10));
							((MenuMochilaEquipSlot) slotDrag).removeEquip();
						}
					} else {
						if (e.getX() > x + width || e.getX() < x || e.getY() > y + height || e.getY() < y) {
							slotDrag.item.botarNoChao(Player.getPlayer().getXCentro() + Util.randomInt(-10, 10), Player.getPlayer().getYCentro() + Util.randomInt(-10, 10));
							Player.getPlayer().mochilaItens.remove(slotDrag.index);
							slotDrag.item = null;
						}
					}
					updatePlayerMochila();
				}
				
			}
		};
		
		mouseMoved = new MouseListener() {
			public void acao(MouseEvent e) {
				boolean mudou = false;

				for (int i = 0; i < slots.size(); i++) {
					if (e.getX() > slots.get(i).getX() && e.getX() < slots.get(i).getX() + LADO) {
						if (e.getY() > slots.get(i).getY() && e.getY() < slots.get(i).getY() + LADO) {
							if (mouseSelected != slots.get(i) && slots.get(i).item != null) {
								mouseSelected = slots.get(i);
								mudou = true;
							}
						}
					}
				}
				
				for (int i = 0; i < equips.size(); i++) {
					if (e.getX() > equips.get(i).getX() && e.getX() < equips.get(i).getX() + LADO) {
						if (e.getY() > equips.get(i).getY() && e.getY() < equips.get(i).getY() + LADO) {
							if (mouseSelected != equips.get(i) && equips.get(i).item != null) {
								mouseSelected = equips.get(i);
								mudou = true;
							}
						}
					}
				}

				if (mudou) {
					ArrayList<String> palavras = new ArrayList<String>();
					String linhaAtual = "";
					String texto = mouseSelected.item.getText();
					linhas.clear();

					while (texto.length() > 0) {

						if (!texto.contains(" ")) {
							palavras.add(texto);
							texto = "";

						} else {
							palavras.add(texto.substring(0, texto.indexOf(" ") + 1));
							texto = texto.substring(texto.indexOf(" ") + 1, texto.length());

						}

					}

					while (!palavras.isEmpty()) {
						String linhaAtualTemp = "";

						while (true) {
							if (palavras.isEmpty())
								break;

							linhaAtualTemp = linhaAtualTemp.concat(palavras.get(0));

							if (Util.getStringWidh(linhaAtualTemp, fontDescricao) <= width - 10) {
								linhaAtual = linhaAtualTemp;
							} else {
								break;
							}

							palavras.remove(0);
						}

						linhas.add(linhaAtual);
					}
				}
				
			}
		};
		
		mouseDragged = new MouseListener() {
			public void acao(MouseEvent e) {
				isDragging = true;
				
			}
		};
		
	}
	
	public static void reset() {
		equips.clear();
	}

	public static void abrirMenu() {
		slotDrag = null;

		if (!Principal.menuOpcoes() && Player.getPlayer().mochila != null && !Principal.menuLevel) {
			update();
			Principal.menuMochila = true;
			if (primeiraVez) {
				new BalaoDeFala("Eu posso usar Potions de Vida apertando a tecla 1, mas tente guardar para situacões de perigo!", Player.getPlayer(), 250);
				primeiraVez = false;
			}
			
			
			ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed);
			ListenerManager.addListener(ListenerManager.MOUSE_RELEASED, mouseReleased);
			ListenerManager.addListener(ListenerManager.MOUSE_MOVED, mouseMoved);
			ListenerManager.addListener(ListenerManager.MOUSE_DRAG, mouseDragged);
			
			
		}
	}

	public static void fecharMenu() {
		Principal.menuMochila = false;

		updatePlayerMochila();

		
		ListenerManager.removeListener(ListenerManager.MOUSE_PRESSED, mousePressed);
		ListenerManager.removeListener(ListenerManager.MOUSE_RELEASED, mouseReleased);
		ListenerManager.removeListener(ListenerManager.MOUSE_MOVED, mouseMoved);
		ListenerManager.removeListener(ListenerManager.MOUSE_DRAG, mouseDragged);
	}

	private static void updatePlayerMochila() {
		Player.getPlayer().mochilaItens.clear();
		for (int i = 0; i < slots.size(); i++) {
			Player.getPlayer().mochilaItens.put(i, slots.get(i).item);
		}

	}

	public static void update() {
		updateXYWH();
		updateSlots();
		updateHotkey();
		updateEquip();
	}

	private static void updateEquip() {
		for (int i = 0; i < 4; i++) {
			equips.get(i).setX(x - LADO - 10);
			equips.get(i).setY(y + 5 + (i * 35));
		}
	}

	private static void updateHotkey() {
		for (int i = 0; i < 4; i++) {
			hotkeys.get(i).setX(x + width + 10);
			hotkeys.get(i).setY(y + 5 + (i * 35));
		}

	}

	private static void updateXYWH() {
		if (Player.getPlayer().mochila.getSize() <= 5) {
			width = 15 + (Player.getPlayer().mochila.getSize() * (LADO + 5));
		} else {
			width = 15 + (5 * (LADO + 5));
		}

		if (Player.getPlayer().mochila.getSize() <= 5) {
			height = 45 + LADO + HEIGHT_TEXT_BOX;
		} else {
			height = 45 + HEIGHT_TEXT_BOX + ((int) Math.ceil(Player.getPlayer().mochila.getSize() / 5.0) * (LADO + 5));
		}

		x = (JanelaJogo.WIDTH / 2) - width / 2;// - 40 para o menu de
												// hotkey
		y = (JanelaJogo.HEIGHT + GUI.HEIGHT) / 2 - height / 2;

	}

	private static void updateSlots() {
		slots.clear();
		for (int j = 0; j < (int) Math.ceil(Player.getPlayer().mochila.getSize() / 5.0); j++) {
			for (int i = 0; i + (j * 5) < Player.getPlayer().mochila.getSize() && i < 5; i++) {
				MenuMochilaSlot mms = new MenuMochilaSlot(x + 5 + (5 * (i + 1)) + (i * LADO), y + ((j + 1) * 35), (i + (j * 5)));
				if (Player.getPlayer().mochila.getSize() > (i + (j * 5)) && Player.getPlayer().mochilaItens.get(i + (j * 5)) != null) {
					mms.item = Player.getPlayer().mochilaItens.get(i + (j * 5));
				}
				slots.add(mms);
			}
		}
	}

	public static void pintar(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, width, height, 10, 10);
		g.fillRoundRect(x + width + 5, y, 40, 145, 10, 10);
		g.fillRoundRect(x - 15 - LADO, y, 40, 145, 10, 10);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(x + 5, y + height - 65, width - 10, 60, 10, 10);
		g.setColor(Color.GRAY);
		g.drawRoundRect(x + 5, y + height - 65, width - 10, 60, 10, 10);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Mochila:", x + width / 2 - 62 / 2 - 15, y + 20);
		g.drawImage(Player.getPlayer().mochila.getImg(), x + width / 2 - 62 / 2 + 50, y, null);

		for (int i = 0; i < slots.size(); i++) {
			slots.get(i).pintar(g);
		}

		for (int i = 0; i < 4; i++) {
			hotkeys.get(i).pintar(g);
		}

		for (int i = 0; i < 4; i++) {
			equips.get(i).pintar(g);
		}

		if (mouseSelected != null && mouseSelected.item != null) {
			g.setFont(fontDescricao);
			for (int i = 0; i < linhas.size(); i++) {
				g.drawString(linhas.get(i), x + 10, y + height - 50 + (i * 13));
			}
		}

		if (isDragging && slotDrag != null && slotDrag.item != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.drawImage(slotDrag.item.getImg(), (int) JanelaJogo.xMouse - slotDrag.item.getImg().getWidth() / 2, (int) JanelaJogo.yMouse - slotDrag.item.getImg().getHeight() / 2, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}

	}

}
