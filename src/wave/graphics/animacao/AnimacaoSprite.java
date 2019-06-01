package wave.graphics.animacao;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import wave.mob.Mob;
import wave.principal.Principal;

public class AnimacaoSprite {
	private double x;
	private double y;
	private int[] offX;
	private int[] offY;
	private int offXBruto = 0;
	private int offYBruto = 0;
	private float scale = 1;

	private boolean jaUsouMetodo = false;
	
	private ArrayList<BufferedImage> sprites;
	private HashMap<Integer, AnimacaoListener> listeners = new HashMap<Integer, AnimacaoListener>();
	private int index = 0;
	private int delay;
	private int tickAtual;
	
	private boolean isAtivo = false;
	
	public AnimacaoSprite(ArrayList<BufferedImage> sprites, int delay) {
		this.sprites = sprites;
		this.delay = delay;
		
		offX = new int[sprites.size()];
		offY = new int[sprites.size()];
		
		for (int i = 0; i < sprites.size(); i++) {
			offX[i] = 0;
			offY[i] = 0;
		}
	}
	
	public AnimacaoSprite(ArrayList<BufferedImage> sprites, int delay, AnimacaoSpriteGrupo grupo) {
		this.sprites = sprites;
		this.delay = delay;
		
		offX = new int[sprites.size()];
		offY = new int[sprites.size()];
		
		for (int i = 0; i < sprites.size(); i++) {
			offX[i] = 0;
			offY[i] = 0;
		}
		
		grupo.add(this);
	}

	public void update(Mob mob) {
		if (isAtivo) {
			updateXY(mob);
			updateSprite();
			executarMetodo();
		}
		
	}
	
	private void executarMetodo() {
		if (listeners.get(index) != null && !jaUsouMetodo) {
			listeners.get(index).metodo();
			jaUsouMetodo = true;
		}
		
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setDelay(int delayTick) {
		this.delay = delayTick;
	}
	
	public int getDelay() {
		return delay;
	}

	public void setVoid(int index, AnimacaoListener aniListener) {
		listeners.put(index, aniListener);
	}
	
	public void setOffXBruto(int x) {
		offXBruto = x;
	}
	
	public void setOffYBruto(int y) {
		offYBruto = y;
	}
	
	public void setOffX(int index, int x) {
		offX[index] = x;
	}
	
	public void setOffY(int index, int y) {
		offY[index] = y;
	}
	
	public void clearOffX() {
		for (int i = 0; i < sprites.size(); i++) {
			offX[i] = 0;
		}
	}
	
	public void clearOffY() {
		for (int i = 0; i < sprites.size(); i++) {
			offY[i] = 0;
		}
	}
	
	public boolean isAtivo() {
		return isAtivo;
	}
	
	public void start(Mob mob) {	
		if (isAtivo) return;
		isAtivo = true;
		index = 0;
		tickAtual = Principal.tickTotal;
		updateXY(mob);
	}
	
	public void stop() {
		isAtivo = false;
	}

	private void updateSprite() {
		if (Principal.tickTotal >= tickAtual + delay) {
			index++;
			jaUsouMetodo = false;
			if (index > sprites.size() - 1) {
				index = 0;
			}
			
			
			tickAtual = Principal.tickTotal;
		}
		
	}

	private void updateXY(Mob mob) {
		this.x = mob.getXDouble() + offX[index] + offXBruto;
		this.y = mob.getYSprite() + offY[index] + offYBruto;
	}

	public void pintar(Graphics2D g) {
		if (isAtivo) {
			
			try {
				g.drawImage(sprites.get(index), (int)x, (int)y, (int)(sprites.get(index).getWidth() * scale), (int)(sprites.get(index).getHeight() * scale), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}	
	}
	
}
