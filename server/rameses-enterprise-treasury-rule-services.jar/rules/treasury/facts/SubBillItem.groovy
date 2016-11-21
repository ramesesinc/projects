package rules.treasury.facts;

import java.util.*;

public abstract class SubBillItem extends AbstractBillItem {

	BillItem parent;

	public int hashCode() {
		return (parent?.account?.objid+"_"+parent?.txntype+"_"+ account?.objid+"_"+txntype).hashCode();			
	}
}