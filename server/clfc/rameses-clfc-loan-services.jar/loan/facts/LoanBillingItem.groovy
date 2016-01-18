package loan.facts;

import java.math.*;
import org.joda.time.*;

public class LoanBillingItem {
	
	String ledgerid;
	double interest;
	double penalty;
	double others;
	double amountdue;
	double balance;
	boolean hassunday;
	int noofholidays;
	int noofdaysexempted;
	int noofdaysfromschedule;
	Date scheduledate;
	Date currentdate;
	double avgamount;

	public LoanBillingItem() {
	}

	public LoanBillingItem( o ) {
		//if (!o.ledgerid) throw new Exception("Error in creating Loan Billing Item fact. ledgerid is required.");

		if (o.ledgerid) ledgerid = o.ledgerid;
		if (o.hassunday) hassunday = o.hassunday;
		if (o.noofholidays) noofholidays = o.noofholidays;
		if (o.noofdaysexempted) noofdaysexempted = o.noofdaysexempted;
		if (o.dtschedule) scheduledate = parseDate(o.dtschedule);
		if (o.currentdate) currentdate = parseDate(o.currentdate);
		if (o.avgamount) avgamount = o.avgamount;
		
		LocalDate start;
		LocalDate end = new LocalDate(currentdate);
		if (scheduledate) {
			start = new LocalDate(scheduledate);
		} else {
			start = end;
		}
		noofdaysfromschedule = (Days.daysBetween(start, end).getDays() + 1);
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
			ledgerid			: ledgerid,
			interest			: interest,
			penalty				: penalty,
			others				: others,
			amountdue			: amountdue,
			balance				: balance,
			hassunday			: hassunday,
			noofholidays		: noofholidays,
			noofdaysexempted	: noofdaysexempted,
			noofdaysfromschedule: noofdaysfromschedule,
			scheduledate		: scheduledate,
			currentdate			: currentdate,
			avgamount			: avgamount,
		]
	}

	/*int itemno;
	Date duedate;
	double amtdue;
	double interest;
	double penalty;
	double balance;
	double amtpaid;
	double absentpenalty;
	double underpytpenalty;
	double forprincipal;
	int day;

	def toMap() {
		return [
			itemno			: itemno,
			duedate			: duedate,
			amtdue			: amtdue,
			interest		: interest,
			penalty			: penalty,
			amtpaid			: amtpaid,
			forprincipal	: (forprincipal? forprincipal : 0),
			absentpenalty	: (absentpenalty? absentpenalty : 0),
			underpytpenalty	: (underpytpenalty? underpytpenalty : 0),
			day 			: day
		];
	}

	private double underpayment;

	double getDeduction() {

		underpayment = ((itemno * amtdue) - amtpaid) * 0.03;
		if (underpayment <= 0) underpayment = 0;
		def bd = new BigDecimal(( (itemno * (interest + penalty)) + underpayment));
		return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();	
	}

	void debug() {
		println 'interest ' + interest + ' penalty ' + penalty + ' itemno ' + itemno + ' underpayment ' + underpayment;
	} */
}