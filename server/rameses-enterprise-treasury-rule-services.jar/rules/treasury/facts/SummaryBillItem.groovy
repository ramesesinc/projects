package rules.treasury.facts;

import java.util.*;

public class SummaryBillItem extends AbstractBillItem {

	String aggtype;
	List<AbstractBillItem> items = [];

	public double getAmount() {
		if(!items) return 0;
		if( aggtype == "SUM" ) {
			return items.sum{ it.amount };		
		}
		else if( aggtype == "COUNT") {
			return items.size();
		}
		else if( aggtype == "AVG" ) {
			return items.sum{ it.amount } / items.size();
		}
		else if( aggtype == "MAX" ) {
			return items.max{ it.amount };
		}
		else if( aggtype == "MIN" ) {
			return items.min{ it.amount };
		}
		return 0;
	}


}