package treasury.facts;

import java.util.*;

public class SummaryBillItem extends AbstractBillItem {

	List<AbstractBillItem> items = [];
	def keys;

	public int getSortorder() {
		if(items )  {
			return items*.sortorder.max();	
		}
		return 100000;	//just an arbitrary high number	
	}
	

}