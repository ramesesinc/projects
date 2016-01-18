package com.rameses.gov.etracs.rpt.ledger.ui;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import java.rmi.server.UID;

public class RPTLedgerFaasController  
{
    def svc;
    
    def onaddHandler
    def onupdateHandler
    
    def ledger 
    def ledgerfaas
    
    def mode
    def createtype
    
    
    void create() {
        def toyear = 0;
        def toqtr = 0
        if (ledger.faases){
            def lastitem = ledger.faases.last();
            toyear = (lastitem.fromqtr == 1 ? lastitem.fromyear - 1 : lastitem.fromyear);
            toqtr = (lastitem.fromqtr == 1 ? 4 : lastitem.fromqtr - 1);
        }
        ledgerfaas = createLedgerFaas();
        ledgerfaas.toyear = toyear;
        ledgerfaas.toqtr = toqtr 
        mode = 'create'
    }
    
    void createNewRevision(){
        ledgerfaas = createLedgerFaas()
        ledgerfaas.fromyear = 0;
        ledgerfaas.fromqtr = 1;
        ledgerfaas.toyear = 0;
        ledgerfaas.toqtr = 0;
        mode = 'create'
        createtype = 'newfaas'
    }
    
    def createLedgerFaas(){
        return [
            objid           : 'LI' + new UID(),
            state           : 'PENDING',
            rptledgerid     : ledger.objid,
            fromqtr         : 1,
            toqtr           : 4,
            taxable         : true,
            backtax         : false,
            idleland        : 0,
            systemcreated   : false,
            assessedvalue   : 0.0,
            fullpin         : ledger.fullpin,
        ]
    }
    
    void edit() {
        ledgerfaas.txntype = txntypes.find{it.objid == ledgerfaas.txntype.objid}
        ledgerfaas.classification = classifications.find{it.objid == ledgerfaas.classification.objid}
        ledgerfaas.actualuse = classifications.find{it.objid == ledgerfaas.actualuse.objid}
        mode = 'edit' 
    }
    
    def ok() {
        if (!createtype){
            if ( ledgerfaas.toyear != 0 && ledgerfaas.fromyear > ledgerfaas.toyear )
                throw new Exception('From Year must be less than or equal to To Year.')
            else if ( ledgerfaas.toyear != 0 && ledgerfaas.fromyear == ledgerfaas.toyear && ledgerfaas.fromqtr > ledgerfaas.toqtr)
                throw new Exception('From Qtr must be less than or equal to To Qtr.')
        }
        
            
        if( ledgerfaas.assessedvalue == null )
            throw new Exception('Assessed Value is required.')
        
        if (ledgerfaas.assessedvalue < 0.0)
            throw new Exception('Assessed Value must be greater than or equal to zero.')
        
        if( mode == 'create' && onaddHandler ) onaddHandler( ledgerfaas )
        else if( mode == 'edit' && onupdateHandler) onupdateHandler( ledgerfaas )
        return '_close' 
    }
    
    
    List getTxntypes() {
        return svc.getTxnTypes()
    }
    
    List getClassifications() {
        return svc.getClassifications()
    }
    
    List getQuarters(){
        return [1,2,3,4]
    }
    
    List getToQuarters(){
        return [0,1,2,3,4]
    }

    
}
