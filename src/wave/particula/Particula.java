package wave.particula;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import wave.graphics.Grafico;
import wave.graphics.animacao.Animacao;
import wave.graphics.light.Luz;
import wave.graphics.light.LuzBruta;
import wave.mob.Mob;
import wave.principal.Dimensional;
import wave.principal.Principal;

public class Particula extends Dimensional {

	private double angulo;
	private double speed;
	private double anguloRot = 0;
	private double anguloRotVel = 0;
	private float gravidade = 0;
	private float gravidadeRate = 0;

	private float alpha = 1.0f;
	private float alphaVar;

	private Color cor;

	private double xOff = 0;
	private double yOff = 0;
	private double xDiff;
	private double yDiff;

	private boolean seguindo = false;
	private boolean isAni = false;
	private boolean isRodando = false;
	private boolean isAlphaPronto = false;
	private boolean isContorno;
	private boolean isInvisivel = false;

	private CriadorDeParticulas cdp;
	private Animacao ani;

	public Luz luz;
	private int fadeOut;
	
	private Timer timerAlpha;

	public static ArrayList<Particula> todasParticulas = new ArrayList<Particula>();

	public Particula(int x, int y, int width, int height, Color cor, double speed, double angulo, float alphaVar, final int tickDelayAlpha, boolean isContorno, boolean seguindo, float gravidadeRate, CriadorDeParticulas cdp) {
		this.x = x;
		this.y = y;
		this.cor = cor;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.angulo = angulo;
		this.alphaVar = alphaVar;
		this.isContorno = isContorno;
		this.seguindo = seguindo;
		this.gravidadeRate = gravidadeRate;
		this.isInvisivel = cdp.isParticulaInvisivel();
		this.cdp = cdp;

		xDiff = x - cdp.getX();
		yDiff = y - cdp.getY();
		
		if (tickDelayAlpha > 0) {
			timerAlpha = new Timer(5, new ActionListener() {
				int tickAtual = Principal.tickTotal;
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= tickAtual + tickDelayAlpha) {
						isAlphaPronto = true;
						timerAlpha.stop();
					}
					
				}
			});
			timerAlpha.start();
		} else {
			isAlphaPronto = true;
		}

		todasParticulas.add(this);
		
		Grafico.particulas.add(todasParticulas.size());
		
		//new Luz(this, Util.randomInt(3, 5), Util.randomInt(0, 255), Util.randomInt(0, 255), Util.randomInt(0, 255), Util.randomInt(60, 100), 25);
	}

	public Particula(int x, int y, double speed, double angulo, float alphaVar, final int tickDelayAlpha, boolean seguindo, float scale, int tickPorIndex, CriadorDeParticulas cdp, double velRot, ArrayList<BufferedImage> imgs, float gravidadeRate) {
		this.x = x;
		this.y = y;
		this.width = 1;
		this.height = 1;
		this.speed = speed;
		this.angulo = angulo;
		this.alphaVar = alphaVar;
		this.seguindo = seguindo;
		this.cdp = cdp;
		this.isAni = true;
		this.gravidadeRate = gravidadeRate;

		if (velRot != 0)
			isRodando = true;
		this.anguloRotVel = velRot;

		xDiff = x - cdp.getX();
		yDiff = y - cdp.getY();
		
		if (tickDelayAlpha > 0) {
			timerAlpha = new Timer(5, new ActionListener() {
				int tickAtual = Principal.tickTotal;
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= tickAtual + tickDelayAlpha) {
						isAlphaPronto = true;
						timerAlpha.stop();
					}
					
				}
			});
			timerAlpha.start();
		} else {
			isAlphaPronto = true;
		}

		todasParticulas.add(this);
		ani = new Animacao(x, y, imgs, tickPorIndex);
		ani.setScale(scale);
		ani.setSeguindo(this);
		if (imgs.size() == 1)
			ani.setLoop(true);
		ani.setAutoPaint(false);
		ani.start();
		
		Grafico.particulas.add(todasParticulas.size());
	}
	
	public void initLuz(int raio, int r, int g, int b, boolean isBruta, double forca, int fadeIn, int fadeOut) {
		if (luz != null) return;
		
		if (isBruta) {
			luz = new LuzBruta(this, raio, r, g, b, forca, fadeIn, true, true, 0, 0);
		} else {
			luz = new Luz(this, raio, r, g, b, forca, fadeIn, true, true, 0, 0);
		}
		
		this.fadeOut = fadeOut;
	}
	
	public void initLuz(int raio, int r, int g, int b, boolean isBruta, double forca, int fadeIn, int fadeOut, boolean varRaio, boolean varForca, int oscRaio, int oscForca) {
		if (luz != null) return;
		
		if (isBruta) {
			luz = new LuzBruta(this, raio, r, g, b, forca, fadeIn, varRaio, varForca, oscRaio, oscForca);
		} else {
			luz = new Luz(this, raio, r, g, b, forca, fadeIn, varRaio, varForca, oscRaio, oscForca);
		}
		
		this.fadeOut = fadeOut;
	}

	public void update() {
		move();
		updateAlpha();
		updateAnguloRot();
		gravidade();
	}

	private void gravidade() {
		y += gravidade;
		gravidade += gravidadeRate;	
	}

	private void updateAnguloRot() {
		anguloRot += anguloRotVel;
		if (anguloRot >= 360)
			anguloRot -= 360;
	}

	private void move() {
		if (seguindo) {
			xOff += Math.cos(Math.toRadians(angulo)) * speed;
			yOff -= Math.sin(Math.toRadians(angulo)) * speed;
			x = cdp.getX() + xDiff;
			y = cdp.getY() + yDiff;
			x += xOff;
			y += yOff;
		} else {
			x += Math.cos(Math.toRadians(angulo)) * speed;
			y -= Math.sin(Math.toRadians(angulo)) * speed;
		}

	}

	private synchronized void updateAlpha() {
		if (isAlphaPronto) {
			alpha -= alphaVar;

			if (alpha <= 0) {
				alpha = 0;
				remove();
			}

			if (isAni) {
				ani.setAlpha(alpha);
			}
		}

	}

	private synchronized void remove() {
		todasParticulas.remove(this);
		if (isAni)ani.stop();
		if (luz != null) {
			luz.desativar(fadeOut);
		}
	}

	public synchronized void pintar(Graphics2D g) {
		if (isInvisivel) return;
		if (isAni) {
			if (isRodando) {
				ani.pintarManual(g, anguloRot);
			} else {
				ani.pintarManual(g);
			}

			return;
		}

		if (cdp.d != null) {
			if (cdp.d instanceof Mob && ((Mob) cdp.d).isInvisivel() && seguindo)
				return;
		}

		if (cdp.isAlphaAtivo()) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}

		if (isContorno) {
			g.setColor(Color.GRAY);
			g.drawOval((int) x + 1, (int) y + 1, width, height);
		}
		g.setColor(cor);
		g.drawOval((int) x, (int) y, width, height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}
