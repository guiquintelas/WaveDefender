package wave.map.trap;

import wave.graphics.light.Luz;
import wave.gui.menus.MenuLevel;
import wave.map.Tile;
import wave.mob.Mob;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;

public class TrapFogo extends Trap{
	private int dano;
	private int x;
	private int y;
	private CriadorDeParticulas fogo;
	private Luz luzFogo;

	public TrapFogo(Tile tile, int dano) {
		super(tile);
		this.dano = dano;
		this.x = tile.getX();
		this.y = tile.getY();
		fogo = CDPProntos.fogo(null, x, y, Tile.WIDTH, Tile.HEIGHT);
		fogo.setProduzindo(true);
		
		luzFogo = new Luz(this, 20, 255, 150, 0, 80, 25, false, true, 0, 25);
	}
	
	public void colisao(Mob mob) {
		super.colisao(mob);
		mob.tomarHitSemDefesa(dano);
		mob.setFire(dano/2, 3, MenuLevel.getTrapFireLevel());
		fogo.setProduzindo(false);
		luzFogo.desativar(25);
	}



	@Override
	public void update() {
		if (fogo != null) {
			fogo.update(x, y);
		}
	}
	
	

}
