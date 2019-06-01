package wave.tools;

import java.util.ArrayList;

import wave.principal.Principal;



public class Variator {
	
	private VariatorNumero numero;
	private double numeroVar;
	private double var = 0;
	private double fadeInMax = 0;
	private double fadeOutMin = 0;
	private double aux = 0;
	private int ticks = 0;
	private double inicio = 0;
	private double oscilandoOff = 0;
	private int vezesOscilando = 0;
	private int resumeTick = 0;
	
	private static final double MEIO_SIN = 127.3113364688716;
	
	private boolean isVariando = false;
	private boolean isFadeIn = false;
	private boolean isFadeOut = false;
	private boolean isSin = false;
	private boolean isOscilando = false;
	private boolean isOscilandoPronto = false;
	private boolean isEsperando = false;
	private boolean isAtivo = false;
	
	private ArrayList<ActionQueue> filaActions = new ArrayList<ActionQueue>();
	
	public static ArrayList<Variator> todosVariator = new ArrayList<Variator>();
	
	
	public Variator(VariatorNumero numero) {
		this.numero = numero;
		this.numeroVar = numero.getNumero();
		isAtivo = true;
		todosVariator.add(this);
	}
	
	public static void cleanUp() {
		while (todosVariator.size() > 0) {
			todosVariator.get(0).desativar();
		}
	}
	
	public void fadeIn(double inicio, double max, int ticks) {
		if (inicio >= max || (isFadeOut || isOscilando || isEsperando)) return;
		if (ticks == 0) {
			isFadeIn = false;
			numero.setNumero(max);
		//	executarAcao();
			return;
		}
		numero.setNumero(inicio);
		numeroVar = numero.getNumero();
		var = (max - inicio)/(double)ticks;	
		isFadeIn = true;
		fadeInMax = max;
	}
	
	public void fadeInSin(double inicio, double max, int ticks) {
		if (inicio >= max || (isFadeOut || isOscilando || isEsperando)) return;
		if (ticks == 0) {
			isFadeIn = false;
			numero.setNumero(max);
			//executarAcao();
			return;
		}
		numero.setNumero(inicio);
		numeroVar = numero.getNumero();
		this.inicio = inicio;
		this.ticks = ticks;
		aux = 0;
		var = (200/(double)ticks) * ((max - inicio) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;	
		isFadeIn = true;
		isSin = true;
		fadeInMax = max;
	}
	
	public void fadeOutSin(double inicio, double min, int ticks) {
		if (inicio <= min || (isFadeIn || isOscilando || isEsperando)) return;
		if (ticks == 0) {
			isFadeOut = false;
			numero.setNumero(min);
			executarAcao();
			return;
		}
		numero.setNumero(inicio);
		numeroVar = numero.getNumero();
		this.inicio = inicio;
		this.ticks = ticks;
		aux = 180;
		var = (200/(double)ticks) * ((inicio - min) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;
		isFadeOut = true;
		isSin = true;
		fadeOutMin = min;
	}
	
	public void fadeOut(double inicio, double min, int ticks) {
		if (inicio <= min || (isFadeIn || isOscilando || isEsperando)) return;
		if (ticks == 0) {
			isFadeOut = false;
			numero.setNumero(min);
			executarAcao();
			return;
		}
		numero.setNumero(inicio);
		numeroVar = numero.getNumero();
		var = (min - inicio)/(double)ticks;	
		isFadeOut = true;
		fadeOutMin = min;
	}
	
	private void checaFadeIn() {
		if (isFadeIn) {
			if (isSin) {
				aux += 180/(double)ticks;
				var = (200/(double)ticks) * ((fadeInMax - inicio) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;	
			}
			if (aux >= 180 || numeroVar >= fadeInMax) {
				isFadeIn = false;
				isVariando = false;
				isSin = false;
				executarAcao();
			}
		}
	}
	
	private void checaFadeOut() {
		if (isFadeOut) {
			if (isSin) {
				aux += 180/(double)ticks;
				var =  (200/(double)ticks) * ((inicio - fadeOutMin) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;		
			}
			if (aux >= 360 || numeroVar <= fadeOutMin) {
				isFadeOut = false;
				isVariando = false;
				isSin = false;
				executarAcao();
			}
		}
	}
	
	public void oscilar(double off, int ticks, boolean comecarBaixo) {
		if (isFadeIn || isFadeOut || isOscilando || isEsperando) return;
		if (ticks == 0 || off == 0) return; 		
		
		isOscilando = true;
		if (comecarBaixo) {
			aux = 180;
		} else {
			aux = 0;
		}
		var = (200/(double)ticks) * ((off * 2) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;
		this.oscilandoOff = off;
		this.ticks = ticks;
		isOscilandoPronto = false;
		fadeOutMin = numeroVar - off;
		vezesOscilando = 0;
	}
	
	public void esperar(int ticks) {
		if (isFadeIn || isEsperando || isFadeOut || isOscilando || ticks == 0) return;
		isEsperando = true;
		resumeTick = Principal.tickTotal + ticks;
	}
	
	public void update() {
		if (!isAtivo) return;
		
		if (isVariando && (isFadeIn || isFadeOut || isOscilando)) {
			numeroVar += var;
			numero.setNumero(numeroVar);
		}
		checaFadeIn();
		checaFadeOut();
		checaOscilando();
		checaVariando();
		checaEsperando();
		if (!isFadeIn && !isOscilando && !isFadeOut && !isEsperando) executarAcao();
	}
	
	private void checaEsperando() {
		if (isEsperando && Principal.tickTotal >= resumeTick) {
			isEsperando = false;
		}
		
	}
	
	public void changeNumeroAlvoOsc(final double numero) {
		final int tickOsc = this.ticks;
		final double oscOff = oscilandoOff;
		variar(false);
		addAcaoNaFila(new ActionQueue() {
			public boolean action() {			
				if (numero > numeroVar) {
					fadeInSin(numeroVar, numero, 50);
				} else {
					fadeOutSin(numeroVar, numero, 50);
				}
				variar(true);
				return true;
			}
		});
		
		addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				oscilar(oscOff, tickOsc, true);
				variar(true);
				return true;
			}
		});
		//numeroVar = numero;
	}
	
	private void checaOscilando() {
		if (isOscilando) {
			
			aux += 360/(double)ticks;
			if (isOscilandoPronto) {
				var = (200/(double)ticks) * ((oscilandoOff * 4) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;
			} else {
				var = (200/(double)ticks) * ((oscilandoOff * 2) * Math.sin(Math.toRadians(aux)))/MEIO_SIN;
			}
			
			if (aux >= 360) {
				aux = 0;
				isOscilandoPronto = true;
				numeroVar = fadeOutMin;
				vezesOscilando++;
				executarAcao();
			}
			
		}
		
	}
	
	public void pararOscilar() {
		isOscilando = false;
		isVariando = false;
	}

	private void checaVariando() {
		if ( isVariando && !numero.devoContinuar()) {
			variar(false);
		}
	}

	public void variar(boolean variar) {
		if (variar && (isFadeIn || isFadeOut || isOscilando)) {
			isVariando = variar;
		} else if (!variar){
			isVariando = variar;
			isFadeIn = variar;
			isFadeOut = variar;
			isOscilando = variar;
			isSin = false;
		}
	}
	
	public int getVezesOscilando() {
		return vezesOscilando;
	}
	
	public void desativar() {
		todosVariator.remove(this);
	}
	
	private void executarAcao() {
		if (filaActions.size() > 0) {
			if (filaActions.get(0).action()) {
				filaActions.remove(0);
				
			}		
		}
	}
	
	public void addEsperaNaFila(final int ticks) {
		addAcaoNaFila(new ActionQueue() {
			public boolean action() {
				esperar(ticks);
				return true;
			}
		});
	}
	
	public void addAcaoNaFila(ActionQueue acao) {
		filaActions.add(acao);
	}
	
	public void clearFila() {
		filaActions.clear();
	}
	
	public boolean isOscilando() {
		return isOscilando;
	}

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
	}
}
