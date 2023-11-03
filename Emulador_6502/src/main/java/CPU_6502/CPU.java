package CPU_6502;

import Excepciones.Excepcion_Instruccion;

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

	public int D, I, B, Un;
	public boolean C;
	public boolean V;
	public boolean N;
	public boolean Z;

	// Procesor Status
	public int PS;

	// Suma
	public int resta = 0, suma = 0;

	public CPU() {
		Mem mem = new Mem();
		CPU.mem = mem;
	}

	public void reset(int VectorReset, Mem mem2) {
		PC = VectorReset;
		SP = 0xFF;
		D = 0;
		A = 0;
		X = 0;
		Y = 0;
		C = false;
		Z = false;
		I = 0;
		Un = 0;
		B = 0;
		V = false;
		N = false;
		mem = mem2;
		ProcesorStatus();

	}

	private void LDASetStatus() {
		Z = (A == 0);
		N = (A & 0b1000000) > 0;
		ProcesorStatus();

	}

	private void LDXSetStatus() {
		Z = (X == 0);
		N = (X & 0b1000000) > 0;
		ProcesorStatus();

	}

	private void LDYSetStatus() {
		Z = (Y == 0);
		N = (Y & 0b1000000) > 0;
		ProcesorStatus();

	}

	private void setADCFlags(int oldA) {
		A = (suma & 0xFF);
		Z = (A == 0);
		N = (A & 0b01000000) > 0;
		C = (suma & 0xFF00) > 0;
		V = false;

		// Arreglar esto
		if (((oldA ^ suma) & (A ^ suma) & 0x80) > 0) {
			V = true;
		} else {
			V = false;
		}
		ProcesorStatus();
	}

	private void setSBCFlags(int oldA) {
		A = (resta & 0xFF);
		Z = (A == 0);
		N = (A & 0b01000000) > 0;
		C = (resta & 0xFF00) > 0;
		V = false;

		// Arreglar esto
		if (((oldA ^ resta) & (A ^ resta) & 0x80) > 0) {
			V = true;
		} else {
			V = false;
		}
		ProcesorStatus();
	}

	private void setCMPFlags(int data) {
		N = ((A - data) & 0b1000000) > 0;
		C = A >= data;
		Z = A == data;

	}

	private void setCPXFlags(int data) {
		N = ((X - data) & 0b1000000) > 0;
		C = X >= data;
		Z = X == data;

	}

	private void setCPYFlags(int data) {
		N = ((Y - data) & 0b1000000) > 0;
		C = Y >= data;
		Z = Y == data;

	}

	private void setResultFlags(int data) {
		N = (data & 0b1000000) > 0;
		C = Y >= data;
		Z = 0 == data;

	}

	public void ProcesorStatus() {
		int zbit = (Z == true) ? 1 : 0;
		int nbit = (N == true) ? 1 : 0;
		int vbit = (V == true) ? 1 : 0;
		int cbit = (C == true) ? 1 : 0;

		String bits = String.valueOf(cbit).concat(String.valueOf(zbit)).concat(String.valueOf(I))
				.concat(String.valueOf(D)).concat(String.valueOf(B)).concat(String.valueOf(Un))
				.concat(String.valueOf(vbit)).concat(String.valueOf(nbit));
		PS = Integer.parseInt(bits, 2);
		// System.out.println(bits);
	}

	public int execute(int ciclos, Mem mem) {
		CPU.ciclos = ciclos;
		CPU.mem = mem;
		final int ciclosPedidos = ciclos;
		while (CPU.ciclos > 0) {

			int ins = FetchByte(ciclos, mem);
			try {
				switch (OPCODES.getOPCODE(ins)) {

				case INS_LDA_IM: {

					int val = FetchByte(ciclos, mem);

					while (val >= 256) {
						val -= 256;
					}
					A = val;
					LDASetStatus();

					break;
				}
				case INS_LDA_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					A = readByte(ciclos, ZeroPageAddr, mem);
					LDASetStatus();
					break;
				}
				case INS_LDA_ZX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;

					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					A = readByte(ciclos, ZeroPageAddr, mem);
					LDASetStatus();
					break;
				}
				case INS_LDA_AB: {
					int addrAbs = FetchWord(ciclos, mem);

					A = readByte(ciclos, addrAbs, mem);
					LDASetStatus();
					break;
				}
				case INS_LDA_AX: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsX = addrAbs + X;
					A = readByte(ciclos, effAddrAbsX, mem);
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					LDASetStatus();
					break;
				}
				case INS_LDA_AY: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsY = addrAbs + Y;
					A = readByte(ciclos, effAddrAbsY, mem);

					if (effAddrAbsY - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					LDASetStatus();
					break;
				}
				case INS_LDA_IX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					while (addrIX >= 256) {
						addrIX -= 256;
					}
					CPU.ciclos--;
					int effAddr = readWord(ciclos, addrIX, mem);
					A = readByte(ciclos, effAddr, mem);
					LDASetStatus();
					break;
				}
				case INS_LDA_IY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0
					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;

					A = readByte(ciclos, effAddr, mem);

					LDASetStatus();
					break;
				}
				case INS_LDX_IM: {
					int val = FetchByte(ciclos, mem);
					while (val >= 256) {
						val -= 256;
					}
					X = val;
					LDXSetStatus();
					break;
				}
				case INS_LDX_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					X = readByte(ciclos, ZeroPageAddr, mem);
					LDXSetStatus();
					break;
				}
				case INS_LDX_ZPY: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += Y;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					X = readByte(ciclos, ZeroPageAddr, mem);
					LDXSetStatus();
					break;
				}
				case INS_LDX_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					X = readByte(ciclos, addrAbs, mem);
					LDXSetStatus();
					break;
				}
				case INS_LDX_ABY: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + Y;
					X = readByte(ciclos, effAddrAbsX, mem);
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					LDXSetStatus();
					break;
				}
				case INS_LDY_IM: {
					int val = FetchByte(ciclos, mem);
					while (val >= 256) {
						val -= 256;
					}
					Y = val;
					LDYSetStatus();
					break;
				}
				case INS_LDY_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					Y = readByte(ciclos, ZeroPageAddr, mem);
					LDYSetStatus();
					break;
				}
				case INS_LDY_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					Y = readByte(ciclos, ZeroPageAddr, mem);
					LDYSetStatus();
					break;
				}
				case INS_LDY_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					Y = readByte(ciclos, addrAbs, mem);
					LDYSetStatus();
					break;
				}
				case INS_LDY_ABX: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					Y = readByte(ciclos, effAddrAbsX, mem);
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					LDYSetStatus();
					break;
				}
				case INS_STA_ZP: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}

					writeByte(A, ZeroPageAddr, ciclos);

					break;
				}
				case INS_STA_ZPX: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					writeByte(A, ZeroPageAddr, ciclos);

					break;
				}
				case INS_STA_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					writeByte(A, addrAbs, ciclos);
					break;
				}
				case INS_STA_ABX: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					CPU.ciclos--;
					writeByte(A, effAddrAbsX, ciclos);

					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}

					break;
				}
				case INS_STA_ABY: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + Y;
					CPU.ciclos--;
					writeByte(A, effAddrAbsX, ciclos);

					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}

					break;
				}
				case INS_STA_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					writeByte(A, effAddr, ciclos);

					break;
				}
				case INS_STA_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					writeByte(A, effAddr, ciclos);

					break;
				}
				case INS_STX_ZP: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}

					writeByte(X, ZeroPageAddr, ciclos);

					break;
				}
				case INS_STX_ZPY: { // 4 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += Y;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					writeByte(X, ZeroPageAddr, ciclos);
					break;
				}
				case INS_STX_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					writeByte(X, addrAbs, ciclos);

					break;
				}
				case INS_STY_ZP: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}

					writeByte(Y, ZeroPageAddr, ciclos);

					break;
				}
				case INS_STY_ZPX: { // 4 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					writeByte(Y, ZeroPageAddr, ciclos);
					break;
				}
				case INS_STY_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					writeByte(Y, addrAbs, ciclos);

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
					int JSRaddr = FetchWord(ciclos, mem);
					PushPCToStack(ciclos);

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
					int addr = FetchWord(ciclos, mem);
					PC = addr;

					break;
				}
				case INS_JMP_IN: {
					int addr = FetchWord(ciclos, mem);
					addr = readWord(ciclos, addr, mem);
					PC = addr;

					break;
				}
				case INS_TSX_IM: { // 2 c
					X = SP;
					CPU.ciclos--;

					LDXSetStatus();
					break;
				}
				case INS_TXS_IM: { // 2 c
					SP = X;
					CPU.ciclos--;
					break;
				}
				case INS_PHA_IM: { // 3 c
					PushByteToStack(A);
					break;
				}

				case INS_PLA_IM: { // 4 c
					A = PopWordFromStack();
					LDASetStatus();
					break;
				}
				case INS_PLP_IM: { // 3 c
					PS = PopWordFromStack();
					break;
				}
				case INS_PHP_IM: { // 3 c
					PushByteToStack(PS);
					break;
				}
				case INS_AND_IM: { // 2 c
					A &= FetchByte(ciclos, mem);
					LDASetStatus();

					// 0 true, 1 false
					break;
				}
				case INS_AND_ZP: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					A &= mem.data[ZeroPageAddr];
					LDASetStatus();
					break;
				}
				case INS_AND_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					A &= mem.data[ZeroPageAddr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_AND_AB: {
					int addr = FetchWord(ciclos, mem);
					A &= mem.data[addr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_AND_ABX: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					A &= mem.data[effAddrAbsX];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_AND_ABY: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsY = addrAbs + Y;
					if (effAddrAbsY - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					A &= mem.data[effAddrAbsY];
					LDASetStatus();
					break;
				}
				case INS_AND_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					A &= mem.data[effAddr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_AND_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					A &= mem.data[effAddr];
					CPU.ciclos--;
					LDASetStatus();

					break;
				}
				case INS_EOR_IM: { // 2 c
					int byteValue = FetchByte(ciclos, mem);
					while (byteValue >= 256) {
						byteValue -= 256;
					}
					A ^= byteValue;

					LDASetStatus();
					break;
				}
				case INS_EOR_ZP: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					A ^= mem.data[ZeroPageAddr];
					LDASetStatus();
					break;
				}
				case INS_EOR_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					A ^= mem.data[ZeroPageAddr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_EOR_AB: {
					int addr = FetchWord(ciclos, mem);
					A ^= mem.data[addr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_EOR_ABX: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					A ^= mem.data[effAddrAbsX];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_EOR_ABY: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsY = addrAbs + Y;
					if (effAddrAbsY - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					A ^= mem.data[effAddrAbsY];
					LDASetStatus();
					break;
				}
				case INS_EOR_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					A ^= mem.data[effAddr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_EOR_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					A ^= mem.data[effAddr];
					CPU.ciclos--;
					LDASetStatus();

					break;
				}
				case INS_ORA_IM: { // 2 c
					int byteValue = FetchByte(ciclos, mem);
					while (byteValue >= 256) {
						byteValue -= 256;
					}
					A |= byteValue;

					LDASetStatus();
					break;
				}
				case INS_ORA_ZP: { // 3 c
					int ZeroPageAddr = FetchByte(ciclos, mem);
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					A |= mem.data[ZeroPageAddr];
					LDASetStatus();
					break;
				}
				case INS_ORA_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr > 256) {
						ZeroPageAddr -= 256;
					}
					CPU.ciclos--;
					A |= mem.data[ZeroPageAddr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_ORA_AB: {
					int addr = FetchWord(ciclos, mem);
					A |= mem.data[addr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_ORA_ABX: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					A |= mem.data[effAddrAbsX];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_ORA_ABY: {
					int addrAbs = FetchWord(ciclos, mem);

					int effAddrAbsY = addrAbs + Y;
					if (effAddrAbsY - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					A |= mem.data[effAddrAbsY];
					LDASetStatus();
					break;
				}
				case INS_ORA_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					A |= mem.data[effAddr];
					CPU.ciclos--;
					LDASetStatus();
					break;
				}
				case INS_ORA_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					A |= mem.data[effAddr];
					CPU.ciclos--;
					LDASetStatus();

					break;
				}
				case INS_BIT_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					int valor = readByte(ciclos, ZeroPageAddr, mem);
					Z = (A & valor) != 0;
					// PS |= (valor & 0b1100000);
					V = (valor & 0b01000000) != 0;
					N = (valor & 0b10000000) != 0;
					break;
				}
				case INS_BIT_AB: {
					int addr = FetchWord(ciclos, mem);

					int valor = readByte(ciclos, addr, mem);
					Z = (A & valor) != 0;
					V = (valor & 0b0100000) != 0;
					N = (valor & 0b1000000) != 0;
					break;
				}
				case INS_CLC_IM: {
					C = false;
					CPU.ciclos--;
					break;
				}
				case INS_CLD_IM: {
					D = 0;
					CPU.ciclos--;
					break;
				}
				case INS_CLI_IM: {
					I = 0;
					CPU.ciclos--;
					break;
				}
				case INS_CLV_IM: {
					V = false;
					CPU.ciclos--;
					break;
				}
				case INS_SEC_IM: {
					C = true;
					CPU.ciclos--;
					break;
				}
				case INS_SED_IM: {
					D = 1;
					CPU.ciclos--;
					break;
				}
				case INS_SEI_IM: {
					I = 1;
					CPU.ciclos--;
					break;
				}
				case INS_ADC_IM: {
					int oper = FetchByte(CPU.ciclos, CPU.mem);
					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);
					break;
				}
				case INS_ADC_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					int oper = readByte(ciclos, ZeroPageAddr, mem);

					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);
					break;
				}
				case INS_ADC_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					int oper = readByte(ciclos, ZeroPageAddr, mem);

					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);
					break;
				}
				case INS_ADC_AB: {
					int addr = FetchWord(CPU.ciclos, CPU.mem);
					int oper = readByte(CPU.ciclos, addr, CPU.mem);
					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);

					break;
				}
				case INS_ADC_ABX: {

					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}

					int oper = readByte(CPU.ciclos, effAddrAbsX, CPU.mem);

					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);

					break;
				}
				case INS_ADC_ABY: {

					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsY = addrAbs + Y;
					if (effAddrAbsY - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}

					int oper = readByte(CPU.ciclos, effAddrAbsY, CPU.mem);

					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);

					break;
				}
				case INS_ADC_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					int oper = readByte(CPU.ciclos, effAddr, CPU.mem);

					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);
					break;
				}
				case INS_ADC_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					int oper = readByte(CPU.ciclos, effAddr, CPU.mem);

					int oldA = A;
					suma = A;
					suma += oper;
					suma += C ? 1 : 0;

					setADCFlags(oldA);

					break;
				}
				case INS_SBC_IM: {
					int oper = FetchByte(CPU.ciclos, CPU.mem);
					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);
					break;
				}
				case INS_SBC_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					int oper = readByte(ciclos, ZeroPageAddr, mem);

					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);
					break;
				}
				case INS_SBC_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}
					int oper = readByte(ciclos, ZeroPageAddr, mem);

					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);
					break;
				}
				case INS_SBC_AB: {
					int addr = FetchWord(CPU.ciclos, CPU.mem);
					int oper = readByte(CPU.ciclos, addr, CPU.mem);
					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);

					break;
				}
				case INS_SBC_ABX: {

					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}

					int oper = readByte(CPU.ciclos, effAddrAbsX, CPU.mem);

					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);

					break;
				}
				case INS_SBC_ABY: {

					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsY = addrAbs + Y;
					if (effAddrAbsY - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}

					int oper = readByte(CPU.ciclos, effAddrAbsY, CPU.mem);

					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);

					break;
				}
				case INS_SBC_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					int oper = readByte(CPU.ciclos, effAddr, CPU.mem);

					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);
					break;
				}
				case INS_SBC_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					int oper = readByte(CPU.ciclos, effAddr, CPU.mem);

					int oldA = A;
					resta = A;
					resta -= oper;
					resta -= (1 - (C ? 1 : 0));

					setSBCFlags(oldA);

					break;
				}
				case INS_CMP_IM: {
					int data = FetchByte(CPU.ciclos, CPU.mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					int data = readByte(ciclos, addrAbs, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_ABX: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					int data = readByte(ciclos, addrAbs, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_ABY: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + Y;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					int data = readByte(ciclos, effAddrAbsX, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_INX: {
					int addrIX = FetchByte(ciclos, mem);
					addrIX += X;
					CPU.ciclos--;
					while (addrIX >= 256) {
						addrIX -= 256;
					}

					int effAddr = readWord(ciclos, addrIX, mem);
					int data = readByte(ciclos, effAddr, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CMP_INY: {
					int addrIY = FetchByte(ciclos, mem);
					while (addrIY >= 256) {
						addrIY -= 256;
					} // F0

					int effAddr = readWord(ciclos, addrIY, mem);
					effAddr += Y;
					CPU.ciclos--;

					int data = readByte(ciclos, effAddr, mem);
					setCMPFlags(data);
					break;
				}
				case INS_CPX_IM: {
					int data = FetchByte(CPU.ciclos, CPU.mem);
					setCPXFlags(data);
					break;
				}
				case INS_CPX_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					setCPXFlags(data);
					break;
				}
				case INS_CPX_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					int data = readByte(ciclos, addrAbs, mem);
					setCPXFlags(data);
					break;
				}
				case INS_CPY_IM: {
					int data = FetchByte(CPU.ciclos, CPU.mem);
					setCPYFlags(data);
					break;
				}
				case INS_CPY_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					setCPYFlags(data);
					break;
				}
				case INS_CPY_AB: {
					int addrAbs = FetchWord(ciclos, mem);
					int data = readByte(ciclos, addrAbs, mem);
					setCPYFlags(data);
					break;
				}
				case INS_DEC_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					data--;
					CPU.ciclos--;
					writeByte(data, ZeroPageAddr, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_DEC_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					data--;
					CPU.ciclos--;
					writeByte(data, ZeroPageAddr, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_DEC_AB: {
					int addr = FetchWord(ciclos, mem);

					int data = readByte(ciclos, addr, mem);
					data--;
					CPU.ciclos--;
					writeByte(data, addr, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_DEC_ABX: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					if (effAddrAbsX - addrAbs >= 0xFF) {
						CPU.ciclos--;
					}
					int data = readByte(ciclos, effAddrAbsX, mem);
					data--;
					CPU.ciclos--;
					writeByte(data, effAddrAbsX, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_INX_IP: {
					X++;
					CPU.ciclos--;
					LDXSetStatus();
					break;
				}
				case INS_INY_IP: {
					Y++;
					CPU.ciclos--;
					LDYSetStatus();
					break;
				}
				case INS_DEX_IP: {
					X--;
					CPU.ciclos--;
					LDXSetStatus();
					break;
				}
				case INS_DEY_IP: {
					Y--;
					CPU.ciclos--;
					LDYSetStatus();
					break;
				}
				case INS_INC_ZP: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					data++;
					CPU.ciclos--;
					writeByte(data, ZeroPageAddr, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_INC_ZPX: {
					int ZeroPageAddr = FetchByte(ciclos, mem);
					ZeroPageAddr += X;
					CPU.ciclos--;
					while (ZeroPageAddr >= 256) {
						ZeroPageAddr -= 256;
					}

					int data = readByte(ciclos, ZeroPageAddr, mem);
					data++;
					CPU.ciclos--;
					writeByte(data, ZeroPageAddr, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_INC_AB: {
					int addr = FetchWord(ciclos, mem);

					int data = readByte(ciclos, addr, mem);
					data++;
					CPU.ciclos--;
					writeByte(data, addr, ciclos);
					setResultFlags(data);
					break;
				}
				case INS_INC_ABX: {
					int addrAbs = FetchWord(ciclos, mem);
					int effAddrAbsX = addrAbs + X;
					CPU.ciclos--;
					int data = readByte(ciclos, effAddrAbsX, mem);
					data++;
					CPU.ciclos--;
					writeByte(data, effAddrAbsX, ciclos);
					setResultFlags(data);
					break;
				}
				default:

					break;
				}
			} catch (Excepcion_Instruccion e) {
				e.printStackTrace();
			}

		}
		return ciclosPedidos - CPU.ciclos;
	}

	public int compararValorConByte(int acumulador, int addr) {
		int valorByte = CPU.mem.data[addr];
		boolean compara = (valorByte ^ acumulador) == 0;
		return compara ? 0 : 1;
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
		int word = readWord(CPU.ciclos, SPToAddr() + 1, mem);
		SP += 2;
		CPU.ciclos--;
		return word;
	}

	public int PopByteFromStack() {
		SP++;
		int addr = SPToAddr();
		int value = CPU.mem.data[addr];
		CPU.ciclos -= 3;

		return value;
	}

	public int SPToAddr() {
		return 0x100 | SP;
	}

	public void PushPCToStack(int ciclos) {
		writeWord(PC - 1, SPToAddr() - 1, CPU.ciclos);
		SP -= 2;
	}

	public void PushByteToStack(int value) {
		int addr = SPToAddr();
		CPU.mem.data[addr] = value;
		SP--;
		CPU.ciclos -= 2;
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

	public int CargarPrograma(int[] Prg, int nBytes, Mem mem) {
		int addr = -1;
		if (Prg.length > 0 && nBytes > 2) {
			int At = 0;
			int lo = Prg[At++];
			int hi = Prg[At++] << 8;
			addr = lo | hi;
			for (int i = addr; i < addr + nBytes - 2; i++) {
				CPU.mem.data[i] = Prg[At++];
			}
		}
		return addr;
	}

	public void EstadoPrograma() {
		System.out.println("A: " + A);
		System.out.println("X: " + X);
		System.out.println("Y: " + Y);
		System.out.println("PC: " + PC);
		System.out.println("SP: " + SP);
		System.out.println("Flag D: " + D);
		System.out.println("Flag C: " + C);
		System.out.println("Flag I: " + I);
		System.out.println("Flag B: " + B);
		System.out.println("Flag Un: " + Un);
		System.out.println("Flag V: " + V);
		System.out.println("Flag N: " + N);
		System.out.println("Flag Z: " + Z);
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