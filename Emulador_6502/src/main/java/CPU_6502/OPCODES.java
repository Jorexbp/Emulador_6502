package CPU_6502;

public class OPCODES {
	// opcodes

	// LDA OPCODES
	public final static int INS_LDA_IM = 0xA9;
	public final static int INS_LDA_ZP = 0xA5;
	public final static int INS_LDA_ZX = 0xB5;
	public final static int INS_LDA_AB = 0xAD;
	public final static int INS_LDA_AX = 0xBD;
	public final static int INS_LDA_AY = 0xB9;
	public final static int INS_LDA_IX = 0xA1;
	public final static int INS_LDA_IY = 0xB1;

	// LDX OPCODES
	public final static int INS_LDX_IM = 0xA2;
	public final static int INS_LDX_ZP = 0xA6;
	public final static int INS_LDX_ZPY = 0xB6;
	public final static int INS_LDX_AB = 0xAE;
	public final static int INS_LDX_ABY = 0xBE;

	// LDY OPCODES
	public final static int INS_LDY_IM = 0xA0;
	public final static int INS_LDY_ZP = 0xA4;
	public final static int INS_LDY_ZPX = 0xB4;
	public final static int INS_LDY_AB = 0xAC;
	public final static int INS_LDY_ABX = 0xBC;

	// STA OPCODES
	public final static int INS_STA_ZP = 0x85;
	public final static int INS_STA_ZPX = 0x95;
	public final static int INS_STA_AB = 0x8D;
	public final static int INS_STA_ABX = 0x9D;
	public final static int INS_STA_ABY = 0x99;
	public final static int INS_STA_INX = 0x81;
	public final static int INS_STA_INY = 0x91;

	// STX OPCODES
	public final static int INS_STX_ZP = 0x86;
	public final static int INS_STX_ZPY = 0x96;
	public final static int INS_STX_AB = 0x8E;

	// STX OPCODES
	public final static int INS_STY_ZP = 0x84;
	public final static int INS_STY_ZPX = 0x94;
	public final static int INS_STY_AB = 0x8C;

	// TAX OPCODE
	public final static int INS_TAX_IP = 0xAA;

	// TAY OPCODE
	public final static int INS_TAY_IP = 0xA8;

	// TXA OPCODE
	public final static int INS_TXA_IP = 0x8A;

	// TYA OPCODE
	public final static int INS_TYA_IP = 0x98;

	public final static int INS_JSR = 0x20;
	public final static int INS_RTS = 0x60;
	
	public final static int INS_JMP_AB = 0x4C;
	public final static int INS_JMP_IN = 0x6C;

}
