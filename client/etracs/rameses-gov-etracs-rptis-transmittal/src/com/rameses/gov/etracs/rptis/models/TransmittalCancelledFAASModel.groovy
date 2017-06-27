package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class TransmittalCancelledFAASModel extends TransmittalModel
{
    public String getFileType(){
        return 'cancelledfaas';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('cancelledfaas:lookup', [
            onselect : {items ->
                doValidateItems(items);
                items.each{
                    def item = buildItemData(it);
                    validateItem(item);
                    svc.saveItem(item);
                    entity.items << item;
                }
                listHandler.reload();
            },
            
            onempty : {
                selectedItem.refno = null;
                selectedItem.refid = null;
                selectedItem.state = null;
                selectedItem.message = null;
            },
            
            multiSelect : true,
        ])
    }
    
    public def buildItemData(cancelledfaas){
        def item = [:]
        item.refid = cancelledfaas.objid;
        item.refno = cancelledfaas.txnno;
        item.state = cancelledfaas.state;
        return item;
    }

    List getTransmittalTypes(){
        return ['FORAPPROVAL']
    }

    void doValidateItems(items){
        items.each{cancelledfaas ->
            if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && cancelledfaas.lguid != entity.tolgu.objid )
            throw new Exception('Cancelled FAAS ' + cancelledfaas.txnno + ' is invalid. Only record from ' + entity.tolgu.name + ' is accepted.');
            
        if (!cancelledfaas.state.matches('DRAFT'))
            throw new Exception('Cancelled FAAS ' + cancelledfaas.txnno + ' state is invalid. Only draft state is allowed.')
            
            def exist = entity.items.find{it.refid == cancelledfaas.objid}
            if (exist) throw new Exception('Cancelled FAAS ' + cancelledfaas.txnno + ' has already been added.');
        }
    }    
}
