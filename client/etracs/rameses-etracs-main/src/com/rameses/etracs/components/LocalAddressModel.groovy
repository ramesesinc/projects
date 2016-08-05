package com.rameses.etracs.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class LocalAddressModel extends ComponentBean {
    
    @Service("LGUAddressService")
    def addrSvc;
    
    public void init() {
        if(!entity) {
            setEntity(addrSvc.getLocalAddress());
        }
        if(!entity.province) {
            def m = addrSvc.getLocalAddress();
            if( m.city ) entity.city = m.city;
            if( m.municipality ) entity.municipality = m.municipality;
            entity.province = m.province;
        }
    }
    
    private void buildText() {
        def m1 = [entity.bldgno, entity.unitno, entity.bldgname].findAll{it!=null}.join(" ");
        def m2 = [entity.street, entity.subdivision, entity.barangay?.name].findAll{it!=null}.join(" ");
        def m3 = [entity.city, entity.municipality, entity.province].findAll{it!=null}.join(" ");
        entity.text = [m1,m2,m3].findAll{ it }.join("\n"); 
    }
    
    @PropertyChangeListener
    def listener = [
        ".*": { buildText();}
    ];

    def getLookupBarangay(){
        return Inv.lookupOpener('barangay:lookup', [
            onselect: { brgy -> 
                entity.barangay = brgy;
                buildText();
            },
            onempty :{
                entity.barangay = [:];
                buildText();
            }
        ])
    }
    
    void showText() {
        MsgBox.alert( entity.text );
    }
    
    public def getEntity() {
        return getValue();
    }
    
    public void setEntity(def o ) {
        setValue( o );
    }
    
} 
