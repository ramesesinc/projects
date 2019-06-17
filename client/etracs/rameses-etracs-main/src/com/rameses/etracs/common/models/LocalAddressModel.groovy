package com.rameses.etracs.common.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class LocalAddressModel {
        
    @Service("LGUAddressService")
    def addrSvc;
        
    @Binding 
    def binding;
            
    def entity;
            
    @PropertyChangeListener
    def listener = [
                'entity.barangay' : {
            if (entity.province){
                entity.municipality = entity.barangay?.municipality;
            }
        }
    ]
            
    def getLookupBarangay(){
        return Inv.lookupOpener('barangay:lookup', [
                onselect: { brgy -> 
                    entity.barangay = brgy;
                    entity.city = brgy.city;
                    entity.province = brgy.province;
                    entity.municipality = brgy.municipality;
                },
                onempty :{
                    entity.barangay = [:];
                    entity.putAll(addrSvc.getLocalAddress());
                }
            ])
    }
            
    void init() {
        def m = addrSvc.getLocalAddress();
        entity.putAll( m ); 
        entity.type = 'local'; 
        
        //if there is text and no other fields, place the text in the street field
        if ( entity.text ) {
            if( !entity.unitno && !entity.blgno && !entity.bldgname && !entity.street && !entity.subdivision && !entity.barangay?.objid ) {
                entity.street = entity.text;
            }
        }
        if(!entity.barangay) entity.barangay = [:];
    }
            
    void refresh() {
        if(binding) binding.refresh();
    } 
            
    public String getAddressText(String delimiter) {
        if( delimiter == null ) delimiter = " \n";
        def d1 = [entity.unitno, entity.bldgno, entity.bldgname ].findAll{ it }.join(" ");
        def d2 = [entity.street, entity.subdivision ].findAll{ it }.join(" ");
        def d3 = [entity.barangay?.name, entity.city, entity.municipality, entity.province ].findAll{ it }.join(", ");
        if ( d1 ) d1 = d1+' ';
        if ( d2 ) d2 = d2+' ';
        if ( d3 ) d3 = d3+' ';
        return [d1,d2,d3].findAll{ it }.join( delimiter );
    }
}
