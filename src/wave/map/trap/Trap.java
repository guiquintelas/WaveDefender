package wave.map.trap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.graphics.animacao.Animacao;
import wave.map.Tile;
import wave.mob.Mob;
import wave.particula.CriadorDeParticulas;
import wave.principal.DimensionalObj;
import wave.tools.Util;

public abstract class Trap extends DimensionalObj{
	public static ArrayList<Trap> todasTraps = new ArrayList<Trap>();
	
	public static final RandomSFXGrupo criarTrap = new RandomSFXGrupo(new String[]{"/SFX/botarTrap1.ogg", "/SFX/botarTrap2.ogg"});
	public static final RandomSFXGrupo colisaoTrap = new RandomSFXGrupo(new String[]{"/SFX/trap1.ogg", "/SFX/trap2.ogg", "/SFX/trap3.ogg"});
	
	protected static final BufferedImage[] imgTrap = {Util.carregarImg("/Sprites/trap.png"), Util.carregarImg("/Sprites/trap2.png")};
	private int index;
	
	protected CriadorDeParticulas particulasCriacao;
	
	public Trap(Tile tile) {
		this.x = tile.getX();
		this.y = tile.getY();
		this.width = Tile.WIDTH;
		this.height = Tile.HEIGHT;
		particulasCriacao = new CriadorDeParticulas(tile.getX(), tile.getY(), Tile.WIDTH, Tile.HEIGHT, Animacao.peca1, 1.5f, 5, 100);
		particulasCriacao.addImgs(Animacao.peca3);
		particulasCriacao.addImgs(Animacao.peca4);
		particulasCriacao.setRotacaoVelVar(4, 1.5);
		particulasCriacao.setAlphaVar(20, 10);
		particulasCriacao.setAngulo(90, 45);
		particulasCriacao.setSpeedVar(1.4, .4);
		particulasCriacao.setAlphaDelay(25);
		particulasCriacao.setGravidadeRateVar(.04f, .01f);
		
		particulasCriacao.setProduzindoXYConstante(true, 5);
		
		Trap.criarTrap.play();
		todasTraps.add(this);
		
		this.index = Util.randomInt(0, imgTrap.length - 1);
		
		prontoParaPintar();
	}
	
	public void colisao(Mob mob) {
		Trap.todasTraps.remove(this);
	}
	
	public BufferedImage getImg() {
		return imgTrap[index];
	}
	
	@Override
	public String toString() {
		return "";
	}

	@Override
	public void pintar(Graphics2D g) {}
	
	public void update() {
		
	}
	
}
