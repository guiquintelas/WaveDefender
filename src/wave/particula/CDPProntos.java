package wave.particula;

import java.awt.Color;

import wave.graphics.animaçao.Animaçao;
import wave.graphics.light.Luz;
import wave.principal.Dimensional;
import wave.tools.Variator;
import wave.tools.VariatorNumero;

public class CDPProntos {
	
	public static CriadorDeParticulas brilho(Dimensional d, int xOri, int yOri, int widthOri, int heightOri) {	
		CriadorDeParticulas cdp = new CriadorDeParticulas(xOri, yOri, widthOri, heightOri, 1, 1, Color.YELLOW, (widthOri * heightOri) / 100 + 1);
		cdp.setComLuz(4, Color.YELLOW, 100, 25, 25);
		cdp.addColorLuz(new Color(255, 215, 0));
		cdp.setAlphaVar(25);
		cdp.setSpeed(0);
		cdp.setParticulaInvisivel(true);
		cdp.setProduzindo(true);
		cdp.setSeguindo(true, d);
		cdp.setLuzBruta(true);
		
		return cdp;
	}
	
	public static CriadorDeParticulas fogo(Dimensional d, int xOri, int yOri, int width, int height) {
		CriadorDeParticulas particulasFogo = new CriadorDeParticulas(xOri, yOri, width, height, Animaçao.fogoImgs, .2f, 3, (width * height) / 25);
		particulasFogo.setSpeed(.5);
		particulasFogo.setAngulo(90, 20);
		particulasFogo.addColor(Color.YELLOW);
		particulasFogo.addColor(Color.ORANGE);
		particulasFogo.addColor(Color.RED);
		particulasFogo.setComLuz(7, null, 70, 5, 5);
		particulasFogo.addColorLuz(Color.YELLOW);
		particulasFogo.setAlphaDelay(40);
		particulasFogo.setAlphaVar(20, 20);
		particulasFogo.setParAni(new ParticulaAnimation() {
			public void setAnimation(final Particula par) {
				Variator varG = new Variator(new VariatorNumero() {
					public void setNumero(double numero) {
						if (numero > 255) numero = 255;
						if (numero < 0) numero = 0;
						par.luz.setRGB(255, (int) numero, 0);
					}

					public double getNumero() {
						return par.luz.getGreen();
					}

					public boolean devoContinuar() {
						return Luz.todasLuz.contains(par.luz);
					}
				});

				varG.fadeOut(255, 100, 50);
				varG.variar(true);

			}
		});
		
		return particulasFogo;
	}

}
