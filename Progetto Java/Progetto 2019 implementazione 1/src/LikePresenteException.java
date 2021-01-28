
public class LikePresenteException extends Exception {
	public LikePresenteException() {
		super("Eccezione: L'utente aveva già messo like in precedenza");
	}
	
	public LikePresenteException(String e) {
		super(e);
	}
}
