package wave.particula;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.principal.Dimensional;
import wave.principal.Principal;
import wave.tools.Util;

public class CriadorDeParticulas extends Dimensional {

	private int widthPar;
	private int heightPar;

	private double speedPar = 0.25;
	private double speedParVariacao;
	private double anguloPar = 90;
	private int porcentagem;
	private int alphaDuraçao = 50;
	private int anguloVariacao;
	private int alphaVariacao;
	private double rotacaoVel = 0;
	private double rotacaoVelVariacao = 0;
	private int alphaDelay = 0;
	private float gravidadeRatePar = 0;
	private float gravdiadeRateVar = 0;
	private int porcentagemInicial = 0;

	private ArrayList<Color> cores = new ArrayList<Color>();
	private ArrayList<Color> coresLuz = new ArrayList<Color>();

	private ArrayList<ArrayList<BufferedImage>> imgs = new ArrayList<ArrayList<BufferedImage>>();
	private float scale;
	private int tickPorIndex;

	public Dimensional d = null;
	
	private ParticulaAnimation parAni = null;
	
	//Luz
	private int raio;
	private double força;
	private int fadeIn;
	private int fadeOut;
	private boolean varRaio;
	private boolean varForça;
	private int oscRaio;
	private int oscForça;
	private boolean isLuzCustom = false;	
	private boolean isLuz = false;
	private boolean isBruta = false;

	private boolean isProduzindo = false;
	private boolean isAnguloVariado = false;
	private boolean isAlphaVariado = false;
	private boolean isContorno = false;
	private boolean seguindo = false;
	private boolean isAlphaAtivo = true;
	private boolean isAni = false;
	private boolean isRotacaoVelVariado = false;
	private boolean isSpeedVariado = false;
	private boolean isGravidadeRateVariado = false;
	private boolean isInvisivel = false;

	private Timer timerDelay;
	private Timer timerDelayStatico;

	public CriadorDeParticulas(int xOri, int yOri, int widthOri, int heightOri, int widthPar, int heightPar, Color cor, int porcentagem) {
		this.x = xOri;
		this.y = yOri;
		this.widthPar = widthPar;
		this.heightPar = heightPar;
		this.width = widthOri;
		this.height = heightOri;
		cores.add(cor);
		this.porcentagem = porcentagem;
		this.porcentagemInicial = porcentagem;
	}

	public CriadorDeParticulas(int xOri, int yOri, int widthOri, int heightOri, ArrayList<BufferedImage> imgs, float scale, int tickPorIndex, int porcentagem) {
		this.x = xOri;
		this.y = yOri;
		this.widthPar = 1;
		this.heightPar = 1;
		this.width = widthOri;
		this.height = heightOri;
		addImgs(imgs);
		this.porcentagem = porcentagem;
		this.isAni = true;
		this.scale = scale;
		this.tickPorIndex = tickPorIndex;
		this.porcentagemInicial = porcentagem;
	}
	
	public CriadorDeParticulas(int xOri, int yOri, int widthOri, int heightOri, BufferedImage img, float scale, int tickPorIndex, int porcentagem) {
		this.x = xOri;
		this.y = yOri;
		this.widthPar = 1;
		this.heightPar = 1;
		this.width = widthOri;
		this.height = heightOri;
		addImgs(img);
		this.porcentagem = porcentagem;
		this.porcentagemInicial = porcentagem;
		this.isAni = true;
		this.scale = scale;
		this.tickPorIndex = tickPorIndex;
	}
	
	public void setPorcentagem(int porcentagem) {
		this.porcentagem = porcentagem;
	}
	
	public int getPorcentagemInicial() {
		return porcentagemInicial;
	}
	
	public void setAlphaDelay(int ticks) {
		this.alphaDelay = ticks;
	}
	
	public void addImgs(ArrayList<BufferedImage> imgs) {
		this.imgs.add(imgs);
	}
	
	public void addImgs(BufferedImage img) {
		ArrayList<BufferedImage> imgAl = new ArrayList<BufferedImage>();
		imgAl.add(img);
		this.imgs.add(imgAl);
	}
	
	private ArrayList<BufferedImage> getImgs() {
		int randonIndex = Util.randomInt(0, imgs.size() - 1);
		return imgs.get(randonIndex);
	}

	public void setSeguindo(boolean seguindo, Dimensional d) {
		this.d = d;
		this.seguindo = seguindo;
	}

	public boolean isProduzindo() {
		return isProduzindo;
	}

	public boolean isAlphaAtivo() {
		return isAlphaAtivo;
	}

	public void setAlphaAtivo(boolean isAtivo) {
		this.isAlphaAtivo = isAtivo;
	}
	
	public void setParticulaInvisivel(boolean invisivel) {
		this.isInvisivel = invisivel;
	}
	
	public boolean isParticulaInvisivel() {
		return isInvisivel;
	}
	
	public void setParAni(ParticulaAnimation parAni) {
		this.parAni = parAni;
	}

	private int setRandomX() {
		int xVar = (int) (Math.random() * (width - widthPar));
		return getX() + xVar;
	}

	private int setRandomY() {
		int yVar = (int) (Math.random() * (height - heightPar));
		return getY() + yVar;
	}

	public void update(int x, int y) {
		updateXY(x, y);
		criarParticula();
	}

	public void addColor(Color novaCor) {
		cores.add(novaCor);
	}
	
	public void addColorLuz(Color novaCor) {
		if (!isLuz) return;
		coresLuz.add(novaCor);
	}
	
	public void addAllCoresLuz() {
		coresLuz.addAll(cores);
	}
	
	public void setComLuz(int raio, Color cor, double forca, int fadeIn, int fadeOut) {
		this.raio = raio;
		this.força = forca;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
		isLuz = true;
		if (cor != null) addColorLuz(cor);
	}
	
	public void setComLuz(int raio, Color cor, double forca, int fadeIn, int fadeOut, boolean varRaio, boolean varForça, int oscRaio, int oscForça) {
		this.raio = raio;
		this.força = forca;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
		this.varRaio = varRaio;
		this.varForça = varForça;
		this.oscRaio = oscRaio;
		this.oscForça = oscForça;
		isLuz = true;
		isLuzCustom = true;
		if (cor != null) addColorLuz(cor);
	}
	
	public void setLuzBruta(boolean isBruta) {
		this.isBruta = isBruta;
	}

	public void removeColor(Color novaCor) {
		if (!cores.contains(novaCor)) {
			System.out.println("essa cor nunca foi adicionada a esse criador de particulas");
			return;
		}

		if (cores.size() == 1) {
			System.out.println("impossivel remover cor, quando só uma existe");
			return;
		}

		cores.remove(novaCor);
	}
	
	public void removeColorLuz(Color novaCor) {
		if (!coresLuz.contains(novaCor)) {
			System.out.println("essa cor nunca foi adicionada a esse criador de particulas");
			return;
		}

		if (coresLuz.size() == 1) {
			System.out.println("impossivel remover cor, quando só uma existe");
			return;
		}

		coresLuz.remove(novaCor);
	}

	public void resetCorPadrao() {
		Color corPadrao = cores.get(0);
		cores.clear();
		cores.add(corPadrao);
	}

	private double setAngulo() {
		if (isAnguloVariado) {
			double novoAngulo = anguloPar;
			novoAngulo += anguloVariacao - (int) (Math.random() * (anguloVariacao * 2));
			return novoAngulo;

		} else {
			return anguloPar;
		}
	}
	
	public void setRotacaoVel(double rotacaoVel) {
		this.rotacaoVel = rotacaoVel;
	}
	
	public void setRotacaoVelVar(double rotacaoVel, double variacao) {
		this.rotacaoVel = rotacaoVel;
		this.rotacaoVelVariacao = variacao;
		this.isRotacaoVelVariado = true;
	}
	
	private double getRotacaoVel() {
		if (isRotacaoVelVariado) {
			return Util.randomDouble(rotacaoVel - rotacaoVelVariacao, rotacaoVel + rotacaoVelVariacao);
		} else {
			return rotacaoVel;
		}
	}

	public void setContorno(boolean isContorno) {
		this.isContorno = isContorno;
	}

	public void setAlphaVar(int duraçao) {
		this.alphaDuraçao = duraçao;
	}

	public void setAlphaVar(int duraçao, int variacao) {
		this.alphaDuraçao = duraçao;
		this.alphaVariacao = variacao;
		this.isAlphaVariado = true;
	}

	private float setAlpha() {
		if (isAlphaVariado) {
			int novaDuraçao = alphaDuraçao;
			novaDuraçao += alphaVariacao - (int) (Math.random() * (alphaVariacao * 2));
			if (novaDuraçao <= 0) {
				novaDuraçao = 1;
			}
			return 1.0f / novaDuraçao;
		}

		return 1.0f / alphaDuraçao;
	}

	private void criarParticula() {
		if (isProduzindo) {
			int porcentagemTemp = porcentagem;
			Particula par = null;
			while (porcentagemTemp > 100) {
				
				if (isAni) {
					par = new Particula(setRandomX(), setRandomY(), getSpeed(), setAngulo(), setAlpha(), alphaDelay, seguindo, scale, tickPorIndex, this, getRotacaoVel(), getImgs(), getGravidateRate());
				} else {
					par = new Particula(setRandomX(), setRandomY(), widthPar, heightPar, getCor(), getSpeed(), setAngulo(), setAlpha(),alphaDelay, isContorno, seguindo, getGravidateRate(), this);
				}
				
				if (coresLuz.size() > 0) {
					Color corLuz = getCorLuz();
					
					if (isLuzCustom) {
						par.initLuz(raio, corLuz.getRed(), corLuz.getGreen(), corLuz.getBlue(), isBruta, força, fadeIn, fadeOut, varRaio, varForça, oscRaio, oscForça);
					} else {
						par.initLuz(raio, corLuz.getRed(), corLuz.getGreen(), corLuz.getBlue(), isBruta, força, fadeIn, fadeOut);
					}
					
					if (parAni != null) parAni.setAnimation(par);
					
				}
				
				porcentagemTemp -= 100;
			}

			int chance = 1 + (int) (Math.random() * 100);
			if (chance <= porcentagemTemp) {
				if (isAni) {
					par = new Particula(setRandomX(), setRandomY(), getSpeed(), setAngulo(), setAlpha(),alphaDelay, seguindo, scale, tickPorIndex, this, getRotacaoVel(), getImgs(), getGravidateRate());
				} else {
					par = new Particula(setRandomX(), setRandomY(), widthPar, heightPar, getCor(), getSpeed(), setAngulo(), setAlpha(), alphaDelay, isContorno, seguindo, getGravidateRate(), this);
				}
				
				if (coresLuz.size() > 0) {
					Color corLuz = getCorLuz();
					
					if (isLuzCustom) {
						par.initLuz(raio, corLuz.getRed(), corLuz.getGreen(), corLuz.getBlue(), isBruta, força, fadeIn, fadeOut, varRaio, varForça, oscRaio, oscForça);
					} else {
						par.initLuz(raio, corLuz.getRed(), corLuz.getGreen(), corLuz.getBlue(), isBruta, força, fadeIn, fadeOut);
					}
					
					if (parAni != null) parAni.setAnimation(par);
				}
			}
			//setProduzindo(false);
		}

	}

	private Color getCor() {
		int randomCor = (int) (Math.random() * cores.size());
		return cores.get(randomCor);
	}
	
	private Color getCorLuz() {
		int randomCor = (int) (Math.random() * coresLuz.size());
		return coresLuz.get(randomCor);
	}

	public void setAngulo(double angulo) {
		this.anguloPar = angulo;
	}

	public void setAngulo(double angulo, int variacao) {
		this.anguloPar = angulo;
		this.anguloVariacao = variacao;
		this.isAnguloVariado = true;
	}

	public void setSpeed(double speed) {
		this.speedPar = speed;
	}
	
	public void setSpeedVar(double speed, double variacao) {
		this.speedPar = speed;
		this.speedParVariacao = variacao;
		isSpeedVariado = true;
	}
	
	private double getSpeed() {
		if (isSpeedVariado) {
			return Util.randomDouble(speedPar - speedParVariacao, speedPar + speedParVariacao);
		} else {
			return speedPar;
		}
	}
	
	public void setGravidadeRate(float rate) {
		this.gravidadeRatePar = rate;
	}
	
	public void setGravidadeRateVar(float rate, float var) {
		this.gravidadeRatePar = rate;
		this.gravdiadeRateVar = var;
		this.isGravidadeRateVariado = true;
	}
	
	private float getGravidateRate() {
		if (isGravidadeRateVariado) {
			return Util.randomFloat(gravidadeRatePar - gravdiadeRateVar, gravidadeRatePar + gravdiadeRateVar);
		} else {
			return gravidadeRatePar;
		}
	}

	public void setProduzindo(boolean isProduzindo) {
		this.isProduzindo = isProduzindo;
	}

	public void setProduzindo(final boolean isProduzindo, final int duraçao) {
		this.isProduzindo = isProduzindo;

		if (timerDelay != null) {
			if (timerDelay.isRunning()) {
				timerDelay.stop();
			}
		}

		final int tickAtual = Principal.tickTotal;
		timerDelay = new Timer(5, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= (tickAtual + duraçao)) {
					CriadorDeParticulas.this.isProduzindo = !isProduzindo;
					timerDelay.stop();
				}
			}
		});
		timerDelay.start();

	}

	public void setProduzindoXYConstante(final boolean isProduzindo, final int duraçao) {
		this.isProduzindo = isProduzindo;

		if (timerDelayStatico != null) {
			if (timerDelayStatico.isRunning()) {
				timerDelayStatico.stop();
			}
		}

		final int tickAtual = Principal.tickTotal;
		timerDelayStatico = new Timer(5, new ActionListener() {
			int tickAnterior = Principal.tickTotal + 1;

			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAnterior && isProduzindo) {
					criarParticula();
					tickAnterior++;
				}

				if (Principal.tickTotal >= (tickAtual + duraçao)) {
					CriadorDeParticulas.this.isProduzindo = !isProduzindo;
					timerDelayStatico.stop();
				}
			}
		});
		timerDelayStatico.start();

	}

	private void updateXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
