package LDX_Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CPU_6502.CPU;

public class LDA_AB_A_REG {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		cpu.reset(mem);
		CPU copiaCPU = cpu;
		
		CPU.mem.data[0xFFFC] = CPU.INS_LDA_AB;
		CPU.mem.data[0xFFFD] = 0xFFFF;
		CPU.mem.data[0xFFFF] = 0x84;
		
		int ciclosUsados = cpu.execute(4, mem);
		
		assertEquals(cpu.A, 0x84);
		assertEquals(ciclosUsados, 4);
		
		
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
