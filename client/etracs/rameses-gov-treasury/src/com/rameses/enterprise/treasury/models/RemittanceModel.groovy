package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class RemittanceModel extends CrudFormModel {

    @Service("RemittanceService")
    def remSvc;    
    
    def summaryList;
    def fundList = [];
    def checkList = [];
    
    void afterOpen() {
        //build summaryList
        def m = [_schemaname: 'remittance_af' ];
        m.findBy = [ remittanceid: entity.objid ];
        summaryList = queryService.getList( m );
        
        //list of voided
        m = [_schemaname: 'remittance_fund' ];
        m.findBy = [ remittanceid: entity.objid ];
        fundList = queryService.getList( m );
        entity.amount = fundList.sum{ it.amount };
        
        def m1 = [_schemaname: 'cashreceiptpayment_noncash' ];
        m1.select = "refno,reftype,refdate,amount:{SUM(amount)}";
        m1.groupBy = "refno,reftype,refdate";
        m1.where = [ "remittancefund.remittanceid = :rid", [rid: entity.objid ]];
        checkList = queryService.getList( m1 );
        entity.totalnoncash = entity.totalcheck + entity.totalcr;
    }
    
    def afSummaryHandler = [
        fetchList: { o->
            return summaryList;
        },
        onOpenItem: {o,col->
            def p = [:];
            p.put( "query.afcontrolid", o.controlid );
            p.put( "query.fromseries", o.issuedstartseries );
            p.put( "query.toseries", o.issuedendseries );
            return Inv.lookupOpener("cashreceipt_list:afseries", p );
        }
    ] as BasicListModel;
    
    def fundSummaryHandler = [
        fetchList: { o->
            return fundList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("remittance_fund:open", [entity: o] );
        }
    ] as BasicListModel;
    
    def checkModel = [
        fetchList: { o->
            return checkList;
        }
    ] as BasicListModel;
    
    def getPrintFormData() {
        return entity;
    } 
    
    void remit() {
        if ( MsgBox.confirm('You are about to submit this for liquidation. Proceed?')) {
            entity = remSvc.post( entity ); 
        }
    }
    
}    