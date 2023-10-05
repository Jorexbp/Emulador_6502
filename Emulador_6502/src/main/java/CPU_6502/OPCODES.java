package CPU_6502;

public enum OPCODES {

	// LDA OPCODES
	INS_LDA_IM(0xA9), INS_LDA_ZP(0xA5), INS_LDA_ZX(0xB5), INS_LDA_AB(0xAD), INS_LDA_AX(0xBD), INS_LDA_AY(0xB9),
	INS_LDA_IX(0xA1), INS_LDA_IY(0xB1),

	// LDX OPCODES
	INS_LDX_IM(0xA2), INS_LDX_ZP(0xA6), INS_LDX_ZPY(0xB6), INS_LDX_AB(0xAE), INS_LDX_ABY(0xBE),

	// LDY OPCODES
	INS_LDY_IM(0xA0), INS_LDY_ZP(0xA4), INS_LDY_ZPX(0xB4), INS_LDY_AB(0xAC), INS_LDY_ABX(0xBC),

	// STA OPCODES
	INS_STA_ZP(0x85), INS_STA_ZPX(0x95), INS_STA_AB(0x8D), INS_STA_ABX(0x9D), INS_STA_ABY(0x99), INS_STA_INX(0x81),
	INS_STA_INY(0x91),

	// STX OPCODES
	INS_STX_ZP(0x86), INS_STX_ZPY(0x96), INS_STX_AB(0x8E),

	// STX OPCODES
	INS_STY_ZP(0x84), INS_STY_ZPX(0x94), INS_STY_AB(0x8C),

	// TAX OPCODE
	INS_TAX_IP(0xAA),

	// TAY OPCODE
	INS_TAY_IP(0xA8),

	// TXA OPCODE
	INS_TXA_IP(0x8A),

	// TYA OPCODE
	INS_TYA_IP(0x98),

	INS_JSR(0x20), INS_RTS(0x60),

	INS_JMP_AB(0x4C), INS_JMP_IN(0x6C);

	public final int opcodeValue;

	private OPCODES(int opVal) {
		this.opcodeValue = opVal;
	}

	public static OPCODES getOPCODE(int val) {
		for (OPCODES country : OPCODES.values()) {
			if (country.opcodeValue == val) {
				return country;
			}
		}
		throw new IllegalArgumentException();
	}
}
