package vehicle.facts;

import java.util.*;
import java.rmi.server.*;

public class VehicleApplication {
	
	int appyear;
	Date appdate;
	String apptype;
	String vehicletype;
	String objid;
	Date prevexpirydate;
	String controlno;

	public VehicleApplication( def m ) {
		if(m.controlno) controlno = m.controlno;
		apptype = m.apptype;
		objid = m.objid;
		if( m.vehicletypeid ) vehicletype  = m.vehicletypeid;
		if( m.appyear ) appyear = m.appyear;
		if( m.appdate ) {
			if( m.appdate instanceof String ) {
				def df = new java.text.SimpleDateFormat( "yyyy-MM-dd");
				m.appdate = df.parse( m.appdate );
			}
			appdate = m.appdate;
		}	

		if( m.prevexpirydate ) {
			if( m.prevexpirydate instanceof String ) {
				def df = new java.text.SimpleDateFormat( "yyyy-MM-dd");
				m.prevexpirydate = df.parse( m.prevexpirydate );
			}
			prevexpirydate = m.prevexpirydate;
		}	

		if( !objid ) objid = "VAPP" + new UID();
		if(!apptype) throw new Exception("apptype is required in rules.vehicle.facts.VehicleApplication");
		if(!vehicletype) throw new Exception("vehicletype is required in rules.vehicle.facts.VehicleApplication");
	}

}