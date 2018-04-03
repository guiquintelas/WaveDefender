package wave.map.trap;


import wave.map.Tile;
import wave.mob.Mob;

public class TrapVenenosa extends Trap {	
	private int dano;
	private int ticks;
	
	public TrapVenenosa(int danoBase, int ticks, Tile tile) {
		super(tile);
		this.dano = danoBase;
		this.ticks = ticks;

	}

	public void colisao(Mob mob) {
		mob.tomarHit(dano);
		mob.setEnvenenadoTrue(ticks, dano / 2);
		Trap.colisaoTrap.play();
		super.colisao(mob);
	}

}
