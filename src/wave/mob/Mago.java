package wave.mob;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.audio.SoundEffect;
import wave.graphics.BalaoDeFala;
import wave.graphics.animacao.Animacao;
import wave.graphics.light.Luz;
import wave.item.Dropper;
import wave.particula.CriadorDeParticulas;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.projetil.AtkMago;
import wave.projetil.ProjetilVeneno;
import wave.tools.ActionQueue;
import wave.tools.Util;

public class Mago extends Monstro implements Ranged{
	private int alcance = 150;
	
	private boolean isMovendo = true;
	private boolean isTPed = false;
	private static boolean primeiroMago = true;
	
	private static final int RANGE_MIN_TP = 400;
	
	//Animacoes
	private static final ArrayList<BufferedImage> imgsTp = Util.carregarArrayBI(Animacao.explosaoSrite, 0, 0, 64, 64, 32);
	private static final ArrayList<BufferedImage> imgsMorte = Util.carregarArrayBI(Animacao.explosaoSrite, 0, 832, 64, 64, 40);

	private CriadorDeParticulas particulasFurisoso;
	private CriadorDeParticulas particulasMorte;
	private CriadorDeParticulas particulasTPOrigem;
	private CriadorDeParticulas particulasTPDestino;
	
	//SFX
	private final RandomSFXGrupo atkSFX = new RandomSFXGrupo(new String[]{"/SFX/magoAtk1.ogg", "/SFX/magoAtk2.ogg", "/SFX/magoAtk3.ogg"});
	private static final RandomSFXGrupo tpSFX = new RandomSFXGrupo(new String[]{"/SFX/magoTp1.ogg", "/SFX/magoTp2.ogg"});
	private static final RandomSFXGrupo venenoSFX = new RandomSFXGrupo(new String[]{"/SFX/venenoAtk1.ogg", "/SFX/venenoAtk2.ogg"});
	private final RandomSFXGrupo tomarHitSFX = new RandomSFXGrupo(new String[]{"/SFX/magoTomarHit1.ogg", "/SFX/magoTomarHit2.ogg", "/SFX/magoTomarHit3.ogg"});
	private static final SoundEffect morteSFX = new SoundEffect("/SFX/magoMorte.ogg");
	
	
	public Mago() {
		super(15 + (Principal.nivel * 5), 200,(int)(Principal.nivel / 2) +  8 + (int) (Math.random() * 5), 0, Util.randomDouble(1.2, 1.4), Util.randomDouble(0.8, 1.2), Util.randomInt(-3, 0));

		particulasMorte = new CriadorDeParticulas(getX(), getY(), width, height, 4, 4, new Color(153, 50, 204), 80);
		particulasMorte.setSpeed(2);
		particulasMorte.setAngulo(90, 30);
		
		particulasTPOrigem = new CriadorDeParticulas(getX(), getY(), width, height, 2, 4, Color.RED, 80);
		particulasTPOrigem.setSpeed(1.5);
		particulasTPOrigem.setAngulo(90, 30);
		
		particulasTPDestino = new CriadorDeParticulas(getX(), getY(), width, height, 2, 4, Color.RED, 80);
		particulasTPDestino.setSpeed(1.5);
		particulasTPDestino.setAngulo(90, 30);
		
		particulasFurisoso = new CriadorDeParticulas(getX(), getY() + height, width, 5, 2, 2, Color.MAGENTA, 50);
		particulasFurisoso.setSpeed(1.5);
		particulasFurisoso.setAlphaVar(20, 5);
		particulasFurisoso.setComLuz(6, Color.MAGENTA, 100, 15, 5);
		
		if (primeiroMago) {
			primeiroMago = false;
			
			new BalaoDeFala("Eu sou o monstro mais apelão do jogo, amo o Frota por isso.", this, 250);
		}

	}
	
	protected int expValor() {
		return 80;
	}
	
	protected void updateResto() {
		atiraVeneno();
		updateParticulaFurioso();
	}
	
	private void updateParticulaFurioso() {
		if (!particulasFurisoso.isProduzindo()) {
			return;
		}
	
		if (isMovendo) {
			if (angulo > 90 && angulo < 270) {
				particulasFurisoso.setAngulo(20);
			} else {
				particulasFurisoso.setAngulo(160);
			}
			
		} else {	
			int chance = 1 + (int)(Math.random() * 100);
			
			if (chance <= 50) {
				particulasFurisoso.setAngulo(160);
			} else {
				particulasFurisoso.setAngulo(20);
			}
		}
		
		particulasFurisoso.update(getX(), getY() + height);	
	}
	
	private void atiraVeneno() {
		if (isFurioso) {
			int random = 1 + (int)(Math.random() * 1000);
			if (random <= 2) {
				new ProjetilVeneno(getXCentro(), getYCentro(), getDanoVar());
				venenoSFX.play();
			}
		}
		
	}

	public void setFurioso(boolean isFurioso) {
		super.setFurioso(isFurioso);
		if (this.isFurioso) {
			particulasFurisoso.setProduzindo(true);
		}
		
		
		if (isFurioso && !isTPed) {
			tpSFX.play();
			isTPed = true;
			int novoX = 1 +(int)(Math.random() * (JanelaJogo.WIDTH - width));
			int novoY = 1 +(int)(Math.random() * (JanelaJogo.HEIGHT- height));
			double distancia = Math.sqrt(Math.pow(novoX + (width / 2) - Player.getPlayer().getXCentro(), 2) + Math.pow(novoY + (height / 2) - Player.getPlayer().getYCentro(), 2));
			
			while(distancia < RANGE_MIN_TP) {
				novoX = 1 +(int)(Math.random() * (JanelaJogo.WIDTH - width));
				novoY = 1 +(int)(Math.random() * (JanelaJogo.HEIGHT- height));
				distancia = Math.sqrt(Math.pow(novoX + (width / 2) - Player.getPlayer().getXCentro(), 2) + Math.pow(novoY + (height / 2) - Player.getPlayer().getYCentro(), 2));
			}
			
			particulasTPOrigem.update(getX() + 10, getY() + 10);
			particulasTPOrigem.setProduzindoXYConstante(true, 25);
			
			Animacao aniA = new Animacao(getXCentro(), getYSpriteCentro(), imgsTp, 1);
			aniA.setScale(1.2f);
			aniA.start();
			
			final Luz luzAniA = new Luz(aniA, 80, 150, 150, 255, 100, 15, true, true, 0, 0);
			luzAniA.raioVar.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					luzAniA.desativar(20);
					return true;

				}
			});
			
			x = novoX;
			y = novoY;
			
			particulasTPDestino.update(getX() + 10, getY() + 10);
			particulasTPDestino.setProduzindoXYConstante(true, 25);
			
			Animacao ani = new Animacao(getXCentro(), getYSpriteCentro(), imgsTp, 2);
			ani.setInvertido(true);
			ani.setScale(1.2f);
			ani.setFadeOut(18, .6f);
			ani.start();
			
			final Luz luzAni = new Luz(ani, 80, 150, 150, 255, 100, 15, true, true, 0, 0);
			luzAni.raioVar.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					luzAni.desativar(50);
					return true;
				}
			});
			
			new Luz(this, 50, 255, 0, 255, 80, 50);
		}
	}
	
	protected RandomSFXGrupo getTomarHitSFX() {
		return this.tomarHitSFX;
	}

	public String toString() {
		return "Mago";
	}

	public Color getCor() {
		return new Color(153, 50, 204);
	}

	public int getAlcance() {
		return alcance;
	}
	
	protected Color sangueCor() {
		return new Color(153, 50, 204);
	}

	protected void updateAngulo() {
		// - 90 para acertar com as cordenadas do campo, 0 para direita, 90 para
		// cima, 180 para direita e 270 para baixo
		double anguloNovo = Math.toDegrees(Math.atan2(Player.getPlayer().getXCentro() - getXCentro(), Player.getPlayer().getYCentro() - getYCentro())) - 90;
		if (anguloNovo < 0) {
			anguloNovo += 360;
		}

		double distancia = Math.sqrt(Math.pow(getXCentro() - Player.getPlayer().getXCentro(), 2) + Math.pow(getYCentro() - Player.getPlayer().getYCentro(), 2));
		isMovendo = true;

		if (distancia <= alcance - 50) {
			// angulo simetrico demora mas foi kkk kekek heuhue
			angulo = anguloNovo + 180;
			return;
		}

		if (distancia > alcance - 50 && distancia < alcance - 30) {
			isMovendo = false;
			return;
		}

		if (distancia >= alcance + height + 10) {
			angulo = anguloNovo;
		}

	}

	protected boolean checaVivo() {
		
		if (!super.checaVivo()) {
			particulasMorte.update(getX(), getY() + 10);
			particulasMorte.setProduzindoXYConstante(true, 25);
			particulasFurisoso.setProduzindo(false);
			
			Animacao ani = new Animacao(getXCentro(), getYSpriteCentro(), imgsMorte, 3);
			ani.setScale(1.3f);
			ani.start();
			
			final Luz luzMorte = new Luz(ani, ani.getWidth() - 30, 150, 150, 255, 100, 35, true, true, 0, 0);
			luzMorte.forcaVar.addEsperaNaFila(30);
			luzMorte.forcaVar.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					luzMorte.desativar(35);
					return true;
				}
			});
			
			morteSFX.play();
			return false;
		} else {
			return true;
		}
	}

	protected void moveChase() {
		if (isMovendo) {
			super.moveChase();
		}
	}

	protected void atacarDist() {
		double anguloDeAtk = Math.toDegrees(Math.atan2(Player.getPlayer().getXCentro() - getXCentro(), Player.getPlayer().getYCentro() - getYCentro())) - 90;
		new AtkMago(this, anguloDeAtk, isFurioso);
		atkSFX.play();
	}

	protected void atiraGround() {
		// para nao atirar ground
	}
	
	protected void droppar() {
		dropper.setCap(1);
		dropper.addItem(Dropper.MOCHILA_CHIQUE, 10, null);
		dropper.addItem(Dropper.MOCHILA_MAL, 5);
		dropper.addItem(Dropper.MEDIEVAL_BOTA, 8);
		dropper.addItem(Dropper.MEDIEVAL_LEGS, 8);
		dropper.addItem(Dropper.PLATE_LEGS, 6);
		dropper.addItem(Dropper.PLATE_BOOTS, 6);
		dropper.addItem(Dropper.GOLD_BOOTS, 4);
		dropper.addItem(Dropper.GOLD_LEGS, 4);
		super.droppar();
	}

	public void pintarMob(Graphics2D g) {
		super.pintarMob(g);
		g.setColor(Color.BLACK);
		g.drawLine(getX() - 5, getY(), getX() - 5, getY() + height);
		g.setColor(Color.RED);
		g.drawOval(getX() - 6, getY() - 2, 3, 3);
	//	g.drawOval(getXCentro() - raioVisao, getYCentro() - raioVisao, raioVisao * 2, raioVisao * 2);
	}

}
