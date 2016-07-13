package waterworks.facts;

import com.rameses.util.*;
import treasury.facts.*;
import java.util.*;

public class WaterBillDate {

	int year;
	int month;

	Date fromperiod;
	Date toperiod;
	Date duedate;
	Date disconnectiondate;

	public WaterBillDate( def o ) {
		if(o.year) year = o.year;
		if(o.month) month = o.month;
		if(o.fromperiod) fromperiod = o.fromperiod;
		if(o.toperiod) toperiod = o.toperiod;
		if(o.duedate) duedate = o.duedate;
		if(o.disconnectiondate) disconnectiondate = o.disconnectiondate;
	}

}
