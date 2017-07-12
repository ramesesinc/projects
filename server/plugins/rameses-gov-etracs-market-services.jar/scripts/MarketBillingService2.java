import com.rameses.annotations.*;

import treasury.utils.*;

class MarketBillingService2 {

	//this is a generic treasury service
	@Service("AssessmentRuleService")
	def assmtSvc;

	@DataContext("market_account")
	def acctEm;

	@DataContext("market_recurring_fee")
	def recurFeeEm;

	@DataContext("market_other_fee")
	def otherFeeEm;

	@Service("DateService")
	def dateSvc;

	private def buildMarketAccountFact(def o ) {
		acctEm.find('');		
	}

	private def buildRecurringFees(def o) {
		def list = [];
		recurFeeEm.find([parentid: o.objid]).each {
			list << new RecurringFee( amount: frequency: txntype: );
		}
		return list;
	}

	private def buildOtherFees(def o) {
		def list = [];
		recurFeeEm.find([parentid: o.objid]).each {
			list << new MonthBillItem( amount: frequency: txntype:);
		}
		return list;	
	}

	@ProxyMethod
	public def getBilling( def p ) {
		if(!o.fromdate) throw new Exception("fromdate is required in MarketBillingService");
		if(!o.marketunit) throw new Exception("marketunit is required in MarketBillingService");

		def amtPaid = 0.0;
		def svrDate = dateSvc.getBasicServerDate();
		def todate = svrDate;
		
		if(o.payoption) {
			if( o.payoption.todate ) {
				todate = dateSvc.parseDate( o.payoption.todate, null ).date;
			}
			else if( o.payoption.numdays ) {
				todate = DateUtil.add( o.fromdate, (o.payoption.numdays-1) + "d" );
			}
			else if( o.payoption.nummonths ) {
				todate = DateUtil.add( o.fromdate, o.payoption.nummonths + "M" );
				todate = DateUtil.add( todate, "-1d");
			}
			else if( o.payoption?.amount ) {
				amtPaid = o.payoption.amount;
			}
		}

		def fb = new FactBuilder();
		fb.facts << new MarketRentalUnit( o.marketunit );
		fb.facts << new MarketBillingPeriod( [fromdate: o.fromdate, todate: todate ] ) ;
		fb.facts << new treasury.facts.SystemDate(svrDate);
		fb.facts << new treasury.facts.BillDate( (o.billdate) ?o.billdate: svrDate );
		fb.facts << new treasury.facts.Payment( amount: amtPaid );

		//build number of months from date todate specified
		MonthEntryBuilder.buildMonthEntries( o.fromdate, todate ).each {
			fb.facts << it;
		}



		return assmtSvc.execute( "marketbilling", [:], fb, resultHandler );
	}

}