import java.util.ArrayList;

/*
Overview: tipo di dato modificabile che rappresenta una categoria, i suoi dati e la lista di contatti (amici) a cui saranno visibili i dati. 
Elemento tipico: <{ (categoria), (<dato0,dato1,dato2, …>), (<amico0,amico1,amico2, …>) }>
 */

public interface Category <E extends Data<?>> {
	
	
	/*
	@EFFECTS: ritorna la stringa categoria.
	*/
	public String getCategoria();
	
	/*
	@REQUIRES: dato!=null.
	@MODIFIES: this.
	@EFFECTS: inserisce un dato.
	@THROWS: se (dato==null) lancia NullPointerException.
	*/
	public void insertDato(E dato);
	
	/*
	@REQUIRES: dato!=null.
	@MODIFIES: this.
	@EFFECTS: rimuove un dato.
	@THROWS: se (dato==null) lancia NullPointerException, se (dato appartiene a  domD) lancia DatoNonPresenteException.
	*/
	public void removeDato(E dato) throws DatoNonPresenteException;
	
	/*
	@REQUIRES: amico!=null.
	@MODIFIES: this
	@EFFECTS: aggiunge un amico 
	@THROWS: se (amico==null) lancia NullPointerException, se (amico appartiene a domF) lancia FriendPresenteException.
	*/
	public void insertFriend(String amico) throws FriendPresenteException;
	
	/* 
	@EFFECTS: ritorna una copia dell'ArrayList che contiene i dati
	*/
	public ArrayList<E> getDati();
	
	/*
	@EFFECTS: ritorna una copia dell'ArrayList che contiene gli amici
	*/
	public ArrayList<String> getFriend();
	
	/*
	@EFFECTS: stampa tutti i dati presenti nella categoria
	*/
	public void DisplayDati();
	
	/*
	@EFFECTS: stampa tutti gli amici presenti nella categoria
	*/
	public void DisplayFriend();
	
	/*
	@EFFECTS: stampa tutte le info della categoria
	*/
	public void Display();
	
	/*
	@EFFECTS: ritorna true se la stringa friend è presente nella struttura dati, false altrimenti.
	*/
	public boolean searchFriend (String friend);
	
	/*
	@EFFECTS: ritorna true se il dato di tipo E è presente nella struttura dati, false altrimenti.
	*/
	public boolean searchDato(E dato);
	
	@Override
	public boolean equals (Object o);
	
	/* 
	@REQUIRES: dato!=null
	@MODIFIES: this
	@EFFECTS: aggiunge un like al dato
	@THROWS: se (dato non appartiene a domD) lancia DatoNonPresenteException.
	*/
	public void addlike(E dato, String friend) throws DatoNonPresenteException, LikePresenteException;

}
