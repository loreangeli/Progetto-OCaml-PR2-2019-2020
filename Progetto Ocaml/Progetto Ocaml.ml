exception NotInteger;;
exception NotBoolean;;
exception NotDictionary;;

type ide = string;;

type exp = 
	  Eint of int 
	| Ebool of bool 
	| Estring of string 
	| Den of ide 
	| Prod of exp * exp 
	| Sum of exp * exp 
	| Diff of exp * exp 
	| Eq of exp * exp 
	| Minus of exp 
	| Min of exp * exp 
	| IsZero of exp 
	| Or of exp*exp 
	| And of exp*exp 
	| Not of exp 
	| Ifthenelse of exp * exp * exp 
	| Let of ide * exp * exp 
	| Fun of ide list * exp 
	| FunCall of exp * exp list 
	| Letrec of ide * exp * exp 
	| EDict of (ide*exp) list 
	| Insert of exp * ide * exp 
	| Remove of exp * ide 
	| Select of exp * ide 
	| Clear of exp 
	| Iterate of exp * exp 
	| Has_key of exp * ide 
	| Fold of exp * exp 
	| Filter of exp * ide list;;

(* Insert-> inserisce coppia <chiave,valore> nel dizionario | Remove-> prende come argomenti il Dizionario e una chiave e rimuove coppia <chiave,valore> dal dizionario | Select-> prende come argomenti il Dizionario
e una chiave e restituisce valore associato a quella chiave | Clear-> prende in ingresso un Dizionario e restituisce un Dizionario vuoto | Has_key-> prende come argomenti un Dizionario e una chiave e controlla l'esistenza 
di quella chiave | Iterate-> prende come argomenti una funzione  e un Dizionario e applica la funzione a tutti gli elementi del dizionario |
Fold-> prende come argomenti una funziona e un Dizionario e applica la funzione sequenzialmente a tutti gli elementi del Dizionario e restituisce il valore ottenuto *) 


(*tipi esprimibili*)
 type evT = Int of int 
	| Bool of bool 
	| String of string 
	| Unbound 
	| FunVal of evFun 
	| RecFunVal of ide * evFun 
	| Dict of (ide*evT) list 
	| Empty

and evFun = ide list * exp * env and 

(*ambiente*)
env = (ide*evT) list;;

let sEnV=[];; (* inizializzazione ambiente vuoto *)

(* Binding di una coppia <identificatore, valore> nell'ambiente *)
let rec bind ambiente identificatore valore = (identificatore,valore)::ambiente;;

(* Binding di una lista di coppie <identificatore, valore> nell'ambiente.  e=environment  i=lista di identificatori  v=lista di valori *)
let rec bindList e  i v = match (i,v) with   
	|([],[]) -> e
	|(x::xs,y::ys)-> let ris = bind e x y in bindList ris xs ys 
	|_ -> failwith("Errore nella bindlist");;

(* restituisce il valore associato all'identificatore *)
let rec applyenv e i = match e with
                        |(ide,valore)::envir -> if ide = i then valore else applyenv envir i 
                        |_ -> failwith("Nessun identificatore valido");;


(*rts*)
(* type checking dinamico *)
let typecheck (s : string) (v : evT) : bool = match s with
	"int" -> (match v with
		Int(_) -> true |
		_ -> false) |
	"bool" -> (match v with
		Bool(_) -> true |
		_ -> false) |
	"string" -> (match v with
		String(_) -> true |
		_ -> false) |
	"Dizionario" -> (match v with
		Dict(u) -> true |
		_ -> false) |
	_ -> failwith("not a valid type");;

(*funzioni primitive*)
let prod x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Int(n*u)
		 |_ -> raise NotInteger )
	else failwith("Type error");;

let sum x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Int(n+u)
		 |_ -> raise NotInteger )
	else failwith("Type error");;

let diff x y = if (typecheck "int" x) && (typecheck "int" y)
	then (match (x,y) with
		(Int(n),Int(u)) -> Int(n-u)
		 |_ -> raise NotInteger )
	else failwith("Type error");;

let eq x y = if ( (typecheck "int" x && typecheck "int" y) || (typecheck "bool" x && typecheck "bool" y) || (typecheck "string" x && typecheck "string" y) )
	then (match (x,y) with
		(Int(n),Int(u)) -> Bool(n=u)
		|(Bool(n),Bool(u)) -> Bool(n=u)	
		|(String(n),String(u)) -> Bool(n=u) 
		|_ -> Bool(false) )
	else failwith("Type error");;

(* se inserisco Int restituisce -Int | se inserisco Bool restituisco not Bool *)
let minus x = if (typecheck "int" x || typecheck "bool" x) 
	then (match x with
	   	Int(n) -> Int(-n)
		|Bool(n) -> Bool(not true) 
		|_ -> Bool(false) )
	else failwith("Type error");;

let iszero x = if (typecheck "int" x)
	then (match x with
		Int(n) -> Bool(n=0)
		|_ -> raise NotInteger)
	else failwith("Type error");;

(* Or logico *)
let _or x y = if (typecheck "bool" x) && (typecheck "bool" y)
	then (match (x,y) with
		(Bool(b),Bool(e)) -> (Bool(b||e))
		 |_ -> raise NotBoolean )
	else failwith("Type error");;

(* And logico *)
let _and x y = if (typecheck "bool" x) && (typecheck "bool" y)
	then (match (x,y) with
		(Bool(b),Bool(e)) -> Bool(b&&e)
		 |_ -> raise NotBoolean )
	else failwith("Type error");;

(* Not logico *)
let _not x = if (typecheck "bool" x)
	then (match x with
		 Bool(true) -> Bool(false) 
		|Bool(false) -> Bool(true)
		|_ -> raise NotBoolean )
	else failwith("Type error");;

let min x y = if (typecheck "int" x) && (typecheck "int" y)
  then Bool(x < y)
  else failwith("Type error");;


(*interprete*)
let rec eval ((e : exp), (r : env)) : evT = match e with
	Eint n -> Int n 
	|Estring n -> String n 
	|Ebool b -> Bool b 
	|IsZero a -> iszero (eval(a, r)) 
	|Den i -> applyenv r i 
	|Eq(a, b) -> eq (eval(a, r)) (eval(b, r)) 
	|Prod(a, b) -> prod (eval(a, r)) (eval(b, r)) 
	|Sum(a, b) -> sum (eval(a, r)) (eval(b, r)) 
	|Diff(a, b) -> diff (eval(a, r)) (eval(b, r)) 
	|Minus a -> minus (eval(a, r)) 
	|Min(a,b) -> min (eval(a, r)) (eval(b, r)) 
	|And(a, b) -> _and (eval(a, r)) (eval(b, r)) 
	|Or(a, b) -> _or (eval(a, r)) (eval(b, r)) 
	|Not a -> _not (eval(a, r)) 
	|Ifthenelse(a, b, c) -> 
		let g = (eval(a, r)) in
			if (typecheck "bool" g) 
				then (if g = Bool(true) then (eval(b, r)) else (eval(c, r)))
				else failwith ("nonboolean guard") 
	|Let(i, e1, e2) -> eval(e2, (bind r i (eval(e1, r)))) 
	|Fun(i, a) -> FunVal(i, a, r) 
	|FunCall(f, eArg) -> 
		let fClosure = (eval(f, r)) in
			(match fClosure with
        |FunVal(args, fBody, fDecEnv) -> 
          let qe = (eval_list eArg r) in 
					  eval(fBody, bindList fDecEnv args qe)
				|RecFunVal(g, (args, fBody, fDecEnv)) -> 
					let qe = (eval_list eArg r) in
						let rEnv = (bind fDecEnv g fClosure) in
							eval(fBody,(bindList rEnv args qe))
				|_ -> failwith("non functional value")) 
        |Letrec(f, funDef, letBody) ->
        	(match funDef with
         		 Fun(i, fBody) -> let r1 = (bind r f (RecFunVal(f, (i, fBody, r)))) in
           			 eval(letBody, r1) |
       			 _ -> failwith("non functional def")) 

	|EDict(lista) -> let x= eval_Dict lista r in if checkduplicate x = false then failwith("errore: lista con duplicati") else
					Dict(x) 
	|Insert(lista, key, valore) -> (match eval(lista, r) with
						Dict(q) -> if has_key q key = false then (let v = eval(valore, r) in
								Dict((key, v)::q) ) else failwith("errore: chiave presente")
						|_ -> raise NotDictionary ) 
	|Remove(lista, key) -> (match eval(lista, r) with
				Dict(q) -> let w= elimina q key in
					Dict(w) 
				|_ -> failwith("not dictionary value") ) 
	|Select(lista, key) -> (match eval(lista, r) with
				Dict(q) -> select q key 
				|_ -> failwith("not dictionary value") ) 
	|Clear(lista) -> (match eval(lista, r) with
				Dict(q) -> Dict([])
				|_ -> failwith("not dictionary value") ) 
	|Has_key(lista, key) ->  (match eval(lista, r) with
				Dict(q) -> if has_key q key = true then Bool true else Bool false  
				|_ -> failwith("not dictionary value"))
	|Iterate(func,dic) -> (match eval(dic,r) with
				Dict(q) -> let newq = applyFun(q,eval(func,r),r) in Dict(newq)
				|_ -> failwith("not dictionary value"))
	|Fold(func,dic) -> (match eval(dic,r) with
		Dict(q) -> fold(q,eval(func,r),r,Int(0))
		|_ -> failwith("not dictionary value"))
		
    	|Filter(dic, lista) -> match eval(dic, r) with
                Dict(q) -> let newq= filter(q, lista) in
                            Dict(newq)
                |_ -> failwith("not dictionary value")
                            
    	and 
       	    filter(dict, lista) = match dict with
            [] -> []
            |(a, b)::xs -> if has_key2 a lista = true then (a, b)::filter(xs, lista) else filter(xs, lista)
   	and 
		has_key2 a lista = match lista with (* list=lista, a=chiave -> restituisce true se esiste una chiave a presente nella lista, false altrimenti *)
			[] -> false 
			| b::xs -> if  a=b then true else has_key2 a xs 

	and 
		checkduplicate list = match list with  (* controlla la presenza di duplicati all'interno della lista -> restituisce true se non ci sono duplicati, false altrimenti *)
			[] -> true
			|(b ,c)::xs -> if has_key xs b = true then false else checkduplicate xs 
	
	and
		eval_Dict v r = match v with	(* v=lista di tipo (ide*exp) list, r=ambiente -> restituisce una lista di tipo (ide*evT) list *)
 			[] -> []
			| (key,elem)::xs -> let v = eval(elem, r) in 
				(key, v)::eval_Dict xs r
	and 
		elimina list a = match list with (* list=lista, a=chiave -> elimina la coppia <a,valore> dalla lista *)
			[] -> [] 
			| (b,c)::xs -> if b=a then xs else (b,c)::elimina xs a 
	and 
		select list a = match list with (* list=lista, a=chiave -> restituisce il valore associato alla coppia <a,valore> presente nella lista, se presente *)
			[] -> Unbound
			| (b,c)::xs -> if  a=b then c else select xs a 
	and 
		has_key list a = match list with (* list=lista, a=chiave -> restituisce true se esiste una coppia <a,valore> presente nella lista, false altrimenti *)
			[] -> false 
			| (b,c)::xs -> if  a=b then true else has_key xs a 
        and
   	        eval_list list r = match list with 
    			[] -> []
			|x::xs -> eval(x,r) :: eval_list xs r
	and	
		applyFun(q,f,r) = ( match q with																	(*Applicazione funzione ad ogni elemento del dizionario*)
			[] -> []
			|(i,v)::qs -> (match f with
				|FunVal(args, fBody, fDecEnv) -> 
					(i,eval(fBody, bindList fDecEnv args [v]))::applyFun(qs,f,r)
				|RecFunVal(g, (args, fBody, fDecEnv)) -> 
					let rEnv = (bind fDecEnv g f) in
					(i,eval(fBody,(bindList rEnv args [v])))::applyFun(qs,f,r)
				|_ -> failwith("non functional value")))
	and
		fold(q,f,r,acc) = ( match q with
			[] -> acc
			|(i,v)::qs -> (match f with
				|FunVal(args, fBody, fDecEnv) -> 
					fold(qs,f,r,eval(fBody, bindList fDecEnv args [acc;v]))
				|RecFunVal(g, (args, fBody, fDecEnv)) -> 
					let rEnv = (bind fDecEnv g f) in
					fold(qs,f,r,eval(fBody,(bindList rEnv args [acc;v])))
				|_ -> failwith("non functional value")) );;
 
(* =============================  TESTS  ================= *)


(* === Test operazioni base === *)
(*Valutazione della somma fra 3 interi*)
eval(Sum(Eint(5),Sum(Eint(3),Eint(5))),sEnV);;     
(*Valutazione della differenza fra 2 interi*)
eval(Diff(Eint(12),Eint(5)),sEnV);;                
(*Valutazione della moltiplicazione fra 2 interi*)
eval(Prod(Eint(3),Eint(5)),sEnV);;                 
(*Valutazione del cambio di segno di un intero*)
eval(Minus(Eint(3)),sEnV);;                        
(*Valutazione del not di un booleano*)
eval(Minus(Ebool(true)),sEnV);;  
(* Test Uguaglianza tra due interi *)
eval(Eq(Eint 3, Eint 4), sEnV);; 				  
(* Test Uguaglianza tra due booleani *)
eval(Eq(Ebool(true), Ebool(false)), sEnV);; 
(* Test Uguaglianza tra due stringhe *)
eval(Eq(Estring("ciao"), Estring("ciao")), sEnV);;
(* Controlla se l'intero è uguale a 0 *)
eval(IsZero(Eint(3)), sEnV);;
(* Test che restituisce il minimo tra due interi *)
eval(Min(Eint(3), Eint(4)), sEnV);;

(* === Test Booleani === *)
(* Test AND tra due booleani *)
eval(And(Ebool(true), Ebool(false)), sEnV);; 			  
(* Valutazione di (a ∨ ¬a) *)
eval(Let("a",Ebool(true),Or(Den("a"),Not(Den("a")))),sEnV);;      
(*Valutazione di (a ∧ b) ∨ (a ∧ c) *)
eval(Let("a",Ebool(true),Let("b",Ebool(false),Let("c",Ebool(true), Or(And(Den("a"),Den("b")),And(Den("a"),Den("c")))))),sEnV);;   
(*Valutazione di (a ∧ b) ∧ (a ∧ c) *)
eval(Let("a",Ebool(true),Let("b",Ebool(false),Let("c",Ebool(true), And(And(Den("a"),Den("b")),And(Den("a"),Den("c")))))),sEnV);;  

(* === Test Funzioni === *)
(*Funzione che somma tre numeri*)
eval(Let("FunSomma",Fun(["a";"b";"c"],Sum(Sum(Den("a"),Den("b")),Den("c"))),FunCall(Den("FunSomma"),[Eint(10);Eint(20);Eint(30)])),sEnV);;
(*Esecuzione funzione di Fibonacci ricorsiva: let rec Fibo n = if n < 2 then 1 else Fibo (n - 2) + Fibo (n - 1) in Fibo 10 *)
eval(Letrec("Fibo",Fun(["n"],Ifthenelse(Min(Den("n"),Eint(3)),Eint(1),Sum(FunCall(Den("Fibo"),[Diff(Den("n"), Eint(2))]),FunCall(Den("Fibo"),[Diff(Den("n"), Eint(1))])))),FunCall(Den("Fibo"),[Eint(10)])),sEnV);;

(* === Test Dizionario === *)
(* creazione di un dizionario vuoto *)
eval(Let("Dictionary1",EDict([]),Den("Dictionary1")),sEnV);; 
(* creazione di un dizionario con chiavi duplicate per vedere se lancia un errore. *)
eval(Let ("Dictionary2", EDict ([("name", Estring "Mario");("matricola",Eint(123456));("matricola",Eint(156456))]), Den ("Dictionary2")), sEnV);;                                           
(* creazione di un dizionario con valori *)
eval(Let ("Dictionary2", EDict ([("name", Estring "Mario");("matricola",Eint(123456))]), Den ("Dictionary2")), sEnV);; 
(* inserimento nel dizionario di nome=Luca *)
eval(Let("Dictionary3",EDict([("name",Estring "Mario" );("matricola",Eint(123456))]),                       
	Insert(Den("Dictionary3"),"nome",Estring "Luca" )),sEnV);;
(* inserimento nel dizionario della chiave "name" per vedere se lancia un errore. (La chiave "name" è già stata inserita) *)
eval(Let("Dictionary31",EDict([("name",Estring "Mario" );("matricola",Eint(123456))]),                       
	Insert(Den("Dictionary31"),"name",Estring "Mario" )),sEnV);;
(* accesso al valore corrispondente alla chiave nome *)
eval(Let("Dictionary4",EDict([("name",Estring "Mario");("matr",Eint(123456));("nome",Estring "Luca")]), 		       
Select(Den("Dictionary4"),"nome")),sEnV);;
(* accesso al valore corrispondente alla chiave matr *)
eval(Let("Dictionary5",EDict([("name",Estring "Mario");("matr",Eint(123456));("nome",Estring "Luca")]),		               
Select(Den("Dictionary5"),"matr")),sEnV);;
(* eliminazione dal dizionario di name *)
eval(Let("Dictionary6",EDict([("name",Estring("Mario"));("matr",Eint(123456));("nome",Estring "Luca")]), 		       
	 Remove(Den("Dictionary6"),"name")),sEnV);;
(* controllo se la chiave nome è presente nel dizionario *)
eval(Let("Dictionary7",EDict([("matr",Eint(123456));("ome",Estring("Luca"))]), 		       
Has_key(Den("Dictionary7"),"nome")),sEnV);;

(* Iterate *)													
eval(Let("Dictionary8",EDict([("Mario",Eint(1));("Luca",Eint(2))]), 		   		        
	Let("Fun",Fun(["a"],Sum(Den("a"),Eint(1))),
		Iterate(Den("Fun"),Den("Dictionary8")))),sEnV);;
(* fold *)
eval(Let("Dictionary9",EDict([("Mario",Eint(1));("Luca",Eint(2));("William",Eint(120));("Lorenzo",Eint(3))]), 		   
	Let("Fun",Fun(["acc";"a"],Sum(Den("a"),Den("acc"))),
		Fold(Den("Fun"),Den("Dictionary9")))),sEnV);;
		
(* filter *)
eval(Let("Dictionary10",EDict([("Mario",Eint(1));("Luca",Eint(2));("William",Eint(120));("Lorenzo",Eint(3))]), 		   
	Filter(Den("Dictionary10"), ["Mario";"William"])),sEnV);;

