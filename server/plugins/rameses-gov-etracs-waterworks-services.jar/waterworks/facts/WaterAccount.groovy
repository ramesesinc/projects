package waterworks.facts;

import com.rameses.util.*;
import treasury.facts.*;

public class WaterAccount {

    String classification;
    String metersize;	
    String barangay;

	public WaterAccount( def acct ) {
		this.classification = acct.classificationid;
		this.metersize = acct.metersize?.objid;
		this.barangay = acct.stuboutnode?.barangay?.objid;
		if ( !this.barangay ) this.barangay = acct.address?.barangay?.objid;
	} 
}
