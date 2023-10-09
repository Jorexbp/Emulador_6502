package LOGICAL_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AND_IM_ZP_ZPX {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_AND_IM.opcodeValue, 2 }, { OPCODES.INS_AND_ZP.opcodeValue, 3 },
				{ OPCODES.INS_AND_ZPX.opcodeValue, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC, mem);
		CPU.A = 0x64;
		CPU.X = 0x02;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0x02;
		CPU.mem.data[0x02] = 0x0E;
		CPU.mem.data[0x04] = 0x0E;

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODE == OPCODES.INS_AND_IM.opcodeValue) {
			assertEquals(CPU.A, 0x00);
			assertEquals(cpu.Z, true);
			assertEquals(cpu.N, false);

		} else if (OPCODE == OPCODES.INS_AND_ZP.opcodeValue) {
			assertEquals(CPU.A, 0x04);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);

		} else if (OPCODE == OPCODES.INS_AND_ZPX.opcodeValue) {
			assertEquals(CPU.A, 0x04);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);

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
