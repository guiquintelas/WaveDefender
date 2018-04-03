package wave.map;

import wave.mob.Mob;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public class TileMap {
	public static final int WIDTH = JanelaJogo.WIDTH / Tile.WIDTH;
	public static final int HEIGHT = JanelaJogo.HEIGHT / Tile.HEIGHT;

	public static Tile[][] tileMap = carregarTileMap();

	private static Tile[][] carregarTileMap() {
		Tile[][] tileMap = new Tile[WIDTH][HEIGHT];

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				tileMap[x][y] = new Tile(x, y);
			}
		}

			for (int y = 0; y < HEIGHT; y++) {
					for (int x = 0; x < WIDTH; x++) {
		//				if (y == 20) {
		//					tileMap[x][y].setWall();
		//				}
		//				
		//				if (y == 10) {
		//					tileMap[x][y].setWall();
		//				}
		//				
		//				if (x == 20) {
		//					tileMap[x][y].setWall();
		//				}
		//				
		//				if (x == 40) {
		//					tileMap[x][y].setWall();
		//				}
		//				
						int random = (int)(Math.random() * 3);
						if (random == 0) {
							if (Principal.debugPF) tileMap[x][y].setWall();
						}
		//				
					}
			}
		//		
		//		tileMap[38][20].setAir();
		//		tileMap[39][20].setAir();
		//		tileMap[40][20].setAir();
		//		tileMap[19][19].setWall();
		//		tileMap[20][10].setAir();
		//		
		if (Principal.debugPF) {
			tileMap[27][15].setWall();
			tileMap[27][16].setWall();
			tileMap[27][17].setWall();
			tileMap[27][18].setWall();
			tileMap[27][19].setWall();
		}

		//		tileMap[27][14].setWall();
		//		tileMap[28][15].setWall();
		//		
		//		tileMap[27][18].setWall();
		//		tileMap[27][17].setWall();
		//		tileMap[28][17].setWall();
		//		
		//		tileMap[31][15].setWall();
		//		tileMap[32][16].setWall();
		//		tileMap[32][15].setWall();
		//		
		//		tileMap[31][18].setWall();
		//		tileMap[31][17].setWall();
		//		tileMap[32][18].setWall();
		//		
		//		tileMap[27][15].setWall();
		//		tileMap[27][14].setWall();
		//		tileMap[28][15].setWall();
		//		tileMap[28][15].setWall();

		return tileMap;
	}

	public static void update() {
		if (Principal.debugPF) return;
		
		for (int i = 0; i < Mob.todosMobs.size(); i++) {
			for (int a = 0; a < Mob.todosMobs.get(i).getTiles().size(); a++) {
				Mob.todosMobs.get(i).getTiles().get(a).checaColisao(Mob.todosMobs.get(i));
				//	Mob.todosMobs.get(i).getTiles().get(a).isAtivo = true;
			}
		}

		//		for (int y = 0; y < HEIGHT; y++) {
		//			for (int x = 0; x < WIDTH; x++) {
		//				boolean ativo = false;
		//				for (int i = 0; i < Mob.todosMobs.size(); i++) {
		//					for (int a = 0; a < Mob.todosMobs.get(i).getTiles().size(); a++) {
		//						if (Mob.todosMobs.get(i).getTiles().get(a).equals(tileMap[x][y])) {
		//							ativo = true;
		//						}
		//					}
		//				}
		//
		//				if (tileMap[x][y].isAtivo && !ativo) {
		//					tileMap[x][y].isAtivo = false;;
		//				}
		//			}
		//		}
	}
	
	public static int densidadeParedes(int xOri, int yOri, int raio) {
		int numParedes = 0;
		
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				int tileX = (int)Math.floor(xOri / Tile.WIDTH) + x - raio;
				int tileY = (int)Math.floor(yOri / Tile.HEIGHT) + y - raio;
				
				if ((tileX >= 0 && tileX < WIDTH) && (tileY >= 0 && tileY < HEIGHT)) {
					if (tileMap[tileX][tileY].isWall()) {
						numParedes++;
					}
				}				
			}
		}
		
		return numParedes;
	}
	
	public static Tile getTile(int x, int y) {
		int xT = x / Tile.WIDTH;
		int yT = y / Tile.HEIGHT;
		
		if (xT < 0 || yT < 0 || xT > WIDTH - 1|| yT > HEIGHT - 1) return null;
		
		return TileMap.tileMap[xT][yT];
				
	}
}
