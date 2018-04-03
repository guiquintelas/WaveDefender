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
import wave.graphics.NovoStatusAnima�ao;
import wave.graphics.anima�ao.Anima�aoListener;
import wave.graphics.anima�ao.Anima�aoSprite;
import wave.graphics.anima�ao.Anima�aoSpriteGrupo;
import wave.item.Dropper;
import wave.particula.CriadorDeParticulas;
import wave.principal.Principal;
import wave.projetil.ProjetilGround;
import wave.tools.Util;

public class Ogro extends Monstro {
	private static final int DELAY_REGENERA�AO = 150;
	private static final int DELAY_TOMAR_HIT_ANI = 10;
	private static final int DELAY_ATACAR_ANI = 16;
	private static final int WIDTH_SPRITE = 55;
	private static final int HEIGHT_SPRITE = 90;

	private boolean isTomandoHit = false;
	private boolean isAtkando = false;
	private static boolean primeiroOgro = true;

	private static final BufferedImage bitmap = Util.carregarImg("/Sprites/ogroBitmap.png");
	private final Anima�aoSpriteGrupo grupoAni = new Anima�aoSpriteGrupo();
	private static final ArrayList<BufferedImage> spritesParado = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesAndando = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesTomarHit = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesAtkando = new ArrayList<BufferedImage>();
	private static final ArrayList<BufferedImage> spritesParadoFE = Util.carregarArrayBI(bitmap, 0, 0, 55, 90, 6);
	private static final ArrayList<BufferedImage> spritesParadoFD = Util.carregarArrayBI(bitmap, 0, 90, 55, 90, 6);
	private static final ArrayList<BufferedImage> spritesParadoTD = Util.carregarArrayBI(bitmap, 0, 180, 55, 90, 6);
	private static final ArrayList<BufferedImage> spritesParadoTE = Util.carregarArrayBI(bitmap, 0, 270, 55, 90, 6);
	private final Anima�aoSprite paradoFE = new Anima�aoSprite(spritesParadoFE, 9, grupoAni);
	private final Anima�aoSprite paradoFD = new Anima�aoSprite(spritesParadoFD, 9, grupoAni);
	private final Anima�aoSprite paradoTE = new Anima�aoSprite(spritesParadoTE, 9, grupoAni);
	private final Anima�aoSprite paradoTD = new Anima�aoSprite(spritesParadoTD, 9, grupoAni);

	private static final ArrayList<BufferedImage> spritesAndandoFE = Util.carregarArrayBI(bitmap, 0, 360, 60, 90, 6);
	private static final ArrayList<BufferedImage> spritesAndandoFD = Util.carregarArrayBI(bitmap, 0, 450, 60, 90, 6);
	private static final ArrayList<BufferedImage> spritesAndandoTD = Util.carregarArrayBI(bitmap, 0, 540, 60, 90, 6);
	private static final ArrayList<BufferedImage> spritesAndandoTE = Util.carregarArrayBI(bitmap, 0, 630, 60, 90, 6);
	private final Anima�aoSprite andandoFE = new Anima�aoSprite(spritesAndandoFE, 8, grupoAni);
	private final Anima�aoSprite andandoFD = new Anima�aoSprite(spritesAndandoFD, 8, grupoAni);
	private final Anima�aoSprite andandoTE = new Anima�aoSprite(spritesAndandoTE, 8, grupoAni);
	private final Anima�aoSprite andandoTD = new Anima�aoSprite(spritesAndandoTD, 8, grupoAni);

	private static final ArrayList<BufferedImage> spritesAtkFE = Util.carregarArrayBI(bitmap, 0, 720, 67, 90, 4);
	private static final ArrayList<BufferedImage> spritesAtkFD = Util.carregarArrayBI(bitmap, 0, 810, 67, 90, 4);
	private static final ArrayList<BufferedImage> spritesAtkTE = Util.carregarArrayBI(bitmap, 0, 900, 55, 90, 4);
	private static final ArrayList<BufferedImage> spritesAtkTD = Util.carregarArrayBI(bitmap, 0, 990, 55, 90, 4);
	private final Anima�aoSprite atkFE = new Anima�aoSprite(spritesAtkFE, 5, grupoAni);
	private final Anima�aoSprite atkFD = new Anima�aoSprite(spritesAtkFD, 5, grupoAni);
	private final Anima�aoSprite atkTE = new Anima�aoSprite(spritesAtkTE, 5, grupoAni);
	private final Anima�aoSprite atkTD = new Anima�aoSprite(spritesAtkTD, 5, grupoAni);

	private static final ArrayList<BufferedImage> spritesTomandoHitFE = Util.carregarArrayBI(bitmap, 63, 1080, 63, 90, 1);
	private static final ArrayList<BufferedImage> spritesTomandoHitFD = Util.carregarArrayBI(bitmap, 189, 1080, 63, 90, 1);
	private static final ArrayList<BufferedImage> spritesTomandoHitTE = Util.carregarArrayBI(bitmap, 63, 1170, 63, 90, 1);
	private static final ArrayList<BufferedImage> spritesTomandoHitTD = Util.carregarArrayBI(bitmap, 189, 1170, 63, 90, 1);
	private final Anima�aoSprite tomarHitFE = new Anima�aoSprite(spritesTomandoHitFE, 10, grupoAni);
	private final Anima�aoSprite tomarHitFD = new Anima�aoSprite(spritesTomandoHitFD, 10, grupoAni);
	private final Anima�aoSprite tomarHitTE = new Anima�aoSprite(spritesTomandoHitTE, 10, grupoAni);
	private final Anima�aoSprite tomarHitTD = new Anima�aoSprite(spritesTomandoHitTD, 10, grupoAni);


	private Timer timerRegenera�ao;
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
		
		Anima�aoListener atk = new Anima�aoListener() {
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
			new BalaoDeFala("MUAHAHAHA! Voc� nunca conseguir� passar dessa fase!" , this, 250);
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
			lan�aProjetilSFX.play();
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
			if (timerRegenera�ao != null) {
				if (timerRegenera�ao.isRunning()) {
					return;
				}
			}

			timerRegenera�ao = new Timer(5, new ActionListener() {
				int proximoTick = Principal.tickTotal + DELAY_REGENERA�AO;

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

						proximoTick += DELAY_REGENERA�AO;
					}

					if (!isVivo()) {
						timerRegenera�ao.stop();
					}

				}
			});
			timerRegenera�ao.start();

		}
	}

	protected boolean checaVivo() {
		if (!super.checaVivo()) {
			new NovoStatusAnima�ao(getX(), getY(), "GROARRR!");
			roar.play();
			

			
			for (int x = 0; x < Monstro.todosMontros.size(); x++) {
				if (Monstro.todosMontros.get(x) != null) {
					if (!Monstro.todosMontros.get(x).isFurioso) {
						Monstro.todosMontros.get(x).setFurioso(true);
						Monstro.todosMontros.get(x).setSpeed(Monstro.todosMontros.get(x).speed + 0.8);
						new NovoStatusAnima�ao(Monstro.todosMontros.get(x).getX(), Monstro.todosMontros.get(x).getY(), "GROARRR!");
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
		dropper.addItem("Potion de For�a", 10, null);
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
