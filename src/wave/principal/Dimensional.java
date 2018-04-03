package wave.principal;

import java.util.ArrayList;

import wave.map.Tile;
import wave.map.TileMap;

public abstract class Dimensional {
	protected double x;
	protected double y;
	
	protected int width = 1;
	protected int height = 1;
	
	public int getX() {
		return (int)x;
	}
	
	public double getXDouble() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public double getYDouble() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getXCentro() {
		return (int) x + width / 2;
	}
	
	public int getYCentro() {
		return (int) y + height / 2;
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}
	
	public ArrayList<Tile> getTiles() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();

		int xOff = 0;
		int yOff = 0;
		while (true) {
			xOff = 0;
			int xTile = (int) Math.floor((getX() -1 + (xOff * Tile.WIDTH)) / Tile.WIDTH);
			int yTile = (int) Math.floor((getY() -1 + (yOff * Tile.HEIGHT)) / Tile.HEIGHT);
			
			if (xTile < 0 || yTile < 0 || xTile >= TileMap.WIDTH || yTile >= TileMap.HEIGHT) break;

			if (yTile >= TileMap.tileMap[0].length) {
				break;
			}

			if (TileMap.tileMap[xTile][yTile].getY() > getY() + height) {
				break;
			}
			
			while (true) {
				xTile = (int) Math.floor((getX() -1 + (xOff * Tile.WIDTH)) / Tile.WIDTH);
				yTile = (int) Math.floor((getY() -1 + (yOff * Tile.HEIGHT)) / Tile.HEIGHT);
				
				if (xTile >= TileMap.tileMap.length) {
					break;
				}
				
				if (TileMap.tileMap[xTile][yTile].getX() > getX() + width) {
					break;
				}
				tiles.add(TileMap.tileMap[xTile][yTile]);

				xOff++;
			}
			
			
			yOff++;
		}

		return tiles;
	}	
	
	

}
