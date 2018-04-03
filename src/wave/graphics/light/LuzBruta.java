package wave.graphics.light;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import wave.principal.Dimensional;

public class LuzBruta extends Luz{
	private BufferedImage imgLuz;
	private int[] pixelLuzABruto;
	
	public static ArrayList<Luz> todasLuzBruta = new ArrayList<Luz>();

	public LuzBruta(Dimensional d, int raio, int r, int g, int b, double forca, int fade) {
		super(d, raio, r, g, b, forca, fade);
		forçaVar.setAtivo(true);
		raioVar.setAtivo(true);
	}
	
	public LuzBruta(Dimensional d, int raio, int r, int g, int b, double forca, int fade, boolean variarRaio, boolean variarForça, int oscRaio, int oscForça) {
		super(d, raio, r, g, b, forca, fade, variarRaio, variarForça, oscRaio, oscRaio);
		forçaVar.setAtivo(true);
		raioVar.setAtivo(true);
	}
	
	protected ArrayList<Luz> getLista() {
		return todasLuzBruta;
	}
	
	protected synchronized void updateImgLuz() {
		if (raio == 0) return;
		
		diametro = raio * 2;
		pixelLuzABruto = new int[diametro * diametro * 4];
		imgLuz = new BufferedImage(diametro, diametro, BufferedImage.TYPE_4BYTE_ABGR);
		isInit = true;

		for (int y = 0; y < diametro; y++) {
			for (int x = 0; x < diametro; x++) {

				int index = (x + y * diametro) * 4;

				double dist = Math.sqrt(((raio - x)*(raio - x)) + ((raio - y)*(raio - y)));

				if (dist < raio) {
					pixelLuzABruto[index + 3] = (int)(((255 / (double) raio) * (raio - dist)) / (100 / força));
					pixelLuzABruto[index + 2] =  b;
					pixelLuzABruto[index + 1] =  g;
					pixelLuzABruto[index] = r;
				} else {
					continue;
				}
			}
		}
		
		WritableRaster raster = imgLuz.getRaster();
		raster.setPixels(0, 0, diametro, diametro, pixelLuzABruto);
	}
	
	public synchronized void pintar(BufferedImage img) {		
		if (!isInit || isApagada) return;
		int getX = d.getXCentro() + xOff;
		int getY = d.getYCentro() + yOff;	
		img.getGraphics().drawImage(imgLuz, getX - raio, getY - raio , null);
	}

}
