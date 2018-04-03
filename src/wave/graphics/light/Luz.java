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
	protected double força;
	
	protected int xOff = 0;
	protected int yOff = 0;
	
	private int raioNovo;
	private double forçaNova;
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
	public Variator forçaVar;

	public static ArrayList<Luz> todasLuz = new ArrayList<Luz>();

	public Luz(Dimensional d, int raio, int r, int g, int b, double forca, int fade) {
		this.d = d;
		this.raio = raio;
		this.raioFixo = raio;
		this.r = r;
		this.g = g;
		this.b = b;
		this.força = forca;

		getLista().add(this);

		init(fade, true, true, 10, 20);
		checaUpdateImg();
	}

	public Luz(Dimensional d, int raio, int r, int g, int b, double forca, int fade, boolean variarRaio, boolean variarForça, int oscRaio, int oscForça) {
		this.d = d;
		this.raio = raio;
		this.raioFixo = raio;
		this.r = r;
		this.g = g;
		this.b = b;
		this.força = forca;

		getLista().add(this);

		init(fade, variarRaio, variarForça, oscRaio, oscForça);
		checaUpdateImg();
		//updateImgLuz();
	}
	
	public static void setLuzesAtivas(boolean ativa) {
		for (int i = 0; i < todasLuz.size(); i++) {
			todasLuz.get(i).forçaVar.setAtivo(ativa);
			todasLuz.get(i).raioVar.setAtivo(ativa);
		}
	}

	private void init(int fade, boolean variarRaio, boolean variarForça, final int oscRaio, final int oscForça) {
		this.raioNovo = raio;
		this.forçaNova = força;
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

		forçaVar = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero > 100) numero = 100;
				setForça(numero);
			}

			public double getNumero() {
				return getForça();
			}

			public boolean devoContinuar() {
				return getLista().contains(Luz.this);
			}
		});

		if (variarForça) {
			
			if (fade != 0) {
				forçaVar.fadeIn(0, força - oscForça, fade);
				forçaVar.variar(true);
			}			
			
			if (oscForça != 0) {
				forçaVar.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						forçaVar.oscilar(oscForça, 200, true);
						forçaVar.variar(true);
						return true;
					}
				});
			}
			
		}
		
		raioVar.setAtivo(Principal.light);
		forçaVar.setAtivo(Principal.light);
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
					pixelLuzA[index ] = (byte) (((b / (double) raio) * (raio - dist)) / (100 / força));
					pixelLuzA[index + 1] = (byte) (((g / (double) raio) * (raio - dist)) / (100 / força));
					pixelLuzA[index + 2] = (byte) (((r / (double) raio) * (raio - dist)) / (100 / força));

				} else {
					continue;
				}
			}
		}
	}
	
	public void setAtiva(boolean isAtivo) {
		this.isAtivo = isAtivo;
		forçaVar.setAtivo(isAtivo);
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
			força = forçaNova;
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
		forçaVar.variar(false);
		
		raioVar.fadeOutSin(raio, 0, 25);
		raioVar.variar(true);
		forçaVar.fadeOutSin(força, 0, 25);
		forçaVar.variar(true);
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
		forçaVar.variar(false);

		raioVar.fadeOutSin(raio, 0, fadeOut);
		raioVar.variar(true);
		forçaVar.fadeOutSin(força, 0, fadeOut);
		forçaVar.variar(true);
		raioVar.addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				desativarTotal();			
				return true;
			}
		});
	}

	public void desativarTotal() {
		raioVar.desativar();
		forçaVar.desativar();
		getLista().remove(this);
	}

	public double getForça() {
		return forçaNova;
	}

	public void setForça(double força) {
		if (this.força == força) return;
		if (força > 100) força = 100;
		if (força < 0) força = 0;
		this.forçaNova = força;
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
