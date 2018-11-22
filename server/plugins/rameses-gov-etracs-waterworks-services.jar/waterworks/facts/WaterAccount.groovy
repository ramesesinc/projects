package waterworks.facts;

import com.rameses.util.*;
import treasury.facts.*;

public class WaterAccount {

    String classification;
    String metersize;	
    String barangay;
    int units = 1;
    String state;
    boolean metered;

	public WaterAccount( def acct ) {
		this.classification = acct.classificationid;
		this.barangay = acct.stuboutnode?.barangay?.objid;
		if ( !this.barangay ) this.barangay = acct.address?.barangay?.objid;
		if( acct.units ) this.units = acct.units; 
		this.state = acct.state;
		if( !acct.meter?.objid ) {
			this.state = "UNMETERED";
			this.metered = false;
		}
		else {
			this.metersize = acct.meter?.size?.objid;
			this.metered = true;
		}
	} 
}
