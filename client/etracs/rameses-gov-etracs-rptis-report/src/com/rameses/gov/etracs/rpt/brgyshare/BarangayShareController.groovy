package com.rameses.gov.etracs.rpt.brgyshare;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;

class BarangayShareController
{
    @Binding
    def binding;
    
    @Service('ReportParameterService')
    def paramSvc;
    
    @Service('BrgyShareService')
    def svc;
    
    @Service('DateService')
    def dtSvc 
    
    def MODE_CREATE = 'create';
    def MODE_INIT   = 'init';
    def MODE_READ   = 'read';
    def MODE_PREVIEW = 'preview';
    
    def entity;
    def mode;
    
    String title = 'Barangay Share'
    
    def init(){
        def pdate = dtSvc.parseCurrentDate();
        entity = [year:pdate.year]
        mode = MODE_INIT;
        return 'init'
    }
    
    def initBrgyShare(){
        entity = svc.initBrgyShare(entity)
        mode = MODE_CREATE
        return 'default'
    }
    
    void open(){
        entity = svc.openBrgyShare(entity.objid)
        entity.mon = months.find{ it.index == entity.month}
        mode = MODE_READ;        
    }
    
    void save(){
        if (MsgBox.confirm('Save record?')){
            entity = svc.createBrgyShare(entity);
            mode = MODE_READ;
        }
    }
    
    def delete(){
        if (MsgBox.confirm('Delete record?')){
            svc.deleteBrgyShare(entity);
            return '_close';
        }
        return null;
    }
    
    void post(){
        if (MsgBox.confirm('Post record?')){
            entity = svc.postBrgyShare(entity);
            mode = MODE_READ;
        }
    }
    
    def listHandler = [
        fetchList : { return entity.items },
        getColumnList : { return svc.getColumns(entity) }
    ] as EditorListModel
    
        
                
    def reportdata 
    def preview(){
        reportdata = svc.generateBrgyShareReport(entity)
        report.viewReport();
        mode = MODE_PREVIEW;
        return 'preview'
    }
    
    def back(){
        mode = MODE_READ;
        return 'default'
    }
    
    def reportpath = 'com/rameses/gov/etracs/rpt/brgyshare/'
    def report = [
        getReportName : { return reportpath + 'brgyshare.jasper' },
        getReportData : { return reportdata.items },
        getParameters : { 
            def params = paramSvc.getStandardParameter() 
            params.PERIOD = 'For the Month of ' + getMonth(entity.month) + ', ' + entity.year;
            params.CURRENTRATE = reportdata.rate
            params.PREVIOUSRATE = reportdata.rate
            return params;
        }
    ] as ReportModel
            
            
    List getMonths(){
        return svc.getMonths();
    }
    
    List getSharetypes(){
        return ['BASIC', 'COMMON_SHARE']
    }
            
    
    def getMonth(monthid){
        return getMonths().find{it.index == monthid}.caption
    }
}
