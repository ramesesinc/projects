package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
class LandParcelMonitoringModel 
{
    @Service('TaxmappingListService')
    def svc; 
            
    @Service('LGUService')
    def lguSvc;
            
    String title = 'Land Parcels Monitoring';
            
    def lgu;
    def barangay;
            
    def rylist;
    def ry;
            
    def list;
    def selectedItem;
    def selectedFaas;
    def appEnv;
    def json_service_path;
            
    @PropertyChangeListener
    def listener = [
                '.*' : { reload() },
    ]
            
    void init(){
        rylist = svc.getRevisionYears().unique();
        appEnv = com.rameses.rcp.framework.ClientContext.currentContext.appEnv; 
        json_service_path = appEnv['app.host'] +'/'+ appEnv['app.cluster'] +'/json'
    }
            
    void reload(){
        def params = [objid:barangay.objid, ry:ry]
        list = svc.getList(params);
        listHandler?.load();
    }
            
            
    def listHandler = [
        fetchList : {return list}
    ] as BasicListModel
        
        
    def getLgus(){
        return lguSvc.getLgus();
    }
            
            
    def getBarangays(){
        if (! lgu)
        return [];
        return lguSvc.lookupBarangaysByRootId(lgu.objid);
    }
    
    def faasListHandler = [
        fetchList : {
            return svc.getFaasList(selectedItem);
        }
    ] as BasicListModel;
    
    def getSketch() {
        return 'http://' + json_service_path + '/etracs25/FAASReportService.getSketch?objid=' + selectedFaas.objid;
    }
            
}
        