package Program;

import org.junit.Test;
import CPU_6502.CPU;
import static org.junit.Assert.assertEquals;

public class Mini_programa {

	@Test
	public void test() {
		CPU.Mem mem = new CPU.Mem();
		CPU cpu = new CPU();
		cpu.reset(0x0FFF, mem);
		int Prg[] = { 0x00, 0x10, 0xA9, 0xFF, 0x85, 0x90,
				0x8D, 0x00, 0x80, 0x49, 0xCC, 0x4C, 0x02, 0x10
				};
		
		int iniPrg = cpu.CargarPrograma(Prg,14,mem);
		cpu.PC = iniPrg;
		
		assertEquals(CPU.mem.data[0x0FFF], 0x0);
		assertEquals(CPU.mem.data[0x1000], 0xA9);
		assertEquals(CPU.mem.data[0x1001], 0xFF);
		//....
		assertEquals(CPU.mem.data[0x1009], 0x4C);
		assertEquals(CPU.mem.data[0x100A], 0x02);
		assertEquals(CPU.mem.data[0x100B], 0x10);
		assertEquals(CPU.mem.data[0x100C], 0x0);
		
		for (int Reloj = 1000; Reloj > 0;) {
			Reloj -= cpu.execute( 1 , mem );
			System.out.println("A: "+CPU.A);
			System.out.println("X: "+CPU.X);
			System.out.println("Y: "+CPU.Y);
			System.out.println("PC: "+cpu.PC);
			System.out.println("SP: "+cpu.SP);	
			System.out.println("Flag D: "+cpu.D);
			System.out.println("Flag C: "+cpu.C);
			System.out.println("Flag I: "+cpu.I);
			System.out.println("Flag B: "+cpu.B);
			System.out.println("Flag Un: "+cpu.Un);
			System.out.println("Flag V: "+cpu.V);
			System.out.println("Flag N: "+cpu.N);
			System.out.println("Flag Z: "+cpu.Z);
		}
		
		
		
		
	}

}
