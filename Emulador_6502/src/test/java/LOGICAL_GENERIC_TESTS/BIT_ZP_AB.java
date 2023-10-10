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
public class BIT_ZP_AB {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_BIT_ZP.opcodeValue, 3 },
				{ OPCODES.INS_BIT_AB.opcodeValue, 4 } };

		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC, mem);
		CPU.A = 0x33;

		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF42;
		CPU.mem.data[0x42] = 0xCC;
		
		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODE == OPCODES.INS_BIT_ZP.opcodeValue) {
			assertEquals(CPU.A, 0x33);
			assertEquals(cpu.Z, false); // err
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_BIT_AB.opcodeValue) {
			assertEquals(CPU.A, 0x33); // 11001100 - 204
			assertEquals(cpu.Z, false); // & = 0
			assertEquals(cpu.N, false);// 7b -1

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
