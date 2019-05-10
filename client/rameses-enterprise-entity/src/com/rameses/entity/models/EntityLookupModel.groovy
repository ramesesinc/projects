package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.BreakException;
import com.rameses.seti2.models.CrudLookupModel;

class EntityLookupModel extends CrudLookupModel {
    
    boolean allowSelectEntityType = true;
    boolean lookupSelectedItem = false;
            
    def entityTypeMatch;
                                    
    def entityTypes = ['INDIVIDUAL','JURIDICAL','MULTIPLE']; 
    def selectedType;
    
    def confs = [
        'entityno'     : [ width: 80 ],
        'name'         : [ width: 200 ],
        'type'         : [ width: 80 ],
        'address.text' : [ width: 300 ]
    ]; 
    
    public void initColumn( def c ) { 
        
        def conf = confs.get( c.name ); 
        if ( !conf ) return; 
        
        if ( conf.type != null ) c.type = conf.type;
        if ( conf.width != null ) c.width = conf.width; 
        if ( conf.minWidth != null ) c.minWidth = conf.minWidth; 
        if ( conf.maxWidth != null ) c.maxWidth = conf.maxWidth; 
        if ( conf.visible != null ) c.visible = conf.visible; 
        if ( conf.caption != null ) c.caption = conf.caption; 
    }     

    void afterInit() {
        if(entityTypeMatch) {
            entityTypes = entityTypes.findAll{ it.toLowerCase().matches(entityTypeMatch) };
        }
    }
            
    @PropertyChangeListener
    def listener = [
                'selectedType' : { o->
            whereStatement = [ "type=:t", [t: selectedType] ];
            reload();
        }
    ];
            
    def lookupSelectedValue( def o ) {
        //load the actual record after....
        if(lookupSelectedItem) {
            def m = [_schemaname: 'entity' + o.type.toLowerCase() ];
            m.objid = o.objid;
            return persistenceService.read( m );
        }
        return o;
    }
    
    void search() { 
        if ( searchText ) {
            def tmpstr = searchText.toString().trim().replaceAll('%',''); 
            if ( tmpstr.length() <= 1 ) {
                throw new Exception (''' 
                    The number of characters allowed for search text must be greater than 1. 

                    Examples: 
 
                    DE        (Searching lastnames that starts with DE)
                    CRUZ      (Searching lastnames that starts with CRUZ)
                    CRUZ%JUAN (Searching names that starts with CRUZ and followed by JUAN) 

                '''); 
            }
        }
        super.search(); 
    } 
}