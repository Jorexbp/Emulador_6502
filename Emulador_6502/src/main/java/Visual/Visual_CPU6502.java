package Visual;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import CPU_6502.CPU;
import CPU_6502.Comando_Opcode;
import CPU_6502.OPCODES;
import CPU_6502.OPCODES_ENUM;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Visual_CPU6502 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CPU cpu = new CPU();
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private static JTextArea textArea;
	private String command = "", comando = "";
	private ArrayList<String> historial = new ArrayList<>();
	private ArrayList<String> comandosPredefinidos = new ArrayList<>();
	private int n_comando, pregunta = 0, vecRes;
	private static int cortr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Visual_CPU6502 frame = new Visual_CPU6502();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public void escribirExterno(String comando) {
		this.comando = comando;
		new Visual_CPU6502().setVisible(true);
		textArea.append(comando);
		EjecutarComando(comando);
	}

	private void resetCPU() {
		cpu.reset(0xFF01, CPU.mem);
		textArea.append("\nUsuario > ");
		cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;
		textArea.setCaretPosition(cortr);
	}

	private void mostrarComandosPredefinidos() {
		String comandos[] = cargarComandosPredefinidos();
		textArea.append("LOS COMANDOS PREDEFINIDOS SON LOS SIGUIENTES\n");
		for (int i = 0; i < comandos.length; i++) {
			textArea.append("\n" + comandos[i]);
		}
		textArea.append("\n\nUsuario > ");
	}

	public void MostrarPorConsola() {
		textArea.append("\n//////////////////////////////////\nValor del registro A: " + CPU.A + "\n");
		textArea.append("Valor del registro X: " + CPU.X + "\n");
		textArea.append("Valor del registro Y: " + CPU.Y + "\n");

		textArea.append("Valor de la bandera D: " + cpu.D + "\n");
		textArea.append("Valor de la bandera C: " + cpu.C + "\n");
		textArea.append("Valor de la bandera I: " + cpu.I + "\n");
		textArea.append("Valor de la bandera B: " + cpu.B + "\n");
		textArea.append("Valor de la bandera V: " + cpu.V + "\n");

		textArea.append("Valor de la bandera N: " + cpu.N + "\n");
		textArea.append("Valor de la bandera Z: " + cpu.Z + "\n//////////////////////////////////\n\nUsuario > ");
		cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;
		textArea.setCaretPosition(cortr + comando.length());
	}

	private void MostrarMem() {
		String s = "";
		for (int i = 0; i < CPU.mem.data.length; i++) {
			if (CPU.mem.data[i] != 0) {
				s += "|" + CPU.mem.data[i] + "|";
			} else {
				s += CPU.mem.data[i];
			}
		}
		textArea.append("\n" + s.replace("||", "|") + "\nUsuario > ");
		cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;
	}

	private String[] cargarComandosPredefinidos() {
		String comandos[] = { "HELP", "EXIT", "SHOW", "SHOWMEM", "CLEAR", "CL", "RESET" };
		for (int i = 0; i < comandos.length; i++) {
			comandosPredefinidos.add(comandos[i]);
		}
		return comandos;
	}

	private void EjecutarComando(String valoresComas) {
		boolean existe = false;
		StringTokenizer st = new StringTokenizer(valoresComas, ",");
		String comando = "";
		ArrayList<String> valores = new ArrayList<>();
		while (st.hasMoreTokens()) { // No funciona con un For
			valores.add(st.nextToken());
		}

		comando = valores.get(0).trim().toUpperCase();

		for (OPCODES ins : OPCODES.values()) {
			if (comando.equals(ins.toString())) {
				existe = true;
			}
		}
		int opcode = Comando_Opcode.cambiarAOpcode(comando);

		int cont = 1;
		if (existe && opcode != -1) {
			try {
				resetCPU();
				CPU.mem.data[0xFF01] = opcode;
				CPU.mem.data[0xFF02] = Integer.parseInt(valores.get(cont));
				cont++;
				try {
					CPU.mem.data[CPU.mem.data[0xFF02]] = Integer.parseInt(valores.get(cont));
				} catch (Exception e) {

				}
				cpu.execute(2, CPU.mem);

				MostrarPorConsola();
			} catch (Exception e) {
				e.printStackTrace();
				cont--;
				textArea.append("VALORES NO VALIDOS: \"" + (valores.get(cont)).trim() + "\"\nUsuario > ");
			}
		} else {
			if (comando.equals("N") && pregunta == 1) {

				textArea.append("\nUsuario > ");
			} else {
				textArea.append("COMANDO NO ENCONTRADO: \"" + comando + "\"\n");
				textArea.append("Usuario > ");
			}
		}
		cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;

	}

	private void valorarComandoPredefinido(String comando) {
		switch (comando.toUpperCase()) {
		case "HELP":
			mostrarComandosPredefinidos();
			break;
		case "EXIT":
			dispose();
			break;
		case "CLEAR":
			dispose();
			new Visual_CPU6502().setVisible(true);
			break;
		case "CL":
			dispose();
			new Visual_CPU6502().setVisible(true);
			break;
		case "SHOW":
			MostrarPorConsola();
			break;
		case "SHOWMEM":
			MostrarMem();
			break;
		case "RESET":
			resetCPU();
			break;
		default:
			break;
		}

	}

	public Visual_CPU6502() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				try {
					scrollPane.setBounds(0, 0, getWidth(), getHeight());
				} catch (Exception ñ) {

				}
			}
		});
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setExtendedState(MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText("\nBienvenido al emulador de la CPU 6502 creada por Jorge Barba Polán\n" + "Consola 6502 >");
		textArea.setCaretColor(new Color(255, 255, 255));
		textArea.setText(textArea.getText() + "\n" + "Usuario > ");
		((AbstractDocument) textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
			public void insertString(final FilterBypass fb, final int offset, final String string,
					final AttributeSet attr) throws BadLocationException {
				if (offset >= cortr) {
					super.insertString(fb, offset, string, attr);
				}
			}

			public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
				if (offset >= cortr) {
					super.remove(fb, offset, length);
				}
			}

			public void replace(final FilterBypass fb, final int offset, final int length, final String text,
					final AttributeSet attrs) throws BadLocationException {
				if (offset >= cortr) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;

		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;
				String valoresComas = "";
				command = textArea.getText().substring(cortr).trim().toUpperCase();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (comandosPredefinidos.contains(command)) {
						valorarComandoPredefinido(command);
					} else if (textArea.getText().substring(cortr).trim().isEmpty()) {
						textArea.append("Usuario > ");
						cortr += 11;
					} else {
						valoresComas += textArea.getText().substring(cortr).trim();
						EjecutarComando(valoresComas);
						historial.add(command.trim());
						n_comando = 0;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					try {
						textArea.append(historial.get(n_comando).toString());
						cortr += textArea.getText().lastIndexOf("Usuario > ") + 10
								+ historial.get(n_comando).toString().length();
						textArea.setCaretPosition(textArea.getText().length());
						n_comando++;
					} catch (Exception s) {
						n_comando = 0;
						textArea.setCaretPosition(textArea.getText().length());
					}
				}
				if (textArea.getCaretPosition() <= cortr) {
					try {
						textArea.setCaretPosition(cortr);
					} catch (Exception á0) {
					}
				}
			}
		});
		textArea.setFont(new Font("Consolas", Font.BOLD, 14));
		scrollPane.setViewportView(textArea);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);

		cargarComandosPredefinidos();
		cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;

		textArea.setCaretPosition(cortr);

	}
}
