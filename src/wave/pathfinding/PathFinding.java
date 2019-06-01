package wave.pathfinding;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import wave.map.Tile;
import wave.map.TileMap;
import wave.principal.Dimensional;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public class PathFinding {
	private static final double NOINIT = 20000000;

	private Tile tileVerificado;
	
	private double distMin;
	
	private static final int RANGE = 50;
	private int range = RANGE;
	private static final int TIME_OUT = 15;
	private static final int DIST_TIME_OUT = 200;
	private int timeOutAn = 100;
	private static final int RESET_DELAY = 50;
	
	private int forcaEmpurrao = 4;
	private int empurroesSeguidos = 0;

	private double angulo;
	private double anguloFixo;
	private double xOff = 0;
	private double yOff = 0;

	private int xI, yI, xF, yF, xFR, yFR;
	private int width, height;

	private boolean anguloCerto = false;
	private boolean chegou = false;
	//private boolean dentroParede = false;
	private boolean isReset = false;

	private double angulo1 = NOINIT;
	private double angulo2 = NOINIT;

	private boolean anguloProntoMudar = true;
	private boolean anguloAumentando;
	private Timer timerMudanca;
	private Timer timerParado;
	private Timer timerDistMin;
	private Timer timerReset;
	
	private Dimensional d;
	
	public double pathFind(int xF, int yF, Dimensional d) {
		if (isReset) {
			this.xF = xFR;
			this.yF = yFR;		
		} else {
			this.xF = xF;
			this.yF = yF;
		}
		
		this.xI = d.getXCentro();		
		this.yI = d.getYCentro();
		
		chegou = false;
		//dentroParede = false;
		anguloCerto = false;
		this.d = d;
		xOff = 0;
		yOff = 0;

		angulo = Math.toDegrees(Math.atan2(this.xF - xI, this.yF - yI)) - 90;

		if (angulo < 0)
			angulo += 360;

		anguloFixo = angulo;
		
		updateDistMin();
		
		checaReset();

		while (!chegou) {
			tentarChegar();
			//if (dentroParede) return angulo - 180;
			
			modAngulo();

		}
		
		updateRange();
		xOff = 0;
		yOff = 0;
		
		if (timerParado != null) {
			if (timerParado.isRunning()) {
				
				return angulo;
			}
		}
			
		timerParado = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			int xIAtual = xI;
			int yIAtual = yI;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual + TIME_OUT) {
					
					if (Math.abs(xIAtual - xI) < 10 && Math.abs(yIAtual - yI) < 10) {
						mudarDirecaoAngulo(!anguloAumentando);
						if (!anguloCerto)  {
							empurrao();
						} else {
							loop(false);
						}
						System.out.println("Time Out");
						timerParado.stop();
						return;
					}
					
					tickAtual += TIME_OUT;
					xIAtual = xI;
					yIAtual = yI;
					empurroesSeguidos = 0;
					
				}
				
			}
		});
		timerParado.start();
		

		return angulo;
	}
	
	public void pintarPF(Graphics2D g, Graphics2D gP) {
		pintarPath(g);
		gP.setColor(Color.RED);
		gP.drawRect(xI, yI, 1, 1);
		g.setColor(Color.RED);
		g.drawRect(xF, yF, 2, 2);
	}
	
	private void updateRange() {
		int numParedes = TileMap.densidadeParedes(xI, yI, 5);
		
		if (numParedes < 10) {
			range = 150;
		}
		
		if (numParedes >= 10 && numParedes < 20) {
			range = RANGE;
		}
		
		if (numParedes >= 32) {
			int numParedesPerto = TileMap.densidadeParedes(xI, yI, 1);
			if (numParedesPerto >= 5) {
				range = 25;
			}
			
			
			if (numParedesPerto >= 7) {
				range = 20;
			}
			
			if (numParedesPerto < 4) {
				range = 30;
			}
			
		}
	}
	
	private void updateDistMin() {
		double distTemp = Math.sqrt(Math.pow(xI - xF, 2) + Math.pow(yI - yF, 2));
		
		if (distMin == 0) {
			distMin = distTemp;
		}
		
		if (distTemp < distMin) {
			distMin = distTemp;
		}
		
		if (timerDistMin != null) {
			if (timerDistMin.isRunning()) {
				return;
			}
		}
		
		timerDistMin = new Timer(5, new ActionListener() {
			int tickAtual = Principal.tickTotal;
			double distAtual = distMin;
			public void actionPerformed(ActionEvent e) {
				if (Principal.tickTotal >= tickAtual + DIST_TIME_OUT) {
					if (distAtual <= distMin) {
						loop(true);
					} else {
						loop(false);
					}
					timerDistMin.stop();
				}
				
			}
		});
		timerDistMin.start();
	}
	
	private void loop(boolean loop) {
		if (loop) {
			System.out.println("loop");
			timeOutAn += 150;
			System.out.println(timeOutAn);
			if (timeOutAn > 1500) {
				loop(false);
			}
			
		} else {
			timeOutAn = 100;
			System.out.println("unloop");
		}
	}

	
	
	private void empurrao() {
		System.out.println("Empurrou: " + (forcaEmpurrao + empurroesSeguidos) + " de forca");
		d.setX(d.getXDouble() + Math.cos(Math.toRadians(angulo)) * forcaEmpurrao + empurroesSeguidos);
		d.setY(d.getYDouble() - Math.sin(Math.toRadians(angulo)) * forcaEmpurrao + empurroesSeguidos);	
		empurroesSeguidos++;
		
		if (empurroesSeguidos > 3 && !isReset) {
			System.out.println("RESET");
			isReset = true;
			
			
			xFR = (int)(xI + Math.cos(Math.toRadians(anguloFixo - 180)) * (timeOutAn / 4));
			yFR = (int)(yI - Math.sin(Math.toRadians(anguloFixo - 180)) * (timeOutAn / 4));
			
			timerReset = new Timer(5, new ActionListener() {
				int tickAtual = Principal.tickTotal;
				public void actionPerformed(ActionEvent e) {
					if (Principal.tickTotal >= tickAtual + RESET_DELAY + (timeOutAn / 16)) {
						isReset = false;
						timerReset.stop();
					}
					
				}
			});
			timerReset.start();
			
		}
		
		if (empurroesSeguidos > 9) {
			loop(false);
		}
 		
	}
	
	private void checaReset() {
		if (isReset) {
			if ((xI == xF) && (yI == yF)) {
				isReset = false;
				if (timeOutAn > 400) {
					loop(false);
				}
				
			}
		}
	}

	public void pathFinderTile(int xI, int yI, int xF, int yF, Graphics2D g) {
		g.drawLine(xI, yI, xF, yI);
		g.drawLine(xF, yI, xF, yF);
	}
	
	private void colisaoParede() {
		
		Dimensional d = new Dimensional() {};
		d.setX(xI);
		d.setY(yI);		
		
		for (int i = 0; i < d.getTiles().size(); i++) {
			d.getTiles().get(i).checaColisaoPonto(d);
			d.getTiles().get(i).isAtivo = true;
		}
		
		xI = d.getXCentro();
		yI = d.getYCentro();
		
		if (this.d != null) {
			this.d.setX(xI - this.d.getWidth()/2);
			this.d.setY(yI - this.d.getHeight()/2);
		}
		
		//dentroParede = false;
	}

	private void modAngulo() {
		if (!chegou) {
			xOff = 0;
			yOff = 0;

			//pegarPontosExtremos();
			pegarAngulos();
			if (angulo1 < 0) {
				angulo1 += 360;
			}
			
			if (angulo1 > 360) {
				angulo1 -= 360;
			}
			
			if (angulo2 < 0) {
				angulo2 += 360;
			}
			
			if (angulo1 > 360) {
				angulo1 -= 360;
			}
			
			double diffAngulo1Temp = anguloFixo - angulo1;
			double diffAngulo2Temp = anguloFixo - angulo2;
			
			if (diffAngulo1Temp > 180) {
				diffAngulo1Temp -= 360;
			}
			
			if (diffAngulo1Temp < -180) {
				diffAngulo1Temp += 360;
			}
			
			if (diffAngulo2Temp > 180) {
				diffAngulo2Temp -= 360;
			}
			
			if (diffAngulo2Temp < -180) {
				diffAngulo2Temp += 360;
			}
			
			
			double difAngulo1 = Math.abs(diffAngulo1Temp);
			double difAngulo2 = Math.abs(diffAngulo2Temp);
			
			//System.out.println(anguloAumentando);

			if (difAngulo1 < difAngulo2) {
				if (anguloFixo > 90 && anguloFixo < 270) {
					mudarDirecaoAngulo(true);
				} else {
					mudarDirecaoAngulo(true);
				}
				
			} else {
				if (anguloFixo > 90 && anguloFixo < 270) {
					mudarDirecaoAngulo(false);
				} else {
					mudarDirecaoAngulo(false);
				}
			}
		}


	}

	private void mudarDirecaoAngulo(boolean aumentar) {
		if (anguloAumentando == aumentar) {
			if (anguloAumentando) {
				angulo += 0.5;
			} else {
				angulo -= 0.5;
			}
		} else {
			if (anguloProntoMudar) {
				anguloProntoMudar = false;
				anguloAumentando = aumentar;

				if (timerMudanca != null) {
					if (timerMudanca.isRunning()) {
						return;
					}
				}
				timerMudanca = new Timer(5, new ActionListener() {
					int tickAtual = Principal.tickTotal;

					public void actionPerformed(ActionEvent e) {
						if (Principal.tickTotal >= tickAtual + timeOutAn) {
							anguloProntoMudar = true;
							timerMudanca.stop();
						}

					}
				});
				timerMudanca.start();

			} else {
				if (anguloAumentando) {
					angulo += 0.5;
				} else {
					angulo -= 0.5;
				}
			}
		}
	}

	private void pegarAngulos() {
		double anguloTemp = anguloFixo;
		Tile tileVerificadoTemp = tileVerificado;
		int tentativas = 0;
		range = RANGE;

		boolean paraCima = true;

		angulo1 = NOINIT;
		angulo2 = NOINIT;

		while (angulo2 == NOINIT || angulo1 == NOINIT) {
			double xOff = 0;
			double yOff = 0;
			while (true) {

				if ((xI + (int) xOff == xF) && (yI + (int) yOff == yF)) {
					return;
				}

				if ((xI + (int) xOff + width) >= JanelaJogo.WIDTH || (xI + (int) xOff < 0)) {
//					if (paraCima) {
//						angulo1 = anguloTemp;
//					} else {
//						angulo2 = anguloTemp;
//					}
//					paraCima = false;
//					anguloTemp = angulo;
					break;
			}
//
				if ((yI + (int) yOff + height) >= JanelaJogo.HEIGHT || (yI + (int) yOff < 0)) {
//					if (paraCima) {
//						angulo1 = anguloTemp;
//					} else {
//						angulo2 = anguloTemp;
//					}
//					paraCima = false;
//					anguloTemp = angulo;
					break;
				}

				if (!TileMap.tileMap[(int) Math.floor((xI + (int) xOff) / Tile.WIDTH)][(int) Math.floor((yI + (int) yOff) / Tile.HEIGHT)].equals(tileVerificadoTemp)) {
					tileVerificadoTemp = TileMap.tileMap[(int) Math.floor((xI + (int) xOff) / Tile.WIDTH)][(int) Math.floor((yI + (int) yOff) / Tile.HEIGHT)];
				}

				if (tileVerificadoTemp.isWall()) {

					break;
				}
				
				if (Math.abs(xOff) + Math.abs(yOff) >= range) {
					if (paraCima) {
						angulo1 = anguloTemp;
					} else {
						angulo2 = anguloTemp;
					}
					paraCima = false;
					anguloTemp = angulo;
					break;
				}

				xOff += Math.cos(Math.toRadians(anguloTemp));
				yOff -= Math.sin(Math.toRadians(anguloTemp));

			}

			if (paraCima) {
				anguloTemp++;
			} else {
				anguloTemp--;
			}
			//System.out.println("ta peso");
			
			if (anguloTemp > 2000 || anguloTemp < -2000) {
				colisaoParede();
				
				
				System.out.println("nao existe angulos, vc esta dentro da parede ou encurralado");
				
				if (tentativas > 4) {
					System.out.println("diminuindo range de A.I.");
					range -= 40;
					if (range < 10) range = 10;
					tentativas = 0;
				}
				
				tentativas++;
			}
		}

	}

	private void tentarChegar() {
		while (true) {

			if ((xI + (int) xOff == xF) && (yI + (int) yOff == yF)) {
				chegou = true;
				anguloCerto = true;
				break;
			}

			if ((xI + (int) xOff + width) >= JanelaJogo.WIDTH || (xI + (int) xOff < 0)) {
				range = RANGE / 2;
				break;
			}

			if ((yI + (int) yOff + height) >= JanelaJogo.HEIGHT || (yI + (int) yOff < 0)) {
				range = RANGE / 2;
				break;
			}
			
			if ((Math.abs(xOff)  + Math.abs(yOff) >= range))  {
				chegou = true;
				break;
			}

			if (!TileMap.tileMap[(int) Math.floor((xI + (int) xOff) / Tile.WIDTH)][(int) Math.floor((yI + (int) yOff) / Tile.HEIGHT)].equals(tileVerificado)) {
				tileVerificado = TileMap.tileMap[(int) Math.floor((xI + (int) xOff) / Tile.WIDTH)][(int) Math.floor((yI + (int) yOff) / Tile.HEIGHT)];
			}

			if (tileVerificado.isWall()) {
				if (xOff == 0 && yOff == 0)  {
					//dentroParede = true;
					colisaoParede();
				} else {
					xOff -= Math.cos(Math.toRadians(angulo));
					yOff += Math.sin(Math.toRadians(angulo));
				}
				
				
				break;
			}
			
			//System.out.println("preso");	
			
			xOff += Math.cos(Math.toRadians(angulo));
			yOff -= Math.sin(Math.toRadians(angulo));

		}
	}

	private void pintarPath(Graphics2D g) {
		g.setColor(Color.BLACK);
		double xOff = 0;
		double yOff = 0;
		

		while (true) {
			if ((xI + (int) xOff == xF) && (yI + (int) yOff == yF))
				return;

			if ((xI + (int) xOff) >= JanelaJogo.WIDTH || (xI + (int) xOff < 0))
				break;
			if ((yI + (int) yOff) >= JanelaJogo.HEIGHT || (yI + (int) yOff < 0))
				break;
			
			if (Math.abs(xOff) + Math.abs(yOff) >= range)  {
				break;
			}

			g.drawRect(xI + (int) xOff, yI + (int) yOff, 0, 0);

			xOff += Math.cos(Math.toRadians(angulo));
			yOff -= Math.sin(Math.toRadians(angulo));

		}
	}

}
