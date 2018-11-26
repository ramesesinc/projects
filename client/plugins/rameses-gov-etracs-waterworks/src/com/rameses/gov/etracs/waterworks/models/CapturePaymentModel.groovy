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
    
    @Service("PaymentService")
    def paymentSvc;
    
    def entity;
    def consumptionList;
    def otherFeeList;
    def amount = 0;
    def credit = 0;
    
    def paymentRefTypes = ["cashreceipt", "other"];
    
    void initNew() {
        entity = [:];
    }
    
    def consumptionListHandler = [
        isMultiSelect: {
            return true;
        },
        fetchList: {
            return consumptionList;
        }
    ] as EditorListModel;
    
    def otherFeeHandler = [
        isMultiSelect: {
            return true;
        },
        fetchList: {
            return otherFeeList;
        }
    ] as EditorListModel;
    
    void loadInfo() {
        def m = [_schemaname:"vw_waterworks_consumption"];
        m.findBy = [acctid: entity.payer.objid ];
        m.select = "objid,year,month,amtdue:{amount-amtpaid}";
        m.where = ["amount - amtpaid > 0"]; 
        m.orderBy = "year ASC,month ASC";
        consumptionList =  querySvc.getList( m );
        consumptionList.each {
            it.amount = 0;it.discount=0;it.surcharge=0;it.interest=0;it.total=0;
        }
        
        def m1 = [_schemaname:"waterworks_otherfee"];
        m1.findBy = [acctid: entity.payer.objid ];
        m1.where = ["amount - amtpaid > 0"]; 
        m1.orderBy = "year ASC,month ASC";
        otherFeeList =  querySvc.getList( m );
        otherFeeList.each {
            it.amount = 0;it.discount=0;it.surcharge=0;it.interest=0;it.total=0;
        }
    }
    
    void post() {
        if(!MsgBox.confirm("You are about to post this entry. Proceed?")) 
            throw new BreakException();
            
        def pmt = [:];
        pmt.items = [];
        pmt.items.addAll(
            consumptionList.findAll{ it.total > 0 }.collect{
            [ refid: it.objid, reftype:'waterworks_consumption', amount:it.amount, discount:it.discount, surcharge: it.surcharge, interest:it.interest ]
        });
        pmt.items.addAll(
            otherFeeList.findAll{ it.total > 0 }.collect{
            [ refid: it.objid, reftype:'waterworks_otherfee', amount:it.amount, discount:it.discount, surcharge: it.surcharge, interest:it.interest ]
        });
        if(credit>0) {
            pmt.items << [ reftype:"waterworks_credit", amount: credit ];
        }
        paymentSvc.post(entity)
    }
    
    void postAndAdd() {
        post();
        initNew();
    }
    
    

}