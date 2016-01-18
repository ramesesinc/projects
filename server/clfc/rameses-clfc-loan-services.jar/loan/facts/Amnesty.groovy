package loan.facts;

import java.util.*;

public class Amnesty
{
	String ledgerid;
	String type;
	double balance;
	boolean waivedinterest;
	boolean waivedpenalty;

	public Amnesty( o ) {
		if (o.balance) balance = o.balance;
		if (o.ledgerid) ledgerid = o.ledgerid;
		if (o.type) type = o.type;
		if (o.waivedinterest) waivedinterest = o.waivedinterest;
		if (o.waivedpenalty) waivedpenalty = o.waivedpenalty;
	}

	def toMap() {
		return [
			balance 		: balance,
			type 			: type,
			waivedinterest 	: waivedinterest,
			waivedpenalty 	: waivedpenalty
		];
	}
}