package Visual;

public class llamadaExterna {

	public static void main(String[] args) {
		Visual_CPU6502 v = new Visual_CPU6502();
		v.escribirExterno("INS_LDA_IM,66,42");
	}

}
