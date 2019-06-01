package wave.item;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.graphics.BalaoDeFala;
import wave.graphics.Sombra;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.mob.Player;
import wave.principal.DimensionalObj;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.Util;

public abstract class Chest extends DimensionalObj {
	public static final String PADRAO = "padrao";
	public static final String AZUL = "azul";
	public static final String VERMELHO = "vermelho";
	
	protected static final BufferedImage bauSpriteSheet = Util.carregarImg("/Sprites/bau.png");
	
	//padrao
	protected static final ArrayList<BufferedImage> bauImgs = Util.carregarArrayBIVertical(Chest.bauSpriteSheet, 0, 0, 30, 32, 4);
	protected static final BufferedImage img = bauImgs.get(0);
	
	//vermelho
	protected static final ArrayList<BufferedImage> bauVerImgs = Util.carregarArrayBIVertical(Chest.bauSpriteSheet, 32, 0, 30, 32, 4);
	protected static final BufferedImage imgVer = bauVerImgs.get(0);
	
	//azul
	protected static final ArrayList<BufferedImage> bauAzImgs = Util.carregarArrayBIVertical(Chest.bauSpriteSheet, 64, 0, 30, 32, 4);
	protected static final BufferedImage imgAz = bauAzImgs.get(0);
	
	protected BufferedImage imgSel = criarImgSel(img);
	protected BufferedImage imgAzSel = criarImgSel(imgAz);
	protected BufferedImage imgVerSel = criarImgSel(imgVer);
	
	
	protected static final RandomSFXGrupo abrirSfx = new RandomSFXGrupo(new String[]{"/SFX/chest1.ogg", "/SFX/chest2.ogg", "/SFX/chest3.ogg"});
	protected String tipo;
	
	
	protected Dropper dropper;
	protected boolean isSel = false;
	protected boolean isAbrindo = false;
	
	//listener
	private MouseListener mousePressed;
	
	public Chest(int x, int y, int width, int height, String tipo) {
		this.tipo = tipo;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		dropper = new Dropper(getXCentro(), getYCentro());
		sombra = new Sombra(this);
		
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed =  new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getButton() == 3) {
					if (e.getX() > getX() && e.getX() < getX() + Chest.this.width) {
						if (e.getY() > getY() && e.getY() < getY() + Chest.this.height) {
							double dist = Math.sqrt(Math.pow(getXCentro() - Player.getPlayer().getXCentro(), 2) + Math.pow(getYCentro() - Player.getPlayer().getYCentro(), 2));
							if (dist < 70) {
								abrir();
							} else {
								Player.semMana.play();
								new BalaoDeFala("Preciso estar perto do baú para abri-lo!", Player.getPlayer(), 100);
							}
						}
					}
				}		
			}
		});
		
		prontoParaPintar();
	}
	
	private BufferedImage criarImgSel(BufferedImage img) {
		BufferedImage imgSel = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		imgSel.getGraphics().drawImage(img, 0, 0, null);
		for (int y = 0; y < imgSel.getHeight(); y++) {
			for (int x = 0; x < imgSel.getWidth(); x++) {
				int cor = imgSel.getRGB(x, y);
				int alpha = (cor>>24) & 0xff;
				
				if (alpha == 0) {				
					
					if (x - 1 > 0) {
						int alphaEsquerda = (imgSel.getRGB(x - 1, y)>>24) & 0xff;
						if (alphaEsquerda > 100) imgSel.setRGB(x - 1, y, 0xffffff00);
					}
					
					if (x + 1 < imgSel.getWidth()) {
						int alphaDireita = (imgSel.getRGB(x + 1, y)>>24) & 0xff;
						if (alphaDireita > 100) imgSel.setRGB(x + 1, y, 0xffffff00);
					}
					
					if (y + 1 < imgSel.getHeight()) {
						int alphaBaixo = (imgSel.getRGB(x, y + 1)>>24) & 0xff;
						if (alphaBaixo > 100) imgSel.setRGB(x, y + 1, 0xffffff00);
					}
					
					if (y - 1 > 0) {
						int alphaCima = (imgSel.getRGB(x, y - 1)>>24) & 0xff;
						if (alphaCima > 100) imgSel.setRGB(x, y - 1, 0xffffff00);
					}
				} else {
					if (x == 0 || y == 0 || x == imgSel.getWidth() - 1 || y == imgSel.getHeight() - 1) imgSel.setRGB(x, y, 0xffffff00);
				}
				
			}
		}
		return imgSel;
		
	}
	
	public final BufferedImage getImgSel() {
		switch (tipo) {
		case AZUL:
			return imgAzSel;
		case PADRAO:
			return imgSel;
		case VERMELHO:
			return imgVerSel;
		default:
			return imgSel;
		}
	}
	
	public abstract String toString();
	
	public static void clear() {
		for (int i = 0; i < DimensionalObj.todosDimensionalObjs.size(); i++)  {
			if (DimensionalObj.todosDimensionalObjs.get(i) instanceof Chest) {
				DimensionalObj.todosDimensionalObjs.remove(i);
			}
		}			
	}
	
	public static void criarBau() {
		if (Principal.nivel % 3 == 0) {
			ChestPadrao bau  = new ChestPadrao(Util.randomInt(40, JanelaJogo.WIDTH - 40), Util.randomInt(40, JanelaJogo.HEIGHT - 40), ChestPadrao.PADRAO);
			bau.setCap(2);
			
			if (Principal.nivel > 3) {
				bau.addItem(Dropper.MEDIEVAL_HELMET, 5);
				bau.addItem(Dropper.MEDIEVAL_LEGS, 5);
				bau.addItem(Dropper.MEDIEVAL_ARMOR, 5);
				
				bau.addItem(Dropper.PLATE_ARMOR, 4);
				bau.addItem(Dropper.PLATE_BOOTS, 4);
				bau.addItem(Dropper.PLATE_HELMET, 4);
				
				bau.addItem(Dropper.GOLD_ARMOR, 3);
				bau.addItem(Dropper.GOLD_LEGS, 3);
				bau.addItem(Dropper.GOLD_HELMET, 3);
				bau.addItem(Dropper.GOLD_BOOTS, 3);
			}
			
			bau.addItem(Dropper.MOCHILA_CHIQUE, 30);
			bau.addItem(Dropper.COURO_BOTA, 6);
			bau.addItem(Dropper.COURO_LEGS, 6);
			bau.addItem(Dropper.COURO_ARMOR, 5);
			bau.addItem(Dropper.COURO_HELMET, 6);
			
			bau.addItem(Dropper.POTION_VIDA, 100, null);
			bau.addItem(Dropper.POTION_VIDA, 50, null);
			bau.addItem(Dropper.POTION_FORCA, 40, null);
			bau.setExp(75);
		}
		
		if (Principal.nivel % 8 == 0) {
			ChestPadrao bau  = new ChestPadrao(Util.randomInt(40, JanelaJogo.WIDTH - 40), Util.randomInt(40, JanelaJogo.HEIGHT - 40), ChestPadrao.AZUL);	
			bau.setCap(3);
			
			bau.addItem(Dropper.GOLD_ARMOR, 5);
			bau.addItem(Dropper.GOLD_LEGS, 5);
			bau.addItem(Dropper.GOLD_HELMET, 5);
			bau.addItem(Dropper.GOLD_BOOTS, 5);
			
			bau.addItem(Dropper.MEDIEVAL_BOTA, 8);
			bau.addItem(Dropper.MEDIEVAL_HELMET, 8);
			bau.addItem(Dropper.MEDIEVAL_LEGS, 8);
			bau.addItem(Dropper.MEDIEVAL_ARMOR, 8);
			
			bau.addItem(Dropper.COURO_LEGS, 12);
			bau.addItem(Dropper.COURO_ARMOR, 12);
			bau.addItem(Dropper.COURO_HELMET, 12);
			
			bau.addItem(Dropper.PLATE_ARMOR, 6);
			bau.addItem(Dropper.PLATE_BOOTS, 6);
			bau.addItem(Dropper.PLATE_HELMET, 6);
			bau.addItem(Dropper.PLATE_LEGS, 6);
			
			bau.addItem(Dropper.MOCHILA_MAL, 20, null);
			bau.addItem(Dropper.MOCHILA_CHIQUE, 50);
				
			bau.addItem(Dropper.POTION_VIDA, 100, null);
			bau.addItem(Dropper.POTION_VIDA, 50, null);
			bau.addItem(Dropper.POTION_FORCA, 100, null);
			bau.addItem(Dropper.POTION_FORCA, 20, null);
			bau.setExp(250);
		}
	}
	
	public synchronized void abrir() {
		isAbrindo = true;
		ListenerManager.removeListener(ListenerManager.MOUSE_PRESSED, mousePressed);
	}
	
	public void update() {
		checaSel();
		updateResto();
	}
	
	protected abstract void updateResto();

	public void setCap(int cap) {
		dropper.setCap(cap);
	}
	
	protected void checaSel() {
		if (JanelaJogo.xMouse > getX() && JanelaJogo.xMouse < getX() + width) {
			if (JanelaJogo.yMouse > getY() && JanelaJogo.yMouse < getY() + height) {
				isSel = true;
				return;
			}
		}
		
		isSel = false;
	}
	
	public final void addItem(String item, double chance) {
		dropper.addItem(item, chance);
	}
	
	public final void addItem(String item, double chance, Void semCap) {
		dropper.addItem(item, chance, null);
	}
	
	public void pintar(Graphics2D g) {
		if (isAbrindo) return;
		
		if (isSel) {
			g.drawImage(getImgSel(), getX(),  getY(), null);
		} else {
			g.drawImage(getImg(), getX(),  getY(), null);
		}
	}

	public abstract BufferedImage getImg();
}
