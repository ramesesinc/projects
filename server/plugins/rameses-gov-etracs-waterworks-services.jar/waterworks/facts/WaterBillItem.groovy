package waterworks.facts;

import java.util.*;
import com.rameses.util.*;
import treasury.facts.*;

public class WaterBillItem extends BillItem {

	int month;
	int year;
	int priority;

	def toItem() {
		def m = super.toMap();
		m.year = year;
		m.month = month; 
		m.priority = priority;
		return m;		
	}	

	public int hashCode() {
		return (""+ year +"-"+ month +"-"+ txntype).hashCode();
	}	
}	