
public class FriendPresenteException extends Exception {
	public FriendPresenteException() {
		System.out.println("Eccezione: amico gi� presente nella lista");
	}
	
	public FriendPresenteException(String e) {
		super(e);
	}
}
