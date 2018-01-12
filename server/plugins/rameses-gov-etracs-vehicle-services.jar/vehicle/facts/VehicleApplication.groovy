package vehicle.facts;

import java.util.*;
import java.rmi.server.*;

public class VehicleApplication {
	
	String apptype;
	String vehicletype;
	String objid;
	int appyear;

	public VehicleApplication( def m ) {
		apptype = m.apptype;
		vehicletype = m.vehicletype;
		appyear = m.appyear;
		objid = m.objid;
		if( !objid ) objid = "VAPP" + new UID(); 
		if(!apptype) throw new Exception("apptype is required in rules.vehicle.facts.VehicleApplication");
		if(!vehicletype) throw new Exception("vehicletype is required in rules.vehicle.facts.VehicleApplication");
	}

}