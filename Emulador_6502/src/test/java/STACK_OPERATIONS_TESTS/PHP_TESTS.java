package STACK_OPERATIONS_TESTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;
import CPU_6502.OPCODES;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PHP_TESTS {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		
		Object[][] data = new Object[][] { { OPCODES.INS_PHP_IM.opcodeValue, 3 }
 };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU copiaCPU = cpu;
		
		cpu.PS = 0xCC; 
		
		CPU.mem.data[0xFF00] = OPCODE;// Setear los valores correspondientes
		

		int ciclosUsados = cpu.execute(CICLOS, mem);

		if (OPCODE == OPCODES.INS_PHP_IM.opcodeValue) {
			assertEquals(CPU.mem.data[cpu.SPToAddr()+1], 0xCC);
			
		} else if (OPCODE == OPCODES.INS_PLA_IM.opcodeValue) {
			assertEquals(CPU.mem.data[cpu.SPToAddr()+1], 0xCC);
			
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
