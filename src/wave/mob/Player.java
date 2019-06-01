package wave.mob;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.Timer;

import wave.audio.BackMusic;
import wave.audio.RandomSFXGrupo;
import wave.audio.SoundEffect;
import wave.graphics.BalaoDeFala;
import wave.graphics.ExpValorGanhaAnimacao;
import wave.graphics.Grafico;
import wave.graphics.NovoStatusAnimacao;
import wave.graphics.Sombra;
import wave.graphics.animacao.AnimacaoSprite;
import wave.graphics.animacao.AnimacaoSpriteGrupo;
import wave.graphics.light.Luz;
import wave.gui.BarraExp;
import wave.gui.GUI;
import wave.gui.menus.MenuLevel;
import wave.gui.menus.MenuMochila;
import wave.gui.status.StatusBuffDano;
import wave.gui.status.StatusGUI;
import wave.gui.status.StatusGrondedGUI;
import wave.gui.status.StatusTrapPronta;
import wave.gui.status.StatusTrapRecarga;
import wave.gui.status.StatusTraumaGUI;
import wave.input.KeyListener;
import wave.input.ListenerManager;
import wave.input.MouseListener;
import wave.item.Container;
import wave.item.Dropper;
import wave.item.Item;
import wave.item.equip.Equip;
import wave.map.Tile;
import wave.map.TileMap;
import wave.map.trap.Trap;
import wave.map.trap.TrapFogo;
import wave.map.trap.TrapVenenosa;
import wave.particula.CDPProntos;
import wave.particula.CriadorDeParticulas;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.projetil.AtkPlayer;
import wave.tools.Util;
import wave.tools.Variator;
import wave.tools.VariatorNumero;

public final class Player extends Mob implements Ranged {

	//unica referencia ao player ja q só existe um
	private static Player getPlayer;

	public static Player getPlayer() {
		return getPlayer;
	}

	public long timeD;
	public long timeD10;
	public long timeD9;
	public long timeD8;
	public long timeD1;
	public long timeD2;
	public long timeD3;
	public long timeD4;
	public long timeD5;
	public long timeD6;
	public long timeD7;

	// atributos
	private int alcance = 100;
	private int exp = 0;
	private int expTotal = 0;
	private int expLevel = 100;
	private int level = 1;
	public int pontosLevel = 0;
	private int manaMax = 100;
	public float mana = manaMax;
	private float manaRegen = 2.5f;

	private final int MAX_TRAP_INICIAL = 1;

	// constantes
	private static final int PARADO = 0;
	private static final int DELAY_STATUS_TRAUMA = 300; //6 segundos
	private static final int DELAY_REGAN_PASSIVA = 125; // 2.5 segundos
	private static final int WIDTH_HITBOX = 20;
	private static final int HEIGHT_HITBOX = 15;
	private static final int HEIGHT_SPRITE = 55;
	private static final int DELAY_TRAP_RECARGA = 500;

	// booleanas
	private boolean isCima = false;
	private boolean isBaixo = false;
	private boolean isDireita = false;
	private boolean isEsquerda = false;
	private boolean isAtkando = false;
	private boolean isTrapPronta = true;

	//Status
	private boolean isTrauma = false;

	//Mochila
	public HashMap<Integer, Item> mochilaItens = new HashMap<Integer, Item>();
	public Container mochila;

	//Equipes
	public HashMap<String, Equip> equips = new HashMap<String, Equip>();

	//Luz
	private Luz luz;

	//Variator
	public Variator varGLuz;

	// Timers
	private Timer timerAutoAtk;
	private Timer timerTrauma;
	private Timer timerRegenPassiva;
	private Timer timerRecargaTrap;

	//Criadores de Particulas
	private CriadorDeParticulas particulaCuraPassiva;
	private CriadorDeParticulas particulaLevelUp;
	private CriadorDeParticulas brilhoCabeca;
	private CriadorDeParticulas brilhoPeito;
	private CriadorDeParticulas brilhoPerna;
	private CriadorDeParticulas brilhoPe;

	//Sprite
	private static final BufferedImage sprites = Util.carregarImg("/Sprites/playerBitmap.png");
	private static final AnimacaoSpriteGrupo grupoAni = new AnimacaoSpriteGrupo();
	private static final AnimacaoSprite parado = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 0, 30, 55, 11), 15, grupoAni);
	private static final AnimacaoSprite esquerda = new AnimacaoSprite(Util.carregarArrayBI(sprites, 30, 55, 30, 55, 10), 4, grupoAni);
	private static final AnimacaoSprite esquerdaCima = new AnimacaoSprite(Util.carregarArrayBI(sprites, 30, 110, 30, 55, 10), 4, grupoAni);
	private static final AnimacaoSprite cima = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 165, 30, 55, 4), 15, grupoAni);
	private static final AnimacaoSprite cimaDireita = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 220, 30, 55, 4), 15, grupoAni);
	private static final AnimacaoSprite direita = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 275, 30, 55, 4), 15, grupoAni);
	private static final AnimacaoSprite direitaBaixo = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 330, 30, 55, 4), 15, grupoAni);
	private static final AnimacaoSprite baixo = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 385, 30, 55, 4), 15, grupoAni);
	private static final AnimacaoSprite baixoEsquerda = new AnimacaoSprite(Util.carregarArrayBI(sprites, 0, 440, 30, 55, 4), 15, grupoAni);

	//SFX
	private static final RandomSFXGrupo tomarHitSFX = new RandomSFXGrupo(new String[] { "/SFX/playerHit1.ogg", "/SFX/playerHit2.ogg", "/SFX/playerHit3.ogg", "/SFX/playerHit4.ogg" });
	private static final SoundEffect levelUpSFX = new SoundEffect("/SFX/levelUpSfx.ogg");
	public static final SoundEffect semMana = new SoundEffect("/SFX/noMana.ogg");
	public static final SoundEffect coracao = new SoundEffect("/SFX/coracao.ogg");
	private static final RandomSFXGrupo pegarItemSFX = new RandomSFXGrupo(new String[] { "/SFX/item1.ogg", "/SFX/item1.ogg", "/SFX/item2.ogg", "/SFX/item3.ogg", "/SFX/item4.ogg", "/SFX/item5.ogg", "/SFX/item6.ogg" });
	private static final SoundEffect pegarMochilaSFX = new SoundEffect("/SFX/mochila.ogg");
	private static final RandomSFXGrupo equipSFX = new RandomSFXGrupo(new String[] { "/SFX/equip1.ogg", "/SFX/equip2.ogg", "/SFX/equip3.ogg", "/SFX/equip4.ogg", "/SFX/equip5.ogg" });

	//Listeners
	private static KeyListener keyPressed;
	private static KeyListener keyReleased;
	private static MouseListener mousePressed;
	private static MouseListener mouseReleased;
	
	public Player(int x, int y) {
		super(WIDTH_HITBOX, HEIGHT_HITBOX, HEIGHT_SPRITE, 100, 10, 0, 3, 1);

		if (getPlayer != null && getPlayer.exp > 0) new BalaoDeFala("Esse estranho baú deve conter lembrancas da minha vida passada...", this, 250);

		getPlayer = this;
		this.x = x;
		this.y = y;
		this.angulo = 0;

		sombra = new Sombra(this);
		particulasGrounded = new CriadorDeParticulas(getX(), getYSprite(), width, heightSprite, 5, 5, new Color(34, 139, 34), 40);
		particulasGrounded.setSpeed(1);
		particulasGrounded.setAngulo(90, 30);

		particulaCuraPassiva = new CriadorDeParticulas(getX(), getYSprite(), width, heightSprite, 2, 5, new Color(0, 250, 154), 80);
		particulaCuraPassiva.setParticulaInvisivel(true);
		particulaCuraPassiva.setComLuz(6, new Color(0, 200, 154), 50, 5, 15);
		particulaCuraPassiva.addAllCoresLuz();
		particulaCuraPassiva.setLuzBruta(true);

		particulaLevelUp = new CriadorDeParticulas(getX(), getYSprite() - 10, width, 20, 3, 4, new Color(255, 215, 0), 100);
		particulaLevelUp.setSpeed(1);
		particulaLevelUp.setAngulo(90, 30);
		particulaLevelUp.setContorno(true);

		brilhoCabeca = CDPProntos.brilho(this, getX(), getYSprite(), width, 18);
		brilhoPeito = CDPProntos.brilho(this, getX() + 2, getYSprite() + 20, width - 2, 14);
		brilhoPerna = CDPProntos.brilho(this, getX() + 5, getYSprite() + 20 + 14, width - 5, 17);
		brilhoPe = CDPProntos.brilho(this, getX(), getYSprite() + heightSprite - 3, width, 3);
		brilhoCabeca.setProduzindo(false);
		brilhoPeito.setProduzindo(false);
		brilhoPerna.setProduzindo(false);
		brilhoPe.setProduzindo(false);

		grupoAni.setOffXBrutoAll(-5);
		grupoAni.setOffYAll(1, -2);
		grupoAni.setOffYAll(2, -1);

		cima.setOffYBruto(-2);
		baixo.setOffYBruto(4);
		direitaBaixo.setOffYBruto(4);
		baixoEsquerda.setOffYBruto(4);

		//esquerdaCima.setScale(4);
		esquerda.setOffYBruto(2);
		esquerda.setOffY(0, -2);
		esquerda.setOffY(1, -1);
		esquerda.setOffY(3, -1);
		esquerda.setOffY(4, -2);
		esquerda.setOffY(5, -2);
		esquerda.setOffY(6, -1);
		esquerda.setOffY(8, -1);
		esquerda.setOffY(9, -2);

		esquerdaCima.setOffXBruto(-6);
		esquerdaCima.setOffYBruto(2);
		esquerdaCima.setOffY(0, -2);
		esquerdaCima.setOffY(1, -1);
		esquerdaCima.setOffY(3, -1);
		esquerdaCima.setOffY(4, -2);
		esquerdaCima.setOffY(5, -2);
		esquerdaCima.setOffY(6, -1);
		esquerdaCima.setOffY(8, -1);
		esquerdaCima.setOffY(9, -2);
		//grupoAni.setOffYAll(3, -2);

		parado.clearOffY();
		parado.setOffYBruto(0);

		luz = new Luz(this, 150, 255, 223, 100, 100, 100);

		varGLuz = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero > 255) numero = 255;
				if (numero < 0) numero = 0;
				luz.setRGB(luz.getRed(), (int) numero, luz.getBlue());
			}

			public double getNumero() {
				return luz.getGreen();
			}

			public boolean devoContinuar() {
				return Luz.todasLuz.contains(luz);
			}
		});
		
		initListener();
		prontoParaPintar();
	}

	public void updateMob() {
		long time = System.nanoTime();
		grupoAni.updateAll(this);
		timeD = System.nanoTime() - time;
		long time2 = System.nanoTime();
		checaVivo();
		timeD2 = System.nanoTime() - time2;
		long time3 = System.nanoTime();
		checaMove();
		timeD3 = System.nanoTime() - time3;
		long time4 = System.nanoTime();
		move();
		timeD4 = System.nanoTime() - time4;
		long time5 = System.nanoTime();
		regenPassiva();
		timeD5 = System.nanoTime() - time5;
		long time6 = System.nanoTime();
		manaRegen();
		timeD6 = System.nanoTime() - time6;
		long time7 = System.nanoTime();
		barraVida.update();
		timeD7 = System.nanoTime() - time7;
		long time8 = System.nanoTime();
		checaGrounded();
		timeD8 = System.nanoTime() - time8;
		long time9 = System.nanoTime();

		particulasGrounded.update(getX(), getYSprite());
		particulaCuraPassiva.update(getX(), getYSprite());
		particulaLevelUp.update(getX(), getYSprite() - 10);

		brilhoCabeca.update(getX(), getYSprite());
		brilhoPeito.update(getX() + 2, getYSprite() + 20);
		brilhoPerna.update(getX() + 5, getYSprite() + 20 + 14);
		brilhoPe.update(getX(), getYSprite() + heightSprite - 3);

		timeD1 = System.nanoTime() - time9;
		long time10 = System.nanoTime();
		checaTrapPronta();
		timeD9 = System.nanoTime() - time10;

	}

	protected RandomSFXGrupo getTomarHitSFX() {
		return Player.tomarHitSFX;
	}

	public int getManaMax() {
		return manaMax;
	}

	public int getExp() {
		return exp;
	}

	public int getExpTotal() {
		return expTotal;
	}

	public int getExpLevel() {
		return expLevel;
	}

	public int getLevel() {
		return level;
	}
	
	public void setVida(int vida) {
		super.setVida(vida);
		Grafico.vidaPlayer.add(getVida());
	}

	public int getDano() {
		int danoBonus = 0;
		if (equips.get(Equip.CABECA) != null) danoBonus += equips.get(Equip.CABECA).getDano();
		if (equips.get(Equip.PEITO) != null) danoBonus += equips.get(Equip.PEITO).getDano();
		if (equips.get(Equip.PERNA) != null) danoBonus += equips.get(Equip.PERNA).getDano();
		if (equips.get(Equip.PE) != null) danoBonus += equips.get(Equip.PE).getDano();
		return super.getDano() + danoBonus;
	}

	public int getVidaMax() {
		int vidaBonus = 0;
		if (equips.get(Equip.CABECA) != null) vidaBonus += equips.get(Equip.CABECA).getVida();
		if (equips.get(Equip.PEITO) != null) vidaBonus += equips.get(Equip.PEITO).getVida();
		if (equips.get(Equip.PERNA) != null) vidaBonus += equips.get(Equip.PERNA).getVida();
		if (equips.get(Equip.PE) != null) vidaBonus += equips.get(Equip.PE).getVida();
		return super.getVidaMax() + vidaBonus;
	}

	public int getDefesa() {
		int defesaBonus = 0;
		if (equips.get(Equip.CABECA) != null) defesaBonus += equips.get(Equip.CABECA).getDefesa();
		if (equips.get(Equip.PEITO) != null) defesaBonus += equips.get(Equip.PEITO).getDefesa();
		if (equips.get(Equip.PERNA) != null) defesaBonus += equips.get(Equip.PERNA).getDefesa();
		if (equips.get(Equip.PE) != null) defesaBonus += equips.get(Equip.PE).getDefesa();
		return super.getDefesa() + defesaBonus;
	}

	public float getMana() {
		int manaBonus = 0;
		if (equips.get(Equip.CABECA) != null) manaBonus += equips.get(Equip.CABECA).getMana();
		if (equips.get(Equip.PEITO) != null) manaBonus += equips.get(Equip.PEITO).getMana();
		if (equips.get(Equip.PERNA) != null) manaBonus += equips.get(Equip.PERNA).getMana();
		if (equips.get(Equip.PE) != null) manaBonus += equips.get(Equip.PE).getMana();
		return mana + manaBonus;
	}

	public float getManaRegen() {
		float manaBonus = 0;
		if (equips.get(Equip.CABECA) != null) manaBonus += equips.get(Equip.CABECA).getManaRegen();
		if (equips.get(Equip.PEITO) != null) manaBonus += equips.get(Equip.PEITO).getManaRegen();
		if (equips.get(Equip.PERNA) != null) manaBonus += equips.get(Equip.PERNA).getManaRegen();
		if (equips.get(Equip.PE) != null) manaBonus += equips.get(Equip.PE).getManaRegen();
		return manaRegen + manaBonus;
	}

	public double getSpeed() {
		float speedBonus = 0;
		if (equips.get(Equip.CABECA) != null) speedBonus += equips.get(Equip.CABECA).getSpeed();
		if (equips.get(Equip.PEITO) != null) speedBonus += equips.get(Equip.PEITO).getSpeed();
		if (equips.get(Equip.PERNA) != null) speedBonus += equips.get(Equip.PERNA).getSpeed();
		if (equips.get(Equip.PE) != null) speedBonus += equips.get(Equip.PE).getSpeed();
		return super.getSpeed() + speedBonus;
	}

	public double getVelAtk() {
		float velAtkBonus = 0;
		if (equips.get(Equip.CABECA) != null) velAtkBonus += equips.get(Equip.CABECA).getVelAtk();
		if (equips.get(Equip.PEITO) != null) velAtkBonus += equips.get(Equip.PEITO).getVelAtk();
		if (equips.get(Equip.PERNA) != null) velAtkBonus += equips.get(Equip.PERNA).getVelAtk();
		if (equips.get(Equip.PE) != null) velAtkBonus += equips.get(Equip.PE).getVelAtk();
		return getVelAtkCru() + velAtkBonus;
	}

	protected boolean checaVivo() {
		if (!super.checaVivo()) {
			Principal.isRodando = false;
			varGLuz.desativar();
			
			return false;
		}
		return true;
	}

	public int quantosItens() {
		int quantos = 0;

		for (int i = 0; i < mochila.getSize(); i++) {
			if (mochilaItens.get(i) != null) {
				quantos++;
			}
		}

		return quantos;
	}

	public void equipar(Equip equip) {
		if (equips.get(equip.getID()) == null) {
			equips.put(equip.getID(), equip);
			equipSFX.play();

			if (equip.toString() == Dropper.GOLD_ARMOR)  brilhoPeito.setProduzindo(true);
			if (equip.toString() == Dropper.GOLD_HELMET) brilhoCabeca.setProduzindo(true);
			if (equip.toString() == Dropper.GOLD_LEGS)   brilhoPerna.setProduzindo(true);
			if (equip.toString() == Dropper.GOLD_BOOTS)  brilhoPe.setProduzindo(true);

		} else {
			System.out.println("Voce ja tem um equip no " + equip.getID());
		}
	}

	public void desequipar(String id) {

		if (equips.get(id).toString()  == Dropper.GOLD_ARMOR)  brilhoPeito.setProduzindo(false);
		if (equips.get(id).toString()  == Dropper.GOLD_HELMET) brilhoCabeca.setProduzindo(false);
		if (equips.get(id).toString()  == Dropper.GOLD_LEGS)   brilhoPerna.setProduzindo(false);
		if (equips.get(id).toString()  == Dropper.GOLD_BOOTS)  brilhoPe.setProduzindo(false);

		equips.put(id, null);
	}

	public synchronized void pegarItem(Item item) {
		if (item instanceof Container) {
			if (mochila == null || mochila.getSize() < ((Container) item).getSize()) {
				if (mochila == null) {
					new BalaoDeFala("Apertando a letra B (de Backpack) você pode abrir a mochila!", this, 250);

				}
				pegarMochilaSFX.play();
				mochila = ((Container) item);
				MenuMochila.update();
				item.removerDoChao();
				return;
			}
			semMana.play();
			new BalaoDeFala("Minha mochila atual é igual ou melhor que essa!", this, 100);

		} else if (mochila == null) {
			semMana.play();
			new BalaoDeFala("Não posso pegar itens sem mochila!", this, 100);

		} else if (quantosItens() == mochila.getSize()) {
			semMana.play();
			new BalaoDeFala("Não tenho espaco na mochila para este item!", this, 100);
		} else {
			if (quantosItens() < mochila.getSize()) {

				for (int i = 0; i < mochila.getSize(); i++) {
					if (mochilaItens.get(i) == null) {
						mochilaItens.put(i, item);
						break;
					}
				}
				MenuMochila.update();
				item.removerDoChao();
				pegarItemSFX.play();
			}
		}

		System.out.println(mochilaItens);
	}

	public void setBuffDano(final int buff, final int duracao) {
		if (isBuffDano) return;

		new StatusBuffDano(duracao);
		coracao.loop(true);

		setDano(getDanoCru() + buff);
		isBuffDano = true;
		particulasBuffForca.setProduzindo(true);

		timerBuffDano = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;

			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual + duracao) {
					setDano(getDanoCru() - buff);
					isBuffDano = false;
					particulasBuffForca.setProduzindo(false);
					coracao.loop(false);
					timerBuffDano.stop();
				}

				if (!isVivo()) {
					coracao.loop(false);
					timerBuffDano.stop();
				}

			}
		});
		timerBuffDano.start();
	}

	public void setTraumaTrue() {
		isTrauma = true;
		new StatusTraumaGUI(DELAY_STATUS_TRAUMA, this);

		if (timerTrauma != null) {
			if (timerTrauma.isRunning()) {
				timerTrauma.stop();
			}
		}

		final int tickAtual = Principal.tickTotal;
		timerTrauma = new Timer(5, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= (tickAtual + DELAY_STATUS_TRAUMA)) {
					isTrauma = false;
					timerTrauma.stop();
				}

			}
		});
		timerTrauma.start();
	}

	public void ganhaExp(int quanto) {
		exp += quanto;
		expTotal += quanto;
		Grafico.exp.add(expTotal);
		new ExpValorGanhaAnimacao(getX(), getYSprite(), quanto);

		while (checaLevel())
			;
	}

	private boolean checaLevel() {
		if (exp >= expLevel) {
			new NovoStatusAnimacao(getX(), getYSprite(), "LEVEL UP!", new Color(255, 215, 0), 18);
			BackMusic.reduzir(150, 25);
			levelUpSFX.play();
			BarraExp.piscar(150);
			GUI.botaoLevel.piscar(true);

			if (level == 1) new BalaoDeFala("Eu acabei de passar de level! Você pode ver minha experiecia na barra abaixo!", this, 250);
			if (level == 2) new BalaoDeFala("Você pode ver meus atributos clicando no botão abaixo ou apertando a tecla 'c'", this, 300);

			pontosLevel += 2;
			level++;
			setVida(getVidaMax());
			mana = getManaMax();
			exp -= expLevel;
			expLevel += (50 * level);
			manaMax += 25;
			manaRegen += 0.5f;

			particulaLevelUp.resetCorPadrao();

			int random = 1 + (int) (Math.random() * 4);

			if (random == 1) {
				setVidaMax(getVidaMaxCrua() + Util.randomInt(5, 7) * 5);
				particulaLevelUp.addColor(Color.GREEN);
			}

			if (random == 2) {
				setDano(getDanoCru() + Util.randomInt(2, 4));
				particulaLevelUp.addColor(new Color(255, 165, 0));
			}

			if (random == 3) {
				setVelAtkMax(getVelAtkCru() + 0.2d);
				particulaLevelUp.addColor(new Color(64, 224, 208));
			}

			if (random == 4) {
				setSpeed(getSpeedMaxCru() + 0.4f);
				particulaLevelUp.addColor(new Color(65, 105, 225));
			}

			particulaLevelUp.setProduzindo(true, 75);
			return true;
		}
		return false;
	}

	private void checaTrapPronta() {
		if (isTrapPronta) {

			for (int i = 0; i < StatusGUI.todosStatusGUI.size(); i++) {
				if (StatusGUI.todosStatusGUI.get(i) instanceof StatusTrapPronta) {
					return;
				}
			}

			new StatusTrapPronta();
		} else {

			for (int i = 0; i < StatusGUI.todosStatusGUI.size(); i++) {
				if (StatusGUI.todosStatusGUI.get(i) instanceof StatusTrapPronta) {
					StatusGUI.todosStatusGUI.get(i).remove();
				}
			}
		}
	}

	//metodo nao nescessario 
	public Color getCor() {
		if (isInvuKnockBack) {
			return Color.YELLOW;
		}

		return Color.BLACK;
	}

	private int getDelayTrap() {
		return DELAY_TRAP_RECARGA - (int) MenuLevel.getDelayMod() * 50;
	}

	public int getAlcance() {
		return alcance + MenuLevel.getAlcanceMod();
	}

	protected Color sangueCor() {
		return new Color(178, 34, 34);
	}

	public boolean isTrauma() {
		return isTrauma;
	}

	public String toString() {
		return "Player";
	}

	public int tomarHit(int hit) {
		setTraumaTrue();
		return super.tomarHit(hit);
	}

	public void setGroundedTrue(final int duracao) {
		new StatusGrondedGUI(duracao);
		super.setGroundedTrue(duracao);
	}

	public boolean usarMana(int quantidade) {
		if (mana >= quantidade) {
			mana -= quantidade;
			return true;
		} else {
			System.out.println("Mana insuficiente!");
			semMana.play();

			new NovoStatusAnimacao(getX(), getY(), "NO MANA", new Color[] { new Color(0, 191, 255), Color.WHITE, Color.YELLOW }, NovoStatusAnimacao.PADRAO);
			return false;
		}
	}

	private void manaRegen() {
		if (mana < manaMax) {
			mana += manaRegen / 50;
		}

		if (mana > manaMax) {
			mana = manaMax;
		}
	}

	private void regenPassiva() {
		if (!isTrauma) {
			if (timerRegenPassiva != null) {
				if (timerRegenPassiva.isRunning()) {
					return;
				}
			}
			timerRegenPassiva = new Timer(5, new ActionListener() {
				int tickAtual = Principal.tickTotal;

				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= (tickAtual + DELAY_REGAN_PASSIVA)) {
						int vidaAntes = getVida();
						curar(getVidaMax() / 20);

						if (getVida() != vidaAntes) {
							particulaCuraPassiva.setProduzindo(true, 25);
						}

						tickAtual = Principal.tickTotal;
					}

					if (isTrauma) {
						timerRegenPassiva.stop();
					}

				}
			});
			timerRegenPassiva.start();
		}
	}

	private void autoAtk() {
		if (isAtkando && isVivo()) {

			if (timerAutoAtk != null) {
				if (timerAutoAtk.isRunning()) {
					timerAutoAtk.stop();
				}
			}

			timerAutoAtk = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isAtkando) {
						timerAutoAtk.stop();
					}
					atacar();

				}
			});
			timerAutoAtk.start();
		}

	}

	private void atacar() {
		if (isAtkAReady) {

			double anguloAtk = Math.toDegrees(Math.atan2(JanelaJogo.xMouse - getXCentro(), JanelaJogo.yMouse - getYSpriteCentro())) - 90;
			if (anguloAtk < 0) {
				anguloAtk += 360;
			}

			if (MenuLevel.isProjetilTriplo()) {
				if (usarMana(15)) {
					new AtkPlayer(anguloAtk + 15, true);
					new AtkPlayer(anguloAtk, false);
					new AtkPlayer(anguloAtk - 15, false);
				}
			} else if (MenuLevel.isProjetilDuplo()) {
				if (usarMana(10)) {
					new AtkPlayer(anguloAtk + 10, true);
					new AtkPlayer(anguloAtk - 10, false);
				}
			} else {
				if (usarMana(5)) {
					new AtkPlayer(anguloAtk, true);
				}
			}

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

	private void checaGrounded() {
		if (isGrounded) {
			particulasGrounded.setProduzindo(true);
		} else {
			particulasGrounded.setProduzindo(false);
		}
	}

	private void checaMove() {
		isAndando = true;

		if (isCima) {
			if (isDireita) {
				angulo = 45;
				grupoAni.stopAll(cimaDireita);
				cimaDireita.start(this);
				return;
			}

			if (isEsquerda) {
				grupoAni.stopAll(esquerdaCima);
				esquerdaCima.start(this);
				angulo = 135;
				return;
			}

			grupoAni.stopAll(cima);
			cima.start(this);
			angulo = 90;
			return;
		}

		if (isBaixo) {
			if (isDireita) {
				grupoAni.stopAll(direitaBaixo);
				direitaBaixo.start(this);
				angulo = 315;
				return;
			}

			if (isEsquerda) {
				grupoAni.stopAll(baixoEsquerda);
				baixoEsquerda.start(this);
				angulo = 225;
				return;
			}

			grupoAni.stopAll(baixo);
			baixo.start(this);
			angulo = 270;
			return;
		}

		if (isDireita) {
			grupoAni.stopAll(direita);
			direita.start(this);
			angulo = 360;
			return;
		}

		if (isEsquerda) {
			grupoAni.stopAll(esquerda);
			esquerda.start(this);
			angulo = 180;
			return;
		}

		grupoAni.stopAll(parado);
		parado.start(this);
		angulo = PARADO;
		isAndando = false;
	}

	private void move() {
		if (isAndando && !isGrounded) {
			x += Math.cos(Math.toRadians(angulo)) * speed;
			y -= Math.sin(Math.toRadians(angulo)) * speed;
		}

		if (x < 0) {
			x = 0;
		}

		if (x > (JanelaJogo.WIDTH - width)) {
			x = (JanelaJogo.WIDTH - width);
		}

		if (y < 0) {
			y = 0;
		}

		if (y > (JanelaJogo.HEIGHT - height)) {
			y = (JanelaJogo.HEIGHT - height);
		}

	}
	
	private void initListener() {
		
		if (mousePressed != null || mouseReleased != null || keyPressed != null || keyReleased != null) {
			ListenerManager.removeListener(ListenerManager.MOUSE_PRESSED, mousePressed);
			ListenerManager.removeListener(ListenerManager.MOUSE_RELEASED, mouseReleased);
			ListenerManager.removeListener(ListenerManager.KEY_PRESSED, keyPressed);
			ListenerManager.removeListener(ListenerManager.KEY_RELEASED, keyReleased);
		}
		
		ListenerManager.addListener(ListenerManager.KEY_PRESSED, keyPressed = new KeyListener() {
			public void acao(KeyEvent e) {
				if (!Principal.menuMochila) {
					for (int x = 0; x < 4; x++) {
						if (e.getKeyCode() == MenuMochila.hotkeys.get(x).keycode) {
							MenuMochila.hotkeys.get(x).usar();
						}
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_W) {
					isCima = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_S) {
					isBaixo = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_D) {
					isDireita = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_A) {
					isEsquerda = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_E) {
					if (isTrapPronta && Trap.todasTraps.size() < MAX_TRAP_INICIAL + MenuLevel.getMaxTrapMod()) {
						if (usarMana(60 - MenuLevel.getCustoTrapMod())) {
							Tile tile = TileMap.tileMap[(int) Math.floor(getXCentro() / Tile.WIDTH)][(int) (Math.floor(getYCentro() / Tile.HEIGHT))];
							
							if (MenuLevel.getTrapFireLevel() > 0) {
								tile.setTrap(new TrapFogo(tile, getDanoVar()));
							} else {
								tile.setTrap(new TrapVenenosa(getDanoVar(), 3 + (int) Math.floor(level / 3), tile));
							}
							
							new StatusTrapRecarga(getDelayTrap());

							isTrapPronta = false;

							timerRecargaTrap = new Timer(5, new ActionListener() {
								int tickAtual = Principal.tickTotal;

								public void actionPerformed(ActionEvent e) {
									if (Principal.tickTotal >= tickAtual + getDelayTrap()) {
										isTrapPronta = true;
										timerRecargaTrap.stop();
									}

								}
							});
							timerRecargaTrap.start();

						}
					} else {
						if (Trap.todasTraps.size() >= MAX_TRAP_INICIAL + MenuLevel.getMaxTrapMod()) new BalaoDeFala("Ja tenho muitas traps no campo!", Player.this, 100);
						semMana.play();
					}

				}
				
			}
		});
		
		ListenerManager.addListener(ListenerManager.KEY_RELEASED, keyReleased = new KeyListener() {
			public void acao(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					isCima = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_S) {
					isBaixo = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_D) {
					isDireita = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_A) {
					isEsquerda = false;
				}
				
			}
		});
		
		ListenerManager.addListener(ListenerManager.MOUSE_PRESSED, mousePressed = new MouseListener() {
			public void acao(MouseEvent e) {
				if (Principal.isPausado() || Principal.editorTiles || Principal.menuMochila || Principal.menuLevel) return;

				if (e.getButton() == 1 && e.getY() < JanelaJogo.HEIGHT) {
					isAtkando = true;
					autoAtk();
				}
				
			}
		});
		
		ListenerManager.addListener(ListenerManager.MOUSE_RELEASED, mouseReleased = new MouseListener() {
			public void acao(MouseEvent e) {
				if (e.getButton() == 1) isAtkando = false;
				
			}
		});
	}


	public void pintarMob(Graphics2D g) {
		if (isInvisivel) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			grupoAni.pintarAll(g);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			grupoAni.pintarAll(g);
		}

		//alcance
		//g.drawOval(getX() - (getAlcance() - width / 2), getYSpriteCentro() - (getAlcance() - height / 2), getAlcance() * 2, getAlcance() * 2);

		//mouse
		g.setColor(Color.BLACK);
		g.drawRect((int) JanelaJogo.xMouse, (int) JanelaJogo.yMouse, 1, 1);

	}

	public Luz getLuz() {
		return luz;
	}
}
