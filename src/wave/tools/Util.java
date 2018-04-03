package wave.tools;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import wave.principal.Principal;
import wave.principal.Tela;


public class Util {
	private static final Graphics2D g = Tela.getGraphics2d();
	
	public static BufferedImage carregarImg(String path) {
		BufferedImage img = null;

		try {
			img = ImageIO.read(Principal.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
	}
	
	public static BufferedImage carregarQuickImg(String soNome) {
		return carregarImg("/Sprites/" + soNome + ".png");
	}

	public static ArrayList<BufferedImage> carregarArrayBI(BufferedImage spriteSheet, int xInicial, int yInicial, int width, int height, int quantasBI) {
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		boolean inicial = true;

		for (int y = yInicial; y < spriteSheet.getHeight(); y += height) {
			for (int x = 0; x < spriteSheet.getWidth(); x += width) {
				if (inicial) {
					x = xInicial;
					inicial = false;
				}
				
				imgs.add(spriteSheet.getSubimage(x, y, width, height));
				if (imgs.size() >= quantasBI) {
					return imgs;
				}
			}
		}
		
		System.out.println("deu ruim");
		return imgs;
	}
	
	public static ArrayList<BufferedImage> carregarArrayBIVertical(BufferedImage spriteSheet, int xInicial, int yInicial, int width, int height, int quantasBI) {
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		boolean inicial = true;

		for (int x = xInicial; x < spriteSheet.getWidth(); x += width) {
			for (int y = 0; y < spriteSheet.getHeight(); y += height) {
				if (inicial) {
					y = yInicial;
					inicial = false;
				}
				
				imgs.add(spriteSheet.getSubimage(x, y, width, height));
				if (imgs.size() >= quantasBI) {
					return imgs;
				}
			}
		}
		
		System.out.println("deu ruim");
		return imgs;
	}

	public static double randomDouble(double min, double max) {
		return min + (Math.random() * (max - min));
	}
	
	public static float randomFloat(float min, float max) {
		return min + (float)(Math.random() * (max - min));
	}

	public static int randomInt(int min, int max) {
		return min + (int) (Math.random() * (max - min) + 0.5);
	}
	
	public static int getStringWidh(String string, Font font) {
		if (string == null || string == "") {
			return 0;
		}
		
		FontMetrics fm = g.getFontMetrics(font);
		try {
			return fm.stringWidth(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static int getStringHeight(String string, Font font) {
		 FontRenderContext frc = g.getFontRenderContext();
		   return (int)font.getLineMetrics(string, frc).getHeight();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<BufferedImage> removeFirsts(ArrayList<BufferedImage> imgs, int indexs) {
		ArrayList<BufferedImage> imgsTemp = (ArrayList<BufferedImage>)imgs.clone();
		for (int i = 0; i < indexs; i++) {
			imgsTemp.remove(0);
		}
		return imgsTemp;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<BufferedImage> removeLasts(ArrayList<BufferedImage> imgs, int indexs) {
		ArrayList<BufferedImage> imgsTemp = (ArrayList<BufferedImage>)imgs.clone();
		for (int i = 0; i < indexs; i++) {
			imgsTemp.remove(imgs.size() - 1);
		}
		return imgsTemp;
	}
	
	public static boolean compararAngulos(double angulo1, double angulo2) {
		while (angulo1 > 180)angulo1 -= 360;
		while (angulo1 < -180)angulo1 += 360;
		while (angulo2 > 180)angulo2 -= 360;
		while (angulo2 < -180)angulo2 += 360;
		
		return angulo1 > angulo2;
	}
 }
