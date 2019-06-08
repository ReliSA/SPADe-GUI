package ostatni;

import org.apache.log4j.Logger;

/**
 * Třída reprezentuje podmínku vytvořenou při vytváření SQL dotazu v GUI.
 *
 * @author Patrik Bezděk
 */
public class Condition {
    /**
     * Název sloupce podmínky
     */
    private String name;
    /**
     * Operátor v podmínce
     */
    private String operator;
    /**
     * Hodnota porovnání
     */
    private String value;
    /**
     * Typ hodnoty k porovnání
     */
    private String type;
    /**
     * Rozhodnutí o validitě hodnoty vzhledem k typu
     */
    private boolean valid;
    static Logger log = Logger.getLogger(Condition.class);

    /**
     * Základní konstruktor
     */
    public Condition(){}

    /**
     * Parametrický kontruktor třídy
     * @param name název sloupce podmínky
     * @param operator operátor porovnání
     * @param value hodnota k porovnání
     * @param type typ hodnoty
     */
    public Condition(String name, String operator, String value, String type) {
        this.name = name;
        this.operator = operator;
        this.value = value;
        this.type = type;
    }

    /**
     * Vrací název sloupce v podmínce
     * @return název sloupce v podmínce
     */
    public String getName() {
        return name;
    }

    /**
     * Nastaví název sloupce v podmínce
     * @param name název sloupce v podmínce
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Vrací operátor porovnání
     * @return operátor
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Nastaví operátor porovnání
     * @param operator operátor
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Vrací hodnotu k porovnání
     * @return hodnota k porovnání
     */
    public String getValue() {
        return value;
    }

    /**
     * Nastaví hodnotu k porovnání
     * @param value hodnota k porovnání
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Vrací typ hodnoty podmínky
     * @return typ hodnoty
     */
    public String getType() {
        return type;
    }

    /**
     * Nastaví typ hodnoty v podmínce
     * @param type typ hodnoty
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Nastavení validity zadané hodnoty vzhledem k typu
     * @param valid validita hodnoty
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Validace zadané hodnoty vzhlede k typu
     * @return true pokud hodnota souhlasí s typem
     */
    public boolean validate(){
        String value = this.getValue();
        switch(this.getType()){
            case "number":
                try { Float.parseFloat(value);
                } catch(NumberFormatException e) {
                    try { Double.parseDouble(value);
                    } catch(NumberFormatException e1) {
                        try { Integer.parseInt(value);
                        } catch(NumberFormatException e2) {
                            try { Long.parseLong(value);
                            } catch(NumberFormatException e3) {
                                log.warn(e3);
                                setValid(false);
                                return false;
                            }
                        }
                    }
                }
                break;
            case "boolean":
                if(value.length() != 1){
                    setValid(false);
                    return false;
                }
                if(value.charAt(0) == '0' || value.charAt(0) == '1'){
                    setValid(true);
                    return true;
                } else {
                    setValid(false);
                    return false;
                }
            case "date":
                // only DD-MM-YYYY pattern is allowed
                if(value.trim().matches("^([0-2][0-9]|(3)[0-1])(\\-)(((0)[0-9])|((1)[0-2]))(\\-)\\d{4}$")){
                    setValid(true);
                    return true;
                } else {
                    setValid(false);
                    return false;
                }
            case "text":
                setValid(true);
                return true;
        }
        setValid(true);
        return true;
    }
}
