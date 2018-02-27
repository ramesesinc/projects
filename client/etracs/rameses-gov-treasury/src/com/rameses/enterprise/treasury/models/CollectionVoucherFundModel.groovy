package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class CollectionVoucherFundModel extends CrudFormModel {

    @Service("CollectionVoucherService")
    def collSvc;    
    
    boolean showCashBreakdown = true;
    boolean showCreditMemos = true;

    def checkList = [];
    def creditMemoList = [];

    void afterOpen() {
         //build summaryList
         def p = [collectionvoucherid: entity.parentid, fundid: entity.fund.objid ];
         def m = [_schemaname: 'cashreceiptpayment_noncash' ];
         m.where = ["receipt.remittance.collectionvoucherid =:collectionvoucherid AND fund.objid=:fundid", p];
         def list = queryService.getList( m );
         checkList = list.findAll{ it.reftype == 'CHECK' };
         creditMemoList = list.findAll{ it.reftype != 'CHECK' };
         if( entity.totalcheck + entity.totalcash == 0 ) showCashBreakdown = false;
         if( entity.totalcr == 0 ) showCreditMemos = false;
    }

    def checkModel = [
        fetchList: {o ->
            return checkList;
        }
    ] as BasicListModel; 

    def creditMemoModel = [
        fetchList: {o -> 
            return creditMemoList;
        }
    ] as BasicListModel;

    def getPrintFormData() { 
        def rdata = collSvc.getReportData([ objid: entity.parentid, fund: entity.fund ]); 
        if ( rdata ) rdata.putAll( entity ); 

        return rdata;
    } 
}    