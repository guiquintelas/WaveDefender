package wave.mob;

import wave.graphics.BalaoDeFala;
import wave.item.Dropper;
import wave.principal.Principal;
import wave.projetil.ProjetilGround;
import wave.tools.Util;

public class MonstroPadrao extends Monstro {
	public static boolean tutorial = true;
	
	public MonstroPadrao() {
		super(15 + (Principal.nivel * 5), 100, Util.randomInt(15, 25), 0, Util.randomDouble(1.2, 2), 1, Util.randomInt(0, 2));
		//new Luz(this, 100, Util.randomInt(0, 255), Util.randomInt(0, 255), Util.randomInt(0, 255), 100, Util.randomInt(100, 150));
	}

	protected void updateResto() {
		atiraGround();
		
	}

	private void atiraGround() {
		int random = 1 + (int)(Math.random() * 1000);
		if (random <= 2 && isFurioso  && ProjetilGround.todosGProjetils.size() < 1) {
			new ProjetilGround(getXCentro(), getYSpriteCentro(), getDanoVar());
			lancaProjetilSFX.play();
		}
	}
	
	protected int expValor() {
		return 20;
	}
	
	protected void droppar() {
		if (tutorial) {
			dropper.addItem(Dropper.MOCHILA, 100);
			dropper.addItem(Dropper.POTION_VIDA, 100);
			//dropper.addItem(Dropper.POTION_FORcA, 100);
			//dropper.addItem(Dropper.POTION_FORcA, 100);
			//dropper.addItem(Dropper.GOLD_HELMET, 100);
			//dropper.addItem(Dropper.GOLD_ARMOR, 100);
			//dropper.addItem(Dropper.GOLD_LEGS, 100);
			//dropper.addItem(Dropper.GOLD_BOOTS, 100);
			
			new BalaoDeFala("Eu posso pegar itens do chão apertando o botão direito do mouse!", Player.getPlayer(), 350);
			tutorial = false;
		}
		
		super.droppar();
		
	}

}
