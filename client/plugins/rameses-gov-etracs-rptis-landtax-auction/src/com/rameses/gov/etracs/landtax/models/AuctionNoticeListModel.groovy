package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionNoticeListModel extends CrudListModel
{
    def print() {
        if (entityContext) {
            return Inv.lookupOpener('batchprint:init', [
                    reportHandler: 'propertyauction_notice:batchprint',
                    items: getItems(),
            ]);
        }
        return null;
    }
    
    def getItems() {
        def m = [_schemaname: 'propertyauction_notice'];
        m.where = ["step_objid = :stepid and state = 'FORDELIVERY'", [stepid: entityContext.step.objid]];
        def items = getQueryService().getList(m);
        if (!items){
            throw new Exception('There are notices for delivery.');
        }
        return items;
    }
    
}