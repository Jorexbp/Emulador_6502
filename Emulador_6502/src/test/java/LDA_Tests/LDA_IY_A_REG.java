package LDA_Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CPU_6502.CPU;

public class LDA_IY_A_REG {
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
		cpu.Y = 0x04;
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFFFC] = CPU.INS_LDA_IY;
		CPU.mem.data[0xFFFD] = 0x02;
		CPU.mem.data[0x0002] = 0x00; 
		CPU.mem.data[0x0003] = 0x80; 
		CPU.mem.data[0x8004] = 0x37; // 0x8000 + 0x4

		int ciclosUsados = cpu.execute(5, mem);

		assertEquals(cpu.A, 0x37);
		assertEquals(ciclosUsados, 5);

		assertEquals(cpu.Z, false);
		assertEquals(cpu.N, false);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
		// Ya funciona y listo para implementar mxas
	}

}
