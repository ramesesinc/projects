package aml.actions;

import aml.facts.*;
import com.rameses.rules.common.*;
import com.rameses.util.*;

public class AggregateSendout implements RuleActionHandler {

	def em; 
	def result; 

	public void execute( params, drools ) {
		int days = params.days;
		def sd = params.sendout_daily;
		def fromdate = sd.date; 
		def todate = DateUtil.add( fromdate, '-'+days+'d' ); 

		def info = em.find([senderid: sd.senderid, currency:sd.currency])
					  .select('amount:{SUM(amount)}') 
					  .where('date between :fromdate and :todate', [fromdate: fromdate, todate:todate]) 
					  .first(); 

		def aggr = new SendoutAggregate([
			senderid : sd.senderid, 
			currency: sd.currency, 
			amount : info.amount, 
			days   : days 
		]); 
		drools.insert( aggr ); 
	} 
}
