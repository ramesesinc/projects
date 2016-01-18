package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;
import org.joda.time.*;

public class AddPostingItem implements RuleActionHandler {
	
	def facts;
	def calendarSvc;
	
	public void execute(def params, def drools) {
		def app = params.LOANAPP;
		int c = app.counter;
		def b = new LoanPostingItem();
		def p = params.PAYMENT;
		//println 'counter ' + c;
		//b.amtpaid = params.amtpaid;
		b.itemno = c+1;
		b.scheduledate = DateUtil.add( app.startdate, c+"d");
		b.isholiday = calendarSvc.isHoliday(b.scheduledate);
		b.issunday = false;

		def cal = Calendar.getInstance();
		cal.setTime(b.scheduledate);		
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) b.issunday = true;

		LocalDate start = new LocalDate(p.dtlastschedule);
		LocalDate end = new LocalDate(b.scheduledate);
		b.noofdaysfromschedule = Days.daysBetween(start, end).getDays();

		def ledger = params.LEDGER;
		if (b.scheduledate >= p.dtlastschedule && b.scheduledate <= p.dtpaid) {
			if (b.itemno > 1) {
				def sd = DateUtil.add(app.startdate, (c-1)+"d");
				def ih = calendarSvc.isHoliday(sd);
				def is = false;
				cal.setTime(sd);
				if (cal.get(Calendar.DAY_OF_WEEK) == 1) is = true;
				if (ih == false && is == false) {
					ledger.noofsundays = 0;
					ledger.noofholidays = 0;
					ledger.noofsundaysthatisaholiday = 0;
				}
			}
			if (b.issunday == true) ledger.noofsundays++;
			if (b.isholiday == true) ledger.noofholidays++;
			if (b.issunday == true && b.isholiday == true) ledger.noofsundaysthatisaholiday++;
		}


		b.noofsundays = ledger.noofsundays;
		b.noofholidays = ledger.noofholidays;
		b.noofsundaysthatisaholiday = ledger.noofsundaysthatisaholiday;

		//println 'app last count ' + app.lastcounter;
		app.counter += 1;
		b.day = app.counter;
		
		if (app.counter > app.lastcounter) facts << b;

		drools.update( app );
	}

}