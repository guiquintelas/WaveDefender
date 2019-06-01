package wave.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

public class ListenerManager implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {

	public static final int MOUSE_DRAG = 1;
	public static final int MOUSE_MOVED = 2;
	public static final int MOUSE_CLICKED = 3;
	public static final int MOUSE_PRESSED = 4;
	public static final int MOUSE_RELEASED = 5;

	public static final int KEY_PRESSED = 6;
	public static final int KEY_TYPED = 7;
	public static final int KEY_RELEASED = 8;

	private static ArrayList<MouseListener> mouseDrag = new ArrayList<MouseListener>();
	private static ArrayList<MouseListener> mouseMoved = new ArrayList<MouseListener>();
	private static ArrayList<MouseListener> mouseClickd = new ArrayList<MouseListener>();
	private static ArrayList<MouseListener> mousePressed = new ArrayList<MouseListener>();
	private static ArrayList<MouseListener> mouseReleased = new ArrayList<MouseListener>();

	private static ArrayList<KeyListener> keyPressed = new ArrayList<KeyListener>();
	private static ArrayList<KeyListener> keyTyped = new ArrayList<KeyListener>();
	private static ArrayList<KeyListener> keyReleased = new ArrayList<KeyListener>();

	private static HashMap<Integer, ArrayList<MouseListener>> mapMouse = new HashMap<Integer, ArrayList<MouseListener>>();
	private static HashMap<Integer, ArrayList<KeyListener>> mapKey = new HashMap<Integer, ArrayList<KeyListener>>();

	public void init(JFrame janela) {
		janela.getContentPane().addMouseListener(this);
		janela.getContentPane().addMouseMotionListener(this);
		janela.addKeyListener(this);
	}

	static {
		mapMouse.put(MOUSE_DRAG, mouseDrag);
		mapMouse.put(MOUSE_MOVED, mouseMoved);
		mapMouse.put(MOUSE_CLICKED, mouseClickd);
		mapMouse.put(MOUSE_PRESSED, mousePressed);
		mapMouse.put(MOUSE_RELEASED, mouseReleased);
		mapKey.put(KEY_PRESSED, keyPressed);
		mapKey.put(KEY_TYPED, keyTyped);
		mapKey.put(KEY_RELEASED, keyReleased);
	}

	

	public static synchronized void addListener(int tipo, MouseListener ml) {
		if (mapMouse.get(tipo).contains(ml)) return;
		mapMouse.get(tipo).add(ml);
	}

	public static synchronized void removeListener(int tipo, MouseListener ml) {
		mapMouse.get(tipo).remove(ml);
	}

	public static synchronized void addListener(int tipo, KeyListener kl) {
		if (mapKey.get(tipo).contains(kl)) return;
		mapKey.get(tipo).add(kl);
	}

	public static synchronized void removeListener(int tipo, KeyListener kl) {
		mapKey.get(tipo).remove(kl);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		for (MouseListener md : mouseDrag) {
			md.acao(e);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		for (MouseListener md : mouseMoved) {
			md.acao(e);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		for (MouseListener md : mouseClickd) {
			md.acao(e);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

		for (int i = 0; i < mousePressed.size(); i++) {
			mousePressed.get(i).acao(e);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		for (int i = 0; i < mouseReleased.size(); i++) {
			mouseReleased.get(i).acao(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//morto
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//morto
	}

	@Override
	public void keyTyped(KeyEvent e) {

		for (KeyListener md : keyTyped) {
			md.acao(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		for (KeyListener md : keyPressed) {
			md.acao(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		for (KeyListener md : keyReleased) {
			md.acao(e);
		}
	}

}
