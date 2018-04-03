package wave.graphics.light;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.principal.Dimensional;
import wave.principal.DimensionalObj;
import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.ActionQueue;
import wave.tools.Variator;
import wave.tools.VariatorNumero;

public class Luz {
	protected Dimensional d;
	private int raioFixo;
	protected int raio;
	protected int diametro;
	protected int r;
	protected int g;
	protected int b;
	protected double for�a;
	
	protected int xOff = 0;
	protected int yOff = 0;
	
	private int raioNovo;
	private double for�aNova;
	private int rNovo;
	private int gNovo;
	private int bNovo;

	protected boolean needUpdate = false;
	private boolean desativando = false;
	protected boolean isInit = false;
	protected boolean isApagada = false;
	private boolean isAtivo = true;

	public byte[] pixelLuzA;

	public Variator raioVar;
	public Variator for�aVar;

	public static ArrayList<Luz> todasLuz = new ArrayList<Luz>();

	public Luz(Dimensional d, int raio, int r, int g, int b, double forca, int fade) {
		this.d = d;
		this.raio = raio;
		this.raioFixo = raio;
		this.r = r;
		this.g = g;
		this.b = b;
		this.for�a = forca;

		getLista().add(this);

		init(fade, true, true, 10, 20);
		checaUpdateImg();
	}

	public Luz(Dimensional d, int raio, int r, int g, int b, double forca, int fade, boolean variarRaio, boolean variarFor�a, int oscRaio, int oscFor�a) {
		this.d = d;
		this.raio = raio;
		this.raioFixo = raio;
		this.r = r;
		this.g = g;
		this.b = b;
		this.for�a = forca;

		getLista().add(this);

		init(fade, variarRaio, variarFor�a, oscRaio, oscFor�a);
		checaUpdateImg();
		//updateImgLuz();
	}
	
	public static void setLuzesAtivas(boolean ativa) {
		for (int i = 0; i < todasLuz.size(); i++) {
			todasLuz.get(i).for�aVar.setAtivo(ativa);
			todasLuz.get(i).raioVar.setAtivo(ativa);
		}
	}

	private void init(int fade, boolean variarRaio, boolean variarFor�a, final int oscRaio, final int oscFor�a) {
		this.raioNovo = raio;
		this.for�aNova = for�a;
		this.rNovo = r;
		this.gNovo = g;
		this.bNovo = b;
		
		raioVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				setRaio((int) numero);
			}

			public double getNumero() {
				return getRaio();
			}

			public boolean devoContinuar() {
				return getLista().contains(Luz.this);
			}
		});

		if (variarRaio) {
			
			if (fade != 0) {
				raioVar.fadeInSin(0, raioFixo, fade);
				raioVar.variar(true);
			}
			
			
			if (oscRaio != 0) {
				raioVar.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						raioVar.oscilar(oscRaio, 200, true);
						raioVar.variar(true);
						return true;
					}
				});
			}
			
		}

		for�aVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero > 100) numero = 100;
				setFor�a(numero);
			}

			public double getNumero() {
				return getFor�a();
			}

			public boolean devoContinuar() {
				return getLista().contains(Luz.this);
			}
		});

		if (variarFor�a) {
			
			if (fade != 0) {
				for�aVar.fadeIn(0, for�a - oscFor�a, fade);
				for�aVar.variar(true);
			}			
			
			if (oscFor�a != 0) {
				for�aVar.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						for�aVar.oscilar(oscFor�a, 200, true);
						for�aVar.variar(true);
						return true;
					}
				});
			}
			
		}
		
		raioVar.setAtivo(Principal.light);
		for�aVar.setAtivo(Principal.light);
	}
	
	protected ArrayList<Luz> getLista() {
		return todasLuz;
	}

	protected synchronized void updateImgLuz() {
		diametro = raio * 2;
		pixelLuzA = new byte[diametro * diametro * 3];
		isInit = true;

		for (int y = 0; y < diametro; y++) {
			for (int x = 0; x < diametro; x++) {

				int index = (x + y * diametro) * 3;

				double dist = Math.sqrt(((raio - x)*(raio - x)) + ((raio - y)*(raio - y)));

				if (dist < raio) {
					//pixelLuzA[index] = (byte) 255;
					pixelLuzA[index ] = (byte) (((b / (double) raio) * (raio - dist)) / (100 / for�a));
					pixelLuzA[index + 1] = (byte) (((g / (double) raio) * (raio - dist)) / (100 / for�a));
					pixelLuzA[index + 2] = (byte) (((r / (double) raio) * (raio - dist)) / (100 / for�a));

				} else {
					continue;
				}
			}
		}
	}
	
	public void setAtiva(boolean isAtivo) {
		this.isAtivo = isAtivo;
		for�aVar.setAtivo(isAtivo);
		raioVar.setAtivo(isAtivo);
	}

	public void update() {	
		checaAtivo();
		if (!isAtivo) return;	
		checaUpdateImg();
	}

	private synchronized void checaUpdateImg() {
		if (needUpdate) {
			raio = raioNovo;
			diametro = raio * 2;
			for�a = for�aNova;
			r = rNovo;
			g = gNovo;
			b = bNovo;
			updateImgLuz();
			needUpdate = false;
		}
		
	}

	private boolean checaAtivo() {
		if (d instanceof DimensionalObj) {
			if (DimensionalObj.todosDimensionalObjs.contains(d) == false) {
				if (isAtivo) {
					desativar();
				} else {
					desativarTotal();
				}
				return false;
			}
		}

		return true;
	}

	public synchronized void pintar(BufferedImage imgJogo) {
		if (!isInit || isApagada) return;
		int getX = d.getXCentro() + xOff;
		int getY = d.getYCentro() + yOff;
		int indexLuz = -3;

		for (int y = getY - raio; y < getY + raio; y++) {
			for (int x = getX - raio; x < getX + raio; x++) {
				indexLuz += 3;

				if (y < 0 || y >= imgJogo.getHeight()) continue;
				if (x < 0 || x >= imgJogo.getWidth()) continue;

				int index = (x + y * JanelaJogo.WIDTH) * 3;

				int blue = LuzAmbiente.pixelLuz[index ] & 0xFF;
				int green = LuzAmbiente.pixelLuz[index + 1] & 0xFF;
				int red = LuzAmbiente.pixelLuz[index + 2] & 0xFF;

				int blueLuz = pixelLuzA[indexLuz] & 0xFF;
				int greenLuz = pixelLuzA[indexLuz + 1] & 0xFF;
				int redLuz = pixelLuzA[indexLuz + 2] & 0xFF;

				int redFinal = red + redLuz;
				int greenFinal = green + greenLuz;
				int blueFinal = blue + blueLuz;

				if (redFinal > 255) redFinal = 255;
				if (greenFinal > 255) greenFinal = 255;
				if (blueFinal > 255) blueFinal = 255;

				LuzAmbiente.pixelLuz[index] = (byte) blueFinal;
				LuzAmbiente.pixelLuz[index + 1] = (byte) greenFinal;
				LuzAmbiente.pixelLuz[index + 2] = (byte) redFinal;
			}
		}
	}

	public int getRaio() {
		return raioNovo;
	}

	public synchronized void setRaio(int raio) {
		if (raio == this.raio) {
			return;
		}
		this.raioNovo = raio;
		needUpdate = true;
		diametro = raio * 2;
	}

	public void desativar() {
		if (desativando) return;
		desativando = true;
		raioVar.variar(false);
		for�aVar.variar(false);
		
		raioVar.fadeOutSin(raio, 0, 25);
		raioVar.variar(true);
		for�aVar.fadeOutSin(for�a, 0, 25);
		for�aVar.variar(true);
		raioVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				desativarTotal();
				return true;
			}
		});
	}
	
	public void desativar(int fadeOut) {
		if (desativando) return;
		if (fadeOut == 0) {
			desativarTotal();
			return;
		}
		desativando = true;
		raioVar.variar(false);
		for�aVar.variar(false);

		raioVar.fadeOutSin(raio, 0, fadeOut);
		raioVar.variar(true);
		for�aVar.fadeOutSin(for�a, 0, fadeOut);
		for�aVar.variar(true);
		raioVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				desativarTotal();			
				return true;
			}
		});
	}

	public void desativarTotal() {
		raioVar.desativar();
		for�aVar.desativar();
		getLista().remove(this);
	}

	public double getFor�a() {
		return for�aNova;
	}

	public void setFor�a(double for�a) {
		if (this.for�a == for�a) return;
		if (for�a > 100) for�a = 100;
		if (for�a < 0) for�a = 0;
		this.for�aNova = for�a;
		needUpdate = true;
	}
	
	public synchronized void setRGB(int r, int g, int b) {
		if (this.r == r && this.g == g && this.b == b) return;
		this.rNovo = r;
		this.gNovo = g;
		this.bNovo = b;
		needUpdate = true;
	}
	
	public int getRed() {
		return rNovo;
	}
	
	public int getGreen() {
		return gNovo;
	}
	
	public int getBlue() {
		return bNovo;
	}

	public void setXOff(int xOff) {
		this.xOff = xOff;
	}

	public void setYOff(int yOff) {
		this.yOff = yOff;
	}
	
	public void setApagada(boolean apagar) {
		isApagada = apagar;
	}

}
