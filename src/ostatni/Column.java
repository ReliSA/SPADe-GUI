package ostatni;

/**
 * Tato třída představuje strukturu sloupce v tabulce
 *
 * @author Patrik Bezděk
 */
public class Column {
    /**
     * Název sloupce
     */
    private String name;
    /**
     * Typ sloupce
     */
    private String type;

    /**
     * Konstruktor třídy
     * @param name název sloupce
     * @param type typ sloupce
     */
    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Vrací název sloupce
     * @return název sloupce
     */
    public String getName() {
        return name;
    }

    /**
     * Vrací typ sloupce.
     * @return typ sloupce
     */
    public String getType() {
        return type;
    }
}
