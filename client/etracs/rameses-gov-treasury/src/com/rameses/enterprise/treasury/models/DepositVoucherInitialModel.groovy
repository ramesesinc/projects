package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherInitialModel extends CrudListModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    def amount = 0;
   
    def fund;
    def fundList;
    def selectedItem;
    
    void afterInit() {
        fundList = depositSvc.getUndepositedFunds();
    }
    
    def collectionListModel = [
        isMultiSelect: {
            return true;
        },
        fetchList: { o->
            if(!fund) return null;
            def m = [_schemaname: 'collectionvoucher_fund' ];
            m.where = ["parent.state='POSTED' AND fund.objid = :fundid AND depositvoucherid IS NULL ", [fundid: fund.objid]];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("collectionvoucher_fund:open", [entity: o] );
            op.target = "popup";
            return op;
        },
        afterSelectionChange: { o ->
            if( o.selected ) {
                amount += o.data.amount;
            }
            else {
                amount -= o.data.amount;
            }
            binding.notifyDepends("selectedItem");
        }
    ] as BasicListModel;
    
    def submit() {
        def selectedList = collectionListModel.getSelectedValue();
        if(!selectedList) throw new Exception("Please select at least one entry");
        
        def p = MsgBox.confirm("You are about to create a deposit for the selected items. Proceed?");
        if(!p) return null;
        
        def openChecks = depositSvc.getOpenSplitChecks( selectedList*.objid );
        
        def addChecks;
        if( openChecks ) {
            boolean pass = false;
            def params = [:];
            params.onselect = { o->
                addChecks = o*.objid;
                pass = true;
            }
            params.listHandler = [
                isMultiSelect: {
                    return true;
                },
                fetchList: {
                    return openChecks;
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
            params.title = "There are split checks found. Select the checks you want included in this deposit"; 
            Modal.show( "simple_list_lookup", params);
            if(!pass) return null;
        };
        
        def m = [fund:fund];
        m.additionalchecks = addChecks;
        m.amount = amount;
        m.items = selectedList;
        def o = depositSvc.create( m );       
        def op = Inv.lookupOpener("depositvoucher:open", [entity: o]); 
        op.target = "self";
        return op;
    }

    
}    