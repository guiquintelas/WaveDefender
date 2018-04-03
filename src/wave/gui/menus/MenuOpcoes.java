package wave.gui.menus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import wave.audio.BackMusic;
import wave.audio.Musica;
import wave.audio.SoundEffect;
import wave.audio.SoundEffect2D;
import wave.gui.BarrasSuperiores;
import wave.gui.GUI;
import wave.gui.ferramentas.Slider;
import wave.gui.ferramentas.SliderNumero;
import wave.principal.JanelaJogo;
import wave.principal.Principal;

public class MenuOpcoes {
	private static final int WIDTH = 300;
	private static final int HEIGHT = 200;

	private static final int x = (JanelaJogo.WIDTH / 2) - WIDTH / 2;
	private static final int y = (JanelaJogo.HEIGHT + GUI.HEIGHT) / 2 - HEIGHT / 2;
	
	private static final JCheckBox barraVidaAtivada = carregarBarraVidaAtivada();
	private static final JCheckBox barraVidaInvertida = carregarBarraVidaInvertida();
	private static final JCheckBox muteMusic = carregarMuteMusic();
	private static final JCheckBox muteSfx = carregarMuteSfx();
	
	private static final ButtonGroup bg = new ButtonGroup();
	private static final JRadioButton posC = carregarPosC(bg);
	private static final JRadioButton posB = carregarPosB(bg);
	private static final JRadioButton posD = carregarPosD(bg);
	private static final JRadioButton posE = carregarPosE(bg);

	private static Slider musicSlider = new Slider(x + 10 + 60, y + 30, WIDTH - 20 - 60 - 25, 20, new SliderNumero() {
		@Override
		public void setNumero(double numero) {
			BackMusic.setVolume((float)numero);
		}

		@Override
		public double getNumero() {
			return Musica.volume;
		}
	}, 1);
	
	private static Slider sfxSlider = new Slider(x + 10 + 60, y + 55, WIDTH - 20 - 60 - 25, 20, new SliderNumero() {
		@Override
		public void setNumero(double numero) {
			SoundEffect.setVolume((float)numero);
			SoundEffect2D.setVolume((float)numero);
		}

		@Override
		public double getNumero() {
			return SoundEffect.volume;
		}
	}, 1);
	
	private static JCheckBox carregarBarraVidaAtivada() {
		final JCheckBox cb = new JCheckBox();
		cb.setBounds(x + WIDTH - 100, y + 114, 20, 20);
		cb.setOpaque(false);
		cb.setFocusable(false);
		cb.setVisible(Principal.isPausado());
		cb.setSelected(BarrasSuperiores.isAtivo);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BarrasSuperiores.isAtivo = !BarrasSuperiores.isAtivo;			
			}
		});
		
		JanelaJogo.janela.getContentPane().add(cb);
		
		return cb;
	}
	
	private static JCheckBox carregarBarraVidaInvertida() {
		final JCheckBox cb = new JCheckBox();
		cb.setBounds(x + WIDTH - 100, y + 95, 20, 20);
		cb.setOpaque(false);
		cb.setFocusable(false);
		cb.setVisible(Principal.isPausado());
		cb.setSelected(BarrasSuperiores.isInvertido);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BarrasSuperiores.isInvertido = !BarrasSuperiores.isInvertido;			
			}
		});
		
		JanelaJogo.janela.getContentPane().add(cb);
		
		return cb;
	}
	
	private static JCheckBox carregarMuteMusic() {
		final JCheckBox cb = new JCheckBox();
		cb.setBounds(x + WIDTH - 30, y + 30, 20, 20);
		cb.setOpaque(false);
		cb.setFocusable(false);
		cb.setVisible(Principal.isPausado());
		cb.setSelected(!BackMusic.isMute());
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BackMusic.setMute(!BackMusic.isMute());	
				musicSlider.setAtivo(!musicSlider.isAtivo());
			}
		});
		
		JanelaJogo.janela.getContentPane().add(cb);
		
		return cb;
	}
	
	private static JCheckBox carregarMuteSfx() {
		final JCheckBox cb = new JCheckBox();
		cb.setBounds(x + WIDTH - 30, y + 55, 20, 20);
		cb.setOpaque(false);
		cb.setFocusable(false);
		cb.setVisible(Principal.isPausado());
		cb.setSelected(!SoundEffect.isMute());
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SoundEffect.setMute(!SoundEffect.isMute());		
				sfxSlider.setAtivo(!sfxSlider.isAtivo());
			}
		});
		
		JanelaJogo.janela.getContentPane().add(cb);
		
		return cb;
	}
	
	private static JRadioButton carregarPosC(ButtonGroup bg) {
		JRadioButton rb = new JRadioButton();
		rb.setBounds(x + 96, y + 90, 20, 20);
		rb.setOpaque(false);
		rb.setFocusable(false);
		rb.setVisible(Principal.isPausado());
		rb.setSelected(BarrasSuperiores.getPosicao() == BarrasSuperiores.TOPO);
		rb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BarrasSuperiores.setPosicao(BarrasSuperiores.TOPO);			
			}
		});
		
		JanelaJogo.janela.getContentPane().add(rb);
		bg.add(rb);
		
		return rb;
	}
	
	private static JRadioButton carregarPosB(ButtonGroup bg) {
		JRadioButton rb = new JRadioButton();
		rb.setBounds(x + 96, y + 120, 20, 20);
		rb.setOpaque(false);
		rb.setFocusable(false);
		rb.setVisible(Principal.isPausado());
		rb.setSelected(BarrasSuperiores.getPosicao() == BarrasSuperiores.BAIXO);
		rb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BarrasSuperiores.setPosicao(BarrasSuperiores.BAIXO);			
			}
		});
		
		JanelaJogo.janela.getContentPane().add(rb);
		bg.add(rb);
		
		return rb;
	}
	
	private static JRadioButton carregarPosD(ButtonGroup bg) {
		JRadioButton rb = new JRadioButton();
		rb.setBounds(x + 111, y + 105, 20, 20);
		rb.setOpaque(false);
		rb.setFocusable(false);
		rb.setVisible(Principal.isPausado());
		rb.setSelected(BarrasSuperiores.getPosicao() == BarrasSuperiores.DIREITA);
		rb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BarrasSuperiores.setPosicao(BarrasSuperiores.DIREITA);			
			}
		});
		
		JanelaJogo.janela.getContentPane().add(rb);
		bg.add(rb);
		
		return rb;
	}
	
	private static JRadioButton carregarPosE(ButtonGroup bg) {
		JRadioButton rb = new JRadioButton();
		rb.setBounds(x + 80, y + 105, 20, 20);
		rb.setOpaque(false);
		rb.setFocusable(false);
		rb.setVisible(Principal.isPausado());
		rb.setSelected(BarrasSuperiores.getPosicao() == BarrasSuperiores.ESQUERDA);
		rb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BarrasSuperiores.setPosicao(BarrasSuperiores.ESQUERDA);			
			}
		});
		
		JanelaJogo.janela.getContentPane().add(rb);
		bg.add(rb);
		
		return rb;
	}
	
	
	
	public static void update() {
		barraVidaAtivada.setVisible(Principal.menuOpçoes());
		barraVidaInvertida.setVisible(Principal.menuOpçoes());
		posC.setVisible(Principal.menuOpçoes());
		posB.setVisible(Principal.menuOpçoes());
		posD.setVisible(Principal.menuOpçoes());
		posE.setVisible(Principal.menuOpçoes());
		muteMusic.setVisible(Principal.menuOpçoes());
		muteSfx.setVisible(Principal.menuOpçoes());
		musicSlider.setAtivo(!BackMusic.isMute());
		sfxSlider.setAtivo(!SoundEffect.isMute());
	}

	public static void pintar(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, WIDTH, HEIGHT, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
		g.drawString("Menu", x + WIDTH / 2 - 20, y + 18);
		g.drawString("Musica: ", x + 10, y + 45);
		musicSlider.pintar(g);
		g.setColor(Color.WHITE);
		g.drawString("SFX: ", x + 10, y + 70);	
		
		g.setFont(new Font("Bookman Old Style", Font.ITALIC, 11));
		g.drawString("Barra Vida/Mana", x + WIDTH / 2 - 40, y + 90);
		
		g.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
		g.drawString(":Ativada", x + WIDTH - 70, y + 130);
		g.drawString(":Invertida", x + WIDTH - 80, y + 110);
		
		g.drawString("Posição:  ", x + 10, y + 120);
		g.drawLine(x + 100, y + 115, x + 110, y + 115);
		g.drawLine(x + 105, y + 110, x + 105, y + 120);
		
		g.setFont(new Font("Bookman Old Style", Font.ITALIC, 12));
		g.drawString("by: Guiquintelas", x + WIDTH - 95, y + HEIGHT - 7);
		sfxSlider.pintar(g);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
}
