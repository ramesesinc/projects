package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class MigrationOpenModel extends com.rameses.seti2.models.CrudFormModel {

    @Service('WaterworksMigrationService')
    def migrationSvc;
    
    @Binding 
    def binding;
    
    @Script('ReportPeriod')
    def reportPeriod;
    
    def file, datafile, html;
    
    void afterOpen() {
        def base64 = new com.rameses.util.Base64Cipher();
        def infodef = entity.infodef; 
        if ( infodef ) {
            infodef = base64.decode( infodef.toString() ); 
        } else {
            infodef = [:]; 
        }
        
        file = datafile = null; 
        if ( infodef.filepath ) {
            file = new java.io.File( infodef.filepath ); 
            datafile = new DataFile( infodef ); 
        }
        
        analyzeData(); 
    }
    
    def fieldListHandler = [
        fetchList: { o-> 
            return (datafile ? datafile.getColFields() :  null); 
        }
    ] as BasicListModel;     
    
    void doPost() {
        if (!MsgBox.confirm('You are about to post this transaction. Proceed?')) return; 

        migrationSvc.initPost([ objid: entity.objid ]); 
        def o = analyzeData(); 
        binding.refresh('html'); 
        
        if ( o.noclassificationidcount > 0 ) {
            throw new Exception('There are still unmapped account type records. Please verify'); 
        } 
        if ( o.nometeridcount > 0 ) {
            throw new Exception('There are still unmapped meter records. Please verify'); 
        }
        if ( o.nometersizeidcount > 0 ) {
            throw new Exception('There are still unmapped meter size records. Please verify'); 
        } 
        
        o = migrationSvc.post([ objid: entity.objid ]); 
        if ( o?.state ) entity.state = o.state; 
    }
    
    def analyzeData() { 
        def o = migrationSvc.analyzeData([ migrationid: entity.objid ]); 
        def stats = [];
        stats << [name:'classificationid', action:'viewUnmapped', value: o.noclassificationidcount, title:'Record count for unmapped classifications'];
        stats << [name:'meterid', action:'viewUnmapped', value: o.nometeridcount, title:'Record count for unmapped meters'];
        stats << [name:'metersizeid', action:'viewUnmapped', value: o.nometersizeidcount, title:'Record count for unmapped meter sizes'];
        stats << [name:'sectorid', action:'viewUnmapped', value: o.nosectoridcount, title:'Record count for unmapped sectors'];
        stats << [name:'zoneid', action:'viewUnmapped', value: o.nozoneidcount, title:'Record count for unmapped sector zones'];
        
        def buff = new StringBuilder();
        buff.append('<html><body><br/>');
        stats.each{ oo-> 
            buff.append('&nbsp;&nbsp;&nbsp;'+ oo.title +' ( <b>'); 
            if ( oo.value > 0 ) {
                buff.append('<font color="red">')
                buff.append( oo.value.toString() ); 
                buff.append('</font>'); 
            } else {
                buff.append( oo.value.toString() ); 
            }
            buff.append('</b> )&nbsp;&nbsp;&nbsp;')
            if ( oo.value > 0 ) {
                buff.append('<a href="'+ oo.action +'" name="'+ oo.name +'">View</a>');
            } 
            buff.append('<br/>');
        } 
        buff.append('</body></html>');
        html = buff.toString();
        return o; 
    }
    
    def viewUnmapped( params ) { 
        if ( !params?.name ) return null; 
        if ( params.name == 'classificationid') {
            params.title = 'Unmapped Classifications';
            params.list = migrationSvc.getUnmappedData([ migrationid: entity.objid, key: 'classificationid' ]); 
        } else if ( params.name == 'meterid') {
            params.title = 'Unmapped Meters';
            params.list = migrationSvc.getUnmappedData([ migrationid: entity.objid, key: 'meterid' ]); 
        } else if ( params.name == 'metersizeid') {
            params.title = 'Unmapped Meter Sizes';
            params.list = migrationSvc.getUnmappedData([ migrationid: entity.objid, key: 'metersizeid' ]); 
        } else if ( params.name == 'sectorid') {
            params.title = 'Unmapped Sectors';
            params.list = migrationSvc.getUnmappedData([ migrationid: entity.objid, key: 'sectorid' ]); 
        } else if ( params.name == 'zoneid') {
            params.title = 'Unmapped Sector Zones';
            params.list = migrationSvc.getUnmappedData([ migrationid: entity.objid, key: 'zoneid' ]); 
        } else {
            params.list = [];
        }
        
        def op = Inv.lookupOpener('waterworks_migration:unmapped:open', params);
        op.target = 'popup'; 
        return op;
    }
}