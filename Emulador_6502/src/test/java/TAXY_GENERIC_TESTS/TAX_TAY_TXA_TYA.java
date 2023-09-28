package TAXY_GENERIC_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TAX_TAY_TXA_TYA {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { CPU_6502.OPCODES.INS_TAX_IP, 2 }, { CPU_6502.OPCODES.INS_TAY_IP, 2 },
				{ CPU_6502.OPCODES.INS_TXA_IP, 2 }, { CPU_6502.OPCODES.INS_TYA_IP, 2 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(mem);
		CPU.X = 0x37;
		CPU.Y = 0x10;
		CPU.A = 0x20;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = OPCODE;

		int ciclosUsados = cpu.execute(2, mem);

		if (CPU_6502.OPCODES.INS_TXA_IP == OPCODE) {
			assertEquals(CPU.X, CPU.A);

		} else if (CPU_6502.OPCODES.INS_TYA_IP == OPCODE) {
			assertEquals(CPU.Y, CPU.A);

		} else if (CPU_6502.OPCODES.INS_TAX_IP == OPCODE) {
			assertEquals(CPU.A, CPU.X);

		} else if (CPU_6502.OPCODES.INS_TAY_IP == OPCODE) {
			assertEquals(CPU.A, CPU.Y);

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
