package aml.facts;

import com.rameses.util.*;

class SendoutDaily {

	String senderid;
	int year;
	int month;
	int day;
	String currency;
	double amount;
	def date; 

	public SendoutDaily( o ) { 
		senderid = o.senderid;
		currency = o.currency;
		amount = o.amount;
		date = o.date;
		year = o.year;
		month = o.month;
		day = o.day;

		def smonth = o.month.toString().padLeft(2,'0');
		def sday = o.day.toString().padLeft(2,'0');
		def sdf = new java.text.SimpleDateFormat('yyyy-MM-dd');
		date = sdf.parse(o.year +'-'+ smonth +'-'+ sday);
	}

}
