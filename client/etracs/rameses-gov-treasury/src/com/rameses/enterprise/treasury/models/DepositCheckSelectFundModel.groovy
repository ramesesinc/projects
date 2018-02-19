package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class DepositCheckSelectFundModel  {

    @Service("QueryService")
    def queryService;
    
    @Service("DepositService")
    def depositService;
    
    def depositid;
    def list;
    def fundTransferList;
    def selectedItem;
    def handler;
    def mode = "step1";
    

    def getFundList() {
       if(!selectedItem) return [];
       if( !selectedItem.funds ) {
           def m = [_schemaname:"cashreceiptpayment_noncash"];
           m.select = "code:{fund.code},title:{fund.title},objid:{fund.objid},fundamt:{amount}";
           m.findBy = [ refid: selectedItem.objid ];
           selectedItem.funds = queryService.getList(m); 
       }
       return selectedItem.funds;
    }
    
    def listHandler = [
        fetchList: { o->
            return list;
        }
    ] as EditorListModel;
    
    def fundTranfserListModel = [
        fetchList: { o->
            return fundTransferList;
        }
    ] as BasicListModel;
    
    //ACTIONS 
    def doCancel() {
        return "_close";
    }

    void doBack() {
        mode = "step1";
    }
    
    void doNext() {
        if( list.find{ it.fund?.objid == null } ) {
            throw new Exception("Please assign fund for all checks");
        }
        def mlist = [];
        list.each { itm->
            itm.funds.findAll{itm.fund.objid != it.objid }.each { f->
                mlist << [fromfund: itm.fund , tofund: [objid:f.objid, code:f.code, title: f.title], amount: f.fundamt ]; 
            }
        }
        fundTransferList = [];
        mlist.groupBy{ [ 
                fromfund: [objid: it.fromfund.objid, title: it.fromfund.title, code: it.fromfund.code ], 
                tofund: [objid: it.tofund.objid, title: it.tofund.title, code:it.tofund.code]  
            ] }.each { k,v->
            k.amount = NumberUtil.round( v.sum{ it.amount } );
            fundTransferList << k;
        }
        fundTranfserListModel.reload();
        mode = "step2";
    }
    
    def doOk() {
        if( list.find{ !it.fund } ) {
            throw new Exception("Please select fund for all items");
        };
        def m = [depositid: depositid];
        m.checks = list.collect{ [ objid:it.objid, fundid: it.fund.objid, amount: it.amount ] };
        m.fundtransfers = fundTransferList;
        depositService.updatePaymentCheckFund( m );    
        handler();
        return "_close";
    }
    
}    