package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherModel extends CrudFormModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    @Service("DepositSlipService")
    def depositSlipSvc;  
    
    

    def collectionListModel = [
        fetchList: { o->
            def m = [_schemaname: 'collectionvoucher_fund' ];
            m.where = [" depositvoucherid = :depositid", [depositid: entity.objid]];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("collectionvoucher_fund:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
        
    void updateVoucher(def amt ) {
        entity.amountdeposited = amt;       
        depositSlipList.reload();
        binding.refresh("entity.amountdeposited");
    }
    
    def selectedItem;
    
    def depositSlipList = [
        fetchList: { o->
            def p = [depositvoucherid:entity.objid, fundid: entity.fund.objid ];
            def m = [_schemaname: 'depositslip' ];
            m.where = ["depositvoucherid = :depositvoucherid " , p ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            return viewDepositSlip();
        }
    ] as BasicListModel;

    def viewDepositSlip() {
        if(!selectedItem) throw new Exception("Please select a deposit slip");
        def p = [:];
        p.entity = selectedItem;
        def op = Inv.lookupOpener("depositslip:open", p );
        op.target = "popup";
        return op;
    }
    
    def addDepositSlip() {
        if( entity.amount == entity.amountdeposited )
            throw new Exception("No amount to deposit");
        def p = [depositvoucher: entity, fundid: entity.fund.objid ];
        return Inv.lookupOpener("depositslip:create", p );
    }
    
    void removeDepositSlip() {
        if(!selectedItem) throw new Exception("Please select a deposit slip");
        if(selectedItem.state == 'VALIDATED' )
            throw new Exception("Cannot delete this because it is already validated");
        def r = depositSlipSvc.removeDepositSlip( selectedItem );
        updateVoucher( r.amountdeposited );
    }
    
    public void approveDepositSlip() {
        if(!selectedItem) throw new Exception("Please select an item");
        if(! MsgBox.confirm("You are about to approve this deposit slip. This cannot be edited anymore. Proceed?")) return;
        depositSlipSvc.approveDepositSlip( [objid: selectedItem.objid ] );
        depositSlipList.reload();
    }
    
    def validateDepositSlip() {
        if( !selectedItem ) throw new Exception("Please choose a bank deposit entry");
        if( selectedItem.totalcash + selectedItem.totalcheck )
            throw new Exception("Please make sure the amount is equal to total cash + total check");
        def h = { o->
            def m = [objid: selectedItem.objid ];
            m.validation = o;
            depositSlipSvc.validateDepositSlip( m );
            depositSlipList.reload();
        }    
        return Inv.lookupOpener("deposit_validation", [ handler: h ] );
    }
    
    public void printDepositSlip() {
        if(!selectedItem) throw new Exception("Please select an item");
        MsgBox.alert('print slip');
    }
    
    public void post() {
        if(! MsgBox.confirm("You are about to post this voucher. Continue?")) return;
        depositSvc.post( [objid: entity.objid ] );
    }
    
    def selectedCheck;
    def checkListModel = [
        fetchList: { o->
            def p = [depid:entity.objid ];
            def m = [_schemaname: 'paymentcheck' ];
            m.select = "objid,refno,refdate,bank.*,amount,deposited:{ CASE WHEN depositslipid IS NULL THEN 0 ELSE 1 END }";
            m.where = ["depositvoucherid = :depid " , p ];
            m.orderBy = "refno";
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            
        }
    ] as BasicListModel;
    
    void addCheck() {
        def params = [:];
        params.onselect = { o->
            def v = [list: o*.objid, depositvoucherid: entity.objid ];
            def tot = depositSvc.addChecks( v );
            entity.totalcheck = tot;
            binding.refresh("entity.totalcheck");
            checkListModel.reload();
        }
        params.listHandler = [
            isMultiSelect: {
                return true;
            },
            fetchList: {
                def m = [_schemaname: 'paymentcheck'];
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
    
    void moveCheck() {
        if(!selectedCheck) throw new Exception("Please select a check to remove");
        def tot = depositSvc.removeCheck( [objid:selectedCheck.objid, depositvoucherid:entity.objid] );
        entity.totalcheck = tot;
        binding.refresh("entity.totalcheck");
        checkListModel.reload();
    }
    
    void addExternalCheck() {
        
    }
    
    
}    