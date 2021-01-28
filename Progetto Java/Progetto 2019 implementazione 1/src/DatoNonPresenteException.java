
public class DatoNonPresenteException extends Exception {
	public DatoNonPresenteException () {
		super("Eccezione: dato non presente");
	}
	
	public DatoNonPresenteException(String e) {
		super(e);
	}
}
