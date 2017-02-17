package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class SetBillItemAccount implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;

		if( billitem == null ) 
			throw new Exception("SetBillItemDueDate error. Please specify a billitem");

		def em = EntityManagerUtil.lookup("itemaccount");

		def itemacct = em.find( [objid: acct.key] ).first();
		if( !itemacct )
			throw new Exception("SetBillItemAccount rule error. Account not found ");

		billitem.account = new Account(itemacct);		

	}
}