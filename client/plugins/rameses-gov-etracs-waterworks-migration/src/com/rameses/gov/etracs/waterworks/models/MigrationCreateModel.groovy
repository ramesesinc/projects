package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class MigrationCreateModel {

    private final String SCHEMA_NAME = 'waterworks_migration';
    
    @Service('PersistenceService') 
    def persistSvc; 
    
    @Service('QueryService') 
    def querySvc; 
    
    @Binding 
    def binding;
    
    @Script('ReportPeriod')
    def reportPeriod;
    
    def base64 = new com.rameses.util.Base64Cipher();
    
    def entity, stepname;
    def file, datafile;
    def mode = 'create'; 
    def title = 'New Migration';
    
    @FormId
    def getFormId() { 
        return SCHEMA_NAME +':create';
    }
    
    def create() {
        mode = 'create'; 
        stepname = 'step1'; 
        entity = [ objid: 'M'+ new java.rmi.server.UID()]; 
        return stepname; 
    }
    
    def doCancel() {
        if ( MsgBox.confirm('You about to cancel this transaction. Proceed?')) {
            return '_close'; 
        } 
        return null;  
    }

    def doClose() {
        return '_close'; 
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
        
        //*
        def fields = datafile.getColFields(); 
        fields.find{ it.name=='acctno' }.cell = [index:4, caption:"ACCTNO"];
        fields.find{ it.name=='acctname' }.cell = [index:5, caption:"NAME"];
        fields.find{ it.name=='accttype' }.cell = [index:6, caption:"ACCTYP"];
        fields.find{ it.name=='prevreading' }.cell = [index:9, caption:"PRVRDG"];
        fields.find{ it.name=='currentreading' }.cell = [index:8, caption:"CURRDG"];
        fields.find{ it.name=='dtreading' }.defaultvalue = "2017-09-01";
        fields.find{ it.name=='meterno' }.cell = [index:7, caption:"MTRNO"];
        fields.find{ it.name=='metersize' }.defaultvalue = "0.5";
        fields.find{ it.name=='sectorcode' }.cell = [index:1, caption:"DISTNO"];
        fields.find{ it.name=='zonecode' }.cell = [index:2, caption:"PHASE"];
        //*/
       
        fieldListHandler.reload(); 
    } 
    
    def getColumnDefs() {
        return (datafile ? datafile.getColDefs() : null); 
    }
    
    def fieldListHandler = [
        fetchList: { o-> 
            return (datafile ? datafile.getColFields() :  null); 
        }, 
        isColumnEditable: { item, colname-> 
            return (mode == 'create' ? true : false); 
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
    
    
    def accttypelist, metersizelist, zonelist; 
    def validitemlist=[], erroritemlist=[];
    def minvaliditems=[], minerroritems=[];
    
    def doNextToStep2() {
        if ( datafile == null ) 
            throw new Exception("Data File is not loaded. Please verify"); 
        
        datafile.colFields.each{ f-> 
            if ( !f.required ) return; 
            if ( f.cell || f.defaultvalue ) {
                //do nothing 
            } else {
                throw new Exception(''+ f.caption +' needs to have a mapping or default value'); 
            }
        }

        validitemlist.clear(); 
        erroritemlist.clear();
        minvaliditems.clear();
        minerroritems.clear();
        
        accttypelist = querySvc.getList([_schemaname: 'waterworks_migrationmapping_accttype', where:["1=1"]]); 
        metersizelist = querySvc.getList([_schemaname: 'waterworks_migrationmapping_metersize', where:["1=1"]]); 
        zonelist = querySvc.getList([_schemaname: 'waterworks_migrationmapping_zone', where:["1=1"]]); 
        
        persistSvc.removeEntity([_schemaname:'waterworks_migration', findBy:[objid: entity.objid], objid: entity.objid]); 
        
        def options = [:]; 
        options.onbuildItem = { o-> 
            o.billyear = entity.billyear;
            o.billmonth = entity.billmonth;
            
            if ( o.accttype ) { 
                def a = accttypelist.find{ it.objid==o.accttype }
                o.classificationid = a?.refid; 
            }
            if ( o.metersize ) { 
                def a = metersizelist.find{ it.objid==o.metersize }
                o.metersizeid = a?.refid; 
            }
            if ( o.sectorcode && o.zonecode ) { 
                def a = zonelist.find{( it.sectorcode==o.sectorcode && it.zonecode==o.zonecode )} 
                o.sectorid = a?.sector?.objid; 
                o.zoneid = a?.zone?.objid; 
            } 
            
            if ( o.sectorid ) { 
                def findBy = [sectorid: o.sectorid, year: entity.billyear, month: entity.billmonth]; 
                def wbc = persistSvc.read([_schemaname: 'waterworks_billing_cycle', findBy: findBy]); 
                if ( !wbc ) throw new Exception('No available billing cycle for the following: Sector='+ o.sectorid +', Year='+ o.billyear +', Month='+ o.billmonth ); 
                
                o.billingcycleid = wbc.objid; 
            } 
            
            o.parentid = entity.objid; 
            o.objid = entity.objid.toString() +'-'+ o.indexno; 
            
            if ( o.haserror ) {
                erroritemlist << o; 
                
                if ( minerroritems.size() < 100 ) {
                    minerroritems << o; 
                }
                
            } else {
                validitemlist << o; 
                
                if ( minvaliditems.size() < 100 ) {
                    minvaliditems << o; 
                }
            }
        } 
        
        datafile.read( options ); 
        stepname = 'step2';
        return stepname; 
    } 
    
    def validListHandler = [
        fetchList: { o-> 
            return minvaliditems; 
        }
    ] as BasicListModel; 
    
    def selectedErrorItem;
    def errorListHandler = [
        fetchList: { o-> 
            return minerroritems; 
        }
    ] as BasicListModel; 
    
    
    def doBackToStep1() { 
        stepname = 'step1'; 
        return stepname; 
    } 
    
    private def uploadOptions = [:]; 
    
    def doUpload() { 
        def infodef = datafile.buildDef(); 
        entity.infodef = base64.encode( infodef );  
        
        def params = [:]; 
        params.currentrow = 0; 
        params.totalrows = validitemlist.size(); 
        params.initHandler = { o-> 
            uploadOptions = o; 
        }
        params.stopHandler = { 
            uploadOptions.cancelled = true; 
        }
        params.startHandler = {
            uploadOptions.cancelled = false;
            upload(); 
        }
        return Inv.lookupOpener('waterworks_migration:upload', params); 
    }
    
    def progressvalue; 
    private void upload() {
        entity.currentrow = 0;
        entity.totalrows = validitemlist.size(); 
        uploadOptions.totalrows = entity.totalrows;
        
        Number size = entity.totalrows; 
        for (int i=0; i<size.intValue(); i++) { 
            if ( uploadOptions.cancelled == true ) break;
            
            def item = validitemlist.get(i); 
            if ( !"true".equals(item.uploaded.toString())) { 
                item._schemaname = 'waterworks_migrationitem'; 
                persistSvc.create( item ); 
                item.uploaded = true; 
            }
            
            uploadOptions.currentrow = (i+1); 
            if ( uploadOptions.refresh ) {
                uploadOptions.refresh(); 
            } 
        }
        
        if ( uploadOptions.cancelled == true ) return; 
        if ( uploadOptions.finish ) { 
            uploadOptions.finish(); 
            
            entity.state = 'PENDING'; 
            entity._schemaname = SCHEMA_NAME;
            def o = persistSvc.create( entity ); 
            if ( o ) entity.putAll( o ); 
            
            stepname = 'finish';
            try {
                def param = [ entity: [:]]; 
                param.entity.objid = entity.objid; 
                def op = Inv.lookupOpener('waterworks_migration:open', param);  
                Inv.invoke( op ); 
            } catch(Throwable t){
                t.printStackTrace(); 
            } finally {
                binding.fireNavigation( '_close' ); 
            }
        } 
    } 
}