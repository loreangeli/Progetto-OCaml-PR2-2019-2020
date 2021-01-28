import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) throws PasswordErrataException, CategoriaNonPresenteException, FriendPresenteException, FriendNonPresenteException, DatoNonPresenteException {
		//scelgo come password "qwerty"
		Board<Integer, Data<Integer>> Bacheca= new Board<Integer, Data<Integer>>("qwerty");
		
		///(1) createCategory ///
		//aggiungo la categoria Basket e Calcio.
		try {
			Bacheca.createCategory("Basket", "qwerty");
			Bacheca.createCategory("Calcio", "qwerty");
		} catch (PasswordErrataException e) {
			e.printStackTrace();
		} catch (CategoriaPresenteException e) {
			e.printStackTrace();
		}
		System.out.println("stampa 1:");
		Bacheca.Display("qwerty");
		
		///(2) removeCategory ///
		//rimuovo la categoria Basket.
		try {
			Bacheca.removeCategory("Basket", "qwerty");
		} catch (PasswordErrataException e) {
			e.printStackTrace();
		} catch (CategoriaNonPresenteException e) {
			e.printStackTrace();
		}
		System.out.println("stampa 2:");
		Bacheca.Display("qwerty");	
		
		///(3) addFriend ///
		//aggiungi Silvia, Lorenzo e Emanuele alla categoria Calcio.
		Bacheca.addFriend("Calcio", "qwerty", "Silvia");
		Bacheca.addFriend("Calcio", "qwerty", "Lorenzo");
		Bacheca.addFriend("Calcio", "qwerty", "Emanuele");
		System.out.println("stampa 3:");
		Bacheca.Display("qwerty");
		
		///(4) removeFriend ///
		//rimuovi Silvia dalla categoria Calcio.
		Bacheca.removeFriend("Calcio", "qwerty", "Silvia");
		System.out.println("stampa 4:");
		Bacheca.Display("qwerty");
		
		///(5) put: inserisce dato ///
		//inserisco 12 e 14 e 10 (tre dati) nella categoria Calcio
		Data<Integer> dato1= new Data<Integer> (12);
		Data<Integer> dato2= new Data<Integer> (14);	
		Data<Integer> dato3= new Data<Integer> (10);
		Bacheca.put("qwerty", dato1, "Calcio");
		Bacheca.put("qwerty", dato2, "Calcio");
		Bacheca.put("qwerty", dato3, "Calcio");	
		System.out.println("stampa 5:");
		Bacheca.Display("qwerty");
		
		///(6) get ///
		//copia il dato 12 nella bacheca.
		Bacheca.get("qwerty", dato1);
		System.out.println("stampa 6:");
		Bacheca.Display("qwerty");
		
		///(7) remove ///
		//rimuove 12 (dato) dalla bacheca. 
		Bacheca.remove("qwerty", dato1);
		System.out.println("stampa 7:");
		Bacheca.Display("qwerty");
		
		///(8) getDataCategory ///
		//restituisce una lista di dati relativa alla categoria Calcio.
		List<Data<Integer>> list=new ArrayList<Data<Integer>>();
		try {
			list=Bacheca.getDataCategory("qwerty", "Calcio");	
		}
		catch(CategoriaNonPresenteException e) {
			e.printStackTrace();
		}
		System.out.println("stampa 8:");
		System.out.println(list);
		
		///(9) getIterator ///
		//restituisce l'iteratore che genera i dati di tutte le categorie e gli stampa ordinati rispetto al numero di like.
		System.out.println("stampa 9:");
		Iterator<Data<Integer>> myIterator;
		myIterator=Bacheca.getIterator("qwerty");	
		//scorro l'iteratore stampando il contenuto
		while (myIterator.hasNext()) {
			Data<Integer> p= myIterator.next();
			System.out.println(p);
		}		
			
		///(10) insertLike ///
		//Lorenzo e Emanuele aggiungono un like al dato 14. Lorenzo aggiunge un like al dato 12.
		System.out.println("stampa 10:");
		try {
			Bacheca.insertLike("Lorenzo", dato1);
			Bacheca.insertLike("Lorenzo", dato2);
			Bacheca.insertLike("Emanuele", dato2);
		} catch (LikePresenteException e) {
			e.printStackTrace();
		}
		
		Bacheca.Display("qwerty");
		
		///(11) getFriendIterator///
		//restituisce un iteratore che genera tutti i dati che sono stati condivisi con Lorenzo.
		System.out.println("stampa 11:");
		myIterator=Bacheca.getFriendIterator("Lorenzo");
		//scorro l'iteratore
		while (myIterator.hasNext()) {
			Data<Integer> p= myIterator.next();
			System.out.println(p);
		}
		
		///(12) getIterator ///
		//restituisce l'iteratore che genera i dati di tutte le categorie e gli stampa ordinati rispetto al numero di like.
		System.out.println("stampa 12:");
		myIterator=Bacheca.getIterator("qwerty");	
		//scorro l'iteratore stampando il contenuto
		while (myIterator.hasNext()) {
			Data<Integer> p= myIterator.next();
			System.out.println(p);
		}			
		
	}
	
}