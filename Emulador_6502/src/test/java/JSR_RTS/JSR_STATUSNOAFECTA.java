package JSR_RTS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import CPU_6502.CPU;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JSR_STATUSNOAFECTA {
	CPU.Mem mem = new CPU.Mem();
	CPU cpu = new CPU();

	@Parameterized.Parameter(0)
	public int OPCODE;
	@Parameterized.Parameter(1)
	public int CICLOS;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		// Necesita 13 por que usa JSR ( 6 ciclos ), el RTS ( 6 ciclos ) y el LDA_IM ( 1
		// ciclo)
		Object[][] data = new Object[][] { { CPU_6502.OPCODES.INS_JSR, 12 } };
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		cpu.reset(0xFF00, mem);
		CPU copiaCPU = cpu;

		CPU.mem.data[0xFF00] = OPCODE;
		CPU.mem.data[0xFF01] = 0x00;
		CPU.mem.data[0xFF02] = 0x80;
		CPU.mem.data[0x8000] = CPU_6502.OPCODES.INS_RTS;
		CPU.mem.data[0xFF03] = 0x37;

		int ciclosUsados = cpu.execute(CICLOS, mem);

		assertEquals(ciclosUsados, CICLOS);

		assertEquals(cpu.N, false);
		assertEquals(cpu.Z, false);

		assertEquals(cpu.C, copiaCPU.C);
		assertEquals(cpu.I, copiaCPU.I);
		assertEquals(cpu.D, copiaCPU.D);
		assertEquals(cpu.B, copiaCPU.B);
		assertEquals(cpu.V, copiaCPU.V);
	}

}
