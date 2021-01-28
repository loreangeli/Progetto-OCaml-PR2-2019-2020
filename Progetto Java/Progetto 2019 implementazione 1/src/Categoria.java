import java.util.ArrayList;
import java.util.Iterator;

public class Categoria <E extends Data<?>> implements Category<E> {
	
	private String Categoria;
	private ArrayList<E> Dati;
	private ArrayList<String> Friends;
	
	public Categoria(String _Categoria) {
		Categoria=new String(_Categoria);
		Dati=new ArrayList<E> ();
		Friends=new ArrayList<String> ();
	}

	public String getCategoria() {
		return Categoria;
	}

	public void insertDato(E dato) {
		if(dato==null)
			throw new NullPointerException();
		Dati.add(dato);
	}

	public void insertFriend(String amico) throws FriendPresenteException {
		if (amico==null)
			throw new NullPointerException();
		if (Friends.contains(amico))
			throw new FriendPresenteException();
		Friends.add(amico);
	}

	public void removeDato(E dato) throws DatoNonPresenteException {
		if(dato==null)
			throw new NullPointerException();
		if ( !searchDato(dato) )
			throw new DatoNonPresenteException();
		
		Dati.remove(dato);
	}

	public void removeFriend(String amico) throws FriendNonPresenteException {
		if (amico==null)
			throw new NullPointerException();
		if (!Friends.contains(amico))
			throw new FriendNonPresenteException();
		Friends.remove(amico);
	}

	public void DisplayDati() {
		System.out.println(Dati);	
	}

	public void DisplayFriend() {
		System.out.println(Friends);
	}

	public void Display() {
		System.out.println(Categoria);
		System.out.println(Dati);
		System.out.println(Friends);
	}

	public ArrayList<E> getDati() {
		ArrayList<E> tmp= new ArrayList<E>();
		tmp.addAll(Dati); //copia i dati di Dati in tmp
		return tmp;
	}

	public ArrayList<String> getFriend() {
		ArrayList<String> tmp= new ArrayList<String>();
		tmp.addAll(Friends); //copia i dati di Friends in tmp
		return tmp;
	}
	
	public boolean searchFriend (String friend) {
		return Friends.contains(friend);
	}
	
	public boolean searchDato(E dato) {
		return Dati.contains(dato);
	}
	
	//controlla se due oggetti di tipo categoria sono uguali.
	public boolean equals (Object o) {
		if (o == this)
			return true;
		try {
			Categoria<E> tmp = (Categoria<E>) o;
			if (tmp.getCategoria().equals(this.Categoria))
				return true;	
		}catch(ClassCastException e) {
			return false;
		}
		return false;
	}
	
	public void addlike(E dato, String friend) throws DatoNonPresenteException, LikePresenteException {
		if (dato==null)
			throw new NullPointerException();
		if (!searchDato(dato))
			throw new DatoNonPresenteException();
		
		// creo l'oggetto iterator per scorrere la lista
		Iterator<E> myIteretor = Dati.iterator();
		             
		while (myIteretor.hasNext()){
		    // visto che abbiamo imposto che l'iterator è di tipo Person
		    // è sufficiente assegnare il valore al nostro oggetto di tipo Person
		    E p = myIteretor.next();
		    if ( p.equals(dato) ) {
		    	p.insertLike(friend);
		    	return;
		    }    	
		}
	}

	
}