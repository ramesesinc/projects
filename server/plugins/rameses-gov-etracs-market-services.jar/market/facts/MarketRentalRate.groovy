package market.facts;


class MarketRentalRate {
	
	int year;
	double rate;
	String type;

	boolean updated = false;

	public def toMap() {
		def m = [:];
		m.year = year;
		m.rate = rate;
		m.type = type;
		return m;
	}

}