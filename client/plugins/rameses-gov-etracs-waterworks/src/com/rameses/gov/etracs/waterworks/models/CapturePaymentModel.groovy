package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import java.text.*;

class CapturePaymentModel extends PageFlowController  {

    @Service("QueryService")
    def querySvc;
    
    @Service("PaymentPostingService")
    def paymentSvc;
    
    def entity;
    def consumptionList;
    def otherFeeList;
    def amount = 0;
    def credit = 0;
    
    def paymentRefTypes = ["cashreceipt", "other"];
    
    void initNew() {
        entity = [amount:0];
        consumptionList = []; 
        otherFeeList = [];
        amount = 0;
        credit = 0;
        consumptionListHandler.reload(); 
        otherFeeHandler.reload();
    }
    
    def consumptionListHandler = [
        fetchList: {
            return consumptionList;
        },
        isColumnEditable: {item,colName->
            if(colName.matches("amount|surcharge|interest|discount")) {
                return true;
            }
        },
        onColumnUpdate: { item,colName->
            if(colName.matches("amount|surcharge|interest|discount")) {
                item.total = item.amount + item.surcharge + item.interest - item.discount;
                amount = (consumptionList+otherFeeList).sum{ it.total }; 
                binding.refresh("amount")
            }
        }
    ] as EditorListModel;
    
    def otherFeeHandler = [
        fetchList: {
            return otherFeeList;
        },
        isColumnEditable: {item,colName->
            if(colName.matches("amount|surcharge|interest|discount")) {
                return true;
            }
        },
        onColumnUpdate: { item,colName->
            if(colName.matches("amount|surcharge|interest|discount")) {
                item.total = item.amount + item.surcharge + item.interest - item.discount;
                amount = (consumptionList+otherFeeList).sum{ it.total }; 
                binding.refresh("amount")
            }
        }
    ] as EditorListModel;
    
    void loadInfo() {
        def m = [_schemaname:"vw_waterworks_consumption"];
        m.findBy = [ acctid: entity.payer.objid ];
        m.select = "objid,year,month,amtdue:{amount-amtpaid-discount}";
        m.where = [" state = 'POSTED' AND hold = 0 AND (amount-amtpaid-discount) > 0 "]; 
        m.orderBy = "year, month";
        consumptionList =  querySvc.getList( m );
        consumptionList.each { 
            it.amount=0; it.discount=0; it.surcharge=0; it.interest=0; it.total=0;
        }
        
        def m1 = [_schemaname:"waterworks_otherfee"];
        m1.findBy = [ acctid: entity.payer.objid ];
        m.select = "objid,year,month,amtdue:{amount-amtpaid-discount},item.*";
        m1.where = [" (amount-amtpaid-discount) > 0 "]; 
        m1.orderBy = "year, month";
        otherFeeList =  querySvc.getList( m1 );
        otherFeeList.each {
            it.amount=0; it.discount=0; it.surcharge=0; it.interest=0; it.total=0;
        }
    }
    
    void post() {
        //check first if totals amount + credit
        if( entity.amount <= 0 )
            throw new Exception("amount to pay must not be zero");
        if( entity.amount != (amount+credit))
            throw new Exception("Amount to pay must equal to amount + credit");
        def errList =  (consumptionList + otherFeeList).findAll{it.amtdue < it.amount};
        if( errList ) {
            def str = errList.collect{ it.year+"-"+it.month+ " due:" + it.amtdue + " paid:" + it.amount  }.join("\n");
            throw new Exception("amount paid must be less than or equal to amt due. Please check the items\n" + str);
        }
        
        if(!MsgBox.confirm("You are about to post this entry. Proceed?")) 
            throw new BreakException();
            
        def pmt = [_schemaname:"waterworks_payment"];
        pmt.putAll( entity );
        pmt.acctid = entity.payer.objid; 
        pmt.txnmode = "CAPTURE";
        pmt.items = [];
        pmt.credits = [];
        pmt.items.addAll(
            consumptionList.findAll{ it.total > 0 }.collect{
            [ refid: it.objid, reftype:'waterworks_consumption', amount:it.amount, discount:it.discount, surcharge: it.surcharge, interest:it.interest ]
        });
        pmt.items.addAll(
            otherFeeList.findAll{ it.total > 0 }.collect{
            [ refid: it.objid, reftype:'waterworks_otherfee', amount:it.amount, discount:it.discount, surcharge: it.surcharge, interest:it.interest ]
        });
        if(credit>0) {
            pmt.credits << [ reftype:"waterworks_credit", amount: credit, remarks: "CAPTURE EXCESS PAYMENT", txntype: "credit", amtpaid:0.0, acctid: pmt.acctid ];
        }
        paymentSvc.post(pmt);
        MsgBox.alert("post success!");
    }
    
    void postAndAdd() {
        post();
        initNew();
    }
}