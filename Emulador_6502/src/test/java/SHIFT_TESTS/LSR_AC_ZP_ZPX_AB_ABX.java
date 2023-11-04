package SHIFT_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LSR_AC_ZP_ZPX_AB_ABX {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_LSR_AC.opcodeValue, 2 }, { OPCODES.INS_LSR_ZP.opcodeValue, 5 },
				{ OPCODES.INS_LSR_ZPX.opcodeValue, 6 }, { OPCODES.INS_LSR_AB.opcodeValue, 6 },
				{ OPCODES.INS_LSR_ABX.opcodeValue, 7 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU.A = 0x02;
		CPU.X = 0x01;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0x1242; // AC
		CPU.mem.data[0x0042] = 0x05; // ZP
		CPU.mem.data[0x0043] = 0x04; // ZPX
		CPU.mem.data[0x1242] = 0x06; // AB
		CPU.mem.data[0x1243] = 0x07; // ABX

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_LSR_AC.opcodeValue == OPCODE) {
			assertEquals(CPU.A, 0x01);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
		} else if (OPCODES.INS_LSR_ZP.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x0042], 0x02);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, true);
		} else if (OPCODES.INS_LSR_ZPX.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x0043], 0x02);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
		} else if (OPCODES.INS_LSR_AB.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x1242], 0x03);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
		} else if (OPCODES.INS_LSR_ABX.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[0x1243], 0x03);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, true);
		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
