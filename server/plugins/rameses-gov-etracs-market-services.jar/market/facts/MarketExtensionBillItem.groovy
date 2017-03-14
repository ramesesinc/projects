package market.facts;

import java.util.*;
import treasury.facts.*;

public class MarketExtensionBillItem extends BillSubItem {
    
    String txntype = "extrate";
    int sortorder = 100;

    public def toMap() {
        def m = super.toMap();
        m.sortorder = (parent.year*12)+parent.month;
        return m;
    }

}