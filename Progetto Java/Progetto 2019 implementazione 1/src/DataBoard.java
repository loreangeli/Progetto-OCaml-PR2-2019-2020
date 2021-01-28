import java.util.Iterator;
import java.util.List;

/*
Overview: tipo di dato modificabile che rappresenta una bacheca costituita da una collezione di oggetti generici che 
estendono il tipo di dato Data. Il proprietario della bacheca può definire le proprie categorie e stilare una lista di
contatti (amici) a cui saranno visibili i dati per ogni tipologia di categoria. I dati condivisi sono visibili 
solamente in lettura: gli amici possono visualizzare il dato ma possono essere modificati solamente dal proprietario 
della bacheca. Gli amici possono associare un “like” al contenuto condiviso.

Elemento tipico: Sia <{password}, {Categoria1, Categoria2, ...}> un insieme di dimensione arbitraria. L'insieme indica l'insieme delle categorie presenti 
nella collezione ognuna identificata da un indice. Ogni Categoria della collezione ha un elemento tipico definito in questo modo: 
<{ (categoria), (<Dato0,Dato1,Dato2, …>), (<amico0,amico1,amico2, …>) }>.  A sua volta ogni Dato della collezione ha un elemento tipico definito in questo 
modo: <{ (dato), (like), (<amico1,amico2,…>)}>.

Nel seguito indicherò:
-La password del proprietario della bacheca con password.  
-L'insieme {categoria1,categoria2,...} con domC.
-L'insieme {amico0,amico1,amico2,...} corrispondende a una particolare categoria con domF.
-L'insieme {dato0,dato1,dato2,...} corrispondente a una particolare categoria con domD.

*/

public interface DataBoard<E extends Data<?>> {

/*
@REQUIRES: Category!=null && passw!=null
@MODIFIES: this
@EFFECTS: crea l'identità di una categoria se vengono rispettati i controlli di identità.
@THROWS: se (Category==null || passw==null) lancia NullPointerException, 
(se Category appartiene domC) lancia CategoriaPresenteException,
se (passw!=password) lancia PasswordErrataException.
*/
public void createCategory(String Category, String passw) throws PasswordErrataException, CategoriaPresenteException;

/*
@REQUIRES: Category!=null && passw!=null
@MODIFIES: this
@EFFECTS: rimuove una categoria se vengono rispettati i controlli di identità.
@THROWS: se (Category==null || passw==null) lancia NullPointerException,  
se (Category non appartiene a domC) lancia CategoriaNonPresenteException,
se (passw!=password) lancia PasswordErrataException.
*/
public void removeCategory(String Category, String passw) throws PasswordErrataException, CategoriaNonPresenteException;

/*
@REQUIRES: Category!=null && passw!=null && friend!=null
@MODIFIES: this
@EFFECTS: aggiunge un amico ad una categoria di dati se vengono rispettati i controlli di
identità.
@THROWS: se (Category==null || passw==null || friend==null) lancia NullPointerException, 
se (Category non appartiene a domC) lancia CategoriaNonPresenteException, 
se (friend appartiene a domF di Category) lancia FriendPresenteException,
se (passw!=password) lancia PasswordErrataException.
*/
public void addFriend(String Category, String passw, String friend) throws PasswordErrataException, CategoriaNonPresenteException, FriendPresenteException;

/*
@REQUIRES: Category!=null && passw!=null && friend!=null
@MODIFIES: this
@EFFECTS: rimuove un amico da una categoria di dati se vengono rispetti i controlli di identità.
@THROWS: se (Category==null || passw==null || friend==null) lancia NullPointerException,
se (Category non appartiene a domC) lancia CategoriaNonPresenteException, 
se (friend non appartiene a domF di Category) lancia FriendNonPresenteException,
se (passw!=password) lancia PasswordErrataException.
*/
public void removeFriend(String Category, String passw, String friend) throws FriendNonPresenteException, CategoriaNonPresenteException, PasswordErrataException, FriendPresenteException;

/*
@REQUIRES: passw!=null && dato!=null && categoria!=null
@MODIFIES: this
@EFFECTS: inserisce un dato in bacheca se vengono rispettati i controlli di identità.
@THROWS: se (passw==null || dato==null || categoria==null) lancia NullPointerException, 
se (passw!=password) lancia PasswordErrataException.
*/
public boolean put(String passw, E dato, String categoria) throws CategoriaNonPresenteException, PasswordErrataException;

/*
@REQUIRES: passw!=null && dato!=null.
@EFFECTS: ottiene una copia del dato in bacheca se vengono rispettati i controlli di identità e il dato si trova in Bacheca,
restituisce null altrimenti.
@THROWS: se (passw==null || dato==null) lancia NullPointerException,
se (passw!=password) lancia PasswordErrataException.
*/
public E get(String passw, E dato) throws PasswordErrataException;

/*
@REQUIRES: passw!=null && dato!=null.
@MODIFIES: this.
@EFFECTS: rimuove il dato dalla bacheca se vengono rispettati i controlli di identità.
@THROWS: se (passw==null || dato==null) lancia NullPointerException, 
se (passw!=password) lancia PasswordErrataException, se (dato non appartiene a domD) lancia DatoNonPresenteException.
*/
public E remove(String passw, E dato) throws PasswordErrataException, DatoNonPresenteException;

/*
@REQUIRES: passw!=null && Category!=null.
@EFFECTS: crea una lista dei dati in bacheca su una determinata categoria se vengono rispettati i controlli di identità.
@THROWS: se (passw==null || Category==null) lancia NullPointerException, 
se (passw!=password) lancia PasswordErrataException,
se (Category non appartiene a domC) lancia CategoriaNonPresenteException. 
*/
public List<E> getDataCategory(String passw, String Category) throws CategoriaNonPresenteException, PasswordErrataException;

/*
@REQUIRES: passw!=null.
@EFFECTS: restituisce un iteratore (senza remove) che genera tutti i dati in bacheca ordinati rispetto al numero di like.
@THROWS: se (passw!=password) lancia PasswordErrataException.
*/
public Iterator<E> getIterator(String passw);

/*
@REQUIRES: friend!=null && data!=null
@MODIFIES:this.like
@EFFECTS: aggiunge un like a un dato.
@THROWS: se (friend==null || data==null) lancia NullPointerException,
se (friend non appartiene a domF) lancia FriendNonPresenteException,
se (dato non appartiene a domD) lancia DatoNonPresenteException.
*/
public void insertLike(String friend,E data) throws DatoNonPresenteException, LikePresenteException;

/*
@REQUIRES: friend!=null 
@EFFECTS: legge un amico e restituisce un iteratore (senza remove) che genera tutti i dati in bacheca condivisi con quell'amico.
@THROWS: se (friend==null) lancia NullPointerException
*/
public Iterator<E> getFriendIterator(String friend);

//altre operazione da definire a scelta

/*
@REQUIRES: passw!=null
@EFFECTS: stampa tutte le categorie e per ogni categoria i dati presenti e gli amici collegati a quella determinata categoria. 
@THROWS: se (passw==null) lancia NullPointerException, se (passw!=password) lancia PasswordErrataException.
*/
public void Display(String passw) throws PasswordErrataException;


}