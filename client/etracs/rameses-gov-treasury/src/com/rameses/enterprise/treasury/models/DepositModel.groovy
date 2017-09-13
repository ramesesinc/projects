package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class DepositModel extends CrudFormModel { 

    @Service("DepositService")
    def depSvc;
    
    def fund;
    def fundList;
    def depositdate;
    
    def listHandler = [
        isMultiSelect : {
            return true;
        },
        fetchList: { o->
            if( !fund ) return null;
            def m = [_schemaname:'liquidation_fund'];
            m.findBy = ["fund.objid": fund.objid ];
            m.where = ["depositid IS NULL"];
            m.select = "objid,controlno,liquidation.dtposted,amount";
            m.orderBy = "liquidation.dtposted";
            return queryService.getList(m);
        },
        onafterFetchList: { 
            binding?.notifyDepends("total"); 
        }, 
        afterSelectionChange: { o-> 
            binding?.notifyDepends("total");
        }
    ] as BasicListModel;
    
    def getTotal() {
        def amount = 0.0; 
        listHandler.selectedValue.each{
            amount += it.amount; 
        }
        return amount; 
    }
    
    public def initNew() {
        def m = [_schemaname:'fund',_limit:1000]
        fundList = queryService.getList(m);
        mode = "initial"
        return "init";
    }
    
    public def submit() {
        entity = depSvc.create([ fund: fund, items: listHandler.selectedValue.collect{[objid: it.objid]}, depositdate: depositdate ]);
        open();
        return "default";
    }
    
    //additional method for adding/removing collections
    public def addCollection() {
        def h = { o->
            depSvc.addItems( [depositid:entity.objid, items: o.collect{[objid:it.objid]} ] );
            binding.refresh();
        }
        return Inv.lookupOpener("liquidation_fund:undeposited:lookup", [onselect: h, fund: entity.fund ]);
    }
    
    
    def  collectionListModel= [
        fetchList: { o->
            def m = [_schemaname : 'liquidation_fund'];
            m.findBy = [depositid: entity.objid ];
            m.orderBy = "liquidation.dtposted";
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    //BANK DEPOSITS 
    def bankDepositListModel = [
        fetchList: { o->
            def m = [_schemaname : 'bankdeposit'];
            m.findBy = [depositid: entity.objid ];
            return queryService.getList(m);
        },
        onOpenItem : { o,colName->
            def h = { v->
                bankDepositListModel.reload();
            }
            return Inv.lookupOpener( "bankdeposit:open", [entity: o, fundid : entity.fund.objid, handler: h ] );
        }
    ] as BasicListModel;
    
    def addBankDeposit() {
        def bd = [_schemaname:'bankdeposit'];
        bd.depositid = entity.objid;
        def r = persistenceService.create( bd );
        bankDepositListModel.reload();
        def h = { v->
            bankDepositListModel.reload();
        }
        return Inv.lookupOpener( "bankdeposit:open", [entity: r, fundid :entity.fund.objid, handler: h ] );
    }
    
    def fundTransferListModel= [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
    
} 