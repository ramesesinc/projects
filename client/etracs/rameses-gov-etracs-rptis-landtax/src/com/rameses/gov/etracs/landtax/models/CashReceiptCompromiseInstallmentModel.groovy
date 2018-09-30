package com.rameses.gov.etracs.landtax.models;
 
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;

class CashReceiptCompromiseInstallmentModel
{
    @Caller 
    def caller;
        
    @Service('RPTReceiptCompromiseService')
    def svc;
    
    def compromise;

    def getEntity(){
        return caller.entity;
    }
    
    void init(){
        compromise = entity.compromise;
        entity.installments = svc.getUnpaidInstallments([objid:compromise.objid]);
        installmentListHandler?.reload();
    }
    
    
    def installmentListHandler = [
        getRows    : {return 50 },
        
        fetchList : { return entity.installments },
        
        onColumnUpdate : {item, colname -> 
            if( 'pay' == colname ) {
                def idx = entity.installments.indexOf(item);
                item.amtdue = 0.0;
                if( item.pay ) {
                    item.amtdue = item.balance;
                    for( int i=0; i < idx; i++ ) {
                        entity.installments[i].pay = item.pay ;
                        entity.installments[i].amtdue = entity.installments[i].balance;
                    }
                }
                for( int i=idx+1; i< entity.installments.size(); i++) {
                    entity.installments[i].pay = false;
                    entity.installments[i].amtdue = 0.0;
                }
                installmentListHandler.load();
                calcReceiptAmount();
            }
            if (colname == 'amtdue'){
                item.pay = true;
                item.partial = item.balance > item.amtdue ? 1 : 0
            }
        },
        
        validate : { li ->
            def item = li.item;
            if (item.amtdue == null )
                throw new Exception('Amount to pay is required.');
            if (item.amtdue > item.balance)
                throw new Exception('Amount to pay must be less than or equal to balance.');
                
            if (item.amtdue < item.balance ){
                //check if this item the latest otherwise not allowed
                def paiditems = entity.installments.findAll{it.pay == true && it.installmentno > item.installmentno}
                if (paiditems){
                    def lastitem = paiditems.last()
                    if (lastitem.installmentno != item.installmentno){
                        item.amtdue = item.balance;
                        throw new Exception('Partial Payment is only allowed on Installment No. ' + lastitem.installmentno + '.')
                    }
                }
            }
            calcReceiptAmount();
        }
        
    ] as EditorListModel
    
    
    void calcReceiptAmount(){
        entity.amount = entity.installments.sum{ it.pay == true ? it.amtdue : 0.0 }
        caller.calcReceiptAmount();
    }
}
