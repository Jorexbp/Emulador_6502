package Excepciones;

public class Excepcion_Instruccion extends Exception {

	private static final long serialVersionUID = 1L;

	public Excepcion_Instruccion(String mensaje) {
		super(mensaje);
	}

	public Excepcion_Instruccion(String command, int cambio) throws Excepcion_Instruccion {
		throw new Excepcion_Instruccion("Instrucción no encontrada: " + command);
	}
}