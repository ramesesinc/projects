package rules.vehicle.facts;

import java.util.*;

public class VehicleApplication {
	
	String apptype;
	String vehicletype;
	Date dtfiled;

	public VehicleApplication( def m ) {
		apptype = m.apptype;
		vehicletype = m.vehicletype;
		if(!apptype) throw new Exception("apptype is required in rules.vehicle.facts.VehicleApplication");
		if(!vehicletype) throw new Exception("vehicletype is required in rules.vehicle.facts.VehicleApplication");
	}

}