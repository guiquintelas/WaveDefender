package wave.graphics.imgprocessing;

public class Cor {
	public int r;
	public int g;
	public int b;
	public int a;
	
	public Cor(int hexInt) {
		a = (hexInt >> 24) & 0x000000FF;
		r= (hexInt >> 16 ) & 0x000000FF;
        g = (hexInt >> 8 ) & 0x000000FF;
        b = (hexInt) & 0x000000FF;
	}
	
	public Cor(int hexInt, boolean alpha) {
		r= (hexInt >> 16 ) & 0x000000FF;
        g = (hexInt >> 8 ) & 0x000000FF;
        b = (hexInt) & 0x000000FF;
	}
}
