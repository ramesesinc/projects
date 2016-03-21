package mt.facts;

class Sendout {

	String branch;
	String zone;
	String region;
	String area;
	String currency;
	double amount; 
	double charge;
	double total;

	public Sendout( o ) { 
		branch = o.branch?.objid; 
		currency = o.currency;
		amount = o.principal;
	}

}
