package treasury.facts;

public class BillItemList  {
	
 	def items = [];
 	def duplicates = [];
 	
 	void addItem( BillItem bi ) {
 		if(!items.contains(bi) ) {
			items << bi;
 		}
 		else {
 			duplicates << bi;
 		}
 	}

 	public List buildBillItems() {
 		def billItems = []
 		for( o in items.sort{it.sortorder} ) {
 			billItems << o.toItem();
 		}
 		return billItems;
 	}

	//this utility groups items by category and account 
	public List buildReceiptItems() {
		def receiptItems = [];

		def acctGroup = items.groupBy{ [it.category, it.account] };
		acctGroup.each { k,v->
			def cat = k[0];
			def acct = k[1];
			if( acct) {
				receiptItems.add( [item: acct.toItem(), category:cat, amount: v.sum{it.amount}] );	
			}
		}

		def surGroup = items.groupBy{ [it.category, it.surchargeAccount] };
		surGroup.each { k,v->
			def cat = k[0];
			def acct = k[1];
			if( acct) {
				receiptItems.add( [item: acct.toItem(), category:cat, amount: v.sum{it.surcharge}] );	
			}
		}

		def intGroup = items.groupBy{ [it.category, it.interestAccount] };
		intGroup.each { k,v->
			def cat = k[0];
			def acct = k[1];
			if(acct) {
				receiptItems.add( [item: acct.toItem(), category:cat, amount: v.sum{it.interest}] );	
			}
		}

		/*
		def discGroup = items.groupBy{ [it.category, it.discountAccount] };
		discGroup.each { k,v->
			def cat = k[0];
			def acct = k[1];
			if(acct) {
				receiptItems.add( [item: acct.toItem(), category:cat, amount: v.sum{it.discount}] );	
			}
		}
		*/
		return receiptItems;
	}

	//build a new list of items based on the amtpaid. If there is excess payment
	//the excess value is returned
	public def applyPayment( def amtpaid ) {
		def paidItems = [];
		for( bi in items.sort{ it.pmtorder } ) {
			if( amtpaid == 0 ) {
				break;
			}	
			else if( bi.total <= amtpaid ) {
				amtpaid -= bi.total;
				paidItems << bi;
			}
			else {
				double d = amtpaid;
		        if( d < bi.total ) {
		            double _total = total;
		            bi.amount = NumberUtil.round( (bi.amount / _total) * d );
		            if( bi.discount > 0.0) {
		                bi.discount = NumberUtil.round( (bi.discount / _total) * d );
		            }    
		            if( bi.surcharge > 0.0 ) {
		                bi.surcharge = NumberUtil.round( (bi.surcharge / _total) * d );
		            }
		            if( bi.interest > 0.0 ) {
		                bi.interest = NumberUtil.round( d - (bi.amount - bi.discount) - bi.surcharge );
		            }
		        }

				amtpaid = 0;
				paidItems << bi;
				break;
			}
		}
		
		items.clear();
		items.addAll( paidItems );
		return amtpaid;
	}

}