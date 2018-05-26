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
        def m = [_schemaname: 'paymentcheck'];
        m.findBy = [depositslipid : entity.objid];
        m.orderBy = "refno";
        checkList = queryService.getList( m );
        checksCount = checkList.size();
    }
    
    void approve() {
        depositSlipSvc.approve( [objid: entity.objid ] );
        entity.state = 'APPROVED'
    }
    
    void disapprove() {
        depositSlipSvc.disapprove( [objid: entity.objid ] );
        entity.state = 'DRAFT'
    }
    
    def validate() {
        def h = { o->
            def m = [objid: entity.objid ];
            m.validation = [refno: o.refno, refdate: o.refdate ];
            depositSlipSvc.validate( m );
            entity.state = 'VALIDATED'
            entity.validation = m.validation;
            binding.refresh();
            handler();
        }    
        return Inv.lookupOpener("deposit_validation", [ handler: h ] );
    }
    
    def checkListModel = [
        fetchList : {
            return checkList;
        }
    ] as BasicListModel;
    
}    