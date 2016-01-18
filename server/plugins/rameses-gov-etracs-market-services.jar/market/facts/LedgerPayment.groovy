package market.facts;

import java.util.*;
import com.rameses.util.*;

public class LedgerPayment {
    
    int month;
    int year;
    private Date datepaid;
    double balance;

    public void setDatepaid( Date d ) {
        this.datepaid = d;
		def dt = new DateBean(this.datepaid);
		month = dt.month;
		year = dt.year;        
    }

    public Date getDatepaid() {
        return this.datepaid;        
    }
}