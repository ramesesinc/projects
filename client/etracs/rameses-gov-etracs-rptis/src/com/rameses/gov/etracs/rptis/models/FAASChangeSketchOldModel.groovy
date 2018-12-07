package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;

class FAASChangeSketchOldModel extends com.rameses.gov.etracs.rpt.faas.ui.FaasSketchInfoController
{
    @Caller
    def caller 
    
    @Invoker 
    def invoker
    
    @Service('FAASSupportService')
    def supportSvc;
    
    @Service('FAASChangeInfoService')
    def svc;  
    
    def changeinfo = [:]
    
    void init() {
        super.init();
        def previnfo = [:];
        previnfo.north = entity.rp.north;
        previnfo.east = entity.rp.east;
        previnfo.west = entity.rp.west;
        previnfo.south = entity.rp.south;
        
        changeinfo = [
            objid       : 'CI' + new java.rmi.server.UID(),
            refid       : entity.objid,
            faasid      : entity.objid,
            rpid        : entity.rp.objid,
            rpuid       : entity.rpu.objid,
            action      : invoker.properties.actiontype,
            newinfo     : [:],
            previnfo    : previnfo,
            redflagid   : entity._redflag?.objid,
        ];
        mode = 'edit'
    }
    
    void initChangeInfo(){
        init();
        mode = 'changeinfo'
    }
    
    def getChangeinfo() {
        updateInfo();
        return changeinfo;
    }
    
    void updateInfo() {
        changeinfo.newinfo.north = entity.rp.north;
        changeinfo.newinfo.east = entity.rp.east;
        changeinfo.newinfo.west = entity.rp.west;
        changeinfo.newinfo.south = entity.rp.south;
    }
    
    def save(){
        if (MsgBox.confirm('Save and apply changes?')){
            updateInfo();
            svc.updateInfo(changeinfo);
            caller.refreshForm();
            return '_close';
        }
        return null;
    }

    def convertToDrawing() {
        if (MsgBox.confirm('Are you sure you want to convert image sketch to drawing format?')){
            return Inv.lookupOpener('faas:changeinfo:convert', [
                entity : entity
            ])
        }
        return null;
    }

    void refreshForm() {
        caller.refreshForm();
    }

}
