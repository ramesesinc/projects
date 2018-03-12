package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.DateUtil;
import java.rmi.server.UID;

class CaptureConsumptionInitialModel {
    
    @Service("WaterworksComputationService")
    def compSvc;
    
    @Service('PersistenceService')
    def persistenceService; 
    
    def parent;    
    def entity; 
    
    def mode = 'init';
    def title = 'Capture Consumption Initial';
    def query = [:]; 
   
    def acctid;
    
    void create() { 
        entity = [:]; 
        acctid = parent?.objid; 
    }
    
    def doCancel() {
        return '_close'; 
    }
    
    def doSave() {
        def startdate = convertDate( entity.fromyear, entity.frommonth, 'Start Date' ); 
        def enddate   = convertDate( entity.toyear, entity.tomonth, 'End Date' ); 
        if ( startdate.after( enddate )) 
            throw new Exception('Start Period must not be greater than the End Period'); 

        def p = [ acctid: acctid, items: [] ];         
        def YM = new java.text.SimpleDateFormat("yyyy-MM");
        while (( startdate.before(enddate) || startdate.equals(enddate) )) {
            def arr = YM.format( startdate ).split('-'); 
            def m = [ acctid: p.acctid ]; 
            m.year = arr[0].toInteger(); 
            m.month = arr[1].toInteger(); 
            p.items << m; 
            startdate = DateUtil.add( startdate, '1M' ); 
        } 
        
        p = compSvc.createBatch( p );  
        def ids = p.items.collect{"'"+ it.objid +"'"}.join(','); 
        if ( !ids ) ids = "''"; 
        
        query.where = [" objid in ("+ ids +") ", [:]];  
        query.debug = true;
        mode = 'entry'; 
        return mode; 
    } 
    
    def convertDate( year, month, title ) { 
        def sb = new StringBuilder(); 
        sb.append( year.toString()).append("-"); 
        sb.append( month.toString().padLeft(2, '0')).append("-01"); 
        try { 
            return java.sql.Date.valueOf( sb.toString() ); 
        } catch(Throwable t) {
            throw new Exception('Invalid '+ title +' value'); 
        }
    }
    
    def handler = [
        beforeColumnUpdate: { item, colName, value ->
            try { 
                item.prevreading = Math.max( 0, item.prevreading ); 
                
                def r = [:];
                def newvalue = null; 
                if (colName == "reading") { 
                    newvalue = Math.max( 0, value ); 
                    
                    def p = [ objid: item.acctid ];
                    p.volume = Math.max( 0, newvalue - item.prevreading ); 
                    r.amount = compSvc.compute( p );
                    r.volume = p.volume; 
                    r.reading = newvalue; 
                    
                } else if (colName == "volume") {
                    newvalue = Math.max( 0, value ); 
                    
                    def p = [ objid: item.acctid, volume: newvalue ];
                    r.amount = compSvc.compute( p ); 
                    r.volume = p.volume; 
                    newvalue = p.volume;
                    
                } else {
                    newvalue = value; 
                }

                def m = [ _schemaname: 'waterworks_consumption' ];
                m.putAll( r );
                m.put( colName, newvalue ); 
                m.findBy = [objid: item.objid ];
                persistenceService.update( m );
                
                m = [ _schemaname: 'waterworks_consumption', objid: item.objid ];
                def o = persistenceService.read( m ); 
                item.putAll( o ); 
                return true; 
            } catch(e) {
                MsgBox.err(e);
                return false;
            }
        }
    ];
}