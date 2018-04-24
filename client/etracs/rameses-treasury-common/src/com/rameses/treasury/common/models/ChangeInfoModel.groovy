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
    
    def entity = [:];
    def oldValues = [:];
    
    def reftype;
    def refkeys;
    def action = "update";
    
    def listener;
    def beforeUpdate;
    def keyfields = [];
    
    @PropertyChangeListener
    def fieldListener;
    
    public void init() {
        if(listener!=null) {
            fieldListener = [:];
            listener.each { k,v->
                if( !k.startsWith("data.")) k = "data." + k;
                fieldListener.put( k, { newVal ->  v( data, newVal ); })
            }
        }
        oldValues = MapBeanUtils.copy( data );
        oldValues.each { k,v->
            keyfields << k;
        }
        super.init();
    }
    
    public String getSchemaName() {
        return workunit?.info?.workunit_properties?.schemaName;
    }    
    
    def doOk() {
        if(beforeUpdate) beforeUpdate( data );
        //store key fields
        entity._schemaname = schemaName;
        if(!entity._schemaname) entity._schemaname = 'changeinfo';
        entity.data = data;
        entity.reftype = reftype;
        entity.refkeys = refkeys;
        entity.action = getAction();
        entity.keyfields = keyfields.join(",");
        
        if( keyfields.size() == 1 ) {
            entity.oldvalue = [oldValues.get( entity.keyfields )];
            entity.newvalue = [data.get( entity.keyfields )];
        }
        else {
            entity.oldvalue = oldValues;
            entity.newvalue = data;
        }
        changeInfoSvc.save( entity );
        return "_close";
    }
    
    
}
        