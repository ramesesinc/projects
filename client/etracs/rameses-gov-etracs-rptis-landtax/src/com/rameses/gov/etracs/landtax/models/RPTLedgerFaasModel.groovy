package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import java.rmi.server.UID;

public class RPTLedgerFaasModel  
{
    @Service('QueryService')
    def qrySvc;
    
    def onadd;
    def onupdate;
    
    def ledger; 
    def ledgerfaas;
    def mode;
    def faastype;
    
    
    void create() {
        ledgerfaas = createLedgerFaas();
        if (ledger.faases){
            def lastitem = ledger.faases.last();
            ledgerfaas.toyear = (lastitem.fromqtr == 1 ? lastitem.fromyear - 1 : lastitem.fromyear);
            ledgerfaas.toqtr = (lastitem.fromqtr == 1 ? 4 : lastitem.fromqtr - 1);
        }
        mode = 'create';
        faastype = 'arrear';
    }
    
    void createNewRevision(){
        ledgerfaas = createLedgerFaas()
        ledgerfaas.fromyear = 0;
        ledgerfaas.fromqtr = 1;
        ledgerfaas.toyear = 0;
        ledgerfaas.toqtr = 0;
        mode = 'create'
        faastype = 'new'
    }
    
    void edit() {
        mode = 'edit' 
    }
    
    def save() {
        if (faastype == 'arrear'){
            if ( ledgerfaas.toyear != 0 && ledgerfaas.fromyear > ledgerfaas.toyear )
                throw new Exception('From Year must be less than or equal to To Year.')
            else if ( ledgerfaas.toyear != 0 && ledgerfaas.fromyear == ledgerfaas.toyear && ledgerfaas.fromqtr > ledgerfaas.toqtr)
                throw new Exception('From Qtr must be less than or equal to To Qtr.')
        }
        
            
        if( ledgerfaas.assessedvalue == null )
            throw new Exception('Assessed Value is required.')
        
        if (ledgerfaas.assessedvalue < 0.0)
            throw new Exception('Assessed Value must be greater than or equal to zero.')
        
        if( mode == 'create' && onadd ) onadd( ledgerfaas )
        else if( mode == 'edit' && onupdate) onupdate( ledgerfaas )
        return '_close' 
    }
    
    
    List getTxntypes() {
        qrySvc.getList([_schemaname:'faas_txntype', select:'objid,name', where:'1=1'])
    }
    
    List getClassifications() {
        qrySvc.getList([_schemaname:'propertyclassification', select:'objid,code,name', , where:'1=1', orderBy:'orderno'])
    }
    
    List getQuarters(){
        return [1,2,3,4]
    }
    
    List getToQuarters(){
        return [0,1,2,3,4]
    }


    def createLedgerFaas(){
        return [
            objid           : 'LI' + new UID(),
            state           : 'PENDING',
            rptledgerid     : ledger.objid,
            fromqtr         : 1,
            toyear          : 0,
            toqtr           : 0,
            taxable         : true,
            backtax         : false,
            idleland        : 0,
            systemcreated   : 0,
            assessedvalue   : 0.0,
            fullpin         : ledger.fullpin,
        ]
    }    
    
}
