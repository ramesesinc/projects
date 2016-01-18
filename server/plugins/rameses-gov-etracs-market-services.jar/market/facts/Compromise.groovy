package market.facts;

import java.text.*;
import java.util.*;
import com.rameses.util.*;

public class Compromise {
    
    Date todate;
   
    public Compromise(Map map) {
        this.todate = new DateBean( map.todate ).date;
    }

    public Date getMonthAfterEnddate(int day) {
    	def db = new DateBean(this.todate);
    	int mon = db.month-1;
    	if( db.day >= day) mon=mon+1;
    	Calendar cal = Calendar.getInstance();
    	cal.set( Calendar.YEAR, db.year );
    	cal.set( Calendar.MONTH, mon );
    	cal.set( Calendar.DATE, day );
    	return cal.getTime();
    }
}