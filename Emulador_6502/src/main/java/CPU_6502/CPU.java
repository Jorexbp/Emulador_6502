package CPU_6502;

import java.io.IOException;

public class CPU {
	// byte[] Dbyte = new byte[8];
//	byte[] DWord = new byte[16];

	public int PC; // Program counter
	public int SP; // Stack pointer

	public static int A;
	public static int X; // Registros
	public static int Y;

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
	public final static int INS_LDX_ZP = 0xA6;
	public final static int INS_LDX_ZPY = 0xB6;
	public final static int INS_LDX_AB = 0xAE;
	public final static int INS_LDX_ABY = 0xBE;

	public final static int INS_LDY_IM = 0xA0;
	public final static int INS_LDY_ZP = 0xA4;
	public final static int INS_LDY_ZPX = 0xB4;
	public final static int INS_LDY_AB = 0xAC;
	public final static int INS_LDY_ABX = 0xBC;

	public final static int INS_JSR = 0x20;

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
		N = (A & 0b01000000) > 0;
	}

	private void LDXSetStatus() {
		Z = (X == 0);
		N = (X & 0b01000000) > 0;
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

				break;
			}
			case INS_LDA_ZP: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				if (ZeroPageAddr >= 256) {
					ZeroPageAddr -= 256;
				}

				A = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDASetStatus();
				break;
			}
			case INS_LDA_ZX: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += X;

				if (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}
				CPU.ciclos--;
				A = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDASetStatus();
				break;
			}
			case INS_LDA_AB: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);

				A = readByte(CPU.ciclos, addrAbs, CPU.mem);
				LDASetStatus();
				break;
			}
			case INS_LDA_AX: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);

				int effAddrAbsX = addrAbs + X;
				A = readByte(CPU.ciclos, effAddrAbsX, CPU.mem);
				if (effAddrAbsX - addrAbs >= 0xFF) {
					CPU.ciclos--;
				}
				LDASetStatus();
				break;
			}
			case INS_LDA_AY: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				int effAddrAbsY = addrAbs + Y;
				A = readByte(CPU.ciclos, effAddrAbsY, CPU.mem);

				if (effAddrAbsY - addrAbs >= 0xFF) {
					CPU.ciclos--;
				}
				LDASetStatus();
				break;
			}
			case INS_LDA_IX: {
				int addrIX = FetchByte(CPU.ciclos, CPU.mem);
				addrIX += X;
				while (addrIX >= 256) {
					addrIX -= 256;
				}
				CPU.ciclos--;
				int effAddr = readWord(CPU.ciclos, addrIX, CPU.mem);
				A = readByte(CPU.ciclos, effAddr, CPU.mem);
				LDASetStatus();
				break;
			}
			case INS_LDA_IY: {
				int addrIY = FetchByte(CPU.ciclos, CPU.mem);
				while (addrIY >= 256) {
					addrIY -= 256;
				} // F0
				int effAddr = readWord(CPU.ciclos, addrIY, CPU.mem);
				effAddr += Y;
				
				A = readByte(CPU.ciclos, effAddr, CPU.mem);

				LDASetStatus();
				break;
			}
			case INS_LDX_IM: {
				int val = FetchByte(CPU.ciclos, CPU.mem);
				X = val;
				LDXSetStatus();
				break;
			}
			case INS_LDX_ZP: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				if (ZeroPageAddr >= 256) {
					ZeroPageAddr -= 256;
				}

				X = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDXSetStatus();
				break;
			}
			case INS_LDX_ZPY: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += Y;
				if (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}
				CPU.ciclos--;
				X = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDXSetStatus();
				break;
			}
			case INS_LDX_AB: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				X = readByte(CPU.ciclos, addrAbs, CPU.mem);
				LDXSetStatus();
				break;
			}
			case INS_LDX_ABY: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				int effAddrAbsX = addrAbs + Y;
				X = readByte(CPU.ciclos, effAddrAbsX, CPU.mem);
				if (effAddrAbsX - addrAbs >= 0xFF) {
					CPU.ciclos--;
				}
				LDXSetStatus();
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

	public int readWord(int ciclos, int addr, Mem mem) {
		int loByte = readByte(CPU.ciclos, addr, CPU.mem);
		int hiByte = readByte(CPU.ciclos, addr + 1, CPU.mem);
		return loByte | (hiByte << 8);
	}

	public int leerByte(int dir) {
		return CPU.mem.data[dir];
	}

	public static void main(String[] args) throws IOException {
//		Mem mem = new Mem();
//		CPU cpu = new CPU();
//		cpu.reset(mem);
//
//		CPU.mem.data[0xFFFC] = INS_JSR;
//		CPU.mem.data[0xFFFD] = 0x42;
//		CPU.mem.data[0xFFFE] = 0x42;
//		CPU.mem.data[0x4242] = INS_LDA_IM;
//		CPU.mem.data[0x4243] = 0x84;
//
//		cpu.execute(9, mem);
//		// 31:27 s vid
//
//		String s = "";
//
//		for (int i = 0; i < CPU.mem.data.length; i++) {
//
//			if (i % 64 == 0 && i != 0) {
//				s += CPU.mem.data[i] + "\n";
//			} else if (CPU.mem.data[i] != 0) {
//				s += "|" + CPU.mem.data[i] + "|";
//			} else {
//				s += CPU.mem.data[i];
//			}
//
//		}
//		System.out.println(s.replace("||", "|"));

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