package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;


public class NewWaterworksApplication extends CrudPageFlowModel {
    
    @Service("WaterworksApplicationService")
    def appSvc; 

    def selectedRequirement;
    def imagedata; 

    /*
    def getLookupApplicant() { 
        def params = [:]; 
        params['query.type'] = 'INDIVIDUAL'; 
        params.allowSelectEntityType = false; 
        params.onempty = { entity.owner = null; } 
        params.onselect = { o-> 
            entity.owner = o; 
            if ( !entity.acctname ) {
                entity.acctname = o?.name; 
            }
        }
        return Inv.lookupOpener('entity:lookup', params); 
    } 
    */

    @PropertyChangeListener
    def listener = [
        'entity.owner' : { o->
            if ( !entity.acctname ) {
                entity.acctname = o?.name; 
                binding.refresh( "entity.acctname" );
            }
        }
    ];
    
    void afterCreate() { 
        entity.address = [:]; 
    }

    void fetchRequirements() { 
        if ( !entity.owner ) throw new BusinessException("Please specify an owner"); 
        def result = appSvc.getInitialInfo( [:] );
        entity.fees = result.fees; 
        entity.total = result.total;
        entity.requirements = result.requirements; 
        requirementList.reload();
    } 

    def feeList = [
        fetchList: {return [];}
    ] as BasicListModel;

    def requirementList = [
        fetchList: {o-> return entity.requirements; }
    ] as BasicListModel;    

    def base64 = new Base64Cipher();

    void addSignature() {
        com.rameses.rcp.sigid.SigIdViewer.open([ 
            getWidth: { return 300; }, 
            getHeight: { return 150; }, 
            onselect: { o-> 
                imagedata = o.imageData;
                def sigdata = [ 
                    image : imagedata, 
                    text  : o.sigString, 
                    key   : entity.objid 
                ]; 
                entity.signature = base64.encode( sigdata );
            } 
        ] as SigIdModel); 
    }      
    
    void removeSignature() { 
        imagedata = null; 
        entity.signature = null; 
    } 
    
    public void viewRequirement() {
        MsgBox.alert('selected ' + this.selectedRequirement);
    }
    
}