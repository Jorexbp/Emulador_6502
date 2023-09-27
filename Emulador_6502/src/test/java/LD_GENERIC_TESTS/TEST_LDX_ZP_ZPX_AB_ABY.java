package LD_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TEST_LDX_ZP_ZPX_AB_ABY {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { CPU_6502.OPCODES.INS_LDX_ZP, 3 }, { CPU_6502.OPCODES.INS_LDX_ZPY, 4 }, { CPU_6502.OPCODES.INS_LDX_AB, 4 },
				{ CPU_6502.OPCODES.INS_LDX_ABY, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(mem);
		CPU.Y = 0x02;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF42;
		CPU.mem.data[0x42] = 0x37; // LDX_ZP
		CPU.mem.data[0x0044] = 0x06; // LDX_ZPY
		CPU.mem.data[0xFF42] = 0x10; // LDX_AB
		CPU.mem.data[0xFF44] = 0x84; // LDX_ABY

		int ciclosUsados = cpu.execute(2, mem);

		if (CPU_6502.OPCODES.INS_LDX_ZP == OPCODE) {
			assertEquals(CPU.X, 0x37);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDX_ZPY == OPCODE) {
			assertEquals(CPU.X, 0x06);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDX_AB == OPCODE) {
			assertEquals(CPU.X, 0x10);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDX_ABY == OPCODE) {
			assertEquals(CPU.X, 0x84);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
