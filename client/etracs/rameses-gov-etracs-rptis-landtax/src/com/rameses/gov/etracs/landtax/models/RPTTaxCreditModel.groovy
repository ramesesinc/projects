package com.rameses.gov.etracs.landtax.models;

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPTTaxCreditModel extends CrudFormModel  
{
    @Service('RPTTaxCreditService')
    def svc;
    
    boolean showConfirm = false;

    @FormId
    @FormTitle
    public String getFormId(){
        if (entity.txnno) {
            return 'Realty Tax Credit : ' + entity.txnno;
        }
        return 'Realty Tax Credit';
    }

    public String getTitle(){
        return 'Realty Tax Credit (' + entity.state + ')'
    }



    def creditTypes;
    
    
    void afterInit(){
        creditTypes = svc.getCreditTypes();
    }

    void afterOpen(){
        entity.ref = [receiptno: entity.refno];
    }

    def getRefTypes() {
        return svc.getReferenceTypes(entity.type);
    }
    
    void approve(){
        if (MsgBox.confirm('Approve credit?')){
            entity.putAll(svc.approve(entity));
            reloadEntity();
        }
    }

    void apply(){
        if (MsgBox.confirm('Apply credit?')){
            entity.putAll(svc.apply(entity));
            reloadEntity();
        }
    }

    def openLedger() {
        return Inv.lookupOpener('rptledger:open', [entity: entity.rptledger]);
    }
    

    def getLookupLedger() {
        return Inv.lookupOpener('rptledger:lookup', [
            onselect : {
                if (!'APPROVED'.equalsIgnoreCase(it.state)) {
                    throw new Exception('Ledger is invalid. Only approved ledger is allowed.')
                }
                entity.rptledger = it;
            },

            onempty: {
                entity.rptledger = null;
            }
        ]);
    }

    def getLookupSourceLedger() {
        if (!entity.rptledger) throw new Exception('Ledger must be specified first.');

        return Inv.lookupOpener('rptledger:lookup', [
            onselect : {
                if (!'APPROVED'.equalsIgnoreCase(it.state)) {
                    throw new Exception('Ledger is invalid. Only approved ledger is allowed.')
                }
                if (entity.rptledger.objid == it.objid) {
                    throw new Exception('Source ledger must not be equal to Ledger with credit.')
                }
                entity.srcledger = it;
            },

            onempty: {
                entity.srcledger = null;
            }
        ]);
    }

    def getLookupReceipt() {
        if (!entity.rptledger) throw new Exception('Ledger must be specified first.');

        return Inv.lookupOpener('rptreceipt:lookup', [
            onselect : {
                entity.ref = it;
                entity.refid = it.objid;
                entity.refno = it.receiptno;
                entity.refdate = it.receiptdate;
                entity.amount = it.amount;
            },

            onempty: {
                entity.ref = null;
                entity.refid = null;
                entity.refno = null;
                entity.refdate = null;
                entity.amount = null;
            }
        ]);
    }


}