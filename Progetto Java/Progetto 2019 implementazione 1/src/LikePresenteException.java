
public class LikePresenteException extends Exception {
	public LikePresenteException() {
		super("Eccezione: L'utente aveva gi� messo like in precedenza");
	}
	
	public LikePresenteException(String e) {
		super(e);
	}
}
