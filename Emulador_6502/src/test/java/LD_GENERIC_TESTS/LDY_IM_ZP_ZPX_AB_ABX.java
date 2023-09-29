package LD_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LDY_IM_ZP_ZPX_AB_ABX {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { CPU_6502.OPCODES.INS_LDY_IM, 2 }, { CPU_6502.OPCODES.INS_LDY_ZP, 3 }, { CPU_6502.OPCODES.INS_LDY_ZPX, 4 },
				{ CPU_6502.OPCODES.INS_LDY_AB, 4 }, { CPU_6502.OPCODES.INS_LDY_ABX, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC,mem);
		CPU.X = 0x02;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF42; // IM
		CPU.mem.data[0x42] = 0x37; // ZP
		CPU.mem.data[0x44] = 0x06; // ZPX
		CPU.mem.data[0xFF42] = 0X42; // AB
		CPU.mem.data[0xFF44] = 0X10; // AB

		int ciclosUsados = cpu.execute(2, mem);

		if (CPU_6502.OPCODES.INS_LDY_IM == OPCODE) {
			assertEquals(CPU.Y, 0x42);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDY_ZP == OPCODE) {
			assertEquals(CPU.Y, 0x37);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDY_ZPX == OPCODE) {
			assertEquals(CPU.Y, 0x06);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDY_AB == OPCODE) {
			assertEquals(CPU.Y, 0x42);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);

		} else if (CPU_6502.OPCODES.INS_LDY_ABX == OPCODE) {
			assertEquals(CPU.Y, 0x10);
			assertEquals(cpu.N, false);
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
