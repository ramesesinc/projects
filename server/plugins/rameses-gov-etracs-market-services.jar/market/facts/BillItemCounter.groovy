package market.facts;

import java.util.*;
import com.rameses.util.*;

public class BillItemCounter {
    
    Date fromdate;
    Date todate;
    Date date;
    double rate;

    public BillItemCounter(Date startdate, Date enddate, double rate) {
        this.date = new DateBean(startdate).date;
        this.fromdate = this.date;
        this.todate = new DateBean(enddate).date;
        this.rate = rate;
    }

    boolean hasNext() {
    	return this.date <= this.todate;
    }

    boolean moveNext() {
    	this.date = DateUtil.add( this.date, "1M" );     	
    }

}