package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class ApplyPostingPaymentWithLacking implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		def POSTINGITEM = params.postingitem;
		POSTINGITEM.forprincipal = NS.round( params.amount.decimalValue );
		POSTINGITEM.paymentapplied = true;
		//def BILLITEM = params.item;
		//println 'AMOUNT ' + params.amount.decimalValue;
		/*
		BILLITEM.forprincipal = NS.round( params.amount.decimalValue );
		def PAYMENT = params.payment;
		BILLITEM.amtpaid = PAYMENT.amount;
		def APP = params.app;
		APP.scheduledate = BILLITEM.duedate;
		*/
		//println 'PAYMENT ' + PAYMENT.toMap();
		//println 'BILLITEM ' + BILLITEM.toMap();
		//println 'APP ' + APP.toMap();
	}

}