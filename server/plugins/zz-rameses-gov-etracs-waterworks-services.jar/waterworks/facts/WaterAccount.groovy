package waterworks.facts;

import com.rameses.util.*;
import treasury.facts.*;

public class WaterAccount {

    String classification;
    String metersize;	
    String barangay;

	public WaterAccount( def acct ) {
		this.classification = acct.classificationid;
		this.metersize = acct.meter?.sizeid;
		this.barangay = acct.barangay?.objid;
	}

}
