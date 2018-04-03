package wave.mob;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.audio.RandomSFXGrupo;
import wave.graphics.Sombra;
import wave.gui.menus.MenuLevel;
import wave.item.Dropper;
import wave.particula.CriadorDeParticulas;
import wave.pathfinding.PathFinding;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public abstract class Monstro extends Mob {

	//atributos
	protected int raioVisao;
	protected int raioVisaoInicial;

	//booleanas
	private boolean isProntoParaAndar = true;
	private boolean isIdle 			  = true;
	protected boolean isChase 		  = false;
	protected boolean isFurioso		  = false;

	//constantes
	private final int idleAndando = 24 + (int) (Math.random() * 100);
	private final int idleDelay = 50 + (int) (Math.random() * 100);
	
	//Timer
	private Timer timerIdleMover;
	
	//Criador deParticulas
	private CriadorDeParticulas particulasMorte;
	
	//Dropper
	protected Dropper dropper;
	
	//SFXs
	private static final RandomSFXGrupo morteSFX = new RandomSFXGrupo(new String[]{"/SFX/morte1.ogg", "/SFX/morte2.ogg", "/SFX/morte3.ogg"});
	protected static final RandomSFXGrupo lançaProjetilSFX = new RandomSFXGrupo(new String[]{"/SFX/projetilGrounded1.ogg", "/SFX/projetilGrounded2.ogg", "/SFX/projetilGrounded3.ogg"});
	
	//todos monstros
	public static ArrayList<Monstro> todosMontros = new ArrayList<Monstro>();
	
	public PathFinding pf = new PathFinding();

	
	public Monstro(int vida, int raioVisao, int dano, int heightSprite, double speed, double velAtk, int defesa) {
		super(20 + (int)(Math.random() * 10), 20 + (int)(Math.random() * 10), heightSprite, vida, dano, defesa, speed, velAtk);
		
		setRandomXY();
		sombra = new Sombra(this);
		this.raioVisao = raioVisao;
		raioVisaoInicial = raioVisao;
		particulasMorte = new CriadorDeParticulas(getX(), getYSprite(), width, this.heightSprite, 1, 2, Color.BLACK, 40);
		particulaTomarDano = new CriadorDeParticulas(getX(), getYSprite(), width, this.heightSprite, 2, 2, Color.YELLOW, 100);
		//particulaTomarDano.setSeguindo(true, this);
		particulaTomarDano.setComLuz(4, Color.YELLOW, 100, 10, 25);
		particulaTomarDano.setGravidadeRateVar(.075f, .02f);
		particulaTomarDano.setSpeed(2);
		particulaTomarDano.setAlphaVar(30, 5);
		
		dropper = new Dropper(this);
		
		todosMontros.add(this);
		prontoParaPintar();
	}
	
	public Monstro(int vida, int raioVisao, int dano, int heightSprite, double speed, double velAtk, int defesa, Void naoProntoPraPintar) {
		super(20 + (int)(Math.random() * 10), 20 + (int)(Math.random() * 10), heightSprite, vida, dano, defesa, speed, velAtk);
		
		setRandomXY();
		sombra = new Sombra(this);
		this.raioVisao = raioVisao;
		raioVisaoInicial = raioVisao;
		particulasMorte = new CriadorDeParticulas(getX(), getYSprite(), width, this.heightSprite, 1, 2, Color.BLACK, 40);
		particulaTomarDano = new CriadorDeParticulas(getX(), getYSprite(), width, this.heightSprite, 2, 2, Color.YELLOW, 100);
		//particulaTomarDano.setSeguindo(true, this);
		particulaTomarDano.setComLuz(4, Color.YELLOW, 100, 10, 25);
		particulaTomarDano.setGravidadeRateVar(.075f, .02f);
		particulaTomarDano.setSpeed(2);
		particulaTomarDano.setAlphaVar(30, 5);
		
		dropper = new Dropper(this);
		
		todosMontros.add(this);
	}
	
	public final void updateMob() {
		updateVisao();
		if (!Principal.monstrosParados) updateIdleStatus();
		if (!Principal.monstrosParados) updateChaseStatus();
		checaColisaoMob();
		tentarAtacarDist();
		barraVida.update();
		updateResto();
		particulasMorte.update(getX(), getYSprite());
		particulaTomarDano.update(getX(), getYSprite());
		
	}
	
	protected abstract void updateResto();

	
	public String toString() {
		return "Monstro";
	}

	private void setChase(boolean chase) {
		if (chase) {
			isIdle = false;
			isChase = chase;
		} else {
			isIdle = true;
			isChase = chase;
		}
	}
	
	public void setFurioso(boolean furioso) {
		isFurioso = furioso;	
		setChase(furioso);
	}
	
	public Color getCor() {
		if (isIdle) {
			return Color.BLACK;
		}
		
		if (isChase) {
			return Color.RED;
		}
		return Color.BLACK;
	}

	protected void setRandomXY() {
		int novoX = 1 +(int)(Math.random() * (JanelaJogo.WIDTH - width -5));
		int novoY = 1 +(int)(Math.random() * (JanelaJogo.HEIGHT- height - 5));
		double distancia = Math.sqrt(Math.pow(novoX + (width / 2) - Player.getPlayer().getXCentro(), 2) + Math.pow(novoY + (height / 2) - Player.getPlayer().getYCentro(), 2));
		
		while(distancia < 150) {
			novoX = 1 +(int)(Math.random() * (JanelaJogo.WIDTH - width));
			novoY = 1 +(int)(Math.random() * (JanelaJogo.HEIGHT- height));
			distancia = Math.sqrt(Math.pow(novoX + (width / 2) - Player.getPlayer().getXCentro(), 2) + Math.pow(novoY + (height / 2) - Player.getPlayer().getYCentro(), 2));
		}
		
		this.x = novoX;
		this.y = novoY;
	}
	
	protected boolean checaVivo() {
		if (!super.checaVivo()) {
			todosMontros.remove(this);
			particulasMorte.setSpeed(0.5);
			particulasMorte.setProduzindoXYConstante(true, 25);
			morteSFX.play();
			
			Player.getPlayer().ganhaExp(valorExp());
			droppar();
			
			return false;
		}	else {
			return true;
		}
		
	}
	
	protected void droppar() {
		dropper.addItem("Potion de Vida", 8 + MenuLevel.getPotionRateMod(), null);
		dropper.droppar();
	}
	
	public final int valorExp() {
		return expValor();
	}
	protected abstract int expValor();
	
	
	public int tomarHit(int hit) {	
		particulaTomarDano.setProduzindo(true, 10);
		return super.tomarHit(hit);
	}

	protected void tentarAtacarMelee() {
		if (isAtkAReady) {

			atacarMelee();
			
			isAtkAReady = false;
			
			final int tickAtual = Principal.tickTotal;
			timerAtk = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= (tickAtual + getVelAtkTickDelay())) {
						isAtkAReady = true;
						timerAtk.stop();
					}
					
				}
			});
			timerAtk.start();
		}
	}
	
	protected void atacarMelee() {
		Player.getPlayer().tomarKnockBack(this.angulo);
		Player.getPlayer().tomarHit(getDanoVar());
	}
	
	protected void tentarAtacarDist() {
		if (!(this instanceof Ranged)) return;
		Ranged mobRanged = (Ranged)this;
				
		double distancia = Math.sqrt(Math.pow(getXCentro() - Player.getPlayer().getXCentro(), 2) + Math.pow(getYCentro() - Player.getPlayer().getYCentro(), 2));

		if (isAtkAReady && distancia <= mobRanged.getAlcance()) {
			
			atacarDist();
			
			isAtkAReady = false;
			final int tickAtual = Principal.tickTotal;
			timerAtk = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= (tickAtual + getVelAtkTickDelay())) {
						isAtkAReady = true;
						timerAtk.stop();
					}
					
				}
			});
			timerAtk.start();
		}
	}
	
	protected void atacarDist() {

	}

	private void checaColisaoMob() {
		for (int index = 0; index < todosMobs.size(); index++) {
			Mob mobAtual = todosMobs.get(index);
			if (mobAtual == this)
				continue;

			if (Math.abs(getXCentro() - mobAtual.getXCentro()) < ((width / 2) + (mobAtual.getWidth() / 2))) {
				if (Math.abs(getYCentro() - mobAtual.getYCentro()) < ((height / 2) + (mobAtual.getHeight() / 2))) {
					Rectangle rect = new Rectangle(getX(), getY(), width, height);
					Rectangle rectMobAtual = new Rectangle(mobAtual.getX(), mobAtual.getY(), mobAtual.getWidth(), mobAtual.getHeight());

					if (mobAtual instanceof Player) {
						tentarAtacarMelee();
					}

					while (rect.intersects(rectMobAtual)) {
						if (rect.intersection(rectMobAtual).getX() >= (x + width / 2)) {
							if (rect.intersection(rectMobAtual).getY() >= (y + height / 2)) {
								if (rect.intersection(rectMobAtual).width > rect.intersection(rectMobAtual).height) {
									y--;
									rect.y--;
									continue;
								}

								if (rect.intersection(rectMobAtual).width < rect.intersection(rectMobAtual).height) {
									x--;
									rect.x--;
									continue;
								}

								y--;
								rect.y--;
								x--;
								rect.x--;
							} else {
								if (rect.intersection(rectMobAtual).width > rect.intersection(rectMobAtual).height) {
									y++;
									rect.y++;
									continue;
								}

								if (rect.intersection(rectMobAtual).width < rect.intersection(rectMobAtual).height) {
									x--;
									rect.x--;
									continue;
								}

								x--;
								rect.x--;
								y++;
								rect.y++;
							}
						} else {
							if (rect.intersection(rectMobAtual).getY() >= (y + height / 2)) {
								if (rect.intersection(rectMobAtual).width > rect.intersection(rectMobAtual).height) {
									y--;
									rect.y--;
									continue;
								}

								if (rect.intersection(rectMobAtual).width < rect.intersection(rectMobAtual).height) {
									x++;
									rect.x++;
									continue;
								}

								y--;
								rect.y--;
								x++;
								rect.x++;
							} else {
								if (rect.intersection(rectMobAtual).width > rect.intersection(rectMobAtual).height) {
									y++;
									rect.y++;
									continue;
								}

								if (rect.intersection(rectMobAtual).width < rect.intersection(rectMobAtual).height) {
									x++;
									rect.x++;
									continue;
								}

								y++;
								rect.y++;
								x++;
								rect.x++;
							}
						}
					}

				}
			}

		}
	}


	//verifica se o player entrou no alcance do mob, ao entrar seu alcance aumenta, ao sair do alcance aprimorando o mob para de seguir o player
	private void updateVisao() {
		if (isFurioso) return;
		
		double distancia = Math.sqrt(Math.pow((x + width / 2) - (Player.getPlayer().getX() + Player.getPlayer().getWidth() / 2), 2) + 
				Math.pow((y + height / 2) - (Player.getPlayer().getY() + Player.getPlayer().getHeight() / 2), 2));
		
		if (distancia <= raioVisao) {
			if (Player.getPlayer().isInvisivel) return;
			setChase(true);
			raioVisao = raioVisaoInicial + 100;
		} else {
			raioVisao = raioVisaoInicial ;
			setChase(false);
		}
	}

	protected void updateIdleStatus() {
		if (Principal.debugPF) return;
		
		if (isIdle) {
			checaMoveIdle();
			moveIdle();
		}
	}

	private void updateChaseStatus() {
		if (isChase || Principal.debugPF) {
			updateAngulo();
			moveChase();
		}
	}

	//ajusta o angulo do mob dependendo do angulo entre ele e o alvo
	protected void updateAngulo() {
		// - 90 para acertar com as cordenadas do campo, 0 para direita, 90 para cima, 180 para esqerda e 270 para baixo
		double anguloNovo = Math.toDegrees(Math.atan2(Player.getPlayer().getXCentro() - getXCentro(), Player.getPlayer().getYCentro() - getYCentro())) - 90;
		if (anguloNovo < 0) {
			anguloNovo += 360;
		}

		if (Principal.debugPF) {
			angulo = pf.pathFind(Player.getPlayer().getXCentro(), Player.getPlayer().getYCentro(), this);
		} else {
			angulo = anguloNovo;
		}
	}

	//executa o movimento idle, criando um agunlo aleatorio e deixando o mob "pensando" eu seu proximo angulo
	private void checaMoveIdle() {
		if (!isAndando && isProntoParaAndar) {
			final int tickAtual = Principal.tickTotal;
			angulo = 1 +(int) (Math.random() * 359);
			isAndando = true;
			
			timerIdleMover = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= (tickAtual + idleAndando)) {
						isAndando = false;
						isProntoParaAndar = false;
					}
					
					if (Principal.tickTotal >= (tickAtual + idleAndando + idleDelay)) {
						isProntoParaAndar = true;
						timerIdleMover.stop();
					}
					
				}
			});
			timerIdleMover.start();
			

			
		}

	}

	//movimento basico, com colisao inteligente com as bordas do campo
	protected void moveIdle() {
		if (isAndando && !isGrounded) {
			x += Math.cos(Math.toRadians(angulo)) * getSpeed();
			y -= Math.sin(Math.toRadians(angulo)) * getSpeed();
			
			checaForaCampo();

		}
	}

	//movimento basico
	protected void moveChase() {
		if (!isGrounded) {
			x += Math.cos(Math.toRadians(angulo)) * getSpeed();	
			y -= Math.sin(Math.toRadians(angulo)) * getSpeed();
			
			checaForaCampo();
		}	
	}
	
	public boolean isFurioso() {
		return isFurioso;
	}
	
	
	public void pintarMob(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawOval(getX(), getY(), width, height);
		
		g.setColor(getCor());
		//g.drawLine(player.getXCentro(), player.getYCentro(), getXCentro(), getYCentro());
		g.fillOval(getX(), getY(), width, height);
		
		//pinta raio de visao
		//g.drawOval(getXCentro() - raioVisao, getYCentro() - raioVisao, raioVisao * 2, raioVisao * 2);
		if (isFurioso) {
			g.setColor(Color.BLACK);
			g.drawLine(getX() + 5, getY() + 5, getX() + 10, getY() + 8);
			g.drawLine(getX() + 15, getY() + 8, getX() + 20, getY() + 5);
		}
		
	}

	
}
