package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class TransmittalCancelledFAASModel extends TransmittalModel
{
    public String getFileType(){
        return 'cancelledfaas';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('cancelledfaas:lookup', [
            onselect : {
                def item = buildItemData(it);
                validateItem(item);
                selectedItem.putAll(item);
            },
            
            onempty : {
                selectedItem.refno = null;
                selectedItem.refid = null;
                selectedItem.state = null;
                selectedItem.message = null;
            }
        ])
    }
    
    public def buildItemData(cancelledfaas){
        if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && cancelledfaas.lguid != entity.tolgu.objid )
            throw new Exception('Cancelled FAAS is invalid. Only record from ' + entity.tolgu.name + ' is accepted.');
            
        if (!cancelledfaas.state.matches('DRAFT'))
            throw new Exception('Cancelled FAAS state is invalid. Only draft state is allowed.')
        def refno = cancelledfaas.txnno;
        def item = [:]
        item.refid = cancelledfaas.objid;
        item.refno = refno;
        item.state = cancelledfaas.state;
        return item;
    }

    List getTransmittalTypes(){
        return ['FORAPPROVAL']
    }

    
}
