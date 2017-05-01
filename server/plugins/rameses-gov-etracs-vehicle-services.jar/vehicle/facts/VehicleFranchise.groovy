package vehicle.facts;

import java.util.*;

public class VehicleFranchise {
	
	String controlno;
	Date startdate;
	int activeyear;

	public VehicleFranchise( def m ) {
		controlno = m.controlno;
		if( m.startdate ) startdate = m.startdate;
		if( m.activeyear ) activeyear = m.activeyear;
		if(!controlno) throw new Exception("apptype is required in rules.vehicle.facts.VehicleFranchise");		
	}

}