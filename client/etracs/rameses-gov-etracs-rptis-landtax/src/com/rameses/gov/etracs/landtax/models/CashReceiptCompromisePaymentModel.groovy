
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;

class CashReceiptCompromisePaymentModel
{
    @Caller 
    def caller;
        
    @Service('RPTReceiptCompromiseService')
    def svc;
    
    
    @PropertyChangeListener 
    def listener = [
        'entity.requiredpayment' : {
            caller.calcReceiptAmount();
        }
    ]
    
    def compromise;
    def currentyeardue = 0.0

    def getEntity(){
        return caller.entity;
    }
    
    void init(){
        compromise = entity.compromise;
    }
    
    List getRequiredPayments() {
        def list = []
        
        if( compromise.downpaymentorno == null &&  (compromise.downpaymentrequired == 1 || compromise.downpaymentrequired )  ) {
            list.add( [type:'downpayment', caption:'Downpayment' ] )
            entity.amount = compromise.downpayment;
        }
        else if( compromise.cypaymentorno == null && ( compromise.cypaymentrequired == 1 || compromise.cypaymentrequired) ) {
            list.add( [type:'cypayment', caption:'Current Year Payment' ] )
            def bill = svc.getCurrentYearDue(compromise.rptledgerid);
            currentyeardue = bill.totals.total;
            entity.bill = bill;
            entity.amount = currentyeardue;
        }
        return list ;
    }
}
