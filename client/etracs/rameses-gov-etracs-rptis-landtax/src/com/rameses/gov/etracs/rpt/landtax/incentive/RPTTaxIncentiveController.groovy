
package com.rameses.gov.etracs.rpt.landtax.incentive;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.*;

public class RPTTaxIncentiveController extends CRUDController
{
    
    String serviceName = 'RPTTaxIncentiveService';
    String entityName  = 'rpttaxincentive';
    String prefixId    = 'TI';
    
    boolean confirmSave = false;
    
    String getTitle(){
        return 'Realty Tax Incentive (' + entity.state + ')';
    }
    
    Map createEntity(){
        return [items : []]
    }
    
    def getLookupEntity(){
        return Inv.lookupOpener('entity:lookup', [
                onselect : {
                    def address = it.address;
                    it.address = address.text;
                    entity.taxpayer = it
                    binding.refresh('entity.taxpayer.*');
                },
                onempty : {
                    entity.taxpayer = null;
                }
                
        ]);
    }
     
    /*=========================================================
     *
     * ITEM SUPPORT
     *
     *===========================================================*/
    def selectedItem;
    
    def getLookupRPTLedger(){
        return InvokerUtil.lookupOpener('rptledger:lookup', [
            onselect : {
                if (it.state == 'PENDING')
                    throw new Exception('Ledger is not yet approved.')
                if (it.state == 'CANCELLED')
                    throw new Exception('Ledger has already been cancelled.')
                    
                selectedItem.ledger = it;
                selectedItem.tdno = it.tdno;
                selectedItem.rptledgerid = it.objid;
            },
            
            onempty : { 
                selectedItem.ledger = null;
                selectedItem.tdno = null;
                selectedItem.rptledgerid = null;
            },
        ])
    }
    
    def listHandler = [
        getRows     : { return 50 },
            
        fetchList   : { return entity.items },
                
        createItem  : { [
                objid       : 'TII' + new java.rmi.server.UID(),
                rpttaxincentiveid : entity.objid,
                basicrate   : 0,
                sefrate     : 0,
        ]},
                
        onRemoveItem : { item ->
                if (MsgBox.confirm('Delete item?')){
                    service.deleteItem(item);
                    entity.items.remove(item);
                    return true;
                }
                return false;
        },
                
        validate :  { li ->
                def item =  li.item;
                RPTUtil.required('TD No.', item.ledger);
                RPTUtil.required('From Year', item.fromyear);
                RPTUtil.required('To Year', item.toyear);
                if (item.basicrate == null)
                    throw new Exception('Basic Incentive Rate is required.')
                if (item.sefrate == null)
                    throw new Exception('SEF Incentive Rate is required.')
        },
                
        onAddItem : {item ->
                entity.items.add( item );
        }
                
    ] as EditorListModel
            
    
    
}