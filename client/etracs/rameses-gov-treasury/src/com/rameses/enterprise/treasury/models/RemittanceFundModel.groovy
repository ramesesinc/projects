package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class RemittanceFundModel extends CrudFormModel {

    @Service("RemittanceService")
    def service;
    
   boolean showCashBreakdown = true;
   boolean showCreditMemos = true;
    
   def checkList = [];
   def creditMemoList = [];
    
   void afterOpen() {
        //build summaryList
        def m = [_schemaname: 'cashreceiptpayment_noncash' ];
        m.findBy = [ remittancefundid: entity.objid ];
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
        entity.totalnoncash = entity.totalcheck + entity.totalcr;
        return entity;
    } 
    
    
    def doCancel() {
        return "_close";
    }
    
    def doOk() {
        def m = [objid:entity.objid, remittanceid:entity.remittanceid, cashbreakdown:entity.cashbreakdown ];
        service.updateRemittanceFundBreakdown( m )
        return "_close";
    }
    
}    