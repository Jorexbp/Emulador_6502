package LDA_Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CPU_6502.CPU;

public class LDA_ZPX_A_REG_WRAPS {
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
		// Escribo en X para comprobar que si escriba en X
		CPU copiaCPU = cpu;

		cpu.X = 0xFF;

		CPU.mem.data[0xFFFC] = CPU.INS_LDA_ZX;
		CPU.mem.data[0xFFFD] = 0x80;
		CPU.mem.data[0x007F] = 0x37;

		int ciclosUsados = cpu.execute(4, mem);
		
		assertEquals(cpu.A, 0x37);
		assertEquals(ciclosUsados, 4);
			
		assertEquals(cpu.Z, false);
		assertEquals(cpu.N, false);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
