package STACK_OPERATIONS_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TSX_TXS_PHA_PLA_PLP {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_TSX_IM.opcodeValue, 2 }, { OPCODES.INS_TXS_IM.opcodeValue, 2 },
				{ OPCODES.INS_PHA_IM.opcodeValue, 3 }, { OPCODES.INS_PLA_IM.opcodeValue, 4 }, { OPCODES.INS_PLP_IM.opcodeValue, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU.A = 0x00; // A.copia -> SP
		CPU.X = 0x10; // X -> SP
		cpu.SP = 0xFE; // SP -> X
cpu.PS = 0;
		cpu.Z = true;
		cpu.N = true;
		CPU copiaCPU = cpu;
		cpu.ProcesorStatus();
		CPU.mem.data[0x01FF] = 0x42;
		CPU.mem.data[0xFF00] = OPCODE;// Setear los valores correspondientes

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODE == OPCODES.INS_TSX_IM.opcodeValue) {
			assertEquals(CPU.X, cpu.SP);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);

		} else if (OPCODE == OPCODES.INS_TXS_IM.opcodeValue) {
			assertEquals(cpu.SP, 0x10);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, true);

		} else if (OPCODE == OPCODES.INS_PHA_IM.opcodeValue) {
			assertEquals(CPU.mem.data[cpu.SPToAddr() + 1], CPU.A);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, true);

		} else if (OPCODE == OPCODES.INS_PLA_IM.opcodeValue) {
			assertEquals(0x42, CPU.A);
			assertEquals(cpu.Z, false);

		}else if (OPCODE == OPCODES.INS_PLP_IM.opcodeValue) {
			assertEquals(0x42, cpu.PS);
		

		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);

		assertEquals(cpu.SP, copiaCPU.SP);
	}

}
