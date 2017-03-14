package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketRentalUnitModel extends CrudFormModel {
        
    /*
    public def getClusterList() {
        if( !entity.market?.objid ) return []; 
        def m = [_schemaname:'market_cluster']
        m.select = 'objid,name,description';
        m.findBy = ['market.objid': entity.market.objid ];
        return qryService.getList( m );
    }
    */

    def historyListModel = [
        fetchList: { o->
            def m = [_schemaname:'market_account'];
            m.select = 'objid,acctname,startdate,dateclosed';
            m.findBy = [ 'unit.objid': entity.objid ];
            m.orderBy = "startdate";
            return qryService.getList(m);
        }
    ] as BasicListModel;
        
}    