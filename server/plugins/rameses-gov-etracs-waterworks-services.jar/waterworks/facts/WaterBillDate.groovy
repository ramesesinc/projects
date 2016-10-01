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

	public WaterBillDate( def m ) {
		if(m.year) year = m.year;
		if(m.month) month = m.month;
		if(m.fromperiod) fromperiod = m.fromperiod;
		if(m.toperiod) toperiod = m.toperiod;
		if(m.duedate) duedate =  m.duedate;
		if(m.disconnectiondate) disconnectiondate = m.disconnectiondate;
	} 
}
