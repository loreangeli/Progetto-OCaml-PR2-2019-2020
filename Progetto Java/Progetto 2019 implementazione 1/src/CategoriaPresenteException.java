
public class CategoriaPresenteException extends Exception {
	public CategoriaPresenteException() {
		super("Eccezione: categoria gi� presente");
	}
	
	public CategoriaPresenteException(String e) {
		super(e);
	}
}
