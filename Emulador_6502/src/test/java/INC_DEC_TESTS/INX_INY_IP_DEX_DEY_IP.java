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
public class INX_INY_IP_DEX_DEY_IP {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_INX_IP.opcodeValue, 2 }, { OPCODES.INS_INY_IP.opcodeValue, 2 },
				{ OPCODES.INS_DEX_IP.opcodeValue, 2 }, { OPCODES.INS_DEY_IP.opcodeValue, 2 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU.X = 0x02;
		CPU.Y = 0x04;

		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_INX_IP.opcodeValue == OPCODE) {
			assertEquals(CPU.X, 0x03);
		} else if (OPCODES.INS_INY_IP.opcodeValue == OPCODE) {
			assertEquals(CPU.Y, 0x05);
		} else if (OPCODES.INS_DEY_IP.opcodeValue == OPCODE) {
			assertEquals(CPU.Y, 0x03);
		} else if (OPCODES.INS_DEX_IP.opcodeValue == OPCODE) {
			assertEquals(CPU.X, 0x01);
		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
