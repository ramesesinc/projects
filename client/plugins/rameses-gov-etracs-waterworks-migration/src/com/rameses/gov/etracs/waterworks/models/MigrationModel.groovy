package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class MigrationModel {

    private final String SCHEMA_NAME = 'waterworks_migration';
    
    @Service('PersistenceService') 
    def persistSvc; 
    
    def entity;
    def file, datafile;
    def mode = 'read'; 

    @FormTitle 
    def getFormTitle() { 
        return 'Migration'; 
    }
    @FormId
    def getFormId() { 
        if ( mode == 'create' ) {
            return SCHEMA_NAME +':create';
        } else {
            return SCHEMA_NAME;
        } 
    }
    String getTitle() {
        if ( mode ==  'create') {
            return 'New Migration'; 
        } else {
            return 'Migration';
        }
    }
    
    void create() {
        mode = 'create';
        entity = [:]; 
    }
    void open() {
        def o = persistSvc.read([_schemaname: SCHEMA_NAME, objid: entity.objid ]); 
        if ( !o ) throw new Exception('migration record not found'); 
        
        def base64 = new com.rameses.util.Base64Cipher();
        def infodef = o.infodef; 
        if ( infodef ) {
            infodef = base64.decode( infodef.toString() ); 
        } else {
            infodef = [:]; 
        }
        
        entity = [:];
        file = datafile = null; 
        if ( infodef.filepath ) {
            file = new java.io.File( infodef.filepath ); 
            datafile = new DataFile( infodef ); 
        }
    }
    
    
    void setFile( o ) { 
        this.file = o; 
        loadFile(); 
    } 
    
    void loadFile() { 
        try { 
            datafile = new DataFile( file ); 
        } catch( e ) {
            datafile = null; 
            MsgBox.err( e );
        }
        
        fieldListHandler.reload(); 
    } 
    
    def getColumnDefs() {
        return (datafile ? datafile.getColDefs() : null); 
    }
    
    def fieldListHandler = [
        fetchList: { o-> 
            return (datafile ? datafile.getColFields() :  null); 
        }, 
        afterColumnUpdate: { item, colname-> 
            if ( colname == 'defaultvalue' && item != null ) {
                def value = item.get(colname); 
                if ( value != null ) {
                    if (!datafile.isValidDefaultValue( item, value )) {
                        item.remove(colname); 
                    }
                }
            } 
        }
    ] as EditorListModel; 
    
    def doSubmit() { 
        if ( datafile == null ) 
            throw new Exception("Data File is not loaded. Please verify"); 
        
        if ( !MsgBox.confirm('You are about to submit this transaction. Proceed?')) {
            return null; 
        }

        def base64 = new com.rameses.util.Base64Cipher();
        def infodef = datafile.buildDef(); 
        entity.infodef = base64.encode( infodef );  
        entity.totalrows = infodef.totalrows; 
        entity.currentrow = 0; 
        entity.state = 'DRAFT';
        if ( !entity.objid ) { 
            entity.objid = 'IMP'+ new java.rmi.server.UID();
        }         
        entity._schemaname = SCHEMA_NAME;
        persistSvc.create( entity );  
        
        try {
            return '_close'; 
        } finally { 
            try {
                def param = [ entity: [:]]; 
                param.entity.objid = entity.objid; 
                def op = Inv.lookupOpener('waterworks_migration:open', param);  
                Inv.invoke( op ); 
            } catch(Throwable t){
                t.printStackTrace(); 
            }
        }
    }
}