package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class PostBillOfMaterialsModel {
    
    @Service("QueryService")
    def querySvc;
    
    @Caller
    def caller;
    
    def entity = [:];
    
    def paymentOption = "fullpayment";
    
    void init() {
        def m = [_schemaname:'waterworks_application_bom'];
        m.findBy = [parentid: caller.entity.objid];
        m.select = "amount:{SUM(item.unitprice*qty)}";
        m.where = "NOT(cwdsupplied=1)";
        def z = querySvc.findFirst(m);
        entity.amount = z.amount;
    }
    
    def listHandler = [
        fetchList: {
            
        }
    ] as BasicListModel;
    
    
}