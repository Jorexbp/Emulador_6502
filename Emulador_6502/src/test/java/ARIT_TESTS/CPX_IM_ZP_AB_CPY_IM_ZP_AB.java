package ARIT_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CPX_IM_ZP_AB_CPY_IM_ZP_AB {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_CPX_IM.opcodeValue, 2 }, { OPCODES.INS_CPX_ZP.opcodeValue, 3 },
				{ OPCODES.INS_CPX_AB.opcodeValue, 4 }, { OPCODES.INS_CPY_IM.opcodeValue, 2 },
				{ OPCODES.INS_CPY_ZP.opcodeValue, 3 }, { OPCODES.INS_CPY_AB.opcodeValue, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU.X = 0x04;
		CPU.Y = 0x78;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0x0142;
		CPU.mem.data[0x0042] = 0x10; // IM ZP
		CPU.mem.data[0x0142] = 0x02; // AB

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_CPX_IM.opcodeValue == OPCODE) {
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_CPX_ZP.opcodeValue == OPCODE) {
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_CPX_AB.opcodeValue == OPCODE) {
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, true);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_CPY_IM.opcodeValue == OPCODE) {
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_CPY_ZP.opcodeValue == OPCODE) {
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, true);
			assertEquals(cpu.V, false);
		} else if (OPCODES.INS_CPY_AB.opcodeValue == OPCODE) {
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, true);
			assertEquals(cpu.V, false);
		}
		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}

