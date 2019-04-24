package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionPropertyRedeemModel
{
    def svc;
    
    def onredeem;
    def entity;
    def redeeminfo;
    
    void init(){
        redeeminfo = [:]
        redeeminfo.redeemedby = entity.rptledger.taxpayer
        redeeminfo.charges = svc.generateRedemptionCharges([objid:entity.objid]);
        redeeminfo.redeemedamt = redeeminfo.charges.amount.sum();
    }
    
    def redeem(){
        if (MsgBox.confirm('Redeem property?')){
            onredeem(redeeminfo);
            return '_close';
        }
        return null;
    }
}