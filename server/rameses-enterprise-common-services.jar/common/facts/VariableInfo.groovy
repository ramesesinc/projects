package common.facts;

/** Creates a new instance of Variable */
public class VariableInfo {
    
    String objid;
    String name;
    String datatype;
    String category;
    String caption;
    int sortorder;
    String stringvalue;
    int intvalue;
    boolean booleanvalue;
    double decimalvalue;
    
    public VariableInfo() {
    }

    public VariableInfo(def o) {
        this( o.objid, o.name, o.datatype, o.value );
    }    
    
    public VariableInfo(String objid, String name, String datatype, Object value) {
        this.objid = objid;
        this.name = name;
        this.datatype = datatype.toLowerCase();
        if( value!=null && (""+value).trim().length()>0 ) {
            if( datatype == "decimal" ) {
                decimalvalue = Double.parseDouble(value+"");
            } 
            else if(datatype == "integer") {
                intvalue =  Integer.parseInt(value+"");
            } 
            else if( datatype == "boolean") {
                String v = value.toString().toLowerCase().trim();
                if(v == "1" || v == "true") {
                    booleanvalue = true;
                } 
                else {
                    booleanvalue = false;
                }
            } 
            else if(datatype.startsWith("string")) {
                stringvalue = (String)value;
            }
        }
    }
    
    public def toMap() {
        def m = [:];
        m.objid = objid;
        m.name = name;
        m.category = category;
        m.sortorder = sortorder;
        m.caption = caption;
        m.datatype = datatype;
        if( datatype == "string" ) {
            m.value = stringvalue;
        }
        else if( datatype == "integer" ) {
            m.value = intvalue;
        }
        else if( datatype == "decimal" ) {
            m.value = decimalvalue;
        }
        else if( datatype == "boolean" ) {
            m.value = booleanvalue;
        }
        return m;
    }

}


