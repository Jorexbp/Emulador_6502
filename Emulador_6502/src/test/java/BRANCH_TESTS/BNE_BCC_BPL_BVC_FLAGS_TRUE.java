package BRANCH_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BNE_BCC_BPL_BVC_FLAGS_TRUE {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_BNE_RL.opcodeValue, 2 }, { OPCODES.INS_BCC_RL.opcodeValue, 2 },
				{ OPCODES.INS_BPL_RL.opcodeValue, 2 }, { OPCODES.INS_BVC_RL.opcodeValue, 2 },
				{ OPCODES.INS_BCS_RL.opcodeValue, 3 }, { OPCODES.INS_BMI_RL.opcodeValue, 3 },
				{ OPCODES.INS_BEQ_RL.opcodeValue, 3 }, { OPCODES.INS_BVS_RL.opcodeValue, 3 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		cpu.Z = true;
		cpu.C = true;
		cpu.N = true;
		cpu.V = true;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0x01;
// hacer otro pero al reves las flags
		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_BNE_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF02);
		} else if (OPCODES.INS_BCC_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF02);
		} else if (OPCODES.INS_BPL_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF02);
		} else if (OPCODES.INS_BVC_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF02);
		} else if (OPCODES.INS_BCS_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF03);
		} else if (OPCODES.INS_BEQ_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF03);
		} else if (OPCODES.INS_BMI_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF03);
		} else if (OPCODES.INS_BVS_RL.opcodeValue == OPCODE) {
			assertEquals(cpu.PC, 0xFF03);
		}

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
