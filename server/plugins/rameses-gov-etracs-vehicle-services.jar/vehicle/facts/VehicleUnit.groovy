package vehicle.facts;

import java.util.*;
import java.rmi.server.*;

public class VehicleUnit {
	
	String plateno;
	String engineno;
	String bodyno;
	String sidecarno;
	String make;
	String model;
	String color;
	String chassisno;
	String sidecarcolor;

	public VehicleUnit( def m ) {
		this.plateno = m.plateno;
		this.engineno = m.engineno;
		this.bodyno = m.bodyno;
		this.sidecarno = m.sidecarno;
		this.make = m.make;
		this.model = m.model;
		this.color = m.color;
		this.chassisno = m.chassisno;
		this.sidecarcolor = m.sidecarcolor;
	}

}