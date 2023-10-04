package LD_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

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
		Object[][] data = new Object[][] { { OPCODES.INS_LDX_ZP.opcodeValue, 3 }, { OPCODES.INS_LDX_ZPY.opcodeValue, 4 }, { OPCODES.INS_LDX_AB.opcodeValue, 4 },
				{ OPCODES.INS_LDX_ABY.opcodeValue, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC,mem);
		CPU.Y = 0x02;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF42;
		CPU.mem.data[0x42] = 0x37; // LDX_ZP
		CPU.mem.data[0x0044] = 0x06; // LDX_ZPY
		CPU.mem.data[0xFF42] = 0x10; // LDX_AB
		CPU.mem.data[0xFF44] = 0x84; // LDX_ABY

		int ciclosUsados = cpu.execute(2, mem);

		if (OPCODES.INS_LDX_ZP.opcodeValue == OPCODE) {
			assertEquals(CPU.X, 0x37);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDX_ZPY.opcodeValue == OPCODE) {
			assertEquals(CPU.X, 0x06);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDX_AB.opcodeValue == OPCODE) {
			assertEquals(CPU.X, 0x10);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDX_ABY.opcodeValue == OPCODE) {
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
