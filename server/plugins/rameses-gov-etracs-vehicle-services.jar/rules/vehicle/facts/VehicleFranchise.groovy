package rules.vehicle.facts;

import java.util.*;

public class VehicleFranchise {
	
	String controlno;
	Date dtregistered;
	int activeyear;

	public VehicleFranchise( def m ) {
		controlno = m.controlno;
		if( m.dtregistered ) dtregistered = m.dtregistered;
		if( m.activeyear ) activeyear = m.activeyear;
	}

}