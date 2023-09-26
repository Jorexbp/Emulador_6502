package LDX_Tests;

import static org.junit.Assert.*;
import org.junit.Test;

import CPU_6502.CPU;

public class LD_IM_A_REG {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Test
	public void test() {

		cpu.reset(mem);
		CPU copiaCPU = cpu;
		
		CPU.mem.data[0xFFFC] = CPU.INS_LDA_IM;
		CPU.mem.data[0xFFFD] = 0x42;
		
		int ciclosUsados = cpu.execute(2, mem);
		
		assertEquals(CPU.A, 0x42);
		assertEquals(ciclosUsados, 2);
		
		
		assertEquals(cpu.Z, false);
		assertEquals(cpu.N, true);
		
		assertEquals(cpu.C,copiaCPU.C);
		assertEquals(cpu.I,copiaCPU.I);
		assertEquals(cpu.D,copiaCPU.D);
		assertEquals(cpu.B,copiaCPU.B);
		assertEquals(cpu.V,copiaCPU.V);
		// Ya funciona y listo para implementar mas
	}

}

