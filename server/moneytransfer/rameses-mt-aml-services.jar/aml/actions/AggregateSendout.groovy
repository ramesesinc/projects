package aml.actions;

import aml.facts.*;
import com.rameses.rules.common.*;
import com.rameses.util.*;

public class AggregateSendout implements RuleActionHandler {

	def em; 
	def result; 
	def facts;

	public void execute( params, drools ) { 
		int days = params.days.intValue;
		def sd = params.sendout_daily;
		def sdf = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
		def fromdate = sdf.format( DateUtil.add( sd.date, '-'+days+'d' )); 
		def todate = sdf.format( sd.date ); 

		def info = em.select('amount:{SUM(amount)}')
					 .find([ senderid: sd.senderid, currency:sd.currency ])
					 .where('date between :fromdate and :todate', [fromdate: fromdate, todate: todate]) 
					 .first();

		def aggr = new SendoutAggregate([
			senderid : sd.senderid, 
			currency: sd.currency, 
			amount : info.amount, 
			days   : days 
		]); 
		facts << aggr; 
		drools.insert( aggr ); 
	} 
}
