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
   
    def selectedItem;
    
    
    def collectionListModel = [
        isMultiSelect: {
            return true;
        },
        fetchList: { o->
            def m = [_schemaname: 'collectionvoucher' ];
            m.where = [" depositvoucherid IS NULL "];
            m.orderBy = "controldate";
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("collectionvoucher:open", [entity: o] );
            op.target = "popup";
            return op;
        },
        afterSelectionChange: { o ->
            amount = 0.0; 
            collectionListModel.selectedValue.each{
                amount += (it.totalcash + it.totalcheck); 
            } 
            binding.notifyDepends("selectedItem");
        }
    ] as BasicListModel;
    
    def submit() {
        def selectedList = collectionListModel.getSelectedValue();
        if(!selectedList) throw new Exception("Please select at least one entry");
        if( amount <= 0 ) throw new Exception("Amount must be greater than zero");
        if(!MsgBox.confirm("You are about to create a deposit voucher. Proceed?")) return null;
        def m = [:];
        m.items = selectedList*.objid;
        m.amount = amount; 
        def z = depositSvc.create( m );
        MsgBox.alert("Deposit Voucher Control No. " + z.controlno + " created");
        def op = Inv.lookupOpener("depositvoucher:open", [entity: [objid: z.objid ]]);
        op.target = "self";
        return op;
    }
    
    /*
    def submit() {
        
        
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
    */
    
}    