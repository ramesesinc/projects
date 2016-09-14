package com.rameses.entity.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class EntityLookupModel extends ComponentBean {

    @Binding
    def binding;
    
    @Service('PersistenceService')
    def persistenceSvc; 
        
    def _entity;
    
    def onselect; 
    def onempty;
    def entityTypeCaller; 
    
    private boolean _allowCreate = true;
    private boolean _allowOpen = true;
    
    String getEntityType() {
        def type = (entityTypeCaller?.entityType == null? null: entityTypeCaller.entityType); 
        return type; 
    }
        
    def getLookupEntity() { 
        def params = [:]; 
        params.onselect = { o-> 
            fireOnselect( o ); 
        } 
        params.onempty = {
            if ( onempty ) { 
                onempty(); 
            } else {
                setValue( null ); 
            }
            binding.refresh(); 
        }
        def etype = getEntityType();
        if(etype==null) etype = "entity";
        return Inv.lookupOpener( etype+':lookup', params );
    } 
    
    void fireOnselect( o ) { 
        def schemaname = 'entity' + (o.type ? o.type :'').toLowerCase(); 
        o = persistenceSvc.read([ _schemaname: schemaname, objid: o.objid ]); 
        if ( onselect ) { 
            onselect( o ); 
        } else { 
            setValue( o ); 
        } 
        binding.refresh(); 
    }
    
    public def getEntity() {
        return getValue();
    }
    
    public def viewEntity() {
        if( !entity?.objid ) throw new Exception("Please select an entity");
        def type = (entity.type ? entity.type :'').toLowerCase(); 
        if(type==null) type = "individual";
        def op = Inv.lookupOpener( 'entity'+ type +':open', [entity: entity] );
        op.target = 'popup';
        return op;
    }
    
    public def addEntity() {
        def stype = getEntityType(); 
        if ( !stype || stype=='entity' ) stype = 'entityindividual';

        def params = [:]; 
        params.onselect = { o-> 
            fireOnselect( o ); 
        };
        params.allowSelect = true;
        def opener = null; 
        try {
            opener = Inv.lookupOpener( stype +':create', params );
        } catch(Throwable t) {;}
        if ( !opener )
            throw new Exception("No sufficient permission to add entity");
        return opener; 
    } 
    
    public void setAllowCreate(boolean b) {
        _allowCreate = b;
    }
    
    public boolean isAllowCreate() {
        try {
            if(_allowCreate == false) return false;
            def etype = getEntityType();
            if(etype==null) etype = "entityindividual";
            println (etype +':create');
            def o = Inv.lookup( etype +':create' );
            return ( o ? true : false );
        } catch(Throwable t) { 
            return false; 
        } 
    }
    
    public void setAllowOpen(boolean b) {
        _allowOpen = b;
    }
    
    public boolean isAllowOpen() {
        try {
            if(_allowOpen==false) return false;
            def o = getValue();
            if(o?.type==null) {
                o.type = "individual";
            };
            String etype = "entity"+o.type.toLowerCase();
            def op = Inv.lookup( etype +':open' );
            return ( op ? true : false ); 
        } catch(Throwable t) { 
            return false; 
        } 
    } 
    
} 
