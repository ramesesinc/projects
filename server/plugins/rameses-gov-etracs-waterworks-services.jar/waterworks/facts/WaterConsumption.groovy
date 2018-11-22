package waterworks.facts;

import java.util.*;
import com.rameses.util.*;

public class WaterConsumption {
    int month = 0;
    int year = 0;
    int volume = 0;
    double amount = 0.0;
    double amtdue = 0.0;
    String status;
    Date duedate;
    String refid;
    Date disconnectiondate;
    double rate;

    public WaterConsumption(def o) {
		if(o.month) month = o.month;
		if(o.year)  year = o.year;
	    if(o.volume)  volume = o.volume;
	    if(o.amount) amount = o.amount;
	    if(o.status) status = o.status;
	    if(o.duedate) duedate = o.duedate;
	    if(o.amtdue) amtdue = o.amtdue; 
	    if(o.refid) refid = o.refid;
	    if(o.disconnectiondate) disconnectiondate = o.disconnectiondate;
    }

}
