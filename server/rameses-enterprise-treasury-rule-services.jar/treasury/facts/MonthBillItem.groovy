package treasury.facts;

import java.util.*;
import com.rameses.util.*;
import com.rameses.functions.*;

/*********************************************************************************************************************************
* This is used for bill items that need year and month like schedules etc.
**********************************************************************************************************************************/
class MonthBillItem extends BillItem {

	def monthNames = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];
	int year;
	int month;
	Date duedate;
	
	//this is set assuming you are not starting using the whole month
	Date fromdate;
	Date todate;
	
	public int getPaypriority() {
	   return (year*12)+month;
	}

	public def toMap() {
		def m = super.toMap();
		m.year = year;
		m.month = month;
		m.monthname = monthname;
		m.sortorder = getSortorder();
		m.fromdate = fromdate;
		m.todate = todate;
		m.duedate = duedate;

		if(!m.fromdate || !m.todate) {
			def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			m.fromdate = df.parse(year+"-"+month+"-01");
			//if fromdate not provided, by default use beginning of month. 
			//if todate not provided, by default use end of month.
			def tmpDt = DateFunc.getMonthAdd(m.fromdate, 1);
			m.todate = DateFunc.getDayAdd(tmpDt, -1);
		}

		if(fromdate) m.fromday = getFromday();
		if(todate) m.today = getToday();

		return m;
	}

	public int getYearMonth() {
		return (year*12)+month;
	}

	public int hashCode() {
		def buff = new StringBuilder();
		buff.append( yearMonth );
		if( account?.objid ) {
			buff.append( "-" + account.objid  )
		};
		if( txntype ) {
			buff.append( "-" + txntype );
		}
		return buff.toString().hashCode();
	}

	public int getSortorder() {
		return getYearMonth();
	}	

	public String getMonthname() {
        return monthNames[ month - 1 ]; 
    }

    public int getToday() {
		def cal = Calendar.instance;
    	cal.setTime( todate );
    	return cal.get( Calendar.DAY_OF_MONTH );
    }

    public int getFromday() {
    	def cal = Calendar.instance;
    	cal.setTime( fromdate );
    	return cal.get( Calendar.DAY_OF_MONTH );
    }

    public int getNumdays() {
    	if( fromdate == null ) return 0;
    	if(todate == null) return 0;
    	return DateFunc.daysDiff( fromdate, todate ) + 1;
    }

    

}