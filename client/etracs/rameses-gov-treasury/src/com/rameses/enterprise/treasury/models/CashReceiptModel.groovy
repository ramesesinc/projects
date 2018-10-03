package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class CashReceiptModel extends CrudFormModel { 

    String schemaName = "cashreceipt";    
    def afcontrol;
    
    void loadAfControl() {
        if(!afcontrol) {
            def m = [_schemaname: "af_control"];
            m.findBy = ["objid" : entity.controlid ];
            afcontrol = queryService.findFirst( m );
        }
    }
    
    boolean isAllowVoid() { 
        if( entity.formtype != "serial") return false;
        if ( entity.voided==true || entity.voided==1 ) return false; 
        //if ( entity.remitted==true || entity.remitted==1 ) return false; 
        return true; 
    } 
    
    boolean isAllowRevertVoid() { 
        if ( !entity?.voided.toString().matches("1|true") ) return false; 
        return ( entity?.remittance?.objid ? false : true ); 
    } 
    
    public void printReceipt() {
        loadAfControl();
        //load the report forms. This is temporarily loaded,
        if(!afcontrol.afunit.cashreceiptprintout ) {
            throw new Exception("Please define a cashreceipt printout in the af unit")
        }        
        print( afcontrol.afunit.cashreceiptprintout );
    }
    
    public void printReceiptDetail() {
        loadAfControl();
        if(!afcontrol.afunit.cashreceiptdetailprintout ) {
            throw new Exception("Please define a cashreceipt detail printout in the af unit")
        }
        print( afcontrol.afunit.cashreceiptdetailprintout );
    }
    
    public void print( def name ) {
        def h = {
            def op = Inv.lookupOpener( name, [reportData: entity] );
            if( !(op.handle instanceof ReportModel )) {
                throw new Exception("Report Handle for " + n + " must be a ReportModel " );
            }
            op.handle.viewReport(); 
            ReportUtil.print(op.handle.report, true);
        }
        Modal.show("cashreceipt:reprint:verify", [handler:h, receipt:entity]);
    }
    
    public void voidReceipt() {
        Modal.show( "void_cashreceipt", [applySecurity : true, receipt: entity ]);
    }
    
} 