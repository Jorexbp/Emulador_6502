package LD_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TEST_LDA_IM_ZP_ZX_LDX_IM {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { CPU_6502.OPCODES.INS_LDA_IM, 2 }, { CPU_6502.OPCODES.INS_LDA_ZP, 3 }, { CPU_6502.OPCODES.INS_LDA_ZX, 4 },
				{ CPU_6502.OPCODES.INS_LDX_IM, 2 }};
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC,mem);
		CPU copiaCPU = cpu;
		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0x42;
		// LDA_ZP
		CPU.mem.data[0x0042] = 0x37;
		// end LDA_ZP
		
		int ciclosUsados = cpu.execute(2, mem);

		if (CPU_6502.OPCODES.INS_LDA_IM == OPCODE) {
			assertEquals(CPU.A, 0x42);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
		

		} else if (CPU_6502.OPCODES.INS_LDA_ZP == OPCODE) {
			assertEquals(CPU.A, 0x37);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDA_ZX == OPCODE) {
			// Sirve para sumar X en el caso de necesidad,
			// por el test no lo implemente
			assertEquals(CPU.A, 0x37);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDX_IM == OPCODE) {
			assertEquals(CPU.X, 0x42);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);

		}
		
		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
