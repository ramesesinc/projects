package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnHandlerForward extends AFTxnHandler {

    def form;
    
    String title = "Forward Issuance of Accountable Forms";
    
    @FieldValidator
    def validators = [
        "form.startseries" : { o->
            int len = form.afunit.serieslength;
            if( (o+"").length() > len )
                throw new Exception("XSeries length must be less than or equal to "+len);
            
            try {
                Integer.parseInt(o);
            }
            catch(e) {
                throw new ValidatorException("Start series must be a number" );
            }
            if( !o.endsWith("1") )
                throw new ValidatorException("Start series must end with 1" );
            if(o.length() != form.afunit.serieslength ) {
                throw new ValidatorException("Start series length must be " + form.afunit.serieslength );
            }      
        }
    ];

    @PropertyChangeListener
    def listener = [
        "entity.issueto" : { o->
            entity.respcenter = [objid:o.orgid, name:o.orgname];
        },
        "form.startseries" : { o->
            int len = form.afunit.serieslength;
            int interval = form.afunit.interval;
            if(!interval || interval < 0 ) interval = 1;
            int starting = Integer.parseInt(""+o);    
            int ending = (starting + (form.afunit.qty * interval) ) ;
            form.startseries = String.format( "%0"+len+"d", starting);
            form.endseries = String.format( "%0"+len+"d", ending - 1);
            binding.refresh("form.(start|end)series");
        }
    ];
    
    void init() {
        form = [:];
    }
    
    
    
}    