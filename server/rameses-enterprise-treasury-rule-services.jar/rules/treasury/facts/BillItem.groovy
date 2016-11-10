package rules.treasury.facts;

import java.util.*;

class BillItem {
	

	Category category;
	Account account;
	double amount;
	double amtpaid;

	String refid;
	int qtr;
	int month;
	int year;
	int day;
	Date duedate;
	String txntype;
	String remarks;

}