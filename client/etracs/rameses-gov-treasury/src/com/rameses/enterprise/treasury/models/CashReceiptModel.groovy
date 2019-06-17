package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class CashReceiptModel extends CrudFormModel { 

    @Service('Var')
    def varSvc;

    String schemaName = "cashreceipt";    
    def afcontrol;

    def reprint_requires_approval; 
    
    boolean isReprintRequiresApproval() {
        if ( reprint_requires_approval == null ) {
            def sval = varSvc.get('cashreceipt_reprint_requires_approval'); 
            reprint_requires_approval = ( "false".equals(sval.toString()) ? false : true); 
        }
        if ( reprint_requires_approval instanceof Boolean ) {
            return (reprint_requires_approval ? true : false); 
        }
        return false; 
    }

    void loadAfControl() {
        if ( !afcontrol ) { 
            def m = [_schemaname: "af_control"];
            m.findBy = ["objid" : entity.controlid ];
            afcontrol = queryService.findFirst( m );
        } 
    }
    
    boolean isAllowVoid() { 
        if ( entity.formtype != "serial" ) return false;
        if ( entity.voided.toString().matches('1|true')) return false; 
        if ( entity.remitted.toString().matches('1|true')) return false; 
        return ( entity.remittanceid ? false : true ); 
    } 
    
    boolean isAllowRevertVoid() { 
        if ( !entity?.voided.toString().matches("1|true") ) return false; 
        return ( entity?.remittance?.objid ? false : true ); 
    } 
    
    public void printReceipt() {
        loadAfControl();
        if ( afcontrol?.afunit?.cashreceiptprintout ) { 
            print( afcontrol.afunit.cashreceiptprintout ); 
        } else { 
            MsgBox.alert("Unable to print receipt. Please define a setting for cashreceipt printout in AF Unit"); 
        } 
    }
    
    public void printReceiptDetail() {
        loadAfControl();
        if ( afcontrol?.afunit?.cashreceiptdetailprintout ) {
            print( afcontrol.afunit.cashreceiptdetailprintout );
        } else {
            MsgBox.alert("Unable to print receipt detail. Please define a setting for cashreceipt detail printout in AF Unit"); 
        }
    }
    
    public void print( def name ) { 
        boolean pass = false; 
        def h = { pass = true; } 
        Modal.show("cashreceipt:reprint:verify", [ handler: h, receipt: entity, applySecurity: isReprintRequiresApproval() ]); 
        if ( !pass ) return; 
        
        def op = null; 
        try {
            op = Inv.lookupOpener( name, [ reportData: entity ]);
        } catch(Throwable t) { 
            t.printStackTrace(); 
        } 
        
        def opHandle = (op ? op.handle : null); 
        def reportHandle = findReportModel( opHandle ); 
        if ( reportHandle == null ) {
            MsgBox.alert("Report Handle for " + name + " must be a ReportModel " );
            return; 
        }
        
        reportHandle.viewReport(); 
        ReportUtil.print(reportHandle.report, true);
    }
    
    public void voidReceipt() {
        if ( entity.voided.toString().matches('1|true')) {
            MsgBox.alert('Cash Receipt is already voided'); 
        } else {
            Modal.show( "void_cashreceipt", [applySecurity : true, receipt: entity ]);
        }
    }
    
    private def findReportModel( o ) {
        if ( o == null ) return null; 
        else if (o instanceof ReportModel ) return o; 
        else if (o instanceof Opener) return findReportModel( o.handle ); 
        
        if ( o.metaClass.respondsTo(o, 'viewReport' )) {
            def oo = o.viewReport(); 
            return findReportModel( oo ); 
        } else if ( o.metaClass.hasProperty(o, 'report' )) {
            return findReportModel( o.report ); 
        } else {
            return null; 
        }
    }    
    
    def decformat = new java.text.DecimalFormat('#,##0.00'); 
    def getFormattedAmount() {
        return decformat.format( entity.amount ? entity.amount : 0.0 ); 
    }
} 