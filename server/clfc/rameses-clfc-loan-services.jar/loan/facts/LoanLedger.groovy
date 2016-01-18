package loan.facts;

import org.joda.time.*;
import java.util.*;

public class LoanLedger
{
	String ledgerid;
	String appid;
	Date maturitydate;
	Date releasedate;
	Date startdate;
	Date lastpaymentdate;
	Date currentdate;
	int noofdaysfromlastpayment;
	int noofmonthsfromlastpayment;
	int noofyearsfromlastpayment;
	List segregationtypes;	
	double overduepenalty;
	double balance;
	double overpayment;
	boolean hasamnesty;
	String paymentmethod;
	double lackinginterest;
	double lackingpenalty;
	double totalschedulepaymentuptocurrent;
	double totaloverpaymentuptocurrent;
	int noofsundays;
	int noofholidays;
	int noofsundaysthatisaholiday;

	public LoanLedger( o ) {
		//if (!o.ledgerid) throw new Exception("Error in creating Loan Ledger Fact. ledgerid is required.");
		//if (!o.appid) throw new Exception("Error in create Loan Ledger Fact. appid is required.");

		if (o.ledgerid) ledgerid = o.ledgerid;
		if (o.appid) appid = o.appid;
		if (o.dtmatured) maturitydate = parseDate(o.dtmatured);
		if (o.dtrelease) releasedate = parseDate(o.dtrelease);
		if (o.dtstarted) startdate = parseDate(o.dtstarted);
		if (o.currentdate) currentdate = parseDate(o.currentdate);
		if (o.balance) balance = o.balance;
		if (o.hasamnesty) hasamnesty = o.hasamnesty;
		if (o.overduepenalty) overduepenalty = o.overduepenalty;
		if (o.paymentmethod) paymentmethod = o.paymentmethod.toUpperCase();
		if (o.overpayment) overpayment = o.overpayment;
		if (o.lackinginterest) lackinginterest = o.lackinginterest;
		if (o.lackingpenalty) lackingpenalty = o.lackingpenalty;

		if (o.dtlastpaid) lastpaymentdate = parseDate(o.dtlastpaid);

		LocalDate start;
		LocalDate end = new LocalDate(currentdate);
		if (lastpaymentdate) {
			start = new LocalDate(lastpaymentdate)
		} else if (releasedate) {
			start = new LocalDate(releasedate)
		} else {
			start = end;
		}
		noofdaysfromlastpayment = Days.daysBetween(start, end).getDays();
		noofmonthsfromlastpayment = Months.monthsBetween(start, end).getMonths();
		noofyearsfromlastpayment = Years.yearsBetween(start, end).getYears();

		segregationtypes = [];
	}

	def parseDate( date ) {
		if (date == null) return null;
		if (date instanceof Date) {
			return date;
		} else {
			return java.sql.Date.valueOf(date);
		}
	}

	def toMap() {
		return [
			ledgerid 					: ledgerid,
			appid 						: appid,
			maturitydate				: maturitydate,
			releasedate					: releasedate,
			lastpaymentdate				: lastpaymentdate,
			noofdaysfromlastpayment		: noofdaysfromlastpayment,
			noofmonthsfromlastpayment	: noofmonthsfromlastpayment,
			noofyearsfromlastpayment	: noofyearsfromlastpayment,
			segregationtypes			: segregationtypes,
			/*totalinterest				: totalinterest,
			totalpenalty				: totalpenalty,
			totalothers					: totalothers,
			totalbalance				: totalbalance,*/
			balance						: balance,
			overpayment					: overpayment,
			hasamnesty					: hasamnesty,
			paymentmethod				: paymentmethod,
			totalschedulepaymentuptocurrent		: totalschedulepaymentuptocurrent,
			totaloverpaymentuptocurrent	: totaloverpaymentuptocurrent,
			noofsundays 				: noofsundays,
			noofholidays 				: noofholidays,
			noofsundaysthatisaholiday 	: noofsundaysthatisaholiday,
			lackinginterest				: lackinginterest,
			lackingpenalty				: lackingpenalty
		]
	}
}