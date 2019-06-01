package wave.projetil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.graphics.animacao.Animacao;
import wave.graphics.animacao.AnimacaoListener;
import wave.graphics.imgprocessing.Filter;
import wave.graphics.light.Luz;
import wave.gui.menus.MenuLevel;
import wave.mob.Monstro;
import wave.mob.Player;
import wave.particula.CriadorDeParticulas;
import wave.tools.ActionQueue;
import wave.tools.Util;

public class AtkPlayer extends AutoAtkProjetil {
	private static final ArrayList<BufferedImage> imgsColisao = Util.carregarArrayBI(Animacao.explosaoSrite, 0, 256, 64, 64, 39);
	private static final BufferedImage spriteSheet = Util.carregarImg("/Sprites/playerAtkAni.png");
	private static final BufferedImage img = spriteSheet.getSubimage(85, 0, 17, 17);
	private static final BufferedImage imgVeneno = Filter.soVerde(img, 0);
	private static final ArrayList<BufferedImage> aniImgs = Util.carregarArrayBI(spriteSheet, 0, 0, 17, 17, 6);
	private static final ArrayList<BufferedImage> aniImgsVeneno = Util.carregarArrayBI(Filter.soVerde(spriteSheet, 0), 0, 0, 17, 17, 6);
	private Animacao ani;
	
	private boolean isVeneno = false;
	
	//private SombraDinamica sombraD;
	private CriadorDeParticulas particulas;
	private static RandomSFXGrupo sfxs = new RandomSFXGrupo(new String[]{"/SFX/atk1.ogg", "/SFX/atk2.ogg", "/SFX/atk3.ogg"});

	public AtkPlayer(double angulo, boolean sfx) {
		super(Player.getPlayer(), angulo);
		this.width = 5;
		this.height = 5;
		this.speed = 1.5;
		
		particulas = new CriadorDeParticulas(getX(), getY(), width, height, 1, 1, Color.BLACK, 40);
		particulas.setSpeed(0);
		
		
		if (Player.getPlayer().isBuffDano()) particulas.addColor(new Color(139, 69, 19));
		
		
		int random = Util.randomInt(1, 100);
		if (random < MenuLevel.getChanceVenenoMod()) {
			//random < MenuLevel.getChanceVenenoMod()
			isVeneno = true;
			particulas.addColor(Color.GREEN);
			particulas.removeColor(Color.BLACK);
			particulas.setPorcentagem(80);
			particulas.setComLuz(5, Color.GREEN, 100, 5, 10);
			particulas.setAlphaVar(10);
			new Luz(this, 20, 40, 200, 40, 80, 25);
		} else {
			new Luz(this, 20, 255, 255, 255, 50, 25);
			particulas.setComLuz(5, Color.GRAY, 50, 5, 20);
			particulas.setAlphaVar(10);
		}
		
		if (isVeneno) {
			ani = new Animacao(getXCentro(), getYCentro(), aniImgsVeneno, 3);
		} else {
			ani = new Animacao(getXCentro(), getYCentro(), aniImgs, 3);
		}
		
		ani.setSeguindo(this);
		ani.setAutoPaint(false);
		ani.setVoid(5, new AnimacaoListener() {
			public void metodo() {
				speed = 5;
				particulas.setProduzindo(true);
			}
		});
		ani.start();
		
		//sfxs.setVolume(0.5f);
		//sombraD = new SombraDinamica(this, getXCentro(), getYCentro(), Player.getPlayer().getAlcance());
		//sombra = sombraD.sombra;
		//sombra.setHeightOff(8);
		if (sfx) sfxs.play();
	}

	protected void checaColisao() {
		for (int x = 0; x < Monstro.todosMontros.size(); x++) {
			Monstro monstroAtual = Monstro.todosMontros.get(x);

			if (Math.abs(getXCentro() - monstroAtual.getXCentro()) < ((width / 2) + (monstroAtual.getWidth() / 2))) {
				if (Math.abs(getYCentro() - monstroAtual.getYCentro()) < ((height / 2) + (monstroAtual.getHeight() / 2))) {
					int danoDado = monstroAtual.tomarHit(dano);

					if (isVeneno) {
						monstroAtual.setEnvenenadoTrue(3, dano/3);
					}
					
					monstroAtual.tomarKnockBack(angulo);
					monstroAtual.setFurioso(true);
					particulas.setProduzindo(false);
					
					Animacao ani = new Animacao(getXCentro(), getYCentro(), imgsColisao, 1);
					ani.setScale(getScalePorDano(danoDado));
					ani.start();
					
					final Luz luzAni = new Luz(ani, ani.getWidth() / 2 + 5, 255, 220, 200, 100, 15, true, true, 0, 0);
					luzAni.forcaVar.addAcaoNaFila(new ActionQueue() {
						public boolean action() {
							luzAni.desativar(15);
							return true;
						}
					});
					
					removeProjetil();
					
				}
			}
		}

	}
	
	private float getScalePorDano(int dano) {
		float scale = (dano * (3/6.0f))/10f;
		if (scale > .75f) {
			return .75f;
		}
		
		if (scale < .4f) {
			return .4f;
		}
		
		return scale;
	}

	public void pintar(Graphics2D g) {
		//g.setColor(Color.BLACK);
	//	g.fillOval(getX() - width / 2, getY() - height / 2, width, height);
		
		if (ani.isRodando()) {
			ani.pintarManual(g);
		} else {
			if (isVeneno) {
				g.drawImage(imgVeneno, getXCentro() - img.getWidth() / 2, getYCentro() - img.getHeight() / 2, null);
			} else {
				g.drawImage(img, getXCentro() - img.getWidth() / 2, getYCentro() - img.getHeight() / 2, null);
			}
		}
		
		//g.drawImage(Filter.soVerde(spriteSheet, 0), 50, 50, spriteSheet.getWidth() * 4, spriteSheet.getHeight() * 4, null);
		
	}

	protected void updateResto() {
		if (particulas != null) {
			particulas.update(getX(), getY());
		}
	//	sombraD.update();
		
	}

	@Override
	protected void colisao() {
		// TODO Auto-generated method stub
		
	}

}
