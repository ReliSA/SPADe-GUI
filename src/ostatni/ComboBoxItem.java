package ostatni;

/**
 * Třída pro vytvoření položky do JComboBox reprezentující vytvořené konstanty a proměnné
 *
 * @author Patrik Bezděk
 */
public class ComboBoxItem {
    /**
     * Název konstanty/proměnné
     */
    private String name;
    /**
     * Typ konstanty/proměnné
     */
    private String type;
    /**
     * Hodnota konstanty/proměnné
     */
    private String value;

    /**
     * Konstruktor třídy
     * @param name název konstanty/proměnné
     * @param type typ konstanty/proměnné
     * @param value hodnota konstanty/proměnné
     */
    public ComboBoxItem(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Vrací název položky
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Vrací typ položky
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Vrací hodnotu položky
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Vrací název a hodnotu pro zobrazení v JComboBox
     * @return název a hodnotu položky
     */
    public String toString(){
        return this.name + " - " + this.value;
    }
}
