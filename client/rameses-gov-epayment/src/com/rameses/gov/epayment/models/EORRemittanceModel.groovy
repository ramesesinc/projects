package com.rameses.gov.epayment.models;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
 
/*******************************************************************************
* This class is used for Rental, Other Fees and Utilities
********************************************************************************/
public class EORRemittanceModel extends PageFlowController {
    
    @Service("QueryService")
    def queryService;
    
    @Service("PaymentResolverService")
    def resolverService;

    def entity = [:];
    def resolveList = [];
    
    def getPartnerList() {
        def m = [_schemaname:'paymentpartner'];
        m.where = ["1=1"];
        return queryService.getList( m )*.objid;
    }
    
    public void fetchForResolve() {
        //check records...
        resolveList << [ objid:'11212', traceid: '1289999', tracedate: '2018-01-01', paidby:'ABC LUMBER', amount:1000, remarks: 'OK' ];
        resolveList << [ objid:'11212', traceid: '1289999', tracedate: '2018-01-01', paidby:'ABC LUMBER', amount:1000, remarks: 'OK' ];
    }

    def resolveListHandler = [
        fetchList: { o->
            return resolveList;
        }
    ] as BasicListModel;
    
    
    public void resolve() {
        resolverService.resolve( entity );
        resolveListHandler.reload();
    }
    
    def eorListHandler = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
    
    def fundListHandler = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;

}