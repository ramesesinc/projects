package loan.facts;

import java.math.*;
import org.joda.time.*;

public class LoanPostingItem {

	int itemno;
	int day;
	String ledgerid;
	double interest;
	double penalty;
	double amountpaid;
	double lackinginterest;
	double lackingpenalty;
	double partialpayment;
	double forprincipal;
	double absentpenalty;
	double underpaymentpenalty;
	double totaldeduction;
	Date scheduledate;
	Date datepaid;
	int noofdaysfromschedule;
	boolean issunday;
	boolean isholiday;
	int noofsundays;
	int noofholidays;
	int noofsundaysthatisaholiday;
	boolean paymentapplied;
	boolean allowpenalty;
	boolean allowinterest;

	public LoanPostingItem() {}

	public LoanPostingItem( o ) {
		if (o.ledgerid) ledgerid = o.ledgerid;
		if (o.dtschedule) scheduledate = parseDate(o.dtschedule);
		if (o.lackinginterest) lackinginterest = o.lackinginterest;
		if (o.lackingpenalty) lackingpenalty = o.lackingpenalty;
	}

	def parseDate( date ) {
		if (date instanceof Date) {
			return date;
		} else {
			return java.sql.Date.valueOf(date);
		}
	}

	def toMap() {
		return [
			ledgerid					: ledgerid,
			itemno 						: itemno,
			day							: day,
			partialpayment				: partialpayment,
			interest					: interest,
			penalty						: penalty,
			dtschedule 					: scheduledate,
			amtpaid 					: amountpaid,
			dtpaid 						: datepaid,
			forprincipal				: forprincipal,
			absentpenalty				: absentpenalty,
			underpaymentpenalty 		: underpaymentpenalty,
			totaldeduction 				: totaldeduction,
			noofdaysfromschedule		: noofdaysfromschedule,
			issunday					: issunday,
			isholiday					: isholiday,
			noofsundays 				: noofsundays,
			noofholidays 				: noofholidays,
			noofsundaysthatisaholiday 	: noofsundaysthatisaholiday,
			paymentapplied 				: paymentapplied,
			lackinginterest				: lackinginterest,
			lackingpenalty				: lackingpenalty
		]
	}
}