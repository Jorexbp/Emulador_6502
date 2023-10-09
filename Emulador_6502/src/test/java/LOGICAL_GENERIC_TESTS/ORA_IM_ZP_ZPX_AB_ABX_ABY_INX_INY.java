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
public class ORA_IM_ZP_ZPX_AB_ABX_ABY_INX_INY {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_ORA_IM.opcodeValue, 2 }, { OPCODES.INS_ORA_ZP.opcodeValue, 3 },
				{ OPCODES.INS_ORA_ZPX.opcodeValue, 4 }, { OPCODES.INS_ORA_AB.opcodeValue, 4 },
				{ OPCODES.INS_ORA_ABX.opcodeValue, 4 }, { OPCODES.INS_ORA_ABY.opcodeValue, 3 },
				{ OPCODES.INS_ORA_INX.opcodeValue, 6 }, { OPCODES.INS_ORA_INY.opcodeValue, 6 } };

		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC, mem);
		CPU.A = 0x64; // 01000000
		CPU.X = 0x02; // 00000010
		CPU.Y = 0x04; // 00000100

		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFF02; // IM
		CPU.mem.data[0x02] = 0x0E; // ZP
		CPU.mem.data[0x04] = 0x0E; // ZPX // INX efAddr
		CPU.mem.data[0xFF02] = 0x0E; // AB
		CPU.mem.data[0xFF04] = 0x0E; // ABX
		CPU.mem.data[0xFF06] = 0x0E; // ABY
		CPU.mem.data[0x0E] = 0x0E; // INX [efAddr]

		CPU.mem.data[0x12] = 0x0E; // INY [efAddr]

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODE == OPCODES.INS_ORA_IM.opcodeValue) {
			assertEquals(CPU.A, 0x66);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_ZP.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_ZPX.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_AB.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_ABX.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_ABY.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_INX.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

		} else if (OPCODE == OPCODES.INS_ORA_INY.opcodeValue) {
			assertEquals(CPU.A, 0x6E);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);

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
