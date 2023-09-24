package CPU_6502;

import java.io.IOException;

public class CPU {
	// byte[] Dbyte = new byte[8];
//	byte[] DWord = new byte[16];

	public int PC; // Program counter
	public int SP; // Stack pointer

	public int A;
	public int X; // Registros
	public int Y;

	public static int ciclos;

	public static Mem mem;

	// status flags

	public int D, C, I, B, V;
	public boolean N;
	public boolean Z;

	// opcodes
	public final static int INS_LDA_IM = 0xA9;
	public final static int INS_LDA_ZP = 0xA5;
	public final static int INS_LDA_ZX = 0xB5;
	public final static int INS_LDA_AB = 0xAD;
	public final static int INS_LDA_AX = 0xBD;
	public final static int INS_LDA_AY = 0xB9;
	public final static int INS_LDA_IX = 0xA1;
	public final static int INS_LDA_IY = 0xB1;

	public final static int INS_LDX_IM = 0xA2;

	public final static int INS_JSR = 0x0;

	public CPU() {
		Mem mem = new Mem();
		CPU.mem = mem;
	}

	public void reset(CPU.Mem mem2) {
		PC = 0xFFFC;
		SP = 0x0100;
		D = 0;
		A = 0;
		X = 0;
		Y = 0;
		C = 0;
		Z = false;
		I = 0;
		B = 0;
		V = 0;
		N = false;
		CPU.mem = mem2;
	}

	private void LDASetStatus() {
		Z = (A == 0);
		N = (A & 0b10000000) > 0;
	}

	private void LDXSetStatus() {
		Z = (X == 0);
		N = (X & 0b10000000) > 0;
	}

	public int execute(int ciclos, Mem mem) {
		CPU.ciclos = ciclos;
		CPU.mem = mem;
		final int ciclosPedidos = CPU.ciclos;
		while (CPU.ciclos > 0) {

			int ins = FetchByte(CPU.ciclos, CPU.mem);

			switch (ins) {

			case INS_LDA_IM: {

				int val = FetchByte(CPU.ciclos, CPU.mem);
				A = val;
				LDASetStatus();

				// System.out.println(A);
				break;
			}
			case INS_LDA_ZP: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				if(ZeroPageAddr>=256) {
					ZeroPageAddr-=256;
				}
				
				A = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				System.out.println(A);
				LDASetStatus();
				break;
			}
			case INS_LDA_ZX: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += X;
				if(ZeroPageAddr>256) {
					ZeroPageAddr-=256;
				}
				System.out.println(ZeroPageAddr);
				CPU.ciclos--;
				A = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDASetStatus();
				break;
			}
			case INS_JSR: {
				int JSRaddr = FetchWord(CPU.ciclos, CPU.mem);
				writeWord(PC - 1, SP, CPU.ciclos);
				SP++;
				PC = JSRaddr;
				CPU.ciclos--;
				break;
			}
			case INS_LDX_IM: {
				int val = FetchByte(CPU.ciclos, CPU.mem);
				X = val;
				LDXSetStatus();
			}
			default:
				System.out.println("Instruccion no especificada");
				break;
			}
			

		}
		return ciclosPedidos - CPU.ciclos;
	}

	public int FetchWord(int ciclos, Mem mem) {
		int data = CPU.mem.data[PC];
		PC++;

		// |= or/equal
		// el << es para mover bits (bitshifting)
		data |= (CPU.mem.data[PC] << 8);
		PC++;
		CPU.ciclos -= 2;
		// mi pc es little.endian como el 6502
		return data;
	}

	public int FetchByte(int ciclos, Mem mem) {
		int data = CPU.mem.data[PC];
		PC++;
		CPU.ciclos--;
		return data;
	}

	public void writeWord(int data, int addr, int ciclos) {
		CPU.mem.data[addr] = data & 0xFF;
		CPU.mem.data[addr + 1] = (data >> 8);
		CPU.ciclos -= 2;
	}

	public int readByte(int ciclos, int addr, Mem mem) {
		int data = CPU.mem.data[addr];
		CPU.ciclos--;
		return data;
	}

	public int leerByte(int dir) {
		return CPU.mem.data[dir];
	}

	public static void main(String[] args) throws IOException {
		Mem mem = new Mem();
		CPU cpu = new CPU();
		cpu.reset(mem);

		CPU.mem.data[0xFFFC] = INS_JSR;
		CPU.mem.data[0xFFFD] = 0x42;
		CPU.mem.data[0xFFFE] = 0x42;
		CPU.mem.data[0x4242] = INS_LDA_IM;
		CPU.mem.data[0x4243] = 0x84;

		cpu.execute(9, mem);
		// 31:27 s vid

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
		System.out.println(s.replace("||", "|"));

		// System.out.println(cpu.leerByte(0x01));
		// System.out.println(cpu.mem[0x01]);
		// System.out.println(CPU.mem.data.length);
	}

	public static class Mem {
		final static int MAX_MEM = 1024 * 64;
		public int data[] = new int[Mem.MAX_MEM];

		public Mem() {
			for (int i = 0; i < MAX_MEM; i++) {
				data[i] = 0;
			}
		}
	}

}
