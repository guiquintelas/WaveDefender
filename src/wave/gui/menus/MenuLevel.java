package wave.gui.menus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.graphics.imgprocessing.Filter;
import wave.gui.Descritivo;
import wave.gui.ferramentas.Botao;
import wave.mob.Player;
import wave.principal.Dimensional;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.Util;

public class MenuLevel extends Dimensional{	
	public static final int WIDTH = 350;
	public static final int HEIGHT = 250;
	
	private static final int x = (JanelaJogo.WIDTH / 2) - WIDTH / 2;
	private static final int y = (JanelaJogo.HEIGHT) / 2 - HEIGHT / 2;
	
	private static final Font fontTitulo = new Font("Bookman Old Style", Font.BOLD, 14);
	private static final Font font = new Font("Bookman Old Style", Font.PLAIN, 12);
	
	private static final BufferedImage imgLines = Util.carregarImg("/Sprites/skillsLine.png");
	
	protected static final ArrayList<BotaoSkill> botoesSkill = new ArrayList<BotaoSkill>();
	protected static int pontosLevelTemp;
	
	private static BotaoSkill maxTrap;
	private static BotaoSkill custoTrap;
	private static BotaoSkill fireTrap;
	private static BotaoSkill trapExplosiva;
	private static BotaoSkill potionRate;
	private static BotaoSkill maxAlcance;
	private static BotaoSkill chanceVeneno;
	private static BotaoSkill menosDelay;
	private static BotaoSkill projetilDuplo;
	private static BotaoSkill projetilTriplo;
	
	private static final BufferedImage imgVeneno = Util.carregarImg("/Sprites/veneno.png");
	private static final BufferedImage imgPotRate = Util.carregarQuickImg("pots");
	private static final BufferedImage imgAlc = Util.carregarQuickImg("alc");
	private static final BufferedImage imgProjDuplo = Util.carregarQuickImg("projDuplo");
	private static final BufferedImage imgProjTriplo = Util.carregarQuickImg("projTriplo");
	private static final BufferedImage[] imgMaxTrap = {Util.carregarQuickImg("maxTrap"),
														Util.carregarQuickImg("maxTrap2"),
														Util.carregarQuickImg("maxTrap3"),
														Util.carregarQuickImg("maxTrap4"),
														Util.carregarQuickImg("maxTrap5")};
	
	private static final Botao botaoAplicar = new Botao(x + WIDTH - 150, y + HEIGHT - 27, 60, 20, new Color(97, 97, 97), new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (maxAlcance.valorTemp > maxAlcance.valorAtual) {
				Player.getPlayer().getLuz().raioVar.changeNumeroAlvoOsc(Player.getPlayer().getAlcance() + 50 + (maxAlcance.valorTemp - maxAlcance.valorAtual) * 10);
			}
			
			for (int i = 0; i < botoesSkill.size(); i++) {
				botoesSkill.get(i).valorAtual = botoesSkill.get(i).valorTemp;
				Player.getPlayer().pontosLevel = pontosLevelTemp;
			}
			
			
		}
	});
	
	private static final Botao botaoRetornar = new Botao(x + WIDTH - 80, y + HEIGHT - 27, 60, 20, new Color(97, 97, 97), new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			pontosLevelTemp = Player.getPlayer().pontosLevel;
			for (int i = 0; i < botoesSkill.size(); i++) {
				botoesSkill.get(i).valorTemp = botoesSkill.get(i).valorAtual;
				botoesSkill.get(i).botao.setText(botoesSkill.get(i).valorAtual + "/" + botoesSkill.get(i).max);
			}
		}
	});
	
	
	public static void abrirMenu() {
		if (!Principal.menuMochila && !Principal.menuOpcoes()) {
			Principal.menuLevel = true;
			Principal.setPause(true);
			pontosLevelTemp = Player.getPlayer().pontosLevel;
			for (int i = 0; i < botoesSkill.size(); i++) {
				botoesSkill.get(i).valorTemp = botoesSkill.get(i).valorAtual;
				botoesSkill.get(i).botao.setText(botoesSkill.get(i).valorAtual + "/" + botoesSkill.get(i).max);
			}
		}
	}
	
	public static void fecharMenu() {
		Principal.menuLevel = false;
		Principal.setPause(false);
	}
	
	public static void reset() {
		for (int i = 0; i < botoesSkill.size(); i++) {
			botoesSkill.get(i).valorAtual = 0;
			botoesSkill.get(i).valorTemp = 0;
			botoesSkill.get(i).botao.setText(0 + "/" + botoesSkill.get(i).max);
		}
	}
	
	public static void init() {
		
		botaoAplicar.setSombra(new Color(157, 157, 157));
		botaoAplicar.setText("Aplicar", new Font("Arial", Font.PLAIN, 13), Color.WHITE);
		
		botaoRetornar.setSombra(new Color(157, 157, 157));
		botaoRetornar.setText("Retornar", new Font("Arial", Font.PLAIN, 13), Color.WHITE);
		
		maxTrap = new BotaoSkill(x + 10, y + 30, 4, imgMaxTrap[0], "Maximo de Traps: A cada nivel dessa habilidade o maximo de traps em campo é acrescentado em 1.");
		potionRate = new BotaoSkill(x + WIDTH - 40 -10, y + 30, 4, imgPotRate, "Potion Rate: A cada nivel dessa habilidade a chance de um monstro droppar uma POTION DE VIDA aumenta em 2%.");
		maxAlcance = new BotaoSkill(x + WIDTH/2 - 40, y + 30, 4, imgAlc , "Alcance Maximo: A cada nivel dessa habilidade seu alcance maximo aumenta em 10.");
		custoTrap = new BotaoSkill(x + 10 + 40 + 5, y + 30, 3, "Custo Reduzido: A cada nivel nessa habilidade sua trap custa menos 10 de mana.");
		chanceVeneno = new BotaoSkill(x + WIDTH/2 + 5, y + 30, 7, imgVeneno, "Projetil Venenoso: A cada nivel nessa habilidade seu ataque ganha 3% de chance de envenenar.");
		
		fireTrap = new BotaoSkill(x + 10 + 40 + 5, y + 30 + 35 + 40, 4, "Trap Incendiária: Sua trap agora bota fogo em seus inimigos! A cada nivel dessa habilidade seu fogo se espalha para +1 monstro.");
		fireTrap.addBotaoLimitador(custoTrap, 3);
		
		menosDelay = new BotaoSkill(x + 10, y + 30 + 35 + 40, 3, "Recarregamento Rápido: A cada nivel nessa habilidade a recarga de sua trap diminui em 1.5 segundos");
		menosDelay.addBotaoLimitador(maxTrap, 2);
		
		trapExplosiva = new BotaoSkill(x + 30 + 3, y + 30 + 35 + 40 + 40 + 20, 3, "Sua trap agora atinge em ÁREA! Todos atingidos receberão o status da trap e serâo arremessados. A cada nivel dessa habilidade +20 de ÁREA!");
		trapExplosiva.addBotaoLimitador(fireTrap, 2);
		trapExplosiva.addBotaoLimitador(menosDelay, 2);
		
		projetilDuplo = new BotaoSkill(x + WIDTH/2 - 40, y + 30 + 35 + 40, 1, imgProjDuplo, "Projetil Duplo: Seu projetil magicamente se duplica! Mas sem custo de mana também!");
		projetilDuplo.addBotaoLimitador(chanceVeneno, 3);
		projetilDuplo.addBotaoLimitador(maxAlcance, 4);
		
		projetilTriplo = new BotaoSkill(x + WIDTH/2 + 5, y + 30 + 35 + 40 + 40 + 20, 1, imgProjTriplo, "Projetil Triplo: Sinta a emocão de atirar com uma shotgun.");
		projetilTriplo.addBotaoLimitador(projetilDuplo, 1);
		projetilTriplo.addBotaoLimitador(chanceVeneno, 6);
	}
	
	public static void update() {
		botaoAplicar.update();
		botaoRetornar.update();
		updateBotoesSkill();
		if (maxTrap.valorTemp != 0) maxTrap.botao.setAllImg(imgMaxTrap[maxTrap.valorTemp]);
	}

	private static void updateBotoesSkill() {
		for (int i = 0; i < botoesSkill.size(); i++) {
			botoesSkill.get(i).update();
		}
	}
	
	private static void pintarBotoesSkill(Graphics2D g) {
		for (int i = 0; i < botoesSkill.size(); i++) {
			botoesSkill.get(i).botao.pintar(g);
		}
	}
	
	public static int getMaxTrapMod() {
		return maxTrap.valorAtual;
	}
	
	public static int getPotionRateMod() {
		return potionRate.valorAtual * 2;
	}
	
	public static int getAlcanceMod() {
		return maxAlcance.valorAtual * 10;
	}
	
	public static int getCustoTrapMod() {
		return custoTrap.valorAtual * 10;
	}
	
	public static int getChanceVenenoMod() {
		return chanceVeneno.valorAtual * 3;
	}
	
	public static float getDelayMod() {
		return menosDelay.valorAtual * 1.5f;
	}
	
	public static boolean isProjetilDuplo() {
		return projetilDuplo.valorAtual == 1;
	}
	
	public static boolean isProjetilTriplo() {
		return projetilTriplo.valorAtual == 1;
	}
	
	public static int getTrapFireLevel() {
		return fireTrap.valorAtual;
	}
	
	public static void pintar(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, WIDTH, HEIGHT, 5, 5);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		//g.setColor(Color.LIGHT_GRAY);
		//g.fillRoundRect(x + 5, y + 25, WIDTH - 10, HEIGHT - 60, 5, 5);
		g.drawImage(imgLines, x + 5, y + 25, null);
		
		g.setColor(Color.WHITE);
		g.setFont(fontTitulo);
		g.drawString("Menu Level", x + WIDTH / 2 - 41, y + 18);
		
		g.setFont(font);
		g.drawString("Pontos de Habilidade: " + pontosLevelTemp, x + 5, y + HEIGHT - 13);
		
		g.setFont(new Font("Arial", Font.PLAIN, 11));
		g.drawString(maxTrap.valorTemp +  " " + 2 , x + 20 + 3, y + 30 + 40 + 20 + 2);
		g.drawString(custoTrap.valorTemp +  " " + 3 , x + 20 + 3 + 45, y + 30 + 40 + 20 + 2);
		g.drawString(maxAlcance.valorTemp +  " " + 4 , x + 20 + 3 + 126, y + 30 + 40 + 20 + 6);
		g.drawString(chanceVeneno.valorTemp +  " " + 3 , x + 20 + 3 + 162, y + 30 + 40 + 20 + 6);
		g.drawString(chanceVeneno.valorTemp +  " " + 6 , x + 20 + 3 + 179, y + 30 + 40 + 20 + 6 + 49);
		g.drawString(projetilDuplo.valorTemp +  " " + 1 , x + 20 + 3 + 126, y + 30 + 40 + 20 + 81);
		
		botaoAplicar.pintar(g);
		botaoRetornar.pintar(g);
		pintarBotoesSkill(g);
		
	}
}





final class BotaoSkill extends Descritivo{
	protected Botao botao;
	protected int max;
	protected int valorAtual = 0;
	protected int valorTemp = 0;
	protected String descricao;
	private BufferedImage img = null;
	private BufferedImage imgPretoBranco;
	public static final int SIZE = 40;
	
	public SkillListenerDireita slDireita;
	public SkillListenerEsquerda slEsquerda;
	
	public BotaoSkill(int x, int y, int max, String descricao) {
		setXYWH(x, y);
		this.max = max;
		this.descricao = descricao;
		slDireita = new SkillListenerDireita(this);
		slEsquerda = new SkillListenerEsquerda(this);
		
		botao = new Botao(x, y, SIZE, SIZE, new Color(97, 97, 97), slEsquerda);
		botao.setText("0", new Font("Arial", Font.PLAIN, 13), Color.WHITE);
		botao.setTextPosicao(Botao.BAIXO_DIREITA);
		botao.setSombra(new Color(157, 157 ,157));
		botao.setActionListenerDireito(slDireita);
		MenuLevel.botoesSkill.add(this);
	}
	
	public BotaoSkill(int x, int y, int max, BufferedImage img, String descricao) {
		setXYWH(x, y);
		this.max = max;
		this.descricao = descricao;
		slDireita = new SkillListenerDireita(this);
		slEsquerda = new SkillListenerEsquerda(this);
		this.img = img;
		this.imgPretoBranco = Filter.pretoBranco(img);
		
		botao = new Botao(x, y, SIZE, SIZE, img, img, img, slEsquerda);
		botao.setText("0", new Font("Arial", Font.PLAIN, 13), Color.WHITE);
		botao.setTextPosicao(Botao.BAIXO_DIREITA);
		botao.setSombra(new Color(157, 157 ,157));
		botao.setBG(new Color(97, 97, 97));
		botao.setActionListenerDireito(slDireita);
		MenuLevel.botoesSkill.add(this);
	}
	
	public boolean isAtivo() {
		return Principal.menuLevel;
	}
	
	private void setXYWH(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = SIZE;
		this.height = SIZE;
	}
	
	public void addBotaoLimitador(BotaoSkill botao, int valor) {
		slEsquerda.addBotaoLimitador(botao, valor);
		botao.addBotaoQueTrava(this);
	}
	
	private void addBotaoQueTrava(BotaoSkill botao) {
		slDireita.addBotaoQueTrava(botao);
	}
	
	private void checaImg() {
		if (img != null) {
			if (valorTemp > 0) {
				botao.setAllImg(img);
			} else {
				botao.setAllImg(imgPretoBranco);
			}	
		}
	}
	
	public void update() {
		botao.update();
		botao.setEnable(slEsquerda.destravado());
		checaImg();
	}

	@Override
	public String getDescricao() {
		return descricao;
	}
	
}





final class SkillListenerEsquerda implements ActionListener {
	private ArrayList<BotaoSkill> botaoLimitador = new ArrayList<BotaoSkill>();
	private ArrayList<Integer> valorLimitador = new ArrayList<Integer>();
	private BotaoSkill botaoSkill;
	
	public SkillListenerEsquerda(BotaoSkill botao) {
		this.botaoSkill = botao;
	}
	
	public void addBotaoLimitador(BotaoSkill botao, int valor) {
		botaoLimitador.add(botao);
		valorLimitador.add(valor);
	}
	
	public boolean destravado() {
		if (!botaoLimitador.isEmpty()) {
			for (int i = 0; i < botaoLimitador.size(); i++) {
				if (botaoLimitador.get(i).valorTemp < valorLimitador.get(i)) {
					break; 
				}
				
				if (i == botaoLimitador.size() - 1) return true;
			}
		} else {
			return true;
		}
		
		return false;
	}

	public void actionPerformed(ActionEvent e) {		
		if (botaoSkill.valorTemp < botaoSkill.max && MenuLevel.pontosLevelTemp > 0 && destravado()) {
			botaoSkill.valorTemp += 1;
			MenuLevel.pontosLevelTemp--;
		}
		botaoSkill.botao.setText(botaoSkill.valorTemp + "/" + botaoSkill.max);
	}
}






final class SkillListenerDireita implements ActionListener {
	private ArrayList<BotaoSkill> botaoQueTrava = new ArrayList<BotaoSkill>();
	private BotaoSkill botaoSkill;
	
	
	public SkillListenerDireita(BotaoSkill botao) {
		this.botaoSkill = botao;
	}
	
	public void addBotaoQueTrava(BotaoSkill botao) {
		botaoQueTrava.add(botao);
	}

	public void actionPerformed(ActionEvent e) {
		boolean liberado = false;
		if (!botaoQueTrava.isEmpty()) {
			for (int i = 0; i < botaoQueTrava.size(); i++) {
				if (botaoQueTrava.get(i).valorTemp != 0) {
					break; 
				}
				liberado = true;
			}
		} else {
			liberado = true;
		}
		
		if (botaoSkill.valorTemp > botaoSkill.valorAtual && liberado) {
			botaoSkill.valorTemp -= 1;
			MenuLevel.pontosLevelTemp++;
		}
		botaoSkill.botao.setText(botaoSkill.valorTemp + "/" + botaoSkill.max);
	}
}


