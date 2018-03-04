package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class BankModel extends CrudFormModel {

    def cashReportList;
    def cashBreakdownReportList;
    def checkReportList;
    def checkBreakdownReportList;
    
    private def loadItems(def openerName) {
        def arr = [];
        Inv.lookupOpeners( openerName ).each {
            arr << it.caption;
        }
        return arr;
    }
    
    void afterInit() {
        try {
            if(!cashReportList) cashReportList = loadItems( "deposit_slip:cash" ); 
            if(!cashBreakdownReportList) cashBreakdownReportList = loadItems( "deposit_slip:cashbreakdown" ); 
            if(!checkReportList) checkReportList = loadItems( "deposit_slip:check" ); 
            if(!checkBreakdownReportList) checkBreakdownReportList = loadItems( "deposit_slip:checkbreakdown" ); 
        }
        catch(e) {;}
    }
    
    @PropertyChangeListener
    def listener = [
        'entity.depository' : {
            if (entity.depository == 1){
                entity.deposittype = 'ON-US'
            }
            else {
                entity.deposittype = null
            }
        }
    ]
    
}