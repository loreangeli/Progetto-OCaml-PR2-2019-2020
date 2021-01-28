import java.util.HashMap;
import java.util.Map;

/*
Overview: tipo di dato modificabile che rappresenta un oggetto che contiene un dato generico al quale è possibile assegnare un like. 

Elemento tipico: <{ (dato), (like), (<amico1,amico2,...>)}>.
*/

 
public class Data<E> implements Comparable<Data<E>>{
	private int like;
	private E dato;
	private Map<String, Integer> friends;
	
	public Data( E _dato) {
		like=0;	
		this.dato=_dato;
		friends= new HashMap<String, Integer> ();
	}
	
	public void insertLike(String friend) throws LikePresenteException {
		//se friend già presente lancia eccezione. (l'utente ha già messo like)
		if (friends.containsKey(friend))
			throw new LikePresenteException();
			
		like++;
		friends.put(friend, 0);
	}
	
	public E getDato() {
		return dato;
	}

	public void setDato(E dato) {
		this.dato = dato;
	}

	/*EFFECTS: Restituisce una rappresentazione testuale dell'oggetto in forma di stringa: è molto utile ad esempio per 
	 le stampe.*/
	@Override
	public String toString() {
		String tmp= new String();	
		tmp="dato:" + dato + " like:" + like;
		
		return tmp;
	}
	
	public void Display() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int compareTo(Data<E> o) {
		if (this.like < o.like)
			return -1;
		return 1;
	}


}