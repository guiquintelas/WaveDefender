package wave.graphics.imgprocessing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class Filter {
	private static int verPer = 0;
	
	public static BufferedImage pretoBranco(BufferedImage img) {
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		int[] result = new int[width * height * 4];
		
		BufferedImage imgFinal = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				//int cor = img.getRGB(x, y);
				int index = (x + y * width) * 4;
				
				int alpha = pixels[index] & 0xFF;
				//System.out.println("alpha " + alpha);
				
				if (alpha == 0) continue;
				
				int red =   pixels[index + 1] & 0xFF;
	            int green = pixels[index + 2] & 0xFF;
	            int blue =  pixels[index + 3] & 0xFF;
	            
	            int media = (red + green + blue) / 3;
	            
	            
	            result[index] = media;     //red
	            result[index + 1] = media; //green
	            result[index + 2] = media; //blue
	            result[index + 3] = alpha;
	           
			}
		}
		
		WritableRaster raster = imgFinal.getRaster();
		raster.setPixels(0, 0, width, height, result);
		
		return imgFinal;
	}
	
	public static BufferedImage soVermelho(BufferedImage img, int porcentagem) {
		Filter.verPer = (porcentagem + Filter.verPer * 2)/3; 
		
		if (Filter.verPer <= 0) return img;
		if (Filter.verPer > 100) Filter.verPer = 100;
		
		BufferedImage imgFinal = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				int cor = img.getRGB(x, y);
				int alpha = (cor >> 24) & 0x000000FF;
				
				if (alpha == 0) continue;
				
				int	red = (cor >> 16 ) & 0x000000FF;
	            int green = (cor >> 8 ) & 0x000000FF;
	            int blue = (cor) & 0x000000FF;
	            

	            if (green > 0) green = (int)(green - ((green/100.0) * Filter.verPer));
	            if (blue > 0)  blue = (int)(blue - ((blue/100.0) * Filter.verPer));
	            
	            
				imgFinal.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
			}
		}
		
		return imgFinal;
	}
	
	public static BufferedImage soVerde(BufferedImage img, int porcentagem) {
		BufferedImage imgFinal = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				int cor = img.getRGB(x, y);
				int alpha = (cor >> 24) & 0x000000FF;
				
				if (alpha == 0) continue;
				
	            int green = (cor >> 8 ) & 0x000000FF;
	            

	            if (green > 0) green = (int)(green - ((green/100.0) * porcentagem));
	            
	            
				imgFinal.setRGB(x, y, (alpha << 24) | (0 << 16) | (green << 8) | 0);
			}
		}
		
		return imgFinal;
	}
	
	public static BufferedImage soVermelho(BufferedImage img) {
		
		BufferedImage imgFinal = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				int cor = img.getRGB(x, y);
				int alpha = (cor >> 24) & 0x000000FF;
				
				if (alpha == 0) continue;
				
				int	red = (cor >> 16 ) & 0x000000FF;
	            
				imgFinal.setRGB(x, y, (alpha << 24) | (red << 16) | (0 << 8) | 0);
			}
		}
		
		return imgFinal;
	}
	
	public static BufferedImage plainBlur(BufferedImage img, int kernelSize) {
		BufferedImage imgFinal = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Cor cores[][] = new Cor[img.getWidth()][img.getHeight()];
		
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				cores[x][y] = new Cor(img.getRGB(x, y), false);
			}
		}
		
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				int totalR = 0;
				int totalG = 0;
				int totalB = 0;
				int cont = 0;
				
				for (int yOff = -(kernelSize - 1)/2; yOff <= (kernelSize - 1)/2; yOff++) {
					for (int xOff = -(kernelSize - 1)/2; xOff <= (kernelSize - 1)/2; xOff++) {
						if (x + xOff > 0 && x + xOff < img.getWidth() - 1 && y + yOff > 0 && y + yOff < img.getHeight() - 1) {
							cont++;
							totalR += cores[x + xOff][y + yOff].r;
							totalG += cores[x + xOff][y + yOff].g;
							totalB += cores[x + xOff][y + yOff].b;
						}
					}
				}
				
				
				imgFinal.setRGB(x, y, (255 << 24) | (totalR/cont << 16) | (totalG/cont << 8) | totalB/cont);
			}
		}
		
		
		return imgFinal;
	}
	
	public static BufferedImage gaussianBlur(BufferedImage img, int kernelSize, double força) {
		BufferedImage imgFinal = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		int width = imgFinal.getWidth();
		
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		int[] result = new int[width * img.getHeight() * 4];
		
		float[][] kernel = new float[kernelSize][kernelSize];
		
		
		for (int yOff = -(kernelSize - 1)/2; yOff <= (kernelSize - 1)/2; yOff++) {
			for (int xOff = -(kernelSize - 1)/2; xOff <= (kernelSize - 1)/2; xOff++) {
				kernel[xOff + (kernelSize - 1)/2][yOff + (kernelSize - 1)/2] = 1/(float)Math.pow(força, Math.abs(xOff) + Math.abs(yOff));
			}
		}
		
		for (int y = 0; y < imgFinal.getHeight(); y++) {
			for (int x = 0; x < imgFinal.getWidth(); x++) {
				int totalR = 0;
				int totalG = 0;
				int totalB = 0;
				float cont = 0;
				
				for (int yOff = -(kernelSize - 1)/2; yOff <= (kernelSize - 1)/2; yOff++) {
					for (int xOff = -(kernelSize - 1)/2; xOff <= (kernelSize - 1)/2; xOff++) {
						if (x + xOff > 0 && x + xOff < img.getWidth() - 1 && y + yOff > 0 && y + yOff < img.getHeight() - 1) {
							float varKernel = kernel[xOff + (kernelSize - 1)/2][yOff + (kernelSize - 1)/2];
							cont += varKernel;						
							
							int index = ((x + xOff) + ((y + yOff) * width)) * 4;
							totalB += (pixels[index + 1] & 0xFF) * varKernel;
							totalG += (pixels[index + 2] & 0xFF) * varKernel;
							totalR += (pixels[index + 3] & 0xFF) * varKernel;
						}
					}
				}
				
				int index = (x + y * width) * 4;
				
				result[index] = (int)((totalR/cont) + .5f);
				result[index + 1] = (int)((totalG/cont) + .5f);
				result[index + 2] = (int)((totalB/cont) + .5f);
				result[index + 3] = 255;
				
			}
		}
		
		WritableRaster raster = imgFinal.getRaster();
		raster.setPixels(0, 0, width, imgFinal.getHeight(), result);

		return imgFinal;
	}
	
}
