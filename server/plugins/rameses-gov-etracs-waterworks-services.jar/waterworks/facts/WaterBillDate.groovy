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

<<<<<<< HEAD
	public WaterBillDate( def m ) {
		if(m.year) year = m.year;
		if(m.month) month = m.month;
		if(m.fromperiod) fromperiod = m.fromperiod;
		if(m.toperiod) toperiod = m.toperiod;
		if(m.duedate) duedate =  m.duedate;
		if(m.disconnectiondate) disconnectiondate = m.disconnectiondate;
	}	
=======
	public WaterBillDate( def o ) {
		if(o.year) year = o.year;
		if(o.month) month = o.month;
		if(o.fromperiod) fromperiod = o.fromperiod;
		if(o.toperiod) toperiod = o.toperiod;
		if(o.duedate) duedate = o.duedate;
		if(o.disconnectiondate) disconnectiondate = o.disconnectiondate;
	}
>>>>>>> c620ecd866ec669d2d5c4c56154c3101aa5ac698

}
