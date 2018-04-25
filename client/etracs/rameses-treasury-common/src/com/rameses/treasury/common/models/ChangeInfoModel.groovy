package com.rameses.treasury.common.models;
 
import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.constant.*;
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

/****
* This facility only extracts only a portion of the data. 
*/
public class ChangeInfoModel extends DynamicForm {
   
    @Service("ChangeInfoService")
    def changeInfoSvc;
    
    def entity;
    def oldValues = [:];
    
    def reftype;
    def refkeys;
    def action = "update";
    
    def listener;
    def beforeUpdate;
    def keyfield;
    def remarks;
    
    public void init() {
        if(!keyfield) {
            keyfield = invoker.properties.keyfield;
        }
        
        def newData = entity.get( keyfield );
        fields  = [];
        def m = [:];
        m.required = true;
        m.caption = invoker.caption;
        m.name = keyfield;
        invoker.properties.each { k,v->
            if(!k.matches("type|name|target|action")) {
                m.put( k, v );
            }
        }
        fields << m;

        //load reftype
        reftype = workunit?.info?.workunit_properties?.reftype;    
        
        //load refkey
        String _refkey = workunit?.info?.workunit_properties?.refkey;
        if(!_refkey) _refkey = "objid";
        
        //build the refkeys
        refkeys = [(_refkey) : entity.get(_refkey) ];
        data = [ (keyfield) : newData];
        
        if(data) {
            oldValues = MapBeanUtils.copy( data );
        }
       
        super.init();
    }
    
    public String getSchemaName() {
        return workunit?.info?.workunit_properties?.schemaName;
    }    
    
    public boolean vaidate(def keyfield, def value) {
        return true;
    }
    
    def doOk() {
        if(!validate(keyfield, data.get(keyfield)) ) return null;
        
        if(oldValues == data)
            throw new Exception("No changes have been made");
        
        if(beforeUpdate) {
            beforeUpdate( data );
        }
        //store key fields
        def m = [:];
        m._schemaname = schemaName;
        if(!m._schemaname) m._schemaname = 'changeinfo';
        m.remarks = remarks;
        m.data = data;
        m.reftype = reftype;
        m.refkeys = refkeys;
        
        m.action = getAction();
        m.keyfield = keyfield;
        
        def v = oldValues.get(keyfield);
        def v1 = data.get(keyfield);
        if( v instanceof Map ) {
            m.oldvalue = v;
            m.newvalue = v1;
        }
        else {
            m.oldvalue = [v];
            m.newvalue = [v1];
        }
        
        if( m.newvalue == null ) m.newvalue = [:];
        
        changeInfoSvc.save( m );
        try {
            if(caller) caller.reload();
        }
        catch(e){;}
        return "_close";
    }
    
}
        