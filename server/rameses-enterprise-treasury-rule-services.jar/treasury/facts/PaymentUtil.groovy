package treasury.facts;

import java.util.*;
import com.rameses.util.*;

/****************************************
* build a new list of items based on the amtpaid. If there is excess payment
* this will return a list of paid items, excess and list of unpaid items
****************************************/
public class PaymentUtil  {

	public static def applyPayment( def items, def amtpaid ) {
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
		return [items: paidItems, excess:amtpaid];
	}


}	