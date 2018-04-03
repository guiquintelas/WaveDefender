package wave.graphics;

import java.util.ArrayList;

import wave.principal.Dimensional;

public class SombraDinamica {
	public Sombra sombra;
	private Dimensional dAlvo;
	private Dimensional d;
	private int xOri;
	private int yOri;
	private int raio;
	
	private int yOff = 0;
	
	public static ArrayList<SombraDinamica> todasSombrasDinamicas = new ArrayList<SombraDinamica>();
	
	public SombraDinamica(Dimensional d, int xOri, int yOri, int raio) {
		sombra = new Sombra(d);
		this.d = d;
		this.xOri = xOri;
		this.yOri = yOri;
		this.raio = raio;
		
		todasSombrasDinamicas.add(this);
	}
	
	public SombraDinamica(Dimensional d, Dimensional dAlvo) {
		sombra = new Sombra(d);
		this.dAlvo = dAlvo;
		this.d = d;
		todasSombrasDinamicas.add(this);
	}
	
	public void update() {
		setOffs();
	}
	
	public void setYOff(int yOff) {
		this.yOff = yOff;
	}
	
	private void setOffs() {
		double dist = 0;
		if (dAlvo == null) {
			dist = (int)Math.sqrt(Math.pow(d.getXCentro() - xOri, 2) + Math.pow(d.getYCentro() - yOri, 2));
			dist = raio - dist;
			
			if (dist / 3.0 > 15) {
				sombra.setYOff(15 + yOff);
			} else {
				sombra.setYOff((int)(dist / 3) + yOff);
			}
			
			if (90 / dist < 1){
				//System.out.println("1");
				sombra.setWidthOff(-sombra.getDimensional().getWidth() / 2);
			} else {
				//System.out.println(200 / dist);
				sombra.setWidthOff((int)(-sombra.getDimensional().getWidth() / (2 * (90 / dist))));
				//System.out.println( sombra.getDimensional().getHeight() / (int)(900 / dist));
			}
			
			
		} else {
			dist = (int)Math.sqrt(Math.pow(d.getXCentro() - dAlvo.getXCentro(), 2) + Math.pow(d.getYCentro() - dAlvo.getYCentro(), 2));
			
			if (dist / 6.0 > 15) {
				sombra.setYOff(15 + yOff);
			} else {
				sombra.setYOff((int)(dist / 6) + yOff);
			}
			
			if (90 / dist < 1){
				//System.out.println("1");
				sombra.setWidthOff(-sombra.getDimensional().getWidth() / 2);
			} else {
				//System.out.println(200 / dist);
				sombra.setWidthOff((int)(-sombra.getDimensional().getWidth() / (2 * (90 / dist))));
				//System.out.println( sombra.getDimensional().getHeight() / (int)(900 / dist));
			}
		}
	}
	
	
}
