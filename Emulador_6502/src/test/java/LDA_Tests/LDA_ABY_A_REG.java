package LDA_Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CPU_6502.CPU;

public class LDA_ABY_A_REG {
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
		cpu.Y = 0xFF;
		CPU copiaCPU = cpu;
		
		CPU.mem.data[0xFFFC] = CPU.INS_LDA_AY;
		CPU.mem.data[0xFFFD] = 0x02;
		CPU.mem.data[0xFFFE] = 0x44;
		CPU.mem.data[0x4501] = 0x37;
		
		int ciclosUsados = cpu.execute(5, mem);
		
		assertEquals(cpu.A, 0x37);
		assertEquals(ciclosUsados, 5);
		
		
		assertEquals(cpu.Z, false);
		assertEquals(cpu.N, false);
		
		assertEquals(cpu.C,copiaCPU.C);
		assertEquals(cpu.I,copiaCPU.I);
		assertEquals(cpu.D,copiaCPU.D);
		assertEquals(cpu.B,copiaCPU.B);
		assertEquals(cpu.V,copiaCPU.V);
		// Ya funciona y listo para implementar mas
	}

}
