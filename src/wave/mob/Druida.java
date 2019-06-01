package wave.mob;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import wave.audio.RandomSFXGrupo;
import wave.graphics.BalaoDeFala;
import wave.graphics.ModValorAnimacao;
import wave.graphics.animacao.Animacao;
import wave.graphics.light.Luz;
import wave.item.Dropper;
import wave.particula.CriadorDeParticulas;
import wave.principal.Principal;
import wave.projetil.AtkDruida;
import wave.projetil.ProjetilCuraDruida;
import wave.tools.ActionQueue;
import wave.tools.Util;

public class Druida extends Monstro implements Ranged {
	private int alcance = 150;
	
	private boolean isMovendo = true;
	private static boolean primeiroDruida = true;

	private ArrayList<Urso> ursos = new ArrayList<Urso>();
	private Animacao ani = new Animacao(getXCentro(), getYCentro(), Animacao.groundedImgs, 3);
	
	private Luz luz = null;
	
	private CriadorDeParticulas particulasFurioso;
	
	private final RandomSFXGrupo summonSFX = new RandomSFXGrupo(new String[]{"/SFX/druidaSummon1.ogg", "/SFX/druidaSummon2.ogg", "/SFX/druidaSummon3.ogg"});
	private final RandomSFXGrupo tomarHitSFX = new RandomSFXGrupo(new String[]{"/SFX/magoTomarHit1.ogg", "/SFX/magoTomarHit2.ogg", "/SFX/magoTomarHit3.ogg"});

	public Druida() {
		super(15 + (Principal.nivel * 5), 200, (int) (Principal.nivel / 2) + 5 + (int) (Math.random() * 5), 0, Util.randomDouble(1.2, 1.4), Util.randomDouble(0.8, 1.2), Util.randomInt(-3, 0));

		ani.setVaiVolta(true);
		ani.setScale(.75f);
		ursos.add(new Urso(this));
		
		if (primeiroDruida) {
			primeiroDruida = false;
			
			new BalaoDeFala("Meus ursos podem parecer fofinhos, mas só parecem...", this, 250);
		}
		
		particulasFurioso = new CriadorDeParticulas(getX(), getY(), width, heightSprite, 1, 1, new Color(0, 100, 0), 30);
		particulasFurioso.setParticulaInvisivel(true);
		particulasFurioso.addColor(new Color(124, 252, 0));
		particulasFurioso.setSpeed(0);
		particulasFurioso.setComLuz(6, null, 60, 5, 50);
		particulasFurioso.addAllCoresLuz();
	}
	
	protected RandomSFXGrupo getTomarHitSFX() {
		return tomarHitSFX;
	}
	
	protected int expValor() {
		return 80;
	}

	public int getAlcance() {
		return alcance;
	}

	public String toString() {
		return "Druida";
	}

	public Color getCor() {
		return new Color(0, 100, 0);
	}

	protected Color sangueCor() {
		return new Color(0, 100, 0);
	}
	
	public boolean checaVivo() {
		if (!super.checaVivo()) {
			
			if (!ani.isRodando()) {
				ani.setX(getXCentro());
				ani.setY(getYSpriteCentro());
				ani.start();
				Mob.groundedSFX.play();
				
				final Luz luzAni = new Luz(ani, ani.getWidth(), 200, 200, 200, 100, 15, true, true, 0, 0);
				luzAni.forcaVar.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						luzAni.desativar(70);
						return true;
					}
				});
			}
			
			particulasFurioso.setProduzindo(false);
			
			return false;
		} else {
			return true;
		}
	}
	
	protected void updateResto() {
		invocarUrso();
		checaUrsos();
		healarMonstro();
		checaFurioso();
		particulasFurioso.update(getX(), getYSprite());
	}


	private void checaFurioso() {
		if (isFurioso && luz == null) {
			luz = new Luz(this, 45, 107, 142, 35, 70, 25);
			particulasFurioso.setProduzindo(true);
		}
	}

	private void checaUrsos() {
		for (int x = 0; x < ursos.size(); x++) {
			
			if (isFurioso) {
				ursos.get(x).setFurioso(true);
			}
			
			if (!ursos.get(x).isVivo()) {
				setFurioso(true);
				ursos.remove(x);
			}

			
		}

	}

	private void invocarUrso() {
		int chance = (int) (Math.random() * 1000);

		if (ursos.size() < 2 && chance <= 2) {
			Urso urso = new Urso(this);
			ursos.add(urso);
			
			Animacao ani = new Animacao(urso.getXCentro(), urso.getYSpriteCentro(), Animacao.magicImgs, 2);
			ani.setScale(.25f);
			ani.setSeguindo(urso);	
			ani.start();
			
			final Luz luz = new Luz(urso, 50, 50, 150, 200, 100, 15, true, true, 0, 0);
			luz.forcaVar.addAcaoNaFila(new ActionQueue() {
				public boolean action() {
					luz.desativar(35);
					return true;
				}
			});
			
			summonSFX.play();
		}

	}

	private void healarMonstro() {
		int chance = 0 + (int) (Math.random() * 1000);
		if (chance <= 2) {
			for (int x = 0; x < Monstro.todosMontros.size(); x++) {
				Monstro monstroAtual = Monstro.todosMontros.get(x);

				if (monstroAtual.equals(this))
					continue;

				if (monstroAtual.getVida() != monstroAtual.getVidaMax()) {
					double distancia = Math.sqrt(Math.pow(getXCentro() - monstroAtual.getXCentro(), 2) + Math.pow(getYCentro() - monstroAtual.getYCentro(), 2));
					if (distancia < 250) {
						new ProjetilCuraDruida(this, monstroAtual);
						return;
					}
				}
			}
		}

	}

	protected void moveChase() {
		if (isMovendo) {
			super.moveChase();
		}
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
			// angulo simetrico demorou mas foi kkk kekek heuhue
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
	
	public void curar(int curaValor) {
		int vidaAntes = getVida();
		setVida(getVida() + curaValor / 3);
		if (getVida() > getVidaMax()) {
			setVida(getVidaMax());
		}
		int mudancaVida = getVida() - vidaAntes;

		if (mudancaVida > 0) {
			System.out.println(toString() + " healou " + mudancaVida + " de vida");
			new ModValorAnimacao(getX(), getYSprite(), mudancaVida, Color.GREEN);
			healSFX.play();
		}

	}
	
	protected void droppar() {
		dropper.setCap(1);
		dropper.addItem(Dropper.MOCHILA_CHIQUE, 10, null);
		dropper.addItem(Dropper.MOCHILA_MAL, 5);
		dropper.addItem(Dropper.COURO_BOTA, 8);
		dropper.addItem(Dropper.COURO_LEGS, 8);
		dropper.addItem(Dropper.COURO_HELMET, 8);
		dropper.addItem(Dropper.MEDIEVAL_HELMET, 8);
		dropper.addItem(Dropper.PLATE_LEGS, 8);
		dropper.addItem(Dropper.PLATE_BOOTS, 8);
		dropper.addItem(Dropper.GOLD_BOOTS, 4);
		dropper.addItem(Dropper.GOLD_LEGS, 4);
		super.droppar();
	}

	protected void atacarDist() {
		double anguloDeAtk = Math.toDegrees(Math.atan2(Player.getPlayer().getXCentro() - getXCentro(), Player.getPlayer().getYCentro() - getYCentro())) - 90;
		new AtkDruida(this, anguloDeAtk);
	}

	public void pintarMob(Graphics2D g) {
		super.pintarMob(g);
		g.setColor(Color.BLACK);
		g.drawLine(getX() - 5, getY(), getX() - 5, getY() + height);
		g.setColor(Color.BLUE);
		g.drawOval(getX() - 6, getY() - 2, 3, 3);
	}
}
