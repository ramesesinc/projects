package treasury.utils;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import com.rameses.util.*;
import treasury.facts.*;

public class BillItemProvider {
	
	def itemAcctUtil = new ItemAccountUtil();

	public def createFact(def v) {

		def acct = itemAcctUtil.lookup( v.item.objid );
		Fund f = null;
		if( acct.fund?.objid  ) {
			f = new Fund( objid: acct.fund.objid, code: acct.fund.code, title: acct.fund.title);
		}

		def info = [:];
		info.parentid = v.parentid;
		info.refid = v.refid;
		info.reftype = v.reftype;
		info.account = new Account( objid: acct.objid, code: acct.code, title: acct.title, fund: f);
		info.amount = v.amount;
		if(v.year) info.year = v.year;
		if(v.month) info.month = v.month;
		return createBillItemFact( info );
	}

	def createBillItemFact = { o-> new BillItem(o) };

}