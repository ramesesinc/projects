package market.facts;

import java.util.*;
import treasury.facts.*;

public class MarketBillItem {
    
    Date duedate;
    int month;
    int year;
    int day;    

    double rate;
    double extrate;

	double amount = 0.0;
    double amtpaid = 0.0;

	double total = 0.0;
	Account account;
    Account extaccount;
	double surcharge = 0.0;
	double interest = 0.0;
    
    Account surchargeAccount;
    Account interestAccount;
    int sortorder;
    String remarks;

}