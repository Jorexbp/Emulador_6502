package CPU_6502;

public class Comando_Opcode {

	public static int cambiarAOpcode(String comando) {
		int opcode;
		switch (comando) {
		case "INS_LDA_IM":
			opcode = 0xA9;
			break;
		case "INS_LDA_ZP":
			opcode = 0xA5;
			break;
		case "INS_LDA_ZX":
			opcode = 0xB5;
			break;
		case "INS_LDA_AB":
			opcode = 0xAD;
			break;
		case "INS_LDA_AX":
			opcode = 0xBD;
			break;
		case "INS_LDA_AY":
			opcode = 0xB9;
			break;
		case "INS_LDA_IX":
			opcode = 0xA1;
			break;
		case "INS_LDA_IY":
			opcode = 0xB1;
			break;
		case "INS_LDX_IM":
			opcode = 0xA2;
			break;

		case "INS_LDX_ZP":
			opcode = 0xA6;
			break;

		case "INS_LDX_ZPY":
			opcode = 0xB6;
			break;

		case "INS_LDX_AB":
			opcode = 0xAE;
			break;

		case "INS_LDX_ABY":
			opcode = 0xBE;
			break;

		case "INS_LDY_IM":
			opcode = 0xA0;
			break;
		case "INS_LDY_ZP":
			opcode = 0xA4;
			break;
		case "INS_LDY_ZPX":
			opcode = 0xB4;
			break;
		case "INS_LDY_AB":
			opcode = 0xAC;
			break;
		case "INS_LDY_ABX":
			opcode = 0xBC;
			break;

		case "INS_STA_ZP":
			opcode = 0x85;
			break;
		case "INS_STA_ZPX":
			opcode = 0x95;
			break;
		case "INS_STA_AB":
			opcode = 0x8D;
			break;
		case "INS_STA_ABX":
			opcode = 0x9D;
			break;
		case "INS_STA_ABY":
			opcode = 0x99;
			break;
		case "INS_STA_INX":
			opcode = 0x81;
			break;
		case "INS_STA_INY":
			opcode = 0x91;
			break;

		case "INS_STX_ZP":
			opcode = 0x86;
			break;
		case "INS_STX_ZPY":
			opcode = 0x96;
			break;
		case "INS_STX_AB":
			opcode = 0x8E;
			break;

		case "INS_STY_ZP":
			opcode = 0x84;
			break;
		case "INS_STY_ZPX":
			opcode = 0x94;
			break;
		case "INS_STY_AB":
			opcode = 0x8C;
			break;

		case "INS_TAX_IP":
			opcode = 0xAA;
			break;
		case "INS_TAY_IP":
			opcode = 0xA8;
			break;
		case "INS_TXA_IP":
			opcode = 0x8A;
			break;
		case "INS_TYA_IP":
			opcode = 0x98;
			break;

		case "INS_JSR":
			opcode = 0x20;
			break;
		case "INS_RTS":
			opcode = 0x60;
			break;
		case "INS_JMP_AB":
			opcode = 0x4C;
			break;

		case "INS_JMP_IN":
			opcode = 0x6C;
			break;

		default:
			opcode = -1;
			break;
		}

		return opcode;
	}
}
