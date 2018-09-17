package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class DepositSlipModel extends CrudFormModel {

    @Service("DepositSlipService")
    def depositSlipSvc;
    
    def checkList;
    int checksCount;
    
    void afterInit() {
        def m = [_schemaname: 'checkpayment'];
        m.findBy = [depositslipid : entity.objid];
        m.orderBy = "refno";
        checkList = queryService.getList( m );
        checksCount = checkList.size();
    }
    
    void changeState( def state, def params ) {
        def m = [_schemaname:'depositslip'];
        m.findBy = [objid: entity.objid];
        m.state = state;
        if( params ) m.putAll( params );
        persistenceService.update(m);        
        entity.state = state;
        if( params ) entity.putAll( params );
    }
    
    void approve() {
        changeState("APPROVED", null);
    }
    
    void disapprove() {
        changeState("DRAFT", null);
    }
    
    void print() {
        def bh = entity.bankaccount.bank.depositsliphandler;
        if(!bh) throw new Exception("Please specify a depositsliphandler in the bank ");
        def br = "depositslip_printout:" + bh;
        try {
            def op = Inv.lookupOpener(br, [entity: entity ]);
            if(!op) throw new Exception("Opener " + br + " not found. Please ensure it is included in the project");
            op.target = "process";
            Inv.invoke(op);
        }
        catch(ex) {
            MsgBox.err( ex );
        }
    }
    
    void markAsPrinted() {
        changeState( "PRINTED", null );
    }
    
    void validate() {
        def m = [:];
        m.fields = [
            [caption: 'Validation Ref No', name:'refno', required:true],
            [caption: 'Validation Ref Date', name:'refdate', required:true, datatype:'date'],            
        ];
        if(entity.validation) {
            m.data = entity.validation;
        }
        m.handler = { o->
            changeState( "VALIDATED", [validation: o] );
        };
        Modal.show( "dynamic:form", m, [title:"Validate Deposit Slip"] );
    }
    
    def checkListModel = [
        fetchList : {
            return checkList;
        }
    ] as BasicListModel;
    
}    