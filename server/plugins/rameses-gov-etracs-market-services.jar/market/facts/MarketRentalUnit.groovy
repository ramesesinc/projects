package market.facts;

import java.util.*;

class MarketRentalUnit {

	String unitno;
	String unittype;		//MARKET_UNIT_TYPES
	String clusterid;
	String sectionid;
	
	public MarketRentalUnit(def mm) {
		unitno = mm.code;
		unittype = mm.unittype;
		clusterid = mm.clusterid;
		sectionid = mm.sectionid;
	}

}