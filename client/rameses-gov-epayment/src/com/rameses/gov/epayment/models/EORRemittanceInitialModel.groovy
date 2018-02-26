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
public class EORRemittanceInitialModel  extends CrudFormModel {
    
    @Service("OnlinePaymentResolverService")
    def onlineSvc;

    @Service("EORRemittanceService")
    def remittanceSvc;
    
    def partner;
    def partnerList;
    
    void afterCreate() {
        def m = [_schemaname: 'paymentpartner'];
        m.where = ["1=1"];
        partnerList = queryService.getList(m);
    }
    
    @PropertyChangeListener
    def listener = [
        "partner" : { o->
            listHandler.reload();
        }
    ];
    
    def listHandler = [
        fetchList: { o->
            if( !partner ) return [];
            def m = [_schemaname: 'eor'];
            m.where = ["partnerid =:partnerid", [partnerid: partner.objid ]];
            m.orderBy = "tracedate";
            return queryService.getList(m);
        },
        isMultiSelect: {
            return true;
        }
    ] as BasicListModel;
   
    def selectedPO;
    def resolveListHandler = [
        fetchList: { o->
            return [];
        },
        isMultiSelect: {
            return true;
        }
        
    ] as BasicListModel;
    
    
    public void resolve() { 
        resolveListHandler.selectedValue?.each{ 
            onlineSvc.resolve( it ); 
        } 
    }
    
    public def save() {
        if(!MsgBox.confirm("Confirm?")) return null;
        def arr = listHandler.selectedValue;
        entity.partnerid = partner.objid;
        entity.items = arr.collect{ [objid: it.objid ] };
        entity.amount = arr.sum{ it.amount }; 
        entity = remittanceSvc.create( entity );
        return Inv.lookupOpener("eor_remittance:open", [entity: entity ]);
    }
    
    
}