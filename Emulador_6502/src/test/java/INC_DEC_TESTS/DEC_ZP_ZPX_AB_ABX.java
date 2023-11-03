package INC_DEC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DEC_ZP_ZPX_AB_ABX {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_DEC_ZP.opcodeValue, 5 },
				{ OPCODES.INS_DEC_ZPX.opcodeValue, 6 }, { OPCODES.INS_DEC_AB.opcodeValue, 6 },
				{ OPCODES.INS_DEC_ABX.opcodeValue, 7 }, };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU.X = 0x02;

		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0x0142;
		CPU.mem.data[0x0042] = 0x01; // ZP
		CPU.mem.data[0x0044] = 0x03; // ZPX
		CPU.mem.data[0x0142] = 0x05; // AB
		CPU.mem.data[0x0144] = 0x07; // ABX

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_DEC_ZP.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x0042], 0x00);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, true);
			assertEquals(cpu.C, true);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_DEC_ZPX.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x0044], 0x02);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_DEC_AB.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x0142], 0x04);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_DEC_ABX.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x0144], 0x06);
			assertEquals(cpu.N, false);
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
