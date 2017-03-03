package market.facts;

import java.util.*;

/************************************************************
* This class is used for 
*************************************************************/
public class MarketExcessMonthEntry extends MarketMonthEntry {
    
	double rate;	//this is already the total amount
	double extrate;	//this is already the total amount

	public double getLinetotal() {
		return rate + extrate;
	}

}