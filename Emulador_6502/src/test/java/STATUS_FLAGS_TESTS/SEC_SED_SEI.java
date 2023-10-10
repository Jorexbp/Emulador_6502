package STATUS_FLAGS_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SEC_SED_SEI {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {

		Object[][] data = new Object[][] { { OPCODES.INS_SEI_IM.opcodeValue, 2 }, { OPCODES.INS_SEC_IM.opcodeValue, 2 },
				{ OPCODES.INS_SED_IM.opcodeValue, 2 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU copiaCPU = cpu;
		cpu.C = 0;

		cpu.I = 0;
		cpu.D = 0;

		CPU.mem.data[0xFF00] = OPCODE;// Setear los valores correspondientes

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODE == OPCODES.INS_SEC_IM.opcodeValue) {
			assertEquals(cpu.C, 1);
		} else if (OPCODE == OPCODES.INS_SED_IM.opcodeValue) {
			assertEquals(cpu.D, 1);
		} else if (OPCODE == OPCODES.INS_SEI_IM.opcodeValue) {
			assertEquals(cpu.I, 1);
		}
		assertEquals(cpu.N, false);
		assertEquals(cpu.Z, false);
		assertEquals(ciclosUsados, CICLOS);
		assertEquals(cpu.PS, copiaCPU.PS);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);

		assertEquals(cpu.SP, copiaCPU.SP);
	}

}
