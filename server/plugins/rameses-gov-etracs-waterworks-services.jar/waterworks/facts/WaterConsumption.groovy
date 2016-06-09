package waterworks.facts;

import java.util.*;
import com.rameses.util.*;


public class WaterConsumption {

	int month;
	int year;
    int volume;
    double amount;
    double amtdue;
    String status;
    Date duedate;
    String refid;

    public WaterConsumption(def o) {
		month = o.month;
		year = o.year;
	    volume = o.volume;
	    amount = o.amount;
	    status = o.status;
	    duedate = o.duedate;
	    amtdue = o.amtdue; 
	    refid = o.refid;
    }
}
