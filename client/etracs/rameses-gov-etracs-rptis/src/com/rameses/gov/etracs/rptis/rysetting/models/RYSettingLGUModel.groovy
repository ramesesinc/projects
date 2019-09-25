package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class RYSettingLGUModel implements SubPage
{
    @Binding
    def binding 
    
    @Caller
    def caller;
    
    @Service('RYSettingLGUService')
    def svc;
    
    def entity;
    def mode = 'read';
    
    def lgus;
    def selectedLgu;
    
    
    void init(){
        lgus = svc.getLgus(entity);
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    /*---------------------------------------------------------------------
    *
    * LGU Support 
    *
    ----------------------------------------------------------------------*/
            
    def onselectLgu = {data ->
        def brgy;
        if (data.barangay){
            brgy = lgus.find{it.barangayid == data.barangay.objid}
            if (brgy) throw new Exception('Barangay ' + data.barangay.name + ' has already been added.')
        }
        else if( lgus.find{ it.lguid == data.lgu.objid } != null ) {
            throw new Exception( data.lgu.name + ' has already been added.')
        }
        
        def lgu = [:]
        lgu.objid       = 'RYL' + new java.rmi.server.UID();
        lgu.rysettingid = entity.objid;
        lgu.lguid         = data.lgu.objid;
        lgu.lgu         = data.lgu;
        lgu.barangayid    = data.barangay?.objid;
        lgu.barangay    = data.barangay;
        lgu.lguname     = (data.barangay ? data.barangay.name : data.lgu.name);
        lgu.settingtype = entity.type;
        lgu.putAll(svc.createLgu(lgu));
        if (!lgus) lgus = [];
        lgus << lgu;
        buildAppliedTo();
        lguListHandler.load()
    }
    
    def addLgu() {
        return InvokerUtil.lookupOpener('rysettinglgu:lookup', [
            onselect    : onselectLgu,
        ])
    }
    
    def lguListHandler = [
        getColumns : { return [
            new Column(name:'lguname', caption:'Name'),
        ]},
        fetchList : { return lgus },
        onRemoveItem : { item ->
            if( MsgBox.confirm('Delete selected LGU?')) {
                svc.removeLgu(item);
                lgus.remove( item );
                buildAppliedTo();
                return true;
            }
            return false;
        },
        
    ] as EditorListModel
                

    
    void buildAppliedTo(){
        entity.appliedto = lgus?.lguname?.sort().join(', ')
        caller?.binding?.refresh();
    }
}