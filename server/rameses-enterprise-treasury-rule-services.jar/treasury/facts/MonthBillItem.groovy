package treasury.facts;

import java.util.*;
import com.rameses.util.*;

/*********************************************************************************************************************************
* This is used for bill items that need year and month like schedules etc.
**********************************************************************************************************************************/
class MonthBillItem extends BillItem {

	int year;
	int month;
	
	def monthNames = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];

	public int getPaypriority() {
       return (year*12)+month;
    }

    public String getMonthname() {
        return monthNames[ month - 1 ]; 
    }

	public def toMap() {
		def m = super.toMap();
		m.year = year;
		m.month = month;
		m.monthname = monthname;
		return m;
	}

}