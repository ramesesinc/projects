package com.rameses.gov.treasury.certification;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;

public abstract class CertificationController 
{
    @Service('ReportParameterService')
    def paramSvc;

    @Service('DateService')
    def dtSvc;
    
    @Service('CertificationService')
    def svc;
    
    
    @Binding 
    def binding;
    
    @Invoker
    def inv;

    def MODE_CREATE = 'create';
    def MODE_READ   = 'read';
    
    def entity;
    def mode;
    
    abstract String getReportName();
    abstract String getType();
    
    
    String officeName = 'treasury';
        
    def getReportData(){
        entity.name = entity.name.replace('&', '&amp;');
        return entity;
    }
    
    SubReport[] getSubReports() {
        return null;
    }
    
    Map getParameters(){
        return [:];
    }

    
    def getTitle(){
        return inv.caption 
    }

    void afterCreate(entity){
        
    }
    
    
    def init() {
        entity = svc.init(officeName, type);
        afterCreate(entity);
        mode = MODE_CREATE;
        return 'default'
    }
    
    def open(){
        entity = svc.open(entity);
        mode = MODE_READ;
        return doPreview();
    }

    def report =[
        getReportName : { return getReportName() },
        getSubReports : { return getSubReports() },
        getReportData : { return getReportData() },
        getParameters : { paramSvc.getStandardParameter() + getParameters(); }
    ] as ReportModel;

    
    def save(){
        return svc.create( entity )
    }
    
    public def doSave(){
        def retval = null
        if (MsgBox.confirm('Save certification?')) {
            entity.putAll(save());
            mode = MODE_READ;
            retval = doPreview();
        }
        return retval;
    }
    
    
    def doPreview(){
        report.viewReport();
        return "preview";
    }

}