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
public class TEST_LDA_AB_AX_AY_IX_IY {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_LDA_AB.opcodeValue, 4 }, { OPCODES.INS_LDA_AX.opcodeValue, 4 },
				{ OPCODES.INS_LDA_AY.opcodeValue, 4 }, { OPCODES.INS_LDA_IX.opcodeValue, 6 },
				{ OPCODES.INS_LDA_IY.opcodeValue, 5 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFFFC,mem);
		CPU.X = 0x02;
		CPU.Y = 0x04;

		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;
		CPU.mem.data[0xFFFD] = 0xFFF0;
		CPU.mem.data[0xFFF0] = 0x10; // 16 dec
		CPU.mem.data[0xFFF4] = 0x42; // ABY
		CPU.mem.data[0xFFF2] = 0x84; // ABX
		CPU.mem.data[0x00F2] = 0x37; // INX es byte solo lee los dos prim
		CPU.mem.data[0x37] = 0x06; // INX

		CPU.mem.data[0xF0] = 0x15; // INY 0x04 + 0x10 0x15==21
		CPU.mem.data[0x19] = 0x86;

		int ciclosUsados = cpu.execute(2, mem);

		if (OPCODES.INS_LDA_AB.opcodeValue == OPCODE) {
			assertEquals(CPU.A, 0x10);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDA_AX.opcodeValue == OPCODE) {
			assertEquals(CPU.A, 0x84);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDA_AY.opcodeValue == OPCODE) {
			assertEquals(CPU.A, 0x42);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDA_IX.opcodeValue == OPCODE) {
			assertEquals(CPU.A, 0x06);
			assertEquals(cpu.N, false);
			assertEquals(cpu.Z, false);

		} else if (OPCODES.INS_LDA_IY.opcodeValue == OPCODE) {
			assertEquals(CPU.A, 0x86);
			assertEquals(cpu.Z, false);
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
