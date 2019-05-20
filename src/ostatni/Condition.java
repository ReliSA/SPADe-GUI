package ostatni;

import org.apache.log4j.Logger;

public class Condition {
    private String name;
    private String operator;
    private String value;
    private String type;
    private boolean valid;
    static Logger log = Logger.getLogger(Condition.class);

    public Condition(){}

    public Condition(String name, String operator, String value, String type) {
        this.name = name;
        this.operator = operator;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

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
