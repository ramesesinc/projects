package com.rameses.gov.etracs.bpls.reports.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class ReportCategoryModel  {

    @Service("LobReportCategoryMappingService")
    def service;
    
    def groups;
    def selectedGroup;
    def selectedItem;

    String title = "Line of Business Category Mapping"
    
    @PropertyChangeListener
    def listener = [
        "selectedGroup" : { o->
            listModel.reload();
        }
    ];
    
    void init() {
        groups = service.getGroups();
        if(groups) {
            selectedGroup = groups[0];
        }
    }
    
    void addMapping() {
        def h = { o->
            def m = [lobid: selectedItem.lobid, categoryid: o.objid, groupid: selectedGroup.objid ];
            service.addMapping( m );
            listModel.reload();
        }
        Modal.show("lob_report_category:lookup", [groupid: selectedGroup.objid, onselect: h] );
        
    }
    
    void refresh() {
        listModel.reload();
    }
    
    void removeMapping() {
        if(!selectedItem) throw new Exception("Please select an item");
        service.removeMapping( [objid: selectedItem.mappingid] );
        listModel.reload();
    }
    
    def listModel = [
        fetchList: {
            service.getList( [groupid:selectedGroup.objid] );
        }
    ] as BasicListModel;

} 