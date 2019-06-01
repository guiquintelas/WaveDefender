package wave.mob;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.audio.RandomSFXGrupo;
import wave.graphics.BalaoDeFala;
import wave.graphics.NovoStatusAnimacao;
import wave.graphics.animacao.AnimacaoListener;
import wave.graphics.animacao.AnimacaoSprite;
import wave.graphics.animacao.AnimacaoSpriteGrupo;
import wave.item.Dropper;
import wave.particula.CriadorDeParticulas;
import wave.principal.Principal;
import wave.projetil.ProjetilGround;
import wave.tools.Util;

public class Ogro extends Monstro {
	private static final int DELAY_REGENERAcAO = 150;
	private static final int DELAY_TOMAR_HIT_ANI = 10;
	private static final int DELAY_ATACAR_ANI = 16;
	private static final int WIDTH_SPRITE = 55;
	private static final int HEIGHT_SPRITE = 90;

	private boolean isTomandoHit = false;
	private boolean isAtkando = false;
	private static boolean primeiroOgro = true;

	private static final BufferedImage bitmap = Util.carregarImg("/Sprites/ogroBitmap.png");
	private final AnimacaoSpriteGrupo grupoAni = new AnimacaoSpriteGrupo();
	private static final ArrayList<BufferedImage> spritesParado = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesAndando = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesTomarHit = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesAtkando = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesParadoFE = Util.carregarArrayBI(bitmap, 0, 0, 55, 90, 6);
	private static final ArrayList<BufferedImage> spritesParadoFD = Util.carregarArrayBI(bitmap, 0, 90, 55, 90, 6);
	private static final ArrayList<BufferedImage> spritesParadoTD = Util.carregarArrayBI(bitmap, 0, 180, 55, 90, 6);
	private static final ArrayList<BufferedImage> spritesParadoTE = Util.carregarArrayBI(bitmap, 0, 270, 55, 90, 6);
	private final AnimacaoSprite paradoFE = new AnimacaoSprite(spritesParadoFE, 9, grupoAni);
	private final AnimacaoSprite paradoFD = new AnimacaoSprite(spritesParadoFD, 9, grupoAni);
	private final AnimacaoSprite paradoTE = new AnimacaoSprite(spritesParadoTE, 9, grupoAni);
	private final AnimacaoSprite paradoTD = new AnimacaoSprite(spritesParadoTD, 9, grupoAni);

	private static final ArrayList<BufferedImage> spritesAndandoFE = Util.carregarArrayBI(bitmap, 0, 360, 60, 90, 6);
	private static final ArrayList<BufferedImage> spritesAndandoFD = Util.carregarArrayBI(bitmap, 0, 450, 60, 90, 6);
	private static final ArrayList<BufferedImage> spritesAndandoTD = Util.carregarArrayBI(bitmap, 0, 540, 60, 90, 6);
	private static final ArrayList<BufferedImage> spritesAndandoTE = Util.carregarArrayBI(bitmap, 0, 630, 60, 90, 6);
	private final AnimacaoSprite andandoFE = new AnimacaoSprite(spritesAndandoFE, 8, grupoAni);
	private final AnimacaoSprite andandoFD = new AnimacaoSprite(spritesAndandoFD, 8, grupoAni);
	private final AnimacaoSprite andandoTE = new AnimacaoSprite(spritesAndandoTE, 8, grupoAni);
	private final AnimacaoSprite andandoTD = new AnimacaoSprite(spritesAndandoTD, 8, grupoAni);

	private static final ArrayList<BufferedImage> spritesAtkFE = Util.carregarArrayBI(bitmap, 0, 720, 67, 90, 4);
	private static final ArrayList<BufferedImage> spritesAtkFD = Util.carregarArrayBI(bitmap, 0, 810, 67, 90, 4);
	private static final ArrayList<BufferedImage> spritesAtkTE = Util.carregarArrayBI(bitmap, 0, 900, 55, 90, 4);
	private static final ArrayList<BufferedImage> spritesAtkTD = Util.carregarArrayBI(bitmap, 0, 990, 55, 90, 4);
	private final AnimacaoSprite atkFE = new AnimacaoSprite(spritesAtkFE, 5, grupoAni);
	private final AnimacaoSprite atkFD = new AnimacaoSprite(spritesAtkFD, 5, grupoAni);
	private final AnimacaoSprite atkTE = new AnimacaoSprite(spritesAtkTE, 5, grupoAni);
	private final AnimacaoSprite atkTD = new AnimacaoSprite(spritesAtkTD, 5, grupoAni);

	private static final ArrayList<BufferedImage> spritesTomandoHitFE = Util.carregarArrayBI(bitmap, 63, 1080, 63, 90, 1);
	private static final ArrayList<BufferedImage> spritesTomandoHitFD = Util.carregarArrayBI(bitmap, 189, 1080, 63, 90, 1);
	private static final ArrayList<BufferedImage> spritesTomandoHitTE = Util.carregarArrayBI(bitmap, 63, 1170, 63, 90, 1);
	private static final ArrayList<BufferedImage> spritesTomandoHitTD = Util.carregarArrayBI(bitmap, 189, 1170, 63, 90, 1);
	private final AnimacaoSprite tomarHitFE = new AnimacaoSprite(spritesTomandoHitFE, 10, grupoAni);
	private final AnimacaoSprite tomarHitFD = new AnimacaoSprite(spritesTomandoHitFD, 10, grupoAni);
	private final AnimacaoSprite tomarHitTE = new AnimacaoSprite(spritesTomandoHitTE, 10, grupoAni);
	private final AnimacaoSprite tomarHitTD = new AnimacaoSprite(spritesTomandoHitTD, 10, grupoAni);


	private Timer timerRegeneracao;
	private Timer timerTomandoHit;
	private Timer timerAtkando;

	private CriadorDeParticulas particulaRegen;

	//Audio SFX
	private static final RandomSFXGrupo roar = new RandomSFXGrupo(new String[] { "/SFX/roar1.ogg", "/SFX/roar2.ogg", "/SFX/roar3.ogg" });

	public Ogro() {
		super(25 + (Principal.nivel * 10), 125, 15 + (int) (Math.random() * 5), HEIGHT_SPRITE, Util.randomDouble(1, 1.5), Util.randomDouble(0.6, 1.2), Util.randomInt(1, 3) + Principal.nivel / 3);
		this.width = WIDTH_SPRITE;
		this.height = 25;
		carregarBitmap();

		particulaRegen = new CriadorDeParticulas(getX(), getYSprite(), width, heightSprite, 2, 5, new Color(0, 250, 154), 100);
		particulaRegen.setComLuz(8, new Color(0, 250, 154), 70, 15, 50);
		particulaRegen.addColorLuz(Color.GREEN);
		
		andandoFD.setOffYBruto(10);
		andandoFE.setOffYBruto(10);
		andandoFE.setOffXBruto(-10);
		andandoTD.setOffYBruto(10);
		andandoTE.setOffYBruto(10);
		paradoFD.setOffYBruto(10);
		paradoFD.setOffXBruto(-5);
		paradoFE.setOffYBruto(10); 
		paradoFE.setOffXBruto(-5);
		paradoTD.setOffYBruto(10);
		paradoTE.setOffYBruto(10);
		
		atkFD.setOffYBruto(5);
		atkFE.setOffYBruto(5);
		atkTD.setOffYBruto(5);
		atkTE.setOffYBruto(5);
		
		AnimacaoListener atk = new AnimacaoListener() {
			public void metodo() {
				Player.getPlayer().tomarKnockBack(Ogro.this.angulo);
				Player.getPlayer().tomarHit(getDanoVar());
			}
		};
		
		atkFE.setVoid(2, atk);
		atkFD.setVoid(2, atk);
		atkTE.setVoid(2, atk);
		atkTD.setVoid(2, atk);
		
		tomarHitFD.setOffXBruto(-5);
		
		if (primeiroOgro) {
			primeiroOgro = false;
			new BalaoDeFala("MUAHAHAHA! Você nunca conseguirá passar dessa fase!" , this, 250);
		}
	}
	
	protected void updateResto() {
		updateSprite();		
		atiraGround();
		grupoAni.updateAll(this);
		regen();
		particulaRegen.update(getX(), getYSprite());
	}
	
	protected int expValor() {
		return 40;
	}

	public int tomarHit(int hit) {
		int hitDefesa = super.tomarHit(hit);
		if (hitDefesa != 0) {
			isTomandoHit = true;

			final int tickAtual = Principal.tickTotal;
			timerTomandoHit = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= tickAtual + DELAY_TOMAR_HIT_ANI) {
						isTomandoHit = false;
						timerTomandoHit.stop();
					}

				}
			});
			timerTomandoHit.start();
		}
		return hitDefesa;
	}
	
	private void atiraGround() {
		int random = 1 + (int)(Math.random() * 1000);
		if (random <= 2 && isFurioso && ProjetilGround.todosGProjetils.size() < 2) {
			new ProjetilGround(getXCentro(), getYSpriteCentro(), getDanoVar());
			lancaProjetilSFX.play();
		}
	}

	protected void tentarAtacarMelee() {
		if (isAtkAReady) {
			isAtkando = true;

			final int tickAtual = Principal.tickTotal;
			timerAtkando = new Timer(5, new ActionListener() {
				int proximoTick = Principal.tickTotal + 4;

				public void actionPerformed(ActionEvent e) {

					if (Principal.tickTotal >= tickAtual + DELAY_ATACAR_ANI) {
						isAtkando = false;
						timerAtkando.stop();
					}
					if (Principal.tickTotal >= proximoTick + 4) {
						proximoTick += 4;
					}

				}
			});
			timerAtkando.start();
			super.tentarAtacarMelee();
		}
	}
	
	protected void atacarMelee() {
		
	}

	private void updateSprite() {
		if (isAtkando) {
			if (angulo >= 0 && angulo < 90) {
				grupoAni.stopAll(atkTD);
				atkTD.start(this);
			}

			if (angulo >= 90 && angulo < 180) {
				grupoAni.stopAll(atkTE);
				atkTE.start(this);
			}

			if (angulo >= 180 && angulo < 270) {
				grupoAni.stopAll(atkFE);
				atkFE.start(this);
			}

			if (angulo >= 270 && angulo <= 360) {
				grupoAni.stopAll(atkFD);
				atkFD.start(this);
			}
			return;
		}
		
		if (isTomandoHit) {
			if (angulo >= 0 && angulo < 90) {
				grupoAni.stopAll(tomarHitTD);
				tomarHitTD.start(this);
			}

			if (angulo >= 90 && angulo < 180) {
				grupoAni.stopAll(tomarHitTE);
				tomarHitTE.start(this);
			}

			if (angulo >= 180 && angulo < 270) {
				grupoAni.stopAll(tomarHitFE);
				tomarHitFE.start(this);
			}

			if (angulo >= 270 && angulo <= 360) {
				grupoAni.stopAll(tomarHitFD);
				tomarHitFD.start(this);
			}
			return;
		}
		
		if (isAndando || isChase) {
			if (angulo >= 0 && angulo < 90) {
				grupoAni.stopAll(andandoTD);
				andandoTD.start(this);
			}

			if (angulo >= 90 && angulo < 180) {
				grupoAni.stopAll(andandoTE);
				andandoTE.start(this);
			}

			if (angulo >= 180 && angulo < 270) {
				grupoAni.stopAll(andandoFE);
				andandoFE.start(this);
			}

			if (angulo >= 270 && angulo <= 360) {
				grupoAni.stopAll(andandoFD);
				andandoFD.start(this);
			}
		} else {
			if (angulo >= 0 && angulo < 90) {
				grupoAni.stopAll(paradoTD);
				paradoTD.start(this);
			}

			if (angulo >= 90 && angulo < 180) {
				grupoAni.stopAll(paradoTE);
				paradoTE.start(this);
			}

			if (angulo >= 180 && angulo < 270) {
				grupoAni.stopAll(paradoFE);
				paradoFE.start(this);
			}

			if (angulo >= 270 && angulo <= 360) {
				grupoAni.stopAll(paradoFD);
				paradoFD.start(this);
			}
		}
		
		

	}

	private void carregarBitmap() {
		if (!spritesAndando.isEmpty()) {
			return;
		}

		//carregando imagens paradas
		for (int y = 0; y < 359; y += heightSprite) {
			for (int x = 0; x <= 319; x += width) {
				spritesParado.add(bitmap.getSubimage(x, y, width, heightSprite));

			}
		}

		//carregando imagens se movendo
		for (int y = 360; y < 719; y += heightSprite) {
			for (int x = 0; x <= 359; x += 60) {
				spritesAndando.add(bitmap.getSubimage(x, y, 60, heightSprite));
			}
		}

		//carregando imagens tomando hit
		for (int y = 1080; y < 1259; y += heightSprite) {
			for (int x = 0; x <= 251; x += 63) {
				spritesTomarHit.add(bitmap.getSubimage(x, y, 63, heightSprite));
			}
		}

		//carregano imagens atacando
		for (int y = 720; y < 1079; y += heightSprite) {
			if (y <= 810) {
				for (int x = 0; x <= 267; x += 67) {
					spritesAtkando.add(bitmap.getSubimage(x, y, 67, heightSprite));
				}
			} else {
				for (int x = 0; x <= 219; x += 55) {
					spritesAtkando.add(bitmap.getSubimage(x, y, 55, heightSprite));
				}
			}

		}
	}

	protected Color sangueCor() {
		return new Color(107, 142, 35);
	}

	public Color getCor() {
		return new Color(50, 205, 50);
	}

	public String toString() {
		return "Senhora Orc";
	}

	private void regen() {
		if (getVida() != getVidaMax()) {
			if (timerRegeneracao != null) {
				if (timerRegeneracao.isRunning()) {
					return;
				}
			}

			timerRegeneracao = new Timer(5, new ActionListener() {
				int proximoTick = Principal.tickTotal + DELAY_REGENERAcAO;

				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= proximoTick) {

						double vidaParaCurar = Math.abs(getVida() - getVidaMax()) / 5.0;

						if (vidaParaCurar > 0 && vidaParaCurar < 1) {
							vidaParaCurar = 1;
						}

						curar((int) vidaParaCurar);

						if (vidaParaCurar != 0) {
							particulaRegen.setProduzindo(true, 15);
						}

						proximoTick += DELAY_REGENERAcAO;
					}

					if (!isVivo()) {
						timerRegeneracao.stop();
					}

				}
			});
			timerRegeneracao.start();

		}
	}

	protected boolean checaVivo() {
		if (!super.checaVivo()) {
			new NovoStatusAnimacao(getX(), getY(), "GROARRR!");
			roar.play();
			

			
			for (int x = 0; x < Monstro.todosMontros.size(); x++) {
				if (Monstro.todosMontros.get(x) != null) {
					if (!Monstro.todosMontros.get(x).isFurioso) {
						Monstro.todosMontros.get(x).setFurioso(true);
						Monstro.todosMontros.get(x).setSpeed(Monstro.todosMontros.get(x).speed + 0.8);
						new NovoStatusAnimacao(Monstro.todosMontros.get(x).getX(), Monstro.todosMontros.get(x).getY(), "GROARRR!");
						break;
					}
				}
			}
			return false;
		}

		return true;
	}
	
	protected void droppar() {
		dropper.setCap(1);
		dropper.addItem("MochilaAdv", 15, null);
		dropper.addItem("Potion de Forca", 10, null);
		dropper.addItem(Dropper.MOCHILA_CHIQUE, 3);
		dropper.addItem(Dropper.MEDIEVAL_ARMOR, 5);
		dropper.addItem(Dropper.MEDIEVAL_HELMET, 5);
		dropper.addItem(Dropper.COURO_ARMOR, 6);
		dropper.addItem(Dropper.COURO_HELMET, 6);
		dropper.addItem(Dropper.PLATE_ARMOR, 3);
		dropper.addItem(Dropper.PLATE_HELMET, 3);
		dropper.addItem(Dropper.GOLD_ARMOR, 1.5);
		dropper.addItem(Dropper.GOLD_HELMET, 1.5);
		super.droppar();
	}

	public void pintarMob(Graphics2D g) {
		grupoAni.pintarAll(g);

	}

}
