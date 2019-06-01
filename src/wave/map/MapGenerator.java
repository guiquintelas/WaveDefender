package wave.map;

import java.util.ArrayList;
import java.util.Collections;

import wave.graphics.BalaoDeFala;
import wave.item.ChestPadrao;
import wave.item.equip.Equip;
import wave.mob.Player;
import wave.principal.JanelaJogo;
import wave.tools.Util;

public class MapGenerator {
	
	public static void criarMapa() {
		long tempoA = System.currentTimeMillis();
		
		criarGrama();
		criarBau();
		
		System.out.println("tempo de criacao do mapa: " + (System.currentTimeMillis() - tempoA) + "ms");
	}

	private static void criarBau() {
		if (Player.getPlayer() != null && Player.getPlayer().getExpTotal() != 0) {
			ChestPadrao bau = new ChestPadrao(JanelaJogo.WIDTH / 2 - 30/2, 150, ChestPadrao.VERMELHO);
			bau.setExp(Player.getPlayer().getExpTotal() / 3);
			bau.addItem("Mochila", 100);
			bau.addItem("Potion de Vida", 70);
			//bau.addItem("Potion de Forca", 30);
			
			if (Player.getPlayer().equips.get(Equip.CABECA) != null) 	bau.addItem(Player.getPlayer().equips.get(Equip.CABECA).toString(), 30);
			if (Player.getPlayer().equips.get(Equip.PEITO) != null) 	bau.addItem(Player.getPlayer().equips.get(Equip.PEITO).toString(), 30);
			if (Player.getPlayer().equips.get(Equip.PERNA) != null) 	bau.addItem(Player.getPlayer().equips.get(Equip.PERNA).toString(), 30);
			if (Player.getPlayer().equips.get(Equip.PE) != null) 		bau.addItem(Player.getPlayer().equips.get(Equip.PE).toString(), 30);
			
			new BalaoDeFala("Esse estranho baú deve conter lembrancas da minha vida passada...", Player.getPlayer(), 250);
		}
		
	}

	private static void criarGrama() {
		int numDGrama = Util.randomInt(80, 120);
		
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		for (int y = 0; y < TileMap.HEIGHT; y++) {
			for (int x = 0; x < TileMap.WIDTH; x++) {
				tiles.add(TileMap.tileMap[x][y]);
			}
		}
		
		for (int x = 0; x < numDGrama; x++) {		
			int randon = Util.randomInt(0, tiles.size() - 1);			
			tiles.get(randon).setGrama();
			tiles.addAll(tiles.get(randon).getTilesCirculo(4));
			tiles.addAll(tiles.get(randon).getTilesCirculo(2));
			tiles.removeAll(Collections.singleton(tiles.get(randon)));
			
		}
		
	}
	
	public static void clearMap() {
		for (int y = 0; y < TileMap.HEIGHT; y++) {
			for (int x = 0; x < TileMap.WIDTH; x++) {
				TileMap.tileMap[x][y].reset();
			}
		}
	}
}
