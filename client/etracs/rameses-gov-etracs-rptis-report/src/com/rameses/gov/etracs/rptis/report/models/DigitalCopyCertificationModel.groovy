package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class DigitalCopyCertificationModel extends com.rameses.gov.etracs.rpt.report.certification.AbstractCertificationController
{
    @Service('RPTCertificationDigitalCopyService')
    def svc;

    @Service('Var')
    def var;    
    
    String reportPath = 'com/rameses/gov/etracs/rptis/reports/';
    def opener;

    
    public def getService(){
        return svc;
    }
    
    String getReportName(){
        return reportPath + 'certification_digital_copy.jasper';
    }

    SubReport[] getSubReports() {
        return null;
    }
    
    
    Map getParameters(){
        return [
            REPORTTITLE:'Certified Digital Copy', 
            PAGECOUNT: entity.properties.size(),
        ]
    }
    
    def getCertificationTypes(){
        return [
            [type:'byfaas', caption:'By FAAS'],
        ]
    }
    
    def next(){
    	opener = Inv.lookupOpener('attachment:view', [folderName: entity.tdno, isMultiSelect: true]);
        mode = MODE_SELECT;
        return 'selectpage';
    }

    void beforeSave(){
        def selectedItems = opener.handle.attachmentHandler.selectedItem;
        if (!selectedItems) {
            throw new Exception('At least one item should be selected.');
        }

        def folderName = opener.handle.getFolderName();
        entity.properties = [];
        selectedItems.each{ item ->
            entity.properties <<  [file:  folderName + '/' + item.objid]
        }
    }

    def getReportData(){
        def list = [];
        entity.properties.each {
            it.putAll(entity);
            it.filename = getFileServerPath() + it.file
            list << it;
        }

        return list;
    }

    def getFileServerPath() {
        def varname = 'file_server_path';
        def serverPath = var.get(varname);
        if (!serverPath) {
            throw new Exception('System variable ' + varname + ' is not defined.')
        }
        if (!serverPath.endsWith('/')) {
            serverPath += '/'
        }
        return 'file:///' + serverPath;
    }
    
}

