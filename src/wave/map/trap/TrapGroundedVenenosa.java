package wave.map.trap;

import wave.map.Tile;
import wave.mob.Mob;

public class TrapGroundedVenenosa extends Trap{
	
	private int dano;
	private int ticks;
	
	public TrapGroundedVenenosa(int danoBase, int ticks, Tile tile) {
		super(tile);
		this.dano = danoBase;
		this.ticks = ticks;

	}

	public void colisao(Mob mob) {
		mob.tomarHit(dano);
		mob.setEnvenenadoTrue(ticks, dano / 2);
		mob.setGroundedTrue(dano * 5);
		Trap.colisaoTrap.play();
		super.colisao(mob);
	}

}
