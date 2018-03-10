package waterworks.facts;

import java.util.*;
import com.rameses.util.*;
import treasury.facts.*;

public class WaterBillItem extends MonthBillItem {

	int volume = 0;

	public WaterBillItem(def o) {
		super(o);
		if(o.volume) volume = o.volume;
	}

	public WaterBillItem() {}
}	