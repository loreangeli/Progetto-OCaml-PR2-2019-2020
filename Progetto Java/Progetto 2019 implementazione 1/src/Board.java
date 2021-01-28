import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/*
IV: password!=null && Categorie!null && (Ɐ Categoria i, j ϵ Categorie : i!=j => Categorie[i]!=Categorie[j]) && 
(Ɐ Categoria i ϵ Categorie => i!=null)

FA: fa(password, Categorie)=<{password}, {Categoria1.get(key1), Categoria2.get(key2,...)}>
*/

public class Board<T, E extends Data<T>> implements DataBoard <E> {
	
	private String password;
	private ArrayList<Categoria<E>> Categorie;
	
	public Board(String passw) {
		password=new String(passw);
		Categorie=new ArrayList<Categoria<E>>();
	}
	
	
	public void createCategory(String Category, String passw) throws PasswordErrataException, CategoriaPresenteException {
		if (Category==null || passw==null)
			throw new NullPointerException();
		if ( ! check(passw) )
			throw new PasswordErrataException();
		//controllo presenza categoria
		if (checkCategoria(Category) )
			throw new CategoriaPresenteException();
		
		//creo l'oggetto di tipo Categoria
		Categoria<E> tmp=new Categoria<E>(Category);
		//inserisce l'oggetto nell'ArrayList
		Categorie.add(tmp);
	}


	public void removeCategory(String Category, String passw) throws PasswordErrataException, CategoriaNonPresenteException {
		if (Category==null || passw==null)
			throw new NullPointerException();
		
		//controllo di identità
		if (!check(passw))
			throw new PasswordErrataException();

		//controllo se la categoria è presente.
		Categoria<E> tmp= new Categoria<E> (Category);
		int index=Categorie.indexOf(tmp);
		
		if (index==-1)
			throw new CategoriaNonPresenteException();
		
		Categorie.remove(index);
	}

	
	public void addFriend(String Category, String passw, String friend) throws PasswordErrataException, CategoriaNonPresenteException, FriendPresenteException {
		//controllo argomenti
		if (Category==null || passw==null || friend==null)
			throw new NullPointerException();
		//controllo di identità
		if (!check(passw))
			throw new PasswordErrataException();
		//controllo categoria
		if ( !checkCategoria(Category) )
			throw new CategoriaNonPresenteException();
		//controllo se friend è già presente
		if (checkFriend(friend, Category))
			throw new FriendPresenteException();
		
		//add friend//
		//scorro l'Array e cerco la Categoria Category
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
				
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			if ( ( (p.getCategoria()).equals(Category)) ) {
				//categoria trovata
				p.insertFriend(friend);
				return;
			}
		}		
	}


	public void removeFriend(String Category, String passw, String friend) throws FriendNonPresenteException, CategoriaNonPresenteException, PasswordErrataException, FriendPresenteException {
		//controllo argomenti
		if (Category==null || passw==null || friend==null)
			throw new NullPointerException();
		//controllo di identità
		if (!check(passw))
			throw new PasswordErrataException();
		//controllo categoria
		if ( !checkCategoria(Category) )
			throw new CategoriaNonPresenteException();

		//remove friend//
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			if ( ( (p.getCategoria()).equals(Category)) ) {
				//categoria trovata
				p.removeFriend(friend);
				return;
			}
		}	
		
		throw new FriendNonPresenteException();
	}


	public boolean put(String passw, E dato, String categoria) throws CategoriaNonPresenteException, PasswordErrataException {
		if (passw==null || dato==null || categoria==null)
			throw new NullPointerException();
		if ( ! check(passw) )
			throw new PasswordErrataException();
		
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			if ( ( (p.getCategoria()).equals(categoria)) ) {
				//categoria trovata
				p.insertDato(dato);
				return true;
			}
		}			
		
		return false;
	}

	public E get(String passw, E dato) throws PasswordErrataException {
		if (passw==null || dato==null)
			throw new NullPointerException();
		if ( ! check(passw) )
			throw new PasswordErrataException();
		
		//effettua una copia del dato in bacheca
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			if ( p.searchDato(dato) ) {
				//categoria trovata
				p.insertDato(dato);
				return dato;
			}
		}	
		
		return null;
	}
	
	public E remove(String passw, E dato) throws PasswordErrataException, DatoNonPresenteException {
		if (passw==null || dato==null)
			throw new NullPointerException();
		if ( ! check(passw) )
			throw new PasswordErrataException();
		
		//controllo se la categoria è presente//
		//scorro l'Array e cerco la Categoria Category
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			//searchFriend: true se friend appartiene alla categoria, altrimenti false
			//searchDato: ritorna true se il dato di tipo E è presente nella struttura dati, false altrimenti.
			if (p.searchDato(dato)) {	
				p.removeDato(dato);
				return dato;
			}		
		}		
		
		throw new DatoNonPresenteException();	
	}

	public List<E> getDataCategory(String passw, String Category) throws CategoriaNonPresenteException, PasswordErrataException {
		if (passw==null || Category==null)
			throw new NullPointerException();
		if ( ! check(passw) )
			throw new PasswordErrataException();
		
		//cerca categoria
		List<E> list=new ArrayList<E> ();
		List<E> tmp=new ArrayList<E> ();
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		//check mi serve per poter uscire dal ciclo while subito dopo aver trovato la categoria senza quindi
		//dover necessariamente scorrere tutto.
		boolean check=true; 
			
		while (myIterator.hasNext() && check) {
			Categoria<E> p= myIterator.next();
			if ( ( (p.getCategoria()).equals(Category)) ) {
				//categoria trovata
				check=false;			
				//copio i dati in tmp e poi aggiungi i dati di tmp in list		
				tmp=p.getDati();
				list.addAll(tmp);
			}
		}	
		
		if (check==true)
			throw new CategoriaNonPresenteException();
		
		return list;
	}
	
	
	public Iterator<E> getIterator(String passw) {
		if (passw==null)
			throw new NullPointerException();
		
		ArrayList<E> dati= new ArrayList<E>();
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			//copio i dati nell'ArrayList dati.
			dati.addAll(p.getDati());
		}
		
		//ordino i dati rispetto al numero di like
		Collections.sort(dati);	
		
		return Collections.unmodifiableList(dati).iterator();
	}


	public void insertLike(String friend, E data) throws DatoNonPresenteException, LikePresenteException {
		if (friend==null || data==null)
			throw new NullPointerException();
		
		boolean check=true;
		//controllo se la categoria è presente//
		//scorro l'Array e cerco la Categoria Category
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext() && check) {
			Categoria<E> p= myIterator.next();
			//searchFriend: true se friend appartiene alla categoria, altrimenti false
			//searchDato: ritorna true se il dato di tipo E è presente nella struttura dati, false altrimenti.
			if ( p.searchFriend(friend) && p.searchDato(data) ) {
				check=false;
				p.addlike(data, friend);
				return; //esci dopo aver inserito il like al dato.
			}		
		}
		
		throw new DatoNonPresenteException();		
	}
		

	public Iterator<E> getFriendIterator(String friend) {
		if (friend==null)
			throw new NullPointerException();
		
		ArrayList<E> dati= new ArrayList<E>();
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		
		while (myIterator.hasNext()) {
			Categoria<E> p= myIterator.next();
			if (p.searchFriend(friend)) {
				//copio i dati nell'ArrayList dati.
				dati.addAll(p.getDati());
			}
		}
		
		return Collections.unmodifiableList(dati).iterator();
	}
	
	
	///// METODI DI UTILITA' /////
	
	/* @EFFECTS: stampa ogni categoria presente nella collezione */	
	public void Display(String passw) throws PasswordErrataException {
		if (passw==null)
			throw new NullPointerException();
		
		if (!check(passw) )
			throw new PasswordErrataException();
		
		for (Categoria<E> p : Categorie) {
			p.Display();
		}
	}

	
	//METODI CHECK
	
	/* @EFFECTS: ritorna true se credenziali corrette, altrimenti false */
	private boolean check(String passw) {
		return password.equals(passw);
	}
	
	/* @EFFECTS: ritorna true se la categoria è già presente, altrimenti false */	
	private boolean checkCategoria (String Category) {
		//scorro l'Array e cerco la Categoria Category
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		boolean check=true; 
			
		while (myIterator.hasNext() && check) {
			Categoria<E> p= myIterator.next();
			if ( ( (p.getCategoria()).equals(Category)) ) {
				//categoria trovata
				check=false;
			}
		}	
		return !check;
	}
	
	/* @EFFECTS: ritorna true se friend appartiene alla categoria, altrimenti false */
	private boolean checkFriend (String friend, String Category) {
		//scorro l'Array e cerco la Categoria Category
		Iterator<Categoria<E>> myIterator=Categorie.iterator();
		boolean check=true;		
					
		while (myIterator.hasNext() && check) {
			Categoria<E> p= myIterator.next();
			if ( ( (p.getCategoria()).equals(Category)) ) {
				//categoria trovata
				if (p.searchFriend(friend)) //ritorna true se la stringa friend è presente nella struttura dati, false altrimenti. 
					return true;
				else return false;			
			}
		}				
		
		return false;
	}
	
	
}