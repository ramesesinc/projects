package aml.facts;

class SendoutAggregate {

	String senderid;
	String currency;
	double amount;
	int days;

	public SendoutAggregate( o ) { 
		senderid = o.senderid;
		currency = o.currency;
		amount = o.amount;
		days = o.days;
	}

}
