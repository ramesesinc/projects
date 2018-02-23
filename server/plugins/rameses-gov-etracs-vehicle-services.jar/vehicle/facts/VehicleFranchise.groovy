package vehicle.facts;

import java.util.*;

public class VehicleFranchise {
	
	String controlno;
	Date startdate;
	int activeyear;
	String objid;

	public VehicleFranchise( def m ) {
		controlno = m.controlno;
		objid = m.objid;
		if( m.startdate ) startdate = m.startdate;
		if( m.activeyear ) activeyear = m.activeyear;
	}

}