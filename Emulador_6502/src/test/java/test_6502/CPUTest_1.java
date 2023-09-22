package test_6502;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CPU_6502.CPU;





public class CPUTest_1 {
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
		
		CPU.mem.data[0xFFFC] = CPU.INS_JSR;
		CPU.mem.data[0xFFFD] = 0x42;
		CPU.mem.data[0xFFFE] = 0x42;
		CPU.mem.data[0x4242] =CPU.INS_LDA_IM;
		CPU.mem.data[0x4243] = 0x84;
		
		
		cpu.execute(9, mem);
		assertEquals(cpu.A, 132);
		// Ya funciona y listo para implementar mas
	}

}
