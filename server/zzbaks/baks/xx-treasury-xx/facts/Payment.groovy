package market.facts;

import java.util.*;
import com.rameses.util.*;

public class Payment {
    
    def type;   //FULL or PARTIAL
    double amount;

    public Payment(o) {
    	amount = o.amount;
        type = o.type;
    }

}