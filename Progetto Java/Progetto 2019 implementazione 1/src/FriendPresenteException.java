
public class FriendPresenteException extends Exception {
	public FriendPresenteException() {
		System.out.println("Eccezione: amico già presente nella lista");
	}
	
	public FriendPresenteException(String e) {
		super(e);
	}
}
