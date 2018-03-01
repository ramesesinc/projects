package com.rameses.gov.epayment.models;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
 
/*******************************************************************************
* This class is used for Rental, Other Fees and Utilities
********************************************************************************/
public class EORRemittanceModel extends CrudFormModel {
    
    @Service('EORRemittanceService')
    def svc; 
    
    def eorListHandler = [
        fetchList: { o->
            def m = [_schemaname: 'eor'];
            m.findBy = [remittanceid: entity.objid ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("eor:lookup", [entity: o ]);
        }
    ] as BasicListModel;
    
    def selectedFund;
    def fundListHandler = [
        fetchList: { o->
            def m = [_schemaname: 'eor_remittance_fund'];
            m.findBy = [remittanceid: entity.objid ];
            return queryService.getList( m );
        }
    ] as BasicListModel;

    def validateFund() { 
        if ( !selectedFund ) throw new Exception('Please select fund'); 
        
        def p = [:]; 
        p.handler = { o-> 
            svc.updateValidation([ objid: selectedFund.objid, validation: o ]); 
            fundListHandler.reload(); 
        }
        def op = Inv.lookupOpener('deposit_validation', p );
        op.target = 'popup';
        return op;
    }
    
    def assignBankAccount() { 
        if ( !selectedFund ) throw new Exception('Please select fund'); 
        
        def p = [:]; 
        p.fundid = selectedFund.fund.objid; 
        p.onselect = { o-> 
            svc.updateBankAccount([ objid: selectedFund.objid, bankaccount: o ]); 
            fundListHandler.reload(); 
        }
        def op = Inv.lookupOpener('bankaccount:lookup', p );
        op.target = 'popup';
        return op;
        
    }
    
    void post() { 
        if ( MsgBox.confirm('You are about to post this transaction. Proceed?')) {
            def res = svc.post( entity ); 
            if ( res ) entity.putAll( res ); 
            
            binding.refresh();
        }
    }
}