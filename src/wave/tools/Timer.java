package wave.tools;

import java.util.ArrayList;

import wave.principal.Principal;

public abstract class Timer {
	private int tickCriado;
	private int delay;
	
	public static ArrayList<Timer> todosTimers = new ArrayList<Timer>();
	
	public Timer(int delay) {
		this.delay = delay;
		tickCriado = Principal.tickTotal;
		todosTimers.add(this);
	}
	
	public final void update() {
		acaoTick();
		if (Principal.tickTotal >= tickCriado + delay) {
			acao();
			todosTimers.remove(this);
		}
	}
	
	protected void acaoTick() {}

	public abstract void acao();
	
	public final void reset() {
		tickCriado = Principal.tickTotal;
	}
	
	public final void reset(int novoDelay) {
		delay = novoDelay;
		reset();
	}
	
	public final void delete() {
		todosTimers.remove(this);
	}
	
	
}
