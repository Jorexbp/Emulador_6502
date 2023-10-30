package ARIT_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ADC_AB_ABX_IM_ZP_ZPX_INX_INY {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_ADC_AB.opcodeValue, 4 },{ OPCODES.INS_ADC_ABX.opcodeValue, 4 },{ OPCODES.INS_ADC_ABY.opcodeValue, 4 }, { OPCODES.INS_ADC_IM.opcodeValue, 2 },
				{ OPCODES.INS_ADC_ZP.opcodeValue, 3 }, { OPCODES.INS_ADC_ZPX.opcodeValue, 4 }, { OPCODES.INS_ADC_INX.opcodeValue, 6 }, { OPCODES.INS_ADC_INY.opcodeValue, 6 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU.A = 0x7e; // 126
		CPU.X = 0x01;
		CPU.Y = 0x02;
		cpu.C = false;
		cpu.Z = true;
		cpu.N = false;
		cpu.V = false;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0X00;
		CPU.mem.data[0xFF02] = 0x80;
		CPU.mem.data[0x8000] = 0x01; // Operando
		CPU.mem.data[0x00] = 0x10; // Operando
		CPU.mem.data[0x8001] = 0x12; // Operando
		CPU.mem.data[0x8002] = 0x12; // Operando
			
		CPU.mem.data[0x01] = 0x12; // Operando
		CPU.mem.data[0x12] = 0x01; // Operando
		
		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_ADC_AB.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 127);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_ADC_IM.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 126);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_ADC_ZP.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 142);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_ADC_ZPX.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 144);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_ADC_ABX.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 144);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		}else if (OPCODES.INS_ADC_ABY.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 144);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		}else if (OPCODES.INS_ADC_INX.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 127);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		}else if (OPCODES.INS_ADC_INX.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 127);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
