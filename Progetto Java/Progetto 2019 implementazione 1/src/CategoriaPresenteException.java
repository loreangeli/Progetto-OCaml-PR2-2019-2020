
public class CategoriaPresenteException extends Exception {
	public CategoriaPresenteException() {
		super("Eccezione: categoria già presente");
	}
	
	public CategoriaPresenteException(String e) {
		super(e);
	}
}
