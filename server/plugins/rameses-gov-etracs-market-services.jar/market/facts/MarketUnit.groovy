package market.facts;

import java.util.*;
import com.rameses.util.*;

public class MarketUnit {
    
    String unittype;            //STALL, TABLE
    String section;
    String attributes;

    double rate;

    private double convertDouble(def d) {
        if(d==null) return 0;
        return Double.parseDouble(d+"");
    }

    public MarketUnit(Map map) {
        this.section = map.section?.objid;
        if( map.attributes && (map.attributes instanceof List) ) {
            this.attributes = "-" + map.attributes*.objid.join("-" ) + "-";
        }        
        this.rate = convertDouble( map.rate );
    }


}