package vrs.facts;


public class VrsApplication {
	
	String apptype;
	String vehicletype;
	Date dtregistered;

	public VrsApplication(def m) {
		this.apptype = m.apptype;
		this.dtregistered = m.dtregistered;
	}
	
}