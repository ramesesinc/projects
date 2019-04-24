package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionPropertyModel extends CrudFormModel
{
    @Service('PropertyAuctionPropertyService')
    def svc;
    
    boolean isShowConfirm() { return false; }
    
    void submitForPayment(){
        if (MsgBox.confirm('Submit for Payment?')){
            svc.submitSaleForPayment(entity);
            reload();
        }
    }
    
    void approveSale(){
        if (MsgBox.confirm('Approve Sale?')){
            svc.approveSale(entity);
            reload();
        }
    }
    
    def onredeem = {redeeminfo ->
        entity.putAll(redeeminfo)
        svc.redeemSale(entity);
        reload();
    }
    
    def redeemSale(){
        if (MsgBox.confirm('Redeem sale?')){
            def p = [svc:svc];
            p.entity = entity;
            p.onredeem = onredeem;
            return Inv.lookupOpener('propertyauction_property:redeem', p);
        }
        return null;
    }
        
}