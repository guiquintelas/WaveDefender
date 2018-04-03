package wave.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.UIManager;

import wave.audio.BackMusic;
import wave.audio.SoundEffect;
import wave.graphics.BalaoDeFala;
import wave.graphics.ExpValorGanhaAnimaçao;
import wave.graphics.Grafico;
import wave.graphics.ModValorAnimaçao;
import wave.graphics.NovoStatusAnimaçao;
import wave.graphics.Sangue;
import wave.graphics.animaçao.Animaçao;
import wave.graphics.animaçao.AnimaçaoInterface;
import wave.graphics.light.Luz;
import wave.graphics.light.LuzAmbiente;
import wave.graphics.light.LuzBruta;
import wave.gui.DisplayNomes;
import wave.gui.GUI;
import wave.gui.menus.MenuMochila;
import wave.gui.menus.MenuOpcoes;
import wave.input.ListenerManager;
import wave.item.Chest;
import wave.item.Item;
import wave.item.PotionForça;
import wave.item.PotionVida;
import wave.map.MapGenerator;
import wave.map.TileMap;
import wave.map.trap.Trap;
import wave.mob.Druida;
import wave.mob.Mago;
import wave.mob.Mob;
import wave.mob.Monstro;
import wave.mob.MonstroPadrao;
import wave.mob.Ogro;
import wave.mob.Player;
import wave.mob.Ranged;
import wave.mob.Vampiro;
import wave.particula.Particula;
import wave.projetil.Projetil;
import wave.tools.Variator;

public class Principal implements Runnable {
	public static boolean isRodando       = true;
	private static boolean isPausado      = false;
	public static final boolean debugPF   = false;
	public static boolean monstrosParados = false;
	public static boolean editorTiles 	  = false;
	public static boolean light           = true;
	public static boolean dia             = false;
	public static boolean sempreNoite     = true;
	public static boolean showFPS         = true;
	public static boolean grafico  		  = false;
	
	private static boolean menuOpçoes = false;
	public static boolean menuMochila = false;
	public static boolean menuChar = false;
	public static boolean menuLevel = false;

	public static Tela tela;
	public JanelaJogo janela;

	public static int tickTotal = 0;
	private static int tickPerSec = 0;
	public static final int TPS = 50;
	public static int nivel = 1;
	public static boolean isNivelFinalizado = false;
	private static final int DELAY_NOVO_NIVEL = 50; //1 segundo
	private Thread thread;

	public static BackMusic musicas;
	
	private  long timeU;
	private  long timeD;
	private  long timeD1;
	private  long timeTile;
	private  long timeD2;
	private  long timeD3;
	private  long timeD4;
	private  long timeD5;
	private  long timeD6;
	private  long timeD7;
	private  long timeD8;
	private  long timeD9;
	private  long timeD10;
	private  long timeD11;
	private  long timeD12;
	
	private ArrayList<Long> times = new ArrayList<Long>(); 
	private ArrayList<Long> timesVar = new ArrayList<Long>();

	//timers
	private Timer timerDelayNivelNovo;

	public Principal() {
		//BackMusic.setMute(true);
		musicas = new BackMusic(new String[] { "/Music/jungle.ogg", "/Music/getLuckBack.ogg", "/Music/shop.ogg", "/Music/gimme.ogg", "/Music/leanOn.ogg", "/Music/radiaçao.ogg", "/Music/mega.ogg" });
		//musicas = new BackMusic(new String[] { "/SFX/hit2.ogg", "/SFX/hit3.ogg", "/SFX/hit4.ogg" });

		
		tela = new Tela();
		janela = new JanelaJogo("Wave Defender 1.0.4", tela, this);
		new ListenerManager().init(JanelaJogo.janela);
		GUI.init();
		janela.init();

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	private void update() {
		long timeUn = System.nanoTime();
		synchronized (Mago.class) {
			long time = System.nanoTime();
			updateDimiObjs();
			 timeD = System.nanoTime() - time;
			long time1 = System.nanoTime();
			//long timeVar = System.nanoTime();
			updateVariator();
			timeD1 = System.nanoTime() - time1;
			//System.out.println("Variator Delay: " + (System.nanoTime() - timeVar)/1000000.0 + "ms");
			long timeTile = System.nanoTime();
			TileMap.update();
			//updateSFX2D();
			 this.timeTile = System.nanoTime() - timeTile;
			long time2 = System.nanoTime();
			updateSFX();
			 timeD2 = System.nanoTime() - time2;
			long time3 = System.nanoTime();
			updateModAni();
			 timeD3 = System.nanoTime() - time3;
			long time4 = System.nanoTime();
			updateBaloes();
			 timeD4 = System.nanoTime() - time4;
			long time5 = System.nanoTime();
			updateExpGanhaAni();
			 timeD5 = System.nanoTime() - time5;
			long time6 = System.nanoTime();
			updateNovoSatusAni();
			 timeD6 = System.nanoTime() - time6;
			long time8 = System.nanoTime();
			updateAni();
			timeD8 = System.nanoTime() - time8;
			long time9 = System.nanoTime();
			updateParticulas();	
			timeD9 = System.nanoTime() - time9;
			long time10 = System.nanoTime();
			DisplayNomes.update();
			 timeD10 = System.nanoTime() - time10;
			long time11 = System.nanoTime();
			checaFimNivel();
			 timeD11 = System.nanoTime() - time11;
			long time12 = System.nanoTime();
			musicas.play(); 
			if (light && !dia) updateLuz();
			timeD12 = System.nanoTime() - time12;
			long time7 = System.nanoTime();
			updateLuzBruta();
			timeD7 = System.nanoTime() - time7;
			if (grafico) Grafico.update();
			
		}
		timeU = System.nanoTime() - timeUn;
	}

	public synchronized void run() {
		init();
		int travou = 0;
		int tick = 0;
		long timer = System.currentTimeMillis();
		while (isRodando) {
			long now = System.currentTimeMillis();

			if (!isPausado) {
				update();
				tickTotal++;
			}
			GUI.update();
			
			tick++;
			

			if (System.currentTimeMillis() - timer >= 1000 && !isPausado) {
				if (tick < 48) {
					travou++;
					
					System.out.println("TOTAL:.........." + timeU/1000000.0 + "ms");
					System.out.println("updateDimiObjs:." + timeD/1000000.0 + "ms");
					System.out.println("Variator:......." + timeD1/1000000.0 + "ms");
					System.out.println("TileMap:........" + timeTile/1000000.0 + "ms");			
					System.out.println("SFX:............" + timeD2/1000000.0 + "ms");
					System.out.println("ModAni:........." + timeD3/1000000.0 + "ms");
					System.out.println("Balao:.........." + timeD4/1000000.0 + "ms");
					System.out.println("ExpGanhaAni:...." + timeD5/1000000.0 + "ms");
					System.out.println("NovoSatusAni:..." + timeD6/1000000.0 + "ms");
					System.out.println("UpdadeAni:......" + timeD8/1000000.0 + "ms");
					System.out.println("Particulas:....." + timeD9/1000000.0 + "ms");
					System.out.println("DisplayNome:...." + timeD10/1000000.0 + "ms");
					System.out.println("ChecaFimNivel:.." + timeD11/1000000.0 + "ms");
					System.out.println("Luz:............" + timeD12/1000000.0 + "ms");
					System.out.println("LuzBruta:......." + timeD7/1000000.0 + "ms");
					
					System.out.println(DimensionalObj.todosDimensionalObjs);
					System.out.println(times);
					
					System.out.println();
					
					System.out.println(Variator.todosVariator);
					System.out.println(timesVar);
					
					System.out.println();
					
					System.out.println("GrupoAni:....... " + Player.getPlayer().timeD/1000000.0 + "ms");
					//System.out.println("Time1:  " + Player.getPlayer().timeD1);
					System.out.println("ChecaVivo:......." + Player.getPlayer().timeD2/1000000.0 + "ms");
					System.out.println("ChecaMove:......." + Player.getPlayer().timeD3/1000000.0 + "ms");
					System.out.println("Move:............" + Player.getPlayer().timeD4/1000000.0 + "ms");
					System.out.println("RegenPassiva:...." + Player.getPlayer().timeD5/1000000.0 + "ms");
					System.out.println("ManaRegen:......." + Player.getPlayer().timeD6/1000000.0 + "ms");
					System.out.println("BarraVida:......." + Player.getPlayer().timeD7/1000000.0 + "ms");
					System.out.println("ChecaGrounded:..." + Player.getPlayer().timeD8/1000000.0 + "ms");
					System.out.println("Particulas:......" + Player.getPlayer().timeD1/1000000.0 + "ms");
					System.out.println("ChecaTrapPronta:." + Player.getPlayer().timeD9/1000000.0 + "ms");
					
				}

				System.out.println("TPS: " + tick + " FPS: " + tela.getFPS() + " Travou: " + travou);
				
				
				tickPerSec = tick;
				tick = 0;
				timer = System.currentTimeMillis();
			}

			long tempoDecorrido = System.currentTimeMillis() - now;
			if (tempoDecorrido > 1000 / TPS) {
				tempoDecorrido = 1000 / TPS;
			}

			try {
				Thread.sleep((1000 / TPS - tempoDecorrido));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
		if (light) LuzAmbiente.cleanUp();
		Luz.todasLuz.clear();
		LuzBruta.todasLuzBruta.clear();
		Grafico.clearAll();
		tela.pintarGameOver();
	}

	private void init() {
		long tempoA = System.currentTimeMillis();
		System.out.println("inicio");
		Grafico.init();
		MapGenerator.criarMapa();
		new Player(JanelaJogo.WIDTH / 2, JanelaJogo.HEIGHT / 2);	
		MenuMochila.init();	
		LuzAmbiente.init();		
		novoNivel();
		tela.init();
		System.out.println("Tempo de Inicialização: " + (System.currentTimeMillis() - tempoA) + "ms");
	}

	public void novoJogo() {
		nivel = 1;
		isNivelFinalizado = false;
		MapGenerator.clearMap();
		Item.todosItens.clear();
		PotionForça.reset();
		PotionVida.reset();
		DimensionalObj.todosDimensionalObjs.clear();
		Particula.todasParticulas.clear();
		NovoStatusAnimaçao.todasStatusAni.clear();
		AnimaçaoInterface.todasAni.clear();
		Mob.todosMobs.clear();
		Monstro.todosMontros.clear();
		Projetil.todosProjetils.clear();
		ModValorAnimaçao.todosModAni.clear();
		GUI.reset();
		MenuMochila.reset();
		tela.resetarImgPadrao();
		Sangue.sangueParaPintar.clear();
		Trap.todasTraps.clear();
		isRodando = true;	
		
		thread = new Thread(this);
		thread.start();
	}

	private void checaFimNivel() {
		if (Monstro.todosMontros.isEmpty() && !isNivelFinalizado) {
			if (timerDelayNivelNovo != null) {
				if (timerDelayNivelNovo.isRunning()) {
					return;
				}
			}

			final int tickAtual = tickTotal;
			timerDelayNivelNovo = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (tickTotal >= (tickAtual + DELAY_NOVO_NIVEL)) {
						nivel++;
						isNivelFinalizado = true;
						tela.pintarFimNivel();
						timerDelayNivelNovo.stop();
					}
				}
			});
			timerDelayNivelNovo.start();

		}

	}

	public void novoNivel() {
		synchronized (Mago.class) {
			Player.getPlayer().setX(JanelaJogo.WIDTH / 2);
			Player.getPlayer().setY(JanelaJogo.HEIGHT / 2);
			Monstro.todosMontros.clear();
			// formula de mobs: nivel + nivel / 4 + 1
			if (debugPF) {
				criarMob(1);
			} else {
				criarMob(nivel + nivel / 4 + 1);
			}

			isNivelFinalizado = false;
			Projetil.todosProjetils.clear();
			Item.clear();
			ModValorAnimaçao.todosModAni.clear();
			if (nivel >1) Chest.clear();// para nao deletar o bau inicial
			Chest.criarBau();
			System.out.println("novo nivel iniciado");
		}

	}

	private void criarMob(int quantos) {
		for (int x = 1; x <= quantos; x++) {
			int chance = 1 + (int) (Math.random() * 100 - (quantos * 2));
			if (Principal.nivel > 1 && chance <= 40) {
				if (Principal.nivel > 4 && chance <= 10) {
					int quantosMagos = 0;
					for (int m = 0; m < Monstro.todosMontros.size(); m++) {
						if (Monstro.todosMontros.get(m) instanceof Ranged) {
							quantosMagos++;
						}
					}

					if (quantosMagos < nivel - 4) {
						int chanceMD = (int) (Math.random() * 2);
						if (chanceMD == 0) {
							new Mago();
						} else {
							new Druida();
						}

						continue;
					}

				}
				int chanceOV = (int) (Math.random() * 2);
				if (chanceOV == 0) {
					new Ogro();
				} else {
					new Vampiro();
				}
				continue;
			}
			
			new MonstroPadrao();
			
		}
		

		
	}

	private void updateModAni() {
		for (int x = 0; x < ModValorAnimaçao.todosModAni.size(); x++) {
			ModValorAnimaçao.todosModAni.get(x).update();
		}
	}

	private void updateNovoSatusAni() {
		for (int x = 0; x < NovoStatusAnimaçao.todasStatusAni.size(); x++) {
			NovoStatusAnimaçao.todasStatusAni.get(x).update();
		}
	}

	private void updateParticulas() {
		for (int x = 0; x < Particula.todasParticulas.size(); x++) {
			Particula.todasParticulas.get(x).update();
		}
	}

	private void updateExpGanhaAni() {
		for (int x = 0; x < ExpValorGanhaAnimaçao.todasExps.size(); x++) {
			ExpValorGanhaAnimaçao.todasExps.get(x).update();
		}
	}

//	private void updateSFX2D() {
//		for (int x = 0; x < SoundEffect2D.sfxs2d.size(); x++) {
//			SoundEffect2D.sfxs2d.get(x).update();
//		}
//	}

	private void updateAni() {
		for (int x = 0; x < Animaçao.todasAni.size(); x++) {
			AnimaçaoInterface.todasAni.get(x).update();
		}
	}
	
	private void updateDimiObjs() {
		times.clear();
		for (int x = 0; x < DimensionalObj.todosDimensionalObjs.size(); x++) {
			long time = System.nanoTime();
			DimensionalObj.todosDimensionalObjs.get(x).update();
			times.add(System.nanoTime() - time);
		}
	}
	
	
	private void updateBaloes() {
		for (int x = 0; x < Mob.todosMobs.size(); x++) {
			if (BalaoDeFala.todosBaloes.containsKey(Mob.todosMobs.get(x))) {
				BalaoDeFala.todosBaloes.get(Mob.todosMobs.get(x)).update();
			}
		}
	}
	
	private void updateSFX() {
		for (int x = 0; x < SoundEffect.sfxs.size(); x++) {
			SoundEffect.sfxs.get(x).update();			
		}
	}
	
	private void updateLuz() {
		for (int x = 0; x < Luz.todasLuz.size(); x++) {
			Luz.todasLuz.get(x).update();
		}
	}
	
	private void updateLuzBruta() {
		for (int x = 0; x < LuzBruta.todasLuzBruta.size(); x++) {
			LuzBruta.todasLuzBruta.get(x).update();
		}
	}
	
	private void updateVariator() {
		timesVar.clear();
		for (int x = 0; x < Variator.todosVariator.size(); x++) {
			long time = System.nanoTime();
			Variator.todosVariator.get(x).update();
			timesVar.add(System.nanoTime() - time);
		}
	}
	
	public static boolean menuOpçoes() {
		return menuOpçoes;
	}
	
	public static void setMenuOpçoes(boolean menu) {
		setPause(menu);
		menuOpçoes = menu;	
		MenuOpcoes.update();
		
	}
	
	public static int getTPS() {
		return tickPerSec;
	}
	
	public static void setPause(boolean pausar) {
		if (pausar == isPausado) return;
		
		isPausado = pausar;
		if (pausar)Tela.criarImgPausa();
	}
	
	public static boolean isPausado() {
		return isPausado;
	}	

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.setProperty("org.lwjgl.librarypath", "C:/Documents and Settings/ESCRITORIO/Meus documentos/Java Projetos/WaveDefender/native");
		//System.setProperty("org.lwjgl.librarypath", "E:/Documents/Testes em Java/Master Engine/native");

		@SuppressWarnings("unused")
		Principal main = new Principal();
	}

}
