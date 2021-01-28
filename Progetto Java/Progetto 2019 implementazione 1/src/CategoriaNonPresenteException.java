
public class CategoriaNonPresenteException extends Exception {
	public CategoriaNonPresenteException() {
		super("Eccezione: categoria non presente");
	}
	
	public CategoriaNonPresenteException(String e) {
		super(e);
	}
}
