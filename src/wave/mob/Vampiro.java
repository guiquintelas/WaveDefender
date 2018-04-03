package wave.mob;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import wave.audio.RandomSFXGrupo;
import wave.audio.SoundEffect;
import wave.graphics.BalaoDeFala;
import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.item.Dropper;
import wave.particula.CriadorDeParticulas;
import wave.principal.Principal;
import wave.projetil.AtkPlayer;
import wave.projetil.Projetil;
import wave.projetil.ProjetilSangue;
import wave.tools.Util;

public class Vampiro extends Monstro{
	private static final BufferedImage fang = Util.carregarImg("/Sprites/fangs.png");
	
	private static final int DELAY_ENTRE_INV = 75;
	private static final int DURACAO_INV = 250;
	
	private boolean prontoInv = true;
	private static boolean primeiroVamp = true;
	
	private CriadorDeParticulas particulasHit;
	private CriadorDeParticulas particulasFicarInvi;
	private CriadorDeParticulas particulasFicarInviSec;
	
	//Luzes
	private Luz olhoD;
	private Luz olhoE;
	
	private Timer timerDelayInv;
	
	private SoundEffect inv = new SoundEffect("/SFX/vampiroInv1.ogg");
	private SoundEffect invEnd = new SoundEffect("/SFX/vampiroInvEnd2.ogg");
	private RandomSFXGrupo hitsfx = new RandomSFXGrupo(new String[]{"/SFX/vampiroHit1.ogg", "/SFX/vampiroHit2.ogg", "/SFX/vampiroHit3.ogg"});
	private static RandomSFXGrupo atirarSangueSFX = new RandomSFXGrupo(new String[]{"/SFX/projetilSangueSFX1.ogg", "/SFX/projetilSangueSFX2.ogg", "/SFX/projetilSangueSFX3.ogg"});
	
	public Vampiro() {
		super(15 + Principal.nivel * 5, 150, 5 + Principal.nivel * 3, 0, Util.randomDouble(1.8, 2), Util.randomDouble(1, 1.3), Util.randomInt(-3, 0));
		
		particulasHit = new CriadorDeParticulas(getX() - 5, getY() + 10, width + 5, heightSprite, 1, 8, new Color(148, 0, 211), 120);
		particulasHit.addColor(new Color(147, 112, 219));
		particulasHit.addColor(new Color(218, 112, 214));
		particulasHit.setSpeed(1.5);
		particulasHit.setAlphaVar(25, 10);
		particulasHit.setSeguindo(true, this);
		
		particulasFicarInvi = new CriadorDeParticulas(0, 0, width, heightSprite, Util.removeFirsts(Animaçao.fumaçaImgs, 3), .6f, 3, 100);
		particulasFicarInvi.setSpeed(1.2);
		particulasFicarInvi.setAlphaVar(25, 10);
		particulasFicarInvi.setAngulo(90, 90);
		particulasFicarInvi.setAlphaVar(50, 20);
		
		particulasFicarInviSec = new CriadorDeParticulas(0, 0, width, heightSprite, Util.removeFirsts(Animaçao.fumaçaEscuraImgs, 3), .6f, 3, 25);
		particulasFicarInviSec.setSpeed(1.2);
		particulasFicarInviSec.setAlphaVar(25, 10);
		particulasFicarInviSec.setAngulo(90, 90);
		particulasFicarInviSec.setAlphaVar(50, 20);
		
		if (primeiroVamp) {
			primeiroVamp = false;
			new BalaoDeFala("Você me vê, você nao me vê. Vou te matar sem nem perceber!", this, 300);
		}

		olhoD = new Luz(this, 7, 255, 0, 0, 100, 15, true, true, 0 , 40);
		olhoD.setXOff(width / 3);
		olhoD.setYOff(-height/4);
		olhoE = new Luz(this, 7, 255, 0, 0, 100, 15, true, true, 0, 40);
		olhoE.setXOff(-width / 3);
		olhoE.setYOff(-height/4);
	}
	
	public String toString() {
		return "Vampiro";
	}
	
	public Color getCor() {
		return new Color(120, 120, 120);
	}
	
	protected void setInvisivel(boolean isInvisivel) {
		if (isInvisivel == false && this.isInvisivel == true) {
			invEnd.play();
			particulasFicarInvi.update(getX(), getYSprite());
			particulasFicarInvi.setProduzindoXYConstante(true, 25);
			
			particulasFicarInviSec.update(getX(), getYSprite());
			particulasFicarInviSec.setProduzindoXYConstante(true, 25);
		}
		this.isInvisivel = isInvisivel;
		
		olhoD.setApagada(isInvisivel);
		olhoE.setApagada(isInvisivel);
		
	}
	protected void updateResto() {
		ficarInvisivel();
		atiraSangue();
		particulasHit.update(getX(), getY() + 10);
	}	
	
	
	private void atiraSangue() {
		int chance = Util.randomInt(0, 1000);
		if (chance < 3 && isFurioso) {
			new ProjetilSangue(getXCentro(), getYCentro(), getDanoVar(), isInvisivel);
			atirarSangueSFX.play();
		}
	}
	
	public double getSpeed() {
		if (isInvisivel && isChase) {
			return Principal.nivel / 7.0 + speed;
			
		} else {
			return speed;
		}
	}
	
	protected void updateAngulo() {
		// - 90 para acertar com as cordenadas do campo, 0 para direita, 90 para cima, 180 para direita e 270 para baixo
		double anguloNovo = Math.toDegrees(Math.atan2(Player.getPlayer().getXCentro() - getXCentro(), Player.getPlayer().getYCentro() - getYCentro())) - 90;
		if (anguloNovo < 0) {
			anguloNovo += 360;
		}
		
		for (int i = 0; i < Projetil.todosProjetils.size(); i++) {
			if (Projetil.todosProjetils.get(i) instanceof AtkPlayer) {
				
				double anguloPro = Projetil.todosProjetils.get(i).getAngulo();
				double dist = Math.sqrt(Math.pow(Projetil.todosProjetils.get(i).getXCentro() - getXCentro(), 2) + Math.pow(Projetil.todosProjetils.get(i).getYCentro() - getYCentro(), 2));
				
				
				double diferenca = anguloNovo - anguloPro + 180;
				if (diferenca > 180) {
					diferenca -= 360;
				}
				
				if (diferenca < -180) {
					diferenca += 360;
				}
				
				
				if ((diferenca <= 20 && diferenca >= -20) || dist < 50) {
					if (diferenca > 0) {
						anguloNovo -= 70;
					} else {
						anguloNovo += 70;
					}
					
					break;
				} 
			}
		}
		
	//	System.out.println(anguloNovo);

		if (Principal.debugPF) {
			angulo = pf.pathFind(Player.getPlayer().getXCentro(), Player.getPlayer().getYCentro(), this);
		} else {
			angulo = anguloNovo;
		}
	}
	
	private void ficarInvisivel() {
		if (isInvisivel) return;
		
		int chance = (int)(Math.random() * 1000);	
		int porcentagem;
		if (isChase) {
			porcentagem = 4;
		} else {
			porcentagem = 2;
		}
		
		if (chance < porcentagem) {
			if (prontoInv) {
				setInvisivel(DURACAO_INV);
				inv.play();
				particulasFicarInvi.update(getX(), getYSprite());
				particulasFicarInvi.setProduzindoXYConstante(true, 25);
				
				particulasFicarInviSec.update(getX(), getYSprite());
				particulasFicarInviSec.setProduzindoXYConstante(true, 25);
				
				prontoInv = false;
				
				timerDelayInv = new Timer(5, new ActionListener() {
					int tickAtual = Principal.tickTotal;
					public void actionPerformed(ActionEvent e) {
						
						if (Principal.tickTotal >= tickAtual + DURACAO_INV + DELAY_ENTRE_INV) {
							prontoInv = true;
							timerDelayInv.stop();
						}
						
					}
				});
				timerDelayInv.start();
				
			}
			
			
		}
	}
	
	protected void atacarMelee() {
		Player.getPlayer().tomarKnockBack(this.angulo);
		int dano = getDanoVar();
		Player.getPlayer().tomarHit(dano);
		setVidaMax(getVidaMaxCrua() + dano / 2);
		curar(dano / 2, false);
		particulasHit.setProduzindo(true, 15);
		hitsfx.play();
		setInvisivel(false);
	}
	
	public void pintarMob(Graphics2D g) {
		super.pintarMob(g);
		if (isChase) {
			g.setColor(new Color (139, 0, 0));
		} else {
			g.setColor(Color.RED);
		}
		
		g.drawLine(getX() + 5, getY() + 5, getX() + 10, getY() + 8);
		g.drawLine(getX() + 15, getY() + 8, getX() + 20, getY() + 5);
		g.setColor(Color.WHITE);
		g.drawLine(getX() + (int)((width / 5) * 1.5), getY() + (height / 3) * 2, getX() + (int)((width / 5) * 3.5), getY() + (height / 3) * 2);
		g.drawImage(fang, getX() + (int)((width / 5) * 1.5) ,getY() + (height / 3) * 2, null);
		g.drawImage(fang, getX() + (int)((width / 5) * 3.5) ,getY() + (height / 3) * 2, null);
	}

	

	@Override
	protected int expValor() {
		return 60;
	}
	
	protected void droppar() {
		dropper.setCap(2);
		dropper.addItem("Potion de Força", 10);
		dropper.addItem(Dropper.MOCHILA_CHIQUE, 4);
		dropper.addItem(Dropper.COURO_BOTA, 5);
		dropper.addItem(Dropper.COURO_LEGS, 5);
		dropper.addItem(Dropper.GOLD_LEGS, 1.5);
		super.droppar();
	}
}
