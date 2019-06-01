package wave.graphics.animacao;

import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.mob.Mob;

public class AnimacaoSpriteGrupo {
	private ArrayList<AnimacaoSprite> anis = new ArrayList<AnimacaoSprite>();
	
	public AnimacaoSpriteGrupo() {
		
	}
	
	public void add(AnimacaoSprite ani) {
		anis.add(ani);
	}
	
	public void remove(AnimacaoSprite ani) {
		anis.remove(ani);
	}
	
	public void setOffXBrutoAll(int off) {
		for (int i = 0; i < anis.size(); i++) {
			anis.get(i).setOffXBruto(off);
		}
	}
	
	public void setOffYBrutoAll(int off) {
		for (int i = 0; i < anis.size(); i++) {
			anis.get(i).setOffYBruto(off);
		}
	}
	
	public void setOffXAll(int index, int off) {
		for (int i = 0; i < anis.size(); i++) {
			anis.get(i).setOffX(index, off);
		}
	}
	
	public void setOffYAll(int index, int off) {
		for (int i = 0; i < anis.size(); i++) {
			anis.get(i).setOffY(index, off);
		}
	}
	
	public void stopAll() {
		for (int i = 0; i < anis.size(); i++) {
			if (anis.get(i).isAtivo()) {
				anis.get(i).stop();
			}
		}
	}
	
	public void stopAll(AnimacaoSprite ani) {
		for (int i = 0; i < anis.size(); i++) {
			if (anis.get(i).isAtivo() && anis.get(i) != ani) {
				anis.get(i).stop();
			}
		}
	}
	
	public void updateAll(Mob mob) {
		for (int i = 0; i < anis.size(); i++) {
			anis.get(i).update(mob);
		}
	}
	
	public void pintarAll(Graphics2D g) {
		for (int i = 0; i < anis.size(); i++) {
			anis.get(i).pintar(g);
		}
	}
}
