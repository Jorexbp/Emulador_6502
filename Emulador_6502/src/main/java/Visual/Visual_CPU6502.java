package Visual;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import CPU_6502.CPU;
import CPU_6502.Comando_Opcode;
import CPU_6502.OPCODES_ENUM;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.ScrollPaneConstants;

public class Visual_CPU6502 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CPU cpu = new CPU();
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private String command = "";
	private ArrayList<String> historial = new ArrayList<>();
	private int n_comando;
	private int valor_1;

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
	public void MostrarPorConsola() {
		textArea.append("Valor del registro A: " + CPU.A + "\n");
		textArea.append("Valor del registro X: " + CPU.X + "\n");
		textArea.append("Valor del registro Y: " + CPU.Y + "\n");

		textArea.append("Valor de la bandera D: " + cpu.D + "\n");
		textArea.append("Valor de la bandera C: " + cpu.C + "\n");
		textArea.append("Valor de la bandera I: " + cpu.I + "\n");
		textArea.append("Valor de la bandera B: " + cpu.B + "\n");
		textArea.append("Valor de la bandera V: " + cpu.V + "\n");

		textArea.append("Valor de la bandera N: " + cpu.N + "\n");
		textArea.append("Valor de la bandera Z: " + cpu.Z + "\n");
	}

	private void MostrarMem() {
		String s = "";

		for (int i = 0; i < CPU.mem.data.length; i++) {

			if (i % 64 == 0 && i != 0) {
				s += CPU.mem.data[i] + "\n";
			} else if (CPU.mem.data[i] != 0) {
				s += "|" + CPU.mem.data[i] + "|";
			} else {
				s += CPU.mem.data[i];
			}

		}
		textArea.append("\n" + s.replace("||", "|"));
	}

	private void EjecutarComando(String valoresComas) {
		boolean existe = false;
		StringTokenizer st = new StringTokenizer(valoresComas, ",");
		String comando = "";
		String valores[] = new String[st.countTokens()];

		int i = 0;
		while (st.hasMoreTokens()) { // No funciona con un For
			valores[i] = st.nextToken();
			i++;
		}

		comando = valores[0];
		comando = comando.trim().toUpperCase();
		for (OPCODES_ENUM ins : OPCODES_ENUM.values()) {

			if (comando.equals(ins.toString())) {
				existe = true;
			}
		}
		int opcode = Comando_Opcode.cambiarAOpcode(comando);
		int cont = 1;
		if (existe && opcode != -1) {
			try {
				cpu.reset(0xFF00, CPU.mem);

				CPU.mem.data[0xFF01] = opcode;
				CPU.mem.data[0xFF02] = Integer.parseInt(valores[cont]);
				cont++;

				cpu.execute(2, CPU.mem);
				MostrarPorConsola();
			} catch (Exception e) {
				textArea.append("VALORES NO VALIDOS: \"" + valores[cont].trim() + "\"\n");
			}
		} else {
			textArea.append("COMANDO NO ENCONTRADO: \"" + comando + "\"\n");
		}

	}

	public Visual_CPU6502() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				scrollPane.setBounds(0, 0, getWidth(), getHeight());
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

		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String texto = "";
				int cortr = textArea.getText().lastIndexOf("Usuario > ") + 10;
				String valoresComas = "";
				command = textArea.getText().substring(cortr).trim();
				valoresComas = "";
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (command.equals("EXIT")) {
						dispose();
					} else if (command.equals("SHOW")) {
						MostrarMem();
					} else if (textArea.getText().substring(cortr).trim().isEmpty()) {
						textArea.setText(textArea.getText() + "Usuario > ");
					} else {
						valoresComas += textArea.getText().substring(cortr, textArea.getText().lastIndexOf(","));
						valoresComas += textArea.getText().substring(cortr + valoresComas.length()).trim();
						EjecutarComando(valoresComas);
						textArea.append("Usuario > ");

						historial.add(command.trim());
						n_comando = 0;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {

					try {
						texto = textArea.getText();
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

			}
		});

		textArea.setFont(new Font("Consolas", Font.BOLD, 14));
		scrollPane.setViewportView(textArea);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);

	}
}
