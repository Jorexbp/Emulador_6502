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

		case "INS_TSX_IM":
			opcode = 0xBA;
			break;
		case "INS_TXS_IM":
			opcode = 0x9A;
			break;
		case "INS_PHA_IM":
			opcode = 0x48;
			break;
		case "INS_PHP_IM":
			opcode = 0x08;
			break;
		case "INS_PLA_IM":
			opcode = 0x68;
			break;
		case "INS_PLP_IM":
			opcode = 0x28;
			break;
		// and
		case "INS_AND_IM":
			opcode = 0x29;
			break;
		case "INS_AND_ZP":
			opcode = 0x25;
			break;
		case "INS_AND_ZPX":
			opcode = 0x35;
			break;
		case "INS_AND_AB":
			opcode = 0x2D;
			break;
		case "INS_AND_ABX":
			opcode = 0x3D;
			break;
		case "INS_AND_ABY":
			opcode = 0x39;
			break;
		case "INS_AND_INX":
			opcode = 0x21;
			break;
		case "INS_AND_INY":
			opcode = 0x31;
			break;
		// EOR
		case "INS_EOR_IM":
			opcode = 0x49;
			break;
		case "INS_EOR_ZP":
			opcode = 0x45;
			break;
		case "INS_EOR_ZPX":
			opcode = 0x55;
			break;
		case "INS_EOR_AB":
			opcode = 0x4D;
			break;
		case "INS_EOR_ABX":
			opcode = 0x5D;
			break;
		case "INS_EOR_ABY":
			opcode = 0x59;
			break;
		case "INS_EOR_INX":
			opcode = 0x41;
			break;
		case "INS_EOR_INY":
			opcode = 0x51;
			break;
		// OR
		case "INS_ORA_IM":
			opcode = 0x09;
			break;
		case "INS_ORA_ZP":
			opcode = 0x05;
			break;
		case "INS_ORA_ZPX":
			opcode = 0x15;
			break;
		case "INS_ORA_AB":
			opcode = 0x0D;
			break;
		case "INS_ORA_ABX":
			opcode = 0x1D;
			break;
		case "INS_ORA_ABY":
			opcode = 0x19;
			break;
		case "INS_ORA_INX":
			opcode = 0x01;
			break;
		case "INS_ORA_INY":
			opcode = 0x11;
			break;
		// BIT
		case "INS_BIT_ZP":
			opcode = 0x24;
			break;
		case "INS_BIT_AB":
			opcode = 0x2C;
			break;
		// FLAGS
		case "INS_CLC_IM":
			opcode = 0x18;
			break;
		case "INS_CLD_IM":
			opcode = 0xD8;
			break;
		case "INS_CLI_IM":
			opcode = 0x58;
			break;
		case "INS_CLV_IM":
			opcode = 0xB8;
			break;
		case "INS_SEC_IM":
			opcode = 0x38;
			break;
		case "INS_SED_IM":
			opcode = 0xF8;
			break;
		case "INS_SEI_IM":
			opcode = 0x78;
			break;
		// ARIT
		// ADC
		case "INS_ADC_IM":
			opcode = 0x69;
			break;
		case "INS_ADC_ZP":
			opcode = 0x65;
			break;
		case "INS_ADC_ZPX":
			opcode = 0x75;
			break;
		case "INS_ADC_AB":
			opcode = 0x6D;
			break;
		case "INS_ADC_ABX":
			opcode = 0x7D;
			break;
		case "INS_ADC_ABY":
			opcode = 0x79;
			break;
		case "INS_ADC_INX":
			opcode = 0x61;
			break;
		case "INS_ADC_INY":
			opcode = 0x71;
			break;
// SBC
		case "INS_SBC_IM":
			opcode = 0xE9;
			break;
		case "INS_SBC_ZP":
			opcode = 0xE5;
			break;
		case "INS_SBC_ZPX":
			opcode = 0xF5;
			break;
		case "INS_SBC_AB":
			opcode = 0xED;
			break;
		case "INS_SBC_ABX":
			opcode = 0xFD;
			break;
		case "INS_SBC_ABY":
			opcode = 0xF9;
			break;
		case "INS_SBC_INX":
			opcode = 0xE1;
			break;
		case "INS_SBC_INY":
			opcode = 0xF1;
			break;
		// CMP
		case "INS_CMP_IM":
			opcode = 0xC9;
			break;
		case "INS_CMP_ZP":
			opcode = 0xC5;
			break;
		case "INS_CMP_ZPX":
			opcode = 0xD5;
			break;
		case "INS_CMP_AB":
			opcode = 0xCD;
			break;
		case "INS_CMP_ABX":
			opcode = 0xDD;
			break;
		case "INS_CMP_ABY":
			opcode = 0xD9;
			break;
		case "INS_CMP_INX":
			opcode = 0xC1;
			break;
		case "INS_CMP_INY":
			opcode = 0xD1;
			break;
		// CPX
		case "INS_CPX_IM":
			opcode = 0xE0;
			break;
		case "INS_CPX_ZP":
			opcode = 0xE4;
			break;
		case "INS_CPX_AB":
			opcode = 0xEC;
			break;
		// CPY
		case "INS_CPY_IM":
			opcode = 0xC0;
			break;
		case "INS_CPY_ZP":
			opcode = 0xC4;
			break;
		case "INS_CPY_AB":
			opcode = 0xCC;
			break;
		// INC & DEC
		// INC
		case "INS_INC_ZP":
			opcode = 0xE6;
			break;
		case "INS_INC_ZPX":
			opcode = 0xF6;
			break;
		case "INS_INC_AB":
			opcode = 0xEE;
			break;
		case "INS_INC_ABX":
			opcode = 0xFE;
			break;
		// INX
		case "INS_INX_IP":
			opcode = 0xE8;
			break;
		// INY
		case "INS_INY_IP":
			opcode = 0xC8;
			break;
		// DEC
		case "INS_DEC_ZP":
			opcode = 0xC6;
			break;
		case "INS_DEC_ZPX":
			opcode = 0xD6;
			break;
		case "INS_DEC_AB":
			opcode = 0xCE;
			break;
		case "INS_DEC_ABX":
			opcode = 0xDE;
			break;
		// DEX
		case "INS_DEX_IP":
			opcode = 0xCA;
			break;
		// DEY
		case "INS_DEY_IP":
			opcode = 0x88;
			break;

		default:
			opcode = -1;
			break;
		}

		return opcode;
	}
}
