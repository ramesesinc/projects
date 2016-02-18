package waterworks.facts;

import com.rameses.util.*;
import treasury.facts.*;

public class WaterAccount {

    String classification;
    String metersize;	
    String barangay;

	public WaterAccount( def app ) {
		this.classification = app.classificationid;
		this.metersize = app.meter?.sizeid;
		this.barangay = app.barangay?.objid;
	}

}
