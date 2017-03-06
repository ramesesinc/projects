package ovs.facts;

import treasury.facts.*;

class ViolationBillItem extends BillItem {
	
	String violation;
	String ticketid;
    String txntype = "ovs";
	
    public def toMap() {
        def m = super.toMap();
        m.ticketid = ticketid; 
        return m;
    }


}