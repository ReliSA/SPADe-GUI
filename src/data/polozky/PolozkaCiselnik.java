package data.polozky;

/**
 * Třída položek zděděná ze třídy Polozka, k původním atributům id a nazev 
 * navíc přidává atributy idTrida, trida a supertrida.
 * Využívá se pro položky číselníků (priority, severity, resoluce, osoby, status, typ)
 */
public class PolozkaCiselnik extends Polozka {

	private static final long serialVersionUID = -3620899375049836196L;
	private int idTrida;			//identifikátor třídy
	private String trida;			//třída položky
	private String superTrida;		//supertřída položky

	/**
	 * Konstruktor třídy, nastaví atributy
	 * @param id id položky
	 * @param nazev název položky
	 * @param idTrida identifikátor třídy
	 * @param trida třída položky číselníku
	 * @param superTrida supertřída položky číselníku
	 */
	public PolozkaCiselnik(int id, String nazev, int idTrida, String trida, String superTrida) {
		super(id, nazev);
		this.idTrida = idTrida;
		this.trida = trida;
		this.superTrida = superTrida;
	}
	
	/**
	 * Vrátí identifikátor třídy
	 * @return identifikátor třídy
	 */
	public int getIdTrida(){
		return this.idTrida;
	}

	/**
	 * Vrátí třídu položky číselníku
	 * @return třída položky číselníku
	 */
	public String getTrida(){
		return this.trida;
	}
	
	/**
	 * Vrátí supertřídu položky číselníku
	 * @return supertřída položky číselníku
	 */
	public String getSuperTrida(){
		return this.superTrida;
	}
	
	/**
	 * Nastaví id třídy položky číselníku
	 * @param idTrida nové id třídy položky číselníku
	 */
	public void setIdTrida(int idTrida){
		this.idTrida = idTrida;
	}
	
	/**
	 * Nastaví třídu položky číselníku
	 * @param trida nová třída položky číselníku
	 */
	public void setTrida(String trida){
		this.trida = trida;
	}
	
	/**
	 * Nastaví supertřídu položky číselníku
	 * @param superTrida nová supertřída položky číselníku
	 */
	public void setSuperTrida(String superTrida){
		this.superTrida = superTrida;
	}
}
