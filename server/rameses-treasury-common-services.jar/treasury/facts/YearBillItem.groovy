package treasury.facts;

import java.util.*;
import com.rameses.util.*;
import com.rameses.functions.*;

/*********************************************************************************************************************************
* This is used for bill items that need year and month like schedules etc.
**********************************************************************************************************************************/
class YearBillItem extends BillItem {

	int year;
	
	public YearBillItem(def o ) {
		super(o);
		if(o.year) year = o.year;
	}
	
	public YearBillItem() {}

	public int getPaypriority() {
	   return (year*12)+1;
	}

	public def toMap() {
		def m = super.toMap();
		m.year = year;
		return m;
	}

	//in the hash code priority is the txntype not the accout code
	public int hashCode() {
		def buff = new StringBuilder();
		buff.append( year );
		if( txntype ) {
			buff.append( "-" + txntype );
		}
		else if( account?.objid ) {
			buff.append( "-" + account.objid  )
		};
		return buff.toString().hashCode();
	}

	public int getSortorder() {
		return getYear() + super.getSortorder();
	}	


}