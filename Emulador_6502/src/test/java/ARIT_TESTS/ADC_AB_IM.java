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
public class ADC_AB_IM {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { OPCODES.INS_ADC_AB.opcodeValue, 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00,mem);
		CPU.A = 126;
		
		cpu.C = false;
		cpu.Z = true;
		cpu.N = false;
		cpu.V = false;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0X00; 
		CPU.mem.data[0xFF02] = 0x80;
		CPU.mem.data[0x8000] = 0x01; // Operando
		
		
		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODES.INS_ADC_AB.opcodeValue == OPCODE) {
			assertEquals(cpu.suma, 127);
			assertEquals(cpu.N, true);
			assertEquals(cpu.Z, false);
			assertEquals(cpu.C, false);
			assertEquals(cpu.V, true);
		}
		

		assertEquals(ciclosUsados, CICLOS);

		
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
