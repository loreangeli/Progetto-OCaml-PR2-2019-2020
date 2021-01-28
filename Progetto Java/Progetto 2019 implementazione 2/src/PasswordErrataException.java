
public class PasswordErrataException extends Exception {
	public PasswordErrataException() {
		System.out.println("Eccezione: password errata");
	}
	
	public PasswordErrataException(String e) {
		super(e);
	}
}
