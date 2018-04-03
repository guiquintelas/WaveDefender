package wave.graphics.light;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import wave.principal.JanelaJogo;
import wave.principal.Principal;
import wave.tools.ActionQueue;
import wave.tools.Variator;
import wave.tools.VariatorNumero;

public class LuzAmbiente {
	private static byte luzARed   = (byte)255;
	private static byte luzAGreen = (byte)255;
	private static byte luzABlue  = (byte)255;
	
	private static ActionQueue cicloDiaNoiteRedGreen = null;
	private static ActionQueue cicloDiaNoiteBlue = null;
	
	private static Variator diaNoiteRedGreen;
	private static Variator diaNoiteBlue;
	
	public static BufferedImage imgLuz = new BufferedImage(JanelaJogo.WIDTH, JanelaJogo.HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
	public static Graphics2D gL = (Graphics2D)imgLuz.getGraphics();
	public static byte[] pixelLuz = ((DataBufferByte) imgLuz.getRaster().getDataBuffer()).getData();
	
	private synchronized static void resetarPixelPadrao() {		
		for (int p = 0; p < pixelLuz.length; p += 3) {
			pixelLuz[p] = luzABlue;
			pixelLuz[p + 1] = luzAGreen;
			pixelLuz[p + 2] = luzARed;
		}
	}
	
	public static void resetImgLuz() {
		resetarPixelPadrao();
	}
	
	public static void cleanUp() {
		if (Principal.sempreNoite) return;
		diaNoiteBlue.desativar();
		diaNoiteRedGreen.desativar();
		setLuzABlue(255);
		setLuzAGreen(255);
		setLuzARed(255);
	}
	
	public static void init() {
		if (Principal.sempreNoite) {
			luzARed   = (byte)45;
			luzAGreen = (byte)45;
			luzABlue  = (byte)90;
			return;
		}
		diaNoiteRedGreen = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero > 255) numero = 255;
				if (numero < 0) numero = 0;
				setLuzARed((int)numero);
				setLuzAGreen((int)numero);
			}
			
			public double getNumero() {
				return getLuzARed();
			}
			
			public boolean devoContinuar() {
				return true;
			}
		});
		
		diaNoiteBlue = new Variator(new VariatorNumero() {
			public void setNumero(double numero) {
				if (numero > 255) numero = 255;
				if (numero < 0) numero = 0;
				setLuzABlue((int)numero);
			}

			public double getNumero() {
				return getLuzABlue();
			}
			
			public boolean devoContinuar() {
				return true;
			}
		});
		
		
		cicloDiaNoiteRedGreen = new ActionQueue() {
			public boolean action() {
				Principal.dia = false;
				diaNoiteRedGreen.fadeOut(255, 40, 1600);
				diaNoiteRedGreen.variar(true);
				
				diaNoiteRedGreen.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						
						diaNoiteRedGreen.esperar(3000);
						
						diaNoiteRedGreen.addAcaoNaFila(new ActionQueue() {
							public boolean action() {
								diaNoiteRedGreen.fadeIn(40, 255, 1600);
								diaNoiteRedGreen.variar(true);
								
								diaNoiteRedGreen.addAcaoNaFila(new ActionQueue() {
									public boolean action() {
										Principal.dia = true;
										diaNoiteRedGreen.esperar(2400);
										
										diaNoiteRedGreen.addAcaoNaFila(cicloDiaNoiteRedGreen);
										return true;
									}
								});
								return true;
							}
						});
						return true;
					}
				});
				return true;
			}
		};
		
		cicloDiaNoiteBlue = new ActionQueue() {
			public boolean action() {
				diaNoiteBlue.fadeOut(255, 95, 1600);
				diaNoiteBlue.variar(true);
				
				diaNoiteBlue.addAcaoNaFila(new ActionQueue() {
					public boolean action() {
						
						diaNoiteBlue.esperar(3000);
						
						diaNoiteBlue.addAcaoNaFila(new ActionQueue() {
							public boolean action() {
								diaNoiteBlue.fadeIn(95, 255, 1600);
								diaNoiteBlue.variar(true);
								
								diaNoiteBlue.addAcaoNaFila(new ActionQueue() {
									public boolean action() {
										diaNoiteBlue.esperar(2400);
										
										diaNoiteBlue.addAcaoNaFila(cicloDiaNoiteBlue);
										return true;
									}
								});
								return true;
							}
						});
						return true;
					}
				});
				return true;
			}
		};
		
		diaNoiteBlue.addAcaoNaFila(cicloDiaNoiteBlue);
		diaNoiteRedGreen.addAcaoNaFila(cicloDiaNoiteRedGreen);
	}

	public static void pintarLuz(BufferedImage imgJogo) {			
		byte[] pixelJogo = ((DataBufferByte) imgJogo.getRaster().getDataBuffer()).getData();
		
		for (int y = 0; y < JanelaJogo.HEIGHT; y++) {
			for (int x = 0; x < JanelaJogo.WIDTH; x++) {
				
				int index = (x + y * JanelaJogo.WIDTH) * 3;
				int indexJ = (x + y * JanelaJogo.WIDTH) * 4;
				
				int red =   pixelJogo[indexJ + 1] & 0xFF;
	            int green = pixelJogo[indexJ + 2] & 0xFF;
	            int blue =  pixelJogo[indexJ + 3] & 0xFF;
	            
//	            int redLuz =   pixelLuz[index + 1] & 0xFF;
//	            int greenLuz = pixelLuz[index + 2] & 0xFF;
//	            int blueLuz =  pixelLuz[index + 3] & 0xFF;
	            
	            int redLuz =   pixelLuz[index ] & 0xFF;
	            int greenLuz = pixelLuz[index + 1] & 0xFF;
	            int blueLuz =  pixelLuz[index + 2] & 0xFF;
	            
	            int redFinal = (red * redLuz) / 255;
				int greenFinal = (green * greenLuz) / 255;
				int blueFinal = (blue * blueLuz) / 255;
	            
	            
	            pixelJogo[indexJ] = (byte)255;     //alpha
	            pixelJogo[indexJ + 1] = (byte) redFinal; //red
	            pixelJogo[indexJ + 2] = (byte) greenFinal; //green
	            pixelJogo[indexJ + 3] = (byte) blueFinal; //blue

			}
		}

	}

	public static int getLuzARed() {
		return luzARed & 0xFF;
	}

	public synchronized static void setLuzARed(int luzARed) {
		LuzAmbiente.luzARed = (byte)luzARed;
	}

	public static int getLuzAGreen() {
		return luzAGreen & 0xFF;
	}

	public synchronized static void setLuzAGreen(int luzAGreen) {
		LuzAmbiente.luzAGreen = (byte)luzAGreen;
	}

	public static int getLuzABlue() {
		return luzABlue & 0xFF;
	}

	public synchronized static void setLuzABlue(int luzABlue) {
		LuzAmbiente.luzABlue = (byte)luzABlue;
	}
}
