package wave.map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wave.map.trap.Trap;
import wave.mob.Mob;
import wave.mob.Player;
import wave.principal.Dimensional;
import wave.principal.Principal;
import wave.tools.Util;

public class Tile {
	private int x;
	private int y;

	private int xTile;
	private int yTile;

	public static final int WIDTH = 16;
	public static final int HEIGHT = 16;
	
	public static boolean grassDebug = false;

	public boolean isAtivo = false;
	private boolean isWall = false;
	private boolean isTrap = false;
	private boolean isGrass = false;
	public float probGrama = 0;

	public Trap trap;

	private static final BufferedImage imgWall = Util.carregarImg("/Sprites/wall.png");
	private static final BufferedImage imgGrass1 = Util.carregarImg("/Sprites/Grass.png");
	private static final BufferedImage imgGrass2 = Util.carregarImg("/Sprites/Grass2.png");
	private static final BufferedImage imgGrass3 = Util.carregarImg("/Sprites/Grass3.png");
	private static final BufferedImage imgGrass4 = Util.carregarImg("/Sprites/Grass4.png");
	private static final BufferedImage imgGrass5 = Util.carregarImg("/Sprites/Grass5.png");
	private static final BufferedImage imgGrass6 = Util.carregarImg("/Sprites/Grass6.png");
	private static final BufferedImage imgGrassGalho = Util.carregarImg("/Sprites/GrassGalho.png");
	private static final BufferedImage imgGrassGalho2 = Util.carregarImg("/Sprites/GrassGalho2.png");
	private BufferedImage imgGrass;

	public Tile(int x, int y) {
		this.xTile = x;
		this.yTile = y;
		this.x = x * WIDTH;
		this.y = y * HEIGHT;

	}

	public void pintar(Graphics2D g) {
		if (Principal.debugPF | Principal.editorTiles) {
			g.setColor(Color.BLACK);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g.drawLine(x, y, x + WIDTH, y);
			g.drawLine(x, y + HEIGHT, x, y);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}

		if (isWall) {
			g.drawImage(imgWall, x, y, null);

			g.setColor(Color.BLACK);
			if (!isWallUp())
				g.drawLine(x, y, x + WIDTH, y);
			if (!isWallDown())
				g.drawLine(x, y + HEIGHT, x + WIDTH, y + HEIGHT);
			if (!isWallR())
				g.drawLine(x + WIDTH, y, x + WIDTH, y + HEIGHT);
			if (!isWallL())
				g.drawLine(x, y, x, y + HEIGHT);

		}

		if (isTrap) {
			g.drawImage(trap.getImg(), x, y, null);
		} else if (isGrass) {
			g.drawImage(imgGrass, x, y, null);
		}

		//codigo para testes
		if (isAtivo) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.setColor(Color.CYAN);
			g.fillRect(x, y, WIDTH, HEIGHT);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}

		if (probGrama > 0 && !isGrass && grassDebug) {
			if (probGrama > 15) probGrama = 14;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, probGrama / 15));
			g.setColor(Color.GREEN);
			g.fillRect(x, y, WIDTH, HEIGHT);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}

	}

	public void setWall() {
		this.isWall = true;
		this.isGrass = false;
	}

	public void setGrama() {
		if(isGrass) {
			System.out.println("esse tile ja é grama");
			return;
		}
		
		isGrass = true;

		int rand = Util.randomInt(1, 6);

		switch (rand) {
		case 1:
			imgGrass = imgGrass1;
			break;
		case 2:
			imgGrass = imgGrass2;
			break;
		case 3:
			imgGrass = imgGrass3;
			break;
		case 4:
			imgGrass = imgGrass4;
			break;
		case 5:
			imgGrass = imgGrass5;
			break;
		case 6:
			imgGrass = imgGrass6;
			break;
		}
		
		int randon = Util.randomInt(1, 100);
		if (randon <= 3) {
			imgGrass = imgGrassGalho;
		} else if (randon <= 6) {
			imgGrass = imgGrassGalho2;
		}

		ArrayList<Tile> tiles = getTilesCirculo(4);
		for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).probGrama++;
		}
		
		ArrayList<Tile> tiles2 = getTilesCirculo(2);
		for (int i = 0; i < tiles2.size(); i++) {
			tiles2.get(i).probGrama++;
		}
		
		
	}

	public boolean isGrama() {
		return isGrass;
	}

	public void reset() {
		isGrass = false;
		isWall = false;
		isTrap = false;
	}

	public boolean setTrap(Trap trap) {
		if (!isWall) {
			this.trap = trap;
			isTrap = true;
			System.out.println("Armadilha posicionada!");
			
//			int randon = Util.randomInt(1, 2);
//			switch (randon) {
//			case 1:
//				imgTrap = imgTrap1;
//				break;		
//			case 2:
//				imgTrap = imgTrap2;
//				break;
//			}
			
			return true;

		} else {
			System.out.println("Isso é uma parede");
			return false;
		}
	}

	public boolean isTrap() {
		return this.isTrap;
	}

	public void setAir() {
		this.isWall = false;
	}

	public boolean isWall() {
		return isWall;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isWallUp() {
		if ((y / HEIGHT) - 1 < 0) {
			return false;
		}

		return TileMap.tileMap[x / WIDTH][(y / HEIGHT) - 1].isWall();
	}

	public boolean isWallDown() {
		if ((y / HEIGHT) + 1 > TileMap.HEIGHT - 1) {
			return false;
		}

		return TileMap.tileMap[x / WIDTH][(y / HEIGHT) + 1].isWall();

	}

	public boolean isWallR() {
		if ((x / WIDTH) + 1 > TileMap.WIDTH - 1) {
			return false;
		}

		return TileMap.tileMap[(x / WIDTH) + 1][y / HEIGHT].isWall();

	}

	public boolean isWallL() {
		if ((x / WIDTH) - 1 < 0) {
			return false;
		}

		return TileMap.tileMap[(x / WIDTH) - 1][y / HEIGHT].isWall();

	}

	public boolean isColliding(Dimensional d) {
		if (!isWall)
			return false;

		Rectangle rectD = new Rectangle(d.getX(), d.getY(), d.getWidth(), d.getHeight());
		Rectangle rect = new Rectangle(getX(), getY(), WIDTH, HEIGHT);
		return rect.intersects(rectD);
	}

	public void checaColisao(Dimensional d) {
		if (!isWall && !isTrap)
			return;

		if (d instanceof Mob) {

			Mob dMob = (Mob) d;

			if (isTrap) {
				if (!(d instanceof Player)) {
					trap.colisao(dMob);
					this.isTrap = false;
				}
				return;
			}

			dMob.colidindoWall = true;
		}

		Rectangle rectD = new Rectangle(d.getX(), d.getY(), d.getWidth(), d.getHeight());
		Rectangle rect = new Rectangle(getX(), getY(), WIDTH, HEIGHT);

		while (rect.intersects(rectD)) {

			if (rect.intersection(rectD).getX() >= (d.getX() + d.getWidth() / 2)) {
				if (rectD.intersection(rect).getY() >= (d.getY() + d.getHeight() / 2)) {
					if (rectD.intersection(rect).width > rectD.intersection(rect).height && !(isWallUp())) {
						d.setY(d.getY() - 1);
						rectD.y--;
						continue;
					}

					if (rectD.intersection(rect).width < rectD.intersection(rect).height && !(isWallL())) {
						d.setX(d.getX() - 1);
						rectD.x--;
						continue;
					}
					if (!(isWallUp())) {
						d.setY(d.getY() - 1);
						rectD.y--;
					}

					if (!(isWallL())) {
						d.setX(d.getX() - 1);
						rectD.x--;
					}

				} else {
					if (rectD.intersection(rect).width > rectD.intersection(rect).height && !(isWallDown())) {
						d.setY(d.getY() + 1);
						rectD.y++;
						continue;
					}

					if (rectD.intersection(rect).width < rectD.intersection(rect).height && !(isWallL())) {
						d.setX(d.getX() - 1);
						rectD.x--;
						continue;
					}

					if (!(isWallDown())) {
						d.setY(d.getY() + 1);
						rectD.y++;

					}

					if (!(isWallL())) {
						d.setX(d.getX() - 1);
						rectD.x--;

					}

				}
			} else {
				if (rectD.intersection(rect).getY() >= (d.getY() + d.getHeight() / 2)) {
					if (rectD.intersection(rect).width > rectD.intersection(rect).height && !(isWallUp())) {
						d.setY(d.getY() - 1);
						rectD.y--;
						continue;
					}

					if (rectD.intersection(rect).width < rectD.intersection(rect).height && !(isWallR())) {
						d.setX(d.getX() + 1);
						rectD.x++;
						continue;
					}
					if (!(isWallUp())) {
						d.setY(d.getY() - 1);
						rectD.y--;
					}

					if (!(isWallR())) {
						d.setX(d.getX() + 1);
						rectD.x++;
					}

				} else {
					if (rectD.intersection(rect).width > rectD.intersection(rect).height && !(isWallDown())) {
						d.setY(d.getY() + 1);
						rectD.y++;
						continue;
					}

					if (rectD.intersection(rect).width < rectD.intersection(rect).height && !(isWallR())) {
						d.setX(d.getX() + 1);
						rectD.x++;
						continue;
					}

					if (!(isWallDown())) {
						d.setY(d.getY() + 1);
						rectD.y++;
					}

					if (!(isWallR())) {
						d.setX(d.getX() + 1);
						rectD.x++;
					}

					if (isWallDown() && isWallR()) {
						d.setX(d.getX() + 1);
						rectD.x++;
						d.setY(d.getY() + 1);
						rectD.y++;
					}
				}
			}
		}
	}

	public void checaColisaoPonto(Dimensional d) {
		if (d.getWidth() == 1 && d.getHeight() == 1) {
			if (!isWall)
				return;

			while ((d.getX() >= x && d.getX() <= (x + WIDTH)) && (d.getY() >= y && d.getY() <= (y + HEIGHT))) {
				double angulo = Math.toDegrees(Math.atan2(d.getX() - (x + WIDTH / 2), d.getY() - (y + HEIGHT / 2))) - 90;
				if (angulo < 0) {
					angulo += 360;
				}

				//System.out.println(angulo);

				if (angulo >= 315 || (angulo >= 0 && angulo < 45)) {
					d.setX(d.getX() + 1);

				}

				if (angulo >= 45 && angulo < 135) {
					d.setY(d.getY() - 1);
				}

				if (angulo >= 135 && angulo < 225) {
					d.setX(d.getX() - 1);
				}

				if (angulo >= 225 && angulo < 315) {
					d.setY(d.getY() + 1);
				}
				///System.out.println();

			}

		} else {
			System.out.println("erro: isso nao é um ponto");
		}

	}

	public ArrayList<Tile> getTilesCirculo(int raioEmTiles) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();

		for (int y = 0; y <= raioEmTiles * 2; y++) {
			for (int x = 0; x <= raioEmTiles * 2; x++) {
				if (x - raioEmTiles == 0 && y - raioEmTiles == 0)
					continue;

				if (Math.abs(x - raioEmTiles) + Math.abs(y - raioEmTiles) <= raioEmTiles) {

					if (xTile + x - raioEmTiles < 0 || xTile + x - raioEmTiles > TileMap.WIDTH - 1 || yTile + y - raioEmTiles < 0 || yTile + y - raioEmTiles > TileMap.HEIGHT - 1)
						continue;

					tiles.add(TileMap.tileMap[xTile + x - raioEmTiles][yTile + y - raioEmTiles]);
				}
			}
		}

		return tiles;
	}
}
