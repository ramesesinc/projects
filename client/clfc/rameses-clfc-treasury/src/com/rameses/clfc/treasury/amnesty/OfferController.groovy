package com.rameses.clfc.treasury.amnesty;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import java.text.*;

class OfferController 
{
    def entity, handler, parentid, mode = 'read';
    def isfla = false;
    
    /*
    def allowEdit = true;
    def editAmountOnly = false;
    def forApprovalMode = 'read';
    def approvalMode = 'read';
    def allowAmend = false;
    */
    
    def decformat = new DecimalFormat("#,##0.00");
    def dateformat = new SimpleDateFormat("MMM-dd-yyyy");
    
    void init(){
       entity = [
           objid        : "AO" + new UID(),
           parentid     : parentid,
           isspotcash   : 0,
           days         : 0, 
           months       : 0
       ];
    }    

    def doOk(){
        if (mode!='read') {
            if (entity.days > 31) throw new Exception("Days must not be greater than 31.");
            entity._edited = true;
            if (entity.isspotcash == 1) {
                //entity.term = 'Spot cash ' + decformat.format(entity.amount) + ' ' + dateformat.format(java.sql.Date.valueOf(entity.date));
                entity.term = 'Spot cash until ' + dateformat.format(java.sql.Date.valueOf(entity.date));
            } else if (entity.isspotcash == 0) {
                entity.term = '';//decformat.format(entity.amount);
                if (entity.days > 0) entity.term += ' ' + entity.days + ' day(s)';
                if (entity.months > 0) entity.term += ' ' + entity.months + ' month(s)';
                if (entity.days==0 && entity.months==0) entity.term += ' No maturity date';
            }
        }
        if (handler) handler(entity);
        return '_close';
    }

    def doCancel(){
        entity.offer = [:];
        return '_close';
    }
            
}

