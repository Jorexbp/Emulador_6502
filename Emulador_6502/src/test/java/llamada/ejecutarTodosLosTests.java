package llamada;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import ARIT_TESTS.ADC_AB_ABX_IM_ZP_ZPX_INX_INY;
import ARIT_TESTS.SBC_AB_ABX_IM_ZP_ZPX_INX_INY;
import JSR_RTS_TESTS.JMP_AB_IM;
import JSR_RTS_TESTS.JSR_RTS;
import JSR_RTS_TESTS.JSR_STATUSNOAFECTA;
import LD_GENERIC_TESTS.LDY_IM_ZP_ZPX_AB_ABX;
import LD_GENERIC_TESTS.TEST_LDA_AB_AX_AY_IX_IY;
import LD_GENERIC_TESTS.TEST_LDA_IM_ZP_ZX_LDX_IM;
import LD_GENERIC_TESTS.TEST_LDX_ZP_ZPX_AB_ABY;
import LOGICAL_GENERIC_TESTS.AND_IM_ZP_ZPX_AB_INX_INY;
import LOGICAL_GENERIC_TESTS.BIT_ZP_AB;
import LOGICAL_GENERIC_TESTS.ORA_IM_ZP_ZPX_AB_ABX_ABY_INX_INY;
import LOGICAL_GENERIC_TESTS.XOR_IM_ZP_ZPX_AB_ABX_ABY_INX_INY;
import STACK_OPERATIONS_TESTS.PHP_TESTS;
import STACK_OPERATIONS_TESTS.TSX_TXS_PHA_PLA_PLP;
import STATUS_FLAGS_TESTS.CLC_CLD_CLI_CLV_SEC_SED_SEI;
import STATUS_FLAGS_TESTS.SEC_SED_SEI;
import ST_GENERIC_TESTS.STA_ZP_ZPX_AB_ABX_ABY_INX_INY;
import ST_GENERIC_TESTS.STX_ZP_ZPY_AB;
import ST_GENERIC_TESTS.STY_ZP_ZPX_AB;
import TAXY_GENERIC_TESTS.TAX_TAY_TXA_TYA;

public class ejecutarTodosLosTests {
	static JUnitCore junit = new JUnitCore();

	static Result rs;
	static int errores = 0;

	public static void main(String[] args) {
		junit.addListener(new TextListener(System.out));

		ejecutarTestsDeTrasnferenciaDeRegistros(); // 1
		ejecutarTestsDeSaltos();// 3
		ejecutarTestsDeStack();// 2
		ejecutarTestsLD();// 4
		ejecutarTestsLogicos(); // 4
		ejecutarTestsST();// 3
		ejecutarTestsDeStatusFlags();// 2
		ejecutarTestsAritmeticos();
		// 19

		if (errores == 0) {
			System.out.println("Ningun test ha fallado");
		} else {
			System.out.println("Hay algún test que ha fallado");
		}

	}

	public static void ejecutarTestsLD() {
		rs = junit.run(LDY_IM_ZP_ZPX_AB_ABX.class, TEST_LDA_AB_AX_AY_IX_IY.class, TEST_LDA_IM_ZP_ZX_LDX_IM.class,
				TEST_LDX_ZP_ZPX_AB_ABY.class);
		errores += rs.getFailureCount();
	}

	public static void ejecutarTestsDeSaltos() {
		rs = junit.run(JMP_AB_IM.class, JSR_RTS.class, JSR_STATUSNOAFECTA.class);
		errores += rs.getFailureCount();
	}

	public static void ejecutarTestsLogicos() {
		rs = junit.run(AND_IM_ZP_ZPX_AB_INX_INY.class, BIT_ZP_AB.class, ORA_IM_ZP_ZPX_AB_ABX_ABY_INX_INY.class,
				XOR_IM_ZP_ZPX_AB_ABX_ABY_INX_INY.class);
		errores += rs.getFailureCount();
	}

	public static void ejecutarTestsST() {
		rs = junit.run(STA_ZP_ZPX_AB_ABX_ABY_INX_INY.class, STX_ZP_ZPY_AB.class, STY_ZP_ZPX_AB.class);
		errores += rs.getFailureCount();
	}

	public static void ejecutarTestsDeStack() {
		rs = junit.run(TSX_TXS_PHA_PLA_PLP.class, PHP_TESTS.class);
		errores += rs.getFailureCount();
	}

	public static void ejecutarTestsDeTrasnferenciaDeRegistros() {
		rs = junit.run(TAX_TAY_TXA_TYA.class);
		errores += rs.getFailureCount();
	}

	public static void ejecutarTestsDeStatusFlags() {
		rs = junit.run(CLC_CLD_CLI_CLV_SEC_SED_SEI.class, SEC_SED_SEI.class);
		errores += rs.getFailureCount();
	}
	
	public static void ejecutarTestsAritmeticos() {
		rs = junit.run(ADC_AB_ABX_IM_ZP_ZPX_INX_INY.class,SBC_AB_ABX_IM_ZP_ZPX_INX_INY.class);
		errores += rs.getFailureCount();
	}

}
