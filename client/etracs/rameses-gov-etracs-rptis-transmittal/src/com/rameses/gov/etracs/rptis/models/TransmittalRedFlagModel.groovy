package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class TransmittalFAASModel extends TransmittalModel
{
    public String getFileType(){
        return 'rptredflag';
    }
    
    def init(){
        if (!OsirisContext.env.ORGCLASS.equalsIgnoreCase('PROVINCE')){
            throw new Exception('Red Flag transmittal for ' + OsirisContext.env.ORGCLASS.toLowerCase() + ' is not yet supported.');
        }
        return super.init();
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('rptredflag:lookup', [
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
    
    public def buildItemData(redflag){
        def item = [:]
        item.refid = redflag.objid;
        item.refno = redflag.caseno;
        item.state = redflag.state;
        return item;
    }
    
    List getTransmittalTypes(){
        return ['FORAPPROVAL']
    }
    
    void doValidateItems(items){
        items.each{redflag ->
            if (redflag.state != 'OPEN')
            throw new Exception('Red Flag ' + redflag.caseno + ' is invalid. Only open state is allowed.');
            
            if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && redflag.lguid != entity.tolgu.objid )
                throw new Exception('Red Flag ' + redflag.caseno + ' is invalid. Only record from ' + entity.tolgu.name + ' is allowed.');

            def exist = entity.items.find{it.refid == redflag.objid}
            if (exist) throw new Exception('Red Flag ' + redflag.caseno + ' has already been added.');
        }
    }  
    
}
