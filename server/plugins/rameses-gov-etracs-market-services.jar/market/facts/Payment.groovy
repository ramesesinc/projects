package market.facts;

import java.util.*;
import com.rameses.util.*;

public class Payment {
    
    double amount;
    double balance;
    Date date;
    Date todate;

    public Payment(o) {
    	amount = o.totalpaid;
    	balance = amount;
    }

    void deduct( double val ) {
    	balance = NumberUtil.round(balance - val);
    }
    
    boolean hasNext() {
        return this.date <= this.todate;   
    }

    void moveNext() {
        this.date = DateUtil.add( this.date, "1M" );    
    }
}