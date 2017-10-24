package treasury.facts;

import java.util.*;
import com.rameses.util.*;

class BillItem extends AbstractBillItem {

	String refid; 
	String ledgertype; 

	//amount that is left unpaid from the full amount
	double partialunpaid;
	/*
	public BillItem(def o) {
		copy(o);
	}
	*/
	
	//pay priority is only used during apply payment and will not be used anywhere else. This is defined by the extending class.
	int paypriority = 0;
	
	LinkedHashSet<BillSubItem> items = new LinkedHashSet<BillSubItem>();

	public def getTotals( def txntype ) {
		return items.findAll{ it.txntype == txntype }.sum{it.amount};
	}

	public double getTotal() {
		if(items.size()>0) {
			return  NumberUtil.round( amount + items.sum{ it.amount } );		
		}
		else {
			return NumberUtil.round( amount );
		}
	};

	public int hashCode() {
		if( refid ) {
			return refid.hashCode();
		} 
		else {
			return super.hashCode();
		}
	}

	public def toMap() {
		def m = super.toMap();
		m.refid = refid;
		m.ledgerid = refid;	//add this because this is new style 
		m.ledgertype = ledgertype;
		items.each {
			if(it.amount == null) it.amount = 0;
			m.put(it.txntype?.toLowerCase(), NumberUtil.round(it.amount));
		};
		m.total = total;
		m.partialunpaid = partialunpaid;
		if( m.surcharge == null ) m.surcharge = 0;
		if(m.interest == null ) m.interest = 0;
		return m;
	}

	//call this after apply payment
	void recalc() {;}

	//call this to distribute payment and return the remainder
	double applyPayment( double payamt ) {
		double linetotal = NumberUtil.round(total);
		if( payamt >= linetotal ) {
			return payamt - linetotal;
		}

		//in case for partial payments, distribute evenly first to its subitems. The remainder add to the amount of this bill
		double _amt = payamt;
		/*
		println "*****************************************************"
		println "line total is " + linetotal + " while payamt is " + payamt;
		println "% amount of main " + NumberUtil.round( amount / linetotal );
		println "original amount is " + amount;
		println "value of amount ->" + NumberUtil.round( NumberUtil.round( amount / linetotal ) * payamt);
		println "----------------------------------------------------------------"
		*/
		for(BillSubItem bi: items) { 
			def xxx = ( bi.amount / linetotal ) * payamt; 
			def result = NumberUtil.round( xxx );
			bi.amount = result;
			_amt -= bi.amount;
		}
		//println "remainder amount " + NumberUtil.round(_amt);
		//amount =  NumberUtil.round( NumberUtil.round( amount / linetotal ) * payamt ) ;
		//println "% amouint calculated " + NumberUtil.round(amount);
		//_amt -= amount;
		
		partialunpaid = amount - _amt;
		amount = _amt;
		//println "------- end -----------";

		//we must return 0 to indicate that all payment is consumed.
		return 0;
	}


}