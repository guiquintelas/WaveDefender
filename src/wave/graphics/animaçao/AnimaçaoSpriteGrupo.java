package wave.graphics.animaçao;

import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.mob.Mob;

public class AnimaçaoSpriteGrupo {
	private ArrayList<AnimaçaoSprite> anis = new ArrayList<AnimaçaoSprite>();
	
	public AnimaçaoSpriteGrupo() {
		
	}
	
	public void add(AnimaçaoSprite ani) {
		anis.add(ani);
	}
	
	public void remove(AnimaçaoSprite ani) {
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
	
	public void stopAll(AnimaçaoSprite ani) {
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
