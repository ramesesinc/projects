package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherModel extends CrudFormModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    /*
    void addCheck() {
        def params = [:];
        params.onselect = { o->
            def v = [list: o*.objid, depositvoucherid: entity.objid ];
            def tot = depositSvc.addChecks( v );
            entity.totalcheck = tot;
            loadChecks();
            checkListModel.reload();
            binding.refresh("entity.totalcheck|checksCount");
        }
        params.listHandler = [
            isMultiSelect: {
                return true;
            },
            fetchList: {
                def m = [_schemaname: 'checkpayment'];
                m.where = [" depositvoucherid IS NULL AND state = 'FOR-DEPOSIT' "];
                return queryService.getList(m);
            },
            getColumns: {
                return [
                    [name:'refno', caption:'Ref No'],
                    [name:'refdate', caption:'Ref Date'],
                    [name:'bank.name', caption:'Bank'],
                    [name:'amount', caption:'Amount', type:'decimal'],
                ];
            }
        ] as BasicListModel;
        params.title = "Please select the checks you want included in this deposit"; 
        Modal.show( "simple_list_lookup", params);
    }
    */
   /*
    void moveCheck() {
        if(!selectedCheck) throw new Exception("Please select a check to remove");
        if(selectedCheck.depositslipid) throw new Exception("Cannot remove this check. There is already an associated deposit slip");
        def tot = depositSvc.removeCheck( [objid:selectedCheck.objid, depositvoucherid:entity.objid] );
        entity.totalcheck = tot;
        loadChecks();
        checkListModel.reload();
        binding.refresh("entity.totalcheck|checksCount");
    }
    */
   
    void addExternalCheck() {
        def h = { o, saveType ->
            o.depositvoucherid = entity.objid;
            o.amtused = o.amount;
            o.state = 'FOR-DEPOSIT';
            loadChecks();
            checkListModel.reload();
            binding.refresh("entity.totalcheck|checksCount");
        }
        Modal.show("checkpayment:create", [handler: h, external:true ])
    }
    
    public def post() {
        if(! MsgBox.confirm("You are about to post this voucher. Continue?")) return;
        depositSvc.post( [objid: entity.objid ] );
        MsgBox.alert("Posting successful");
        return "_close";
    }
    
    
}    