package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class TransmittalFAASModel extends TransmittalModel
{
    public String getFileType(){
        return 'faas';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('faas:lookup', [
            onselect : {items ->
                doValidateItems(items);
                items.each{it
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
    
    public def buildItemData(faas){
        def refno = (faas.tdno ? faas.tdno : faas.fullpin)
        def item = [:]
        item.refid = faas.objid;
        item.refno = refno;
        item.state = faas.state;
        return item;
    }
    
    void doValidateItems(items){
        items.each{faas ->
            if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && faas.lguid != entity.tolgu.objid )
                throw new Exception('FAAS is invalid. Only FAAS from ' + entity.tolgu.name + ' is accepted.');

            if (entity.type == 'SYNC'){
                if (!faas.state.matches('CURRENT|CANCELLED')) 
                    throw new Exception('FAAS state is invalid. Only current or cancelled state is allowed.');
            }
            else{
                if (RPTUtil.isTrue(faas.datacapture)){
                    if (faas.state != 'FORAPPROVAL')
                        throw new Exception('FAAS state is invalid. Only For Approval state is allowed.')
                }
                else{
                    if (! faas.state.matches('INTERIM'))
                        throw new Exception('FAAS state is invalid. Only Interim state is allowed.')
                    // service.checkTaskForApprovalState()
                }
            }
            def refno = (faas.tdno ? faas.tdno : faas.fullpin)
            def exist = entity.items.find{it.refid == faas.objid}
            if (exist) throw new Exception('FAAS ' + refno + ' has already been added.');
        }
    }

    def view() {
        return Inv.lookupOpener('faasdata:open', [entity: [objid:selectedItem.refid]]);
    }
    
}
