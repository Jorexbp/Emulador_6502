package ST_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class STX_ZP_ZPY_AB {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_STX_ZP.opcodeValue, 3 },
				{ OPCODES.INS_STX_ZPY.opcodeValue, 4 }, { OPCODES.INS_STX_AB.opcodeValue, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC, mem);
		CPU.X = 0x02; // Valor a comprobar en la mem correspondiente
		CPU.Y = 0x04;
		CPU copiaCPU = cpu;

		// COMPROBAR QUE NO TOQUE LAS FLAGS NO NECESARIAS

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF42; // ZP byte = 0x42
		CPU.mem.data[0x0046] = 0x10; // ZPY byte = 0x42 + 0x04
		CPU.mem.data[0xFF42] = 0x00; // AB Word = 0xFF42

		int ciclosUsados = cpu.execute(2, mem);

		if (OPCODES.INS_STX_ZP.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x42], 0x02);

		} else if (OPCODES.INS_STX_ZPY.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x46], 0x02);

		} else if (OPCODES.INS_STX_AB.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0xFF42], 0x02);

		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.N, false);
		assertEquals(cpu.Z, false);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
