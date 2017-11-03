package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketRentalUnitModel extends CrudFormModel {
        
    def historyListModel = [
        fetchList: { o->
            def m = [_schemaname:'market_account'];
            m.select = 'objid,acctname,owner.name,dtstarted,dateclosed';
            m.findBy = [ 'unitid': entity.objid ];
            m.orderBy = "dtstarted";
            return qryService.getList(m);
        }
    ] as BasicListModel;
        
}    