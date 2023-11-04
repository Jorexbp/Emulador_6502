package SYSFUNC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BRK_RTI {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_BRK_IM.opcodeValue, 7 },
				{ OPCODES.INS_RTI_IM.opcodeValue, 6 + 7 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);

		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODES.INS_BRK_IM.opcodeValue;
		CPU.mem.data[0xFFFE] = 0x00;
		CPU.mem.data[0xFFFF] = 0x80;
		CPU.mem.data[0x8000] = OPCODES.INS_RTI_IM.opcodeValue;
		int SP_old = copiaCPU.SP;

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_BRK_IM.opcodeValue == OPCODE) {
			assertEquals(CPU.mem.data[(0x100 | SP_old) - 0], 0xFF);
			assertEquals(CPU.mem.data[(0x100 | SP_old) - 1], 0x01);
			assertEquals(CPU.mem.data[(0x100 | SP_old) - 2], copiaCPU.PS);
		} else if (OPCODES.INS_RTI_IM.opcodeValue == OPCODE) {
			assertEquals(copiaCPU.SP, cpu.SP);
			assertEquals(copiaCPU.PC, cpu.PC);
			assertEquals(copiaCPU.PS, cpu.PS);

		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
