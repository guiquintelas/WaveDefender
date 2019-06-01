package wave.item;

import java.util.ArrayList;

import wave.item.equip.CouroArmor;
import wave.item.equip.CouroBotas;
import wave.item.equip.CouroHelmet;
import wave.item.equip.CouroLegs;
import wave.item.equip.GoldenArmor;
import wave.item.equip.GoldenBoots;
import wave.item.equip.GoldenHelmet;
import wave.item.equip.GoldenLegs;
import wave.item.equip.MedievalArmor;
import wave.item.equip.MedievalBotas;
import wave.item.equip.MedievalHelmet;
import wave.item.equip.MedievalLegs;
import wave.item.equip.PlateArmor;
import wave.item.equip.PlateBoots;
import wave.item.equip.PlateHelmet;
import wave.item.equip.PlateLegs;
import wave.mob.Mob;
import wave.tools.Util;

public class Dropper {
	public static final String POTION_VIDA = "Potion de Vida";
	public static final String POTION_MANA = "Potion de Mana";
	public static final String POTION_FORCA = "Potion de Forca";
	public static final String MOCHILA = "Mochila";
	public static final String MOCHILA_ADV = "MochilaAdv";
	public static final String MOCHILA_MAL = "Mochila do Mal";
	public static final String MOCHILA_CHIQUE = "Mochila Chique";
	public static final String COURO_BOTA = "Botas de Couro";
	public static final String COURO_LEGS = "Calcas de Couro";
	public static final String COURO_HELMET = "Chapéu de Couro";
	public static final String COURO_ARMOR = "Armadura de Couro";
	public static final String MEDIEVAL_LEGS = "Calcas Medievais";
	public static final String MEDIEVAL_HELMET = "Helmet Medieval";
	public static final String MEDIEVAL_ARMOR = "Armadura Medieval";
	public static final String MEDIEVAL_BOTA = "Botas Medievais";
	public static final String PLATE_HELMET = "Plate Helmet";
	public static final String PLATE_ARMOR = "Plate Armor";
	public static final String PLATE_LEGS = "Plate Legs";
	public static final String PLATE_BOOTS = "Plate Boots";
	public static final String GOLD_HELMET = "Golden Helmet";
	public static final String GOLD_ARMOR = "Golden Armor";
	public static final String GOLD_LEGS = "Golden Legs";
	public static final String GOLD_BOOTS = "Golden Boots";

	private int xCentro;
	private int yCentro;
	
	private int quantos = 0;
	private int capacidade = 0;
	private boolean isCapped = false;

	private Mob mob;

	private ArrayList<String> itens = new ArrayList<String>();
	private ArrayList<Item> itensD = new ArrayList<Item>();

	private static final int DIST_MIN = 20;

	public Dropper(int xCentro, int yCentro) {
		this.xCentro = xCentro;
		this.yCentro = yCentro;
	}

	public Dropper(Mob mob) {
		this.mob = mob;
		this.xCentro = mob.getXCentro();
		this.yCentro = mob.getYCentro();
	}
	
	public void setCap(int cap) {
		isCapped = true;
		capacidade = cap;
	}
	

	public boolean addItem(String item, double chanceEm100) {
		double chance = Util.randomDouble(0, 100);
		if (chance <= chanceEm100 && (!isCapped || (quantos < capacidade))) {
			itens.add(item);
			quantos++;
			return true;
		}
		
		return false;
	}
	
	public boolean addItem(String item, double chanceEm100, Void semCap) {
		if (Util.randomDouble(0, 100) <= chanceEm100) {
			itens.add(item);
			return true;
		}
		
		return false;
	}

	public void removeAll() {
		itensD.clear();
	}

	public void droppar() {
		if (mob != null) {
			this.xCentro = mob.getXCentro();
			this.yCentro = mob.getYCentro();
		}

		for (int i = 0; i < itens.size(); i++) {
			int randX = 0;
			int randY = 0;

			boolean liberado = false;
			while (!liberado) {
				int margem = itens.size() * 8;
				randX = Util.randomInt(-margem, margem);
				randY = Util.randomInt(-margem, margem);
				if (i == 0)
					break;
				for (int j = 0; j < itensD.size(); j++) {
					double dist = Math.sqrt(Math.pow(xCentro + randX - itensD.get(j).getX(), 2) + Math.pow(yCentro + randY - itensD.get(j).getY(), 2));

					if (dist > DIST_MIN && j == itensD.size() - 1) {
						liberado = true;
					}

					if (dist < DIST_MIN) {
						break;
					}

				}
			}

			switch (itens.get(i)) {

			case MOCHILA:
				itensD.add(new Mochila(xCentro + randX, yCentro + randY));
				break;

			case POTION_VIDA:
				itensD.add(new PotionVida(xCentro + randX, yCentro + randY));
				break;

			case POTION_MANA:
				itensD.add(new PotionMana(xCentro + randX, yCentro + randY));
				break;

			case MOCHILA_ADV:
				itensD.add(new MochilaAdv(xCentro + randX, yCentro + randY));
				break;

			case POTION_FORCA:
				itensD.add(new PotionForca(xCentro + randX, yCentro + randY));
				break;
				
			case MOCHILA_MAL:
				itensD.add(new MochilaMal(xCentro + randX, yCentro + randY));
				break;
				
			case MOCHILA_CHIQUE:
				itensD.add(new MochilaChique(xCentro + randX, yCentro + randY));
				break;
				
			case COURO_BOTA:
				itensD.add(new CouroBotas(xCentro + randX, yCentro + randY));
				break;
			
			case COURO_LEGS:
				itensD.add(new CouroLegs(xCentro + randX, yCentro + randY));
				break;
			
			case COURO_ARMOR:
				itensD.add(new CouroArmor(xCentro + randX, yCentro + randY));
				break;
			
			case COURO_HELMET:
				itensD.add(new CouroHelmet(xCentro + randX, yCentro + randY));
				break;
				
			case MEDIEVAL_BOTA:
				itensD.add(new MedievalBotas(xCentro + randX, yCentro + randY));
				break;
				
			case MEDIEVAL_LEGS:
				itensD.add(new MedievalLegs(xCentro + randX, yCentro + randY));
				break;
				
			case MEDIEVAL_ARMOR:
				itensD.add(new MedievalArmor(xCentro + randX, yCentro + randY));
				break;
				
			case MEDIEVAL_HELMET:
				itensD.add(new MedievalHelmet(xCentro + randX, yCentro + randY));
				break;
				
			case PLATE_HELMET:
				itensD.add(new PlateHelmet(xCentro + randX, yCentro + randY));
				break;
				
			case PLATE_ARMOR:
				itensD.add(new PlateArmor(xCentro + randX, yCentro + randY));
				break;
				
			case PLATE_LEGS:
				itensD.add(new PlateLegs(xCentro + randX, yCentro + randY));
				break;
				
			case PLATE_BOOTS:
				itensD.add(new PlateBoots(xCentro + randX, yCentro + randY));
				break;
				
			case GOLD_HELMET:
				itensD.add(new GoldenHelmet(xCentro + randX, yCentro + randY));
				break;
				
			case GOLD_ARMOR:
				itensD.add(new GoldenArmor(xCentro + randX, yCentro + randY));
				break;
				
			case GOLD_LEGS:
				itensD.add(new GoldenLegs(xCentro + randX, yCentro + randY));
				break;
				
			case GOLD_BOOTS:
				itensD.add(new GoldenBoots(xCentro + randX, yCentro + randY));
				break;

			default:
				System.out.println("erro NAO EXISTE ESSE ITEM");

			}

		}
	}
}
