package com.rameses.etracs.common.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class NonLocalAddressModel {
        
    @Binding
    def binding;
        
    def entity;
    def addressTypes = ["CITY", "MUNICIPALITY"];
            
    @PropertyChangeListener
    def listener = [
        "entity.addresstype" : { o->
            entity.city = '';
            entity.municipality = '';
        }
    ];
            
    void init() {
        if(!entity.addresstype) {
            entity.addresstype = 'CITY';
            entity.municipality = '';
            entity.city = '';
            entity.province = '';
        }    
        if(!entity.barangay) entity.barangay = [:];
        entity.type = 'nonlocal';
    }
            
    void refresh() {
        if(binding) binding.refresh();
    } 
            
    public String getAddressText(String delimiter) {
        if(delimiter==null) delimiter = " \n";
        def d1 = [entity.unitno, entity.bldgno, entity.bldgname ].findAll{ it }.join(" ");
        def d2 = [entity.street, entity.subdivision ].findAll{ it }.join(" ");
        def d3 = [entity.barangay?.name, entity.city, entity.municipality, entity.province ].findAll{ it }.join(", ");
        if ( d1 ) d1 = d1+' ';
        if ( d2 ) d2 = d2+' ';
        if ( d3 ) d3 = d3+' '; 
        return [d1,d2,d3].findAll{ it }.join( delimiter );
    }
}
