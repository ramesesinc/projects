package loan.facts;

import java.util.*;
import com.rameses.util.*;
import java.math.*;

public class Payment {

	String ledgerid;
	Date dtpaid;
	Date dtlastschedule;
	String paymentmethod;
	double amount;
	double totalpaymentuptocurrent;

	public Payment() {}

	public Payment( o ) {
		amount = o.amount;
		dtpaid = parseDate(o.dtpaid);
		if (o.ledgerid) ledgerid = o.ledgerid;
		if (o.paymentmethod) paymentmethod = o.paymentmethod;
		if (o.totalpaymentuptocurrent) totalpaymentuptocurrent = o.totalpaymentuptocurrent;
		if (o.dtlastschedule) dtlastschedule = parseDate(o.dtlastschedule);
	}

	private def parseDate( date ) {
		if (date instanceof Date) {
			return date;
		} else {
			return java.sql.Date.valueOf(date);
		}
	}

	def toMap() {
		return [
			dtpaid 			: dtpaid,
			amount 			: amount,
			paymentmethod 	: paymentmethod,
			dtlastschedule	: dtlastschedule
		];
	}

	/*
	Date applydate;
	Date dtpaid;
	double amount;
	double avgover;
	int days;
	int maxdays;
	int extradays;
	double deduction;
	double absent;
	boolean hasUnderpayment;
	boolean isExact;
	boolean hasRemainder;
	boolean hasSunday;
	boolean hasRemainderIsExact;
	boolean isOverdue;
	String paymentmethod;

	public Payment( def o ) {
		this.amount = o.amount;
		this.applydate = parseDate(o.startdate);
		this.dtpaid = parseDate(o.dtpaid);

		this.hasSunday = false;
		Calendar c = Calendar.getInstance();
		c.setTime(this.dtpaid);
		if ((c.get(c.DAY_OF_WEEK)-1) == 1) {
			this.hasSunday = true;
		}
	}

	private def parseDate( date ) {		
		if (date instanceof Date) {
			return date;
		} else {
			return java.sql.Date.valueOf(date);
		}
	}

	def toMap() {
		return [
			applydate			: applydate,
			amount				: amount,
			days				: days,
			maxdays				: maxdays, 
			extradays			: extradays,
			deduction			: deduction,
			absent 				: absent,
			hasUnderpayment 	: hasUnderpayment,
			isExact				: isExact,
			hasRemainder		: hasRemainder,
			hasSunday			: hasSunday,
			dtpaid				: dtpaid,
			paymentmethod 		: paymentmethod,
			hasRemainderIsExact	: hasRemainderIsExact,
			isOverdue			: isOverdue
		];
	}
	void moveNextDate() {
		applydate = DateUtil.add( applydate, '1d');
		days++;
		//println 'days ' + days;
	}

	void accumulateAbsent( amt ) {
		absent += amt;
	}

	public double getForPrincipal() {
		def ud = new BigDecimal( this.amount - this.deduction );
		return ud.setScale(2, RoundingMode.HALF_UP).doubleValue();	
	}
	*/

}