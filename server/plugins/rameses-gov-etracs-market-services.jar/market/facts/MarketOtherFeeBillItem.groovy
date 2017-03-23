package market.facts;

import java.util.*;
import treasury.facts.*;
import com.rameses.util.*;

public class MarketOtherFeeBillItem extends BillItem {
    
    int month;
    int year;
    def type;
    String monthname;
    Date duedate;

    public int getPaypriority() {
       return (year*12)+month;
    }

    public def toMap() {
        def m = super.toMap();
        m.month = month;
        m.year = year;
        m.monthname = monthname;
        if(m.surcharge==null) m.surcharge = 0;
        if(m.interest==null) m.interest = 0;
        return m;
    }

    public int hashCode() {
        return refid.hashCode(); 
    }


}