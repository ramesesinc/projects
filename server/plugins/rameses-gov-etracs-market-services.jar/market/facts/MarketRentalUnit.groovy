package market.facts;

import java.util.*;

class MarketRentalUnit {
	
	String unittype;		//MARKET_UNIT_TYPES
	String payfrequency;	//MARKET_PAY_FREQUENCY
	String ratetype;		//DAY OR MONTH
	double rate;
	double extrate;

	String clusterid;
	String sectionid;
	String unitid;

	Date lastdatecovered;
	double partialbalance;
	double partialextbalance;

}