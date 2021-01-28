
public class FriendNonPresenteException extends Exception {
	public FriendNonPresenteException() {
		super("Eccezione: amico non presente");
	}
	
	public FriendNonPresenteException(String e) {
		super(e);
	}
}
