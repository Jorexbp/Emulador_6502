package ST_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class STA_ZP_ZPX_AB_ABX_ABY_INX_INY {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { CPU_6502.OPCODES.INS_STA_ZP, 3 }, { CPU_6502.OPCODES.INS_STA_ZPX, 4 },
				{ CPU_6502.OPCODES.INS_STA_AB, 4 }, { CPU_6502.OPCODES.INS_STA_ABX, 5 },
				{ CPU_6502.OPCODES.INS_STA_ABY, 5 }, { CPU_6502.OPCODES.INS_STA_INX, 6 },
				{ CPU_6502.OPCODES.INS_STA_INY, 6 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC,mem);
		CPU.A = 0x02; // Valor a comprobar en la mem correspondiente
		CPU.X = 0x04;
		CPU.Y = 0x06;
		CPU copiaCPU = cpu;

		// COMPROBAR QUE NO TOQUE LAS FLAGS NO NECESARIAS

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF42; // ZP byte = 0x42
		CPU.mem.data[0x0046] = 0x10; // ZPX byte = 0x42 + 0x04 // INX usa esta dir
		CPU.mem.data[0xFF42] = 0x00; // AB Word = 0xFF42
		CPU.mem.data[0xFF46] = 0x00; // ABX Word = 0xFF42 + 0x04
		CPU.mem.data[0xFF48] = 0x37; // ABY Word = 0xFF42 + 0x06
		CPU.mem.data[0x0010] = 0xA169; // ZPX byte = 0x42 + 0x04
		CPU.mem.data[0x42] = 0x64; // INY Byte 0x64 = 100 + 0x06 = 6A (106)
		CPU.mem.data[0x6A] = 0x00; // INY Byte

		int ciclosUsados = cpu.execute(2, mem);

		if (CPU_6502.OPCODES.INS_STA_ZP == OPCODE) {
			assertEquals(CPU.mem.data[0x42], 0x02);

		} else if (CPU_6502.OPCODES.INS_STA_ZPX == OPCODE) {
			assertEquals(CPU.mem.data[0x46], 0x02);

		} else if (CPU_6502.OPCODES.INS_STA_AB == OPCODE) {
			assertEquals(CPU.mem.data[0xFF42], 0x02);

		} else if (CPU_6502.OPCODES.INS_STA_ABX == OPCODE) {
			assertEquals(CPU.mem.data[0xFF46], 0x02);

		} else if (CPU_6502.OPCODES.INS_STA_ABY == OPCODE) {
			assertEquals(CPU.mem.data[0xFF48], 0x02);

		} else if (CPU_6502.OPCODES.INS_STA_INX == OPCODE) {// A
			assertEquals(CPU.mem.data[0x10], 0x02);

		} else if (CPU_6502.OPCODES.INS_STA_INY == OPCODE) {
			assertEquals(CPU.mem.data[0x6A], 0x02);

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
