package CPU_6502;

import java.io.IOException;

public class CPU {
	// byte[] Dbyte = new byte[8];
	// byte[] DWord = new byte[16];

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

	public CPU() {
		Mem mem = new Mem();
		CPU.mem = mem;
	}

	public void reset(int VectorReset, CPU.Mem mem2) {
		PC = VectorReset;
		SP = 0xFF;
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

	private void LDYSetStatus() {
		Z = (Y == 0);
		N = (Y & 0b01000000) > 0;
	}

	public int execute(int ciclos, Mem mem) {
		CPU.ciclos = ciclos;
		CPU.mem = mem;
		final int ciclosPedidos = CPU.ciclos;
		while (CPU.ciclos > 0) {

			int ins = FetchByte(CPU.ciclos, CPU.mem);
			switch (OPCODES.getOPCODE(ins)) {

			case INS_LDA_IM: {

				int val = FetchByte(CPU.ciclos, CPU.mem);

				while (val >= 256) {
					val -= 256;
				}
				// System.out.println(val);
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
				while (val >= 256) {
					val -= 256;
				}
				X = val;
				LDXSetStatus();
				break;
			}
			case INS_LDX_ZP: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				while (ZeroPageAddr >= 256) {
					ZeroPageAddr -= 256;
				}
				X = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDXSetStatus();
				break;
			}
			case INS_LDX_ZPY: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += Y;
				while (ZeroPageAddr > 256) {
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
			case INS_LDY_IM: {
				int val = FetchByte(CPU.ciclos, CPU.mem);
				while (val >= 256) {
					val -= 256;
				}
				Y = val;
				LDYSetStatus();
				break;
			}
			case INS_LDY_ZP: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				while (ZeroPageAddr >= 256) {
					ZeroPageAddr -= 256;
				}
				Y = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDYSetStatus();
				break;
			}
			case INS_LDY_ZPX: {
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += X;
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}
				CPU.ciclos--;
				Y = readByte(CPU.ciclos, ZeroPageAddr, CPU.mem);
				LDYSetStatus();
				break;
			}
			case INS_LDY_AB: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				Y = readByte(CPU.ciclos, addrAbs, CPU.mem);
				LDYSetStatus();
				break;
			}
			case INS_LDY_ABX: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				int effAddrAbsX = addrAbs + X;
				Y = readByte(CPU.ciclos, effAddrAbsX, CPU.mem);
				if (effAddrAbsX - addrAbs >= 0xFF) {
					CPU.ciclos--;
				}
				LDYSetStatus();
				break;
			}
			case INS_STA_ZP: { // 3 c
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}

				writeByte(A, ZeroPageAddr, CPU.ciclos);

				break;
			}
			case INS_STA_ZPX: { // 3 c
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += X;
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}
				CPU.ciclos--;
				writeByte(A, ZeroPageAddr, CPU.ciclos);

				break;
			}
			case INS_STA_AB: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				writeByte(A, addrAbs, CPU.ciclos);
				break;
			}
			case INS_STA_ABX: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				int effAddrAbsX = addrAbs + X;
				CPU.ciclos--;
				writeByte(A, effAddrAbsX, CPU.ciclos);

				if (effAddrAbsX - addrAbs >= 0xFF) {
					CPU.ciclos--;
				}

				break;
			}
			case INS_STA_ABY: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				int effAddrAbsX = addrAbs + Y;
				CPU.ciclos--;
				writeByte(A, effAddrAbsX, CPU.ciclos);

				if (effAddrAbsX - addrAbs >= 0xFF) {
					CPU.ciclos--;
				}

				break;
			}
			case INS_STA_INX: {
				int addrIX = FetchByte(CPU.ciclos, CPU.mem);
				addrIX += X;
				CPU.ciclos--;
				while (addrIX >= 256) {
					addrIX -= 256;
				}

				int effAddr = readWord(CPU.ciclos, addrIX, CPU.mem);
				writeByte(A, effAddr, CPU.ciclos);

				break;
			}
			case INS_STA_INY: {
				int addrIY = FetchByte(CPU.ciclos, CPU.mem);
				while (addrIY >= 256) {
					addrIY -= 256;
				} // F0

				int effAddr = readWord(CPU.ciclos, addrIY, CPU.mem);
				effAddr += Y;
				CPU.ciclos--;

				writeByte(A, effAddr, CPU.ciclos);

				break;
			}
			case INS_STX_ZP: { // 3 c
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}

				writeByte(X, ZeroPageAddr, CPU.ciclos);

				break;
			}
			case INS_STX_ZPY: { // 4 c
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += Y;
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}
				CPU.ciclos--;
				writeByte(X, ZeroPageAddr, CPU.ciclos);
				break;
			}
			case INS_STX_AB: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				writeByte(X, addrAbs, CPU.ciclos);

				break;
			}
			case INS_STY_ZP: { // 3 c
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}

				writeByte(Y, ZeroPageAddr, CPU.ciclos);

				break;
			}
			case INS_STY_ZPX: { // 4 c
				int ZeroPageAddr = FetchByte(CPU.ciclos, CPU.mem);
				ZeroPageAddr += X;
				while (ZeroPageAddr > 256) {
					ZeroPageAddr -= 256;
				}
				CPU.ciclos--;
				writeByte(Y, ZeroPageAddr, CPU.ciclos);
				break;
			}
			case INS_STY_AB: {
				int addrAbs = FetchWord(CPU.ciclos, CPU.mem);
				writeByte(Y, addrAbs, CPU.ciclos);

				break;
			}

			case INS_TAX_IP: {
				X = A;
				CPU.ciclos--;
				LDXSetStatus();
				break;
			}
			case INS_TAY_IP: {
				Y = A;
				CPU.ciclos--;
				LDYSetStatus();
				break;
			}
			case INS_TXA_IP: {
				A = X;
				CPU.ciclos--;
				LDASetStatus();
				break;
			}
			case INS_TYA_IP: {
				A = Y;
				CPU.ciclos--;
				LDASetStatus();
				break;
			}
			case INS_JSR: {
				int JSRaddr = FetchWord(CPU.ciclos, CPU.mem);
				PushPCToStack(CPU.ciclos);

				PC = JSRaddr;
				CPU.ciclos--;
				break;
			}
			case INS_RTS: {

				PC = PopWordFromStack() + 1;
				CPU.ciclos -= 2;
				break;
			}
			case INS_JMP_AB: {
				int addr = FetchWord(CPU.ciclos, CPU.mem);
				PC = addr;

				break;
			}
			case INS_JMP_IN: {
				int addr = FetchWord(CPU.ciclos, CPU.mem);
				addr = readWord(CPU.ciclos, addr, CPU.mem);
				PC = addr;

				break;
			}
			default:

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

	public int PopWordFromStack() {
		int word = readWord(CPU.ciclos, SPToAddr() + 1, CPU.mem);
		SP += 2;
		CPU.ciclos--;
		return word;
	}

	public int SPToAddr() {
		return 0x100 | SP;
	}

	public void PushPCToStack(int ciclos) {
		writeWord(PC - 1, SPToAddr() - 1, CPU.ciclos);
		SP -= 2;
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

	public void writeByte(int data, int addr, int ciclos) {
		CPU.mem.data[addr] = data;
		CPU.ciclos--;
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