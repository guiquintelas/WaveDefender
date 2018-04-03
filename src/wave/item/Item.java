package wave.item;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.graphics.BalaoDeFala;
import wave.graphics.Sombra;
import wave.graphics.light.Luz;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.mob.Player;
import wave.principal.DimensionalObj;
import wave.principal.JanelaJogo;

public abstract class Item extends DimensionalObj {
	public static final RandomSFXGrupo drinkSFX = new RandomSFXGrupo(new String[]{"/SFX/potDrink1.ogg", "/SFX/potDrink2.ogg", "/SFX/potDrink3.ogg"});
	private static final RandomSFXGrupo dropSFX = new RandomSFXGrupo(new String[]{"/SFX/drop1.ogg", "/SFX/drop2.ogg", "/SFX/drop3.ogg"});
	
	public static ArrayList<Item> todosItens = new ArrayList<Item>();
	protected BufferedImage imgSel = criarImgSel();
	
	protected boolean isSel = false;
	protected boolean isNoChao = true;
	
	protected Luz luz;
	
	//listener
	private MouseListener mousePressed;
	
	public Item(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		this.width = width; 
		this.height = height;
		if (x > JanelaJogo.WIDTH - width) this.x = JanelaJogo.WIDTH - width;
		if (y > JanelaJogo.HEIGHT- height) this.y = JanelaJogo.HEIGHT - height;
		
		sombra = new Sombra(this);
		todosItens.add(this);
		
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed = new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getButton() == 3) {
					if (e.getX() > getX() && e.getX() < getX() + Item.this.width) {
						if (e.getY() > getY() && e.getY() < getY() + Item.this.height) {
							double dist = Math.sqrt(Math.pow(getXCentro() - Player.getPlayer().getXCentro(), 2) + Math.pow(getYCentro() - Player.getPlayer().getYCentro(), 2));
							if (dist < 70) {
								Player.getPlayer().pegarItem(Item.this);
							} else {
								Player.semMana.play();
								new BalaoDeFala("Preciso estar perto do item para pega-lo!", Player.getPlayer(), 100);
							}
						}
					}
				}
				
				mousePressed();
			}
		}); 
		
		prontoParaPintar();
		luz = initLuz();
		
	}
	
	protected Luz initLuz() {
		return new Luz(this, 30, 200, 200, 200, 50, 100, true, false, 5, 0);
	}
	
	public final void update() {
		checaSel();
		updateResto();
	}
	
	protected void mousePressed() {
		
	}
	
	protected abstract void updateResto();
	
	public void pintar(Graphics2D g) {
		if (isSel) {
			g.drawImage(getImgSel(), getX(),  getY(), null);
		} else {
			g.drawImage(getImg(), getX(),  getY(), null);
		}
	}
	
	public abstract String toString();
	
	public abstract String getText(); 
	
	protected void checaSel() {
		if (JanelaJogo.xMouse > getX() && JanelaJogo.xMouse < getX() + width) {
			if (JanelaJogo.yMouse > getY() && JanelaJogo.yMouse < getY() + height) {
				if (!isSel) {
					luz.forçaVar.variar(false);
					luz.forçaVar.fadeIn(luz.getForça(), 100, 25);
					luz.forçaVar.variar(true);
				}
				isSel = true;
				
				return;
			}
		}
		
		if (isSel) {
			luz.forçaVar.variar(false);
			luz.forçaVar.fadeOut(luz.getForça(), 50, 25);
			luz.forçaVar.variar(true);
		}
		isSel = false;
	}
	
	public void removerDoChao() {
		if (isNoChao) {
			todosItens.remove(this);
			DimensionalObj.todosDimensionalObjs.remove(this);
			ListenerManager.removeListener(ListenerManager.MOUSE_PRESSED, mousePressed);
			isNoChao = false;
		}
		
	}
	
	public void botarNoChao(int x, int y) {
		if (!isNoChao) {
			this.x = x;
			this.y = y;
			
			if (x < 0) x = 0;
			if (y < 0) y = 0;
			if (x > JanelaJogo.WIDTH - width) this.x = JanelaJogo.WIDTH - width;
			if (y > JanelaJogo.HEIGHT- height) this.y = JanelaJogo.HEIGHT - height;
		
			dropSFX.play();
			todosItens.add(this);
			DimensionalObj.todosDimensionalObjs.add(this);
			ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed);
			isNoChao = true;
		}
		
	}
	
	
	public static void clear() {	
		while (todosItens.size() != 0) {
			todosItens.get(0).removerDoChao();
		}
	}
	
	public abstract BufferedImage getImg();
	
	public final BufferedImage getImgSel() {
		return imgSel;
	}
	
	private BufferedImage criarImgSel() {
		BufferedImage imgSel = new BufferedImage(getImg().getWidth(), getImg().getHeight(), getImg().getType());
		imgSel.getGraphics().drawImage(getImg(), 0, 0, null);
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
					if (x == 0 || y == 0 || x == imgSel.getWidth() - 1 || y == imgSel.getHeight() - 1) {
						imgSel.setRGB(x, y, 0xffffff00);
					}
				}
				
			}
		}
		return imgSel;
		
	}
}
