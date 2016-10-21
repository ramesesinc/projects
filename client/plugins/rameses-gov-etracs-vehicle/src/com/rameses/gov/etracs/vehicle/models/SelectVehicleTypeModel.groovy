package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;


public class SelectVehicleTypeModel {
    
    def clientContext = com.rameses.rcp.framework.ClientContext.currentContext;
    def session = OsirisContext.getSession();
    
    def folderid = "vehicle";
    def txntype;
    def menuList;
    def homeicon;
    
    @FormTitle
    def formTitle;
    
    @FormTitle
    def formId;

    void buildIcon() {
        def appEnv = clientContext.appEnv; 
        def customfolder = appEnv['app.custom']; 
        homeicon = 'images/' + customfolder + '/home-icon.png';  
        def custom_homeicon = clientContext.getResource(homeicon); 
        if (!custom_homeicon) homeicon = 'home/icons/folder.png';   
    }
    
    void start() {
        buildIcon();
        formId = "vehicle";
        formTitle = "Select Vehicle";
        menuList = [
            [caption: 'MTOP', icon:homeicon, type:'mtop', title: 'MTOP' ],
            [caption: 'Fishing Boat', icon:homeicon, type:'fb', title: 'Boat'],
            [caption: 'Pedicab', icon:homeicon, type:'pedicab', title: 'Pedicab']
        ];
    }
    
    void init() {
        buildIcon();
        MsgBox.alert('Enter TXNTYPE ' +txntype + " folderid " + folderid);
        formTitle = txntype.title;
        menuList = Inv.lookupOpeners( folderid, [txntype: txntype] );
    }
    
    def listModel = [
        fetchList: { o->
            return menuList; 
        },
        onOpenItem: {o-> 
            def param = [:]
            param.folderid = folderid + "/" + o.type;
            param.txntype = o;
            def opener = Inv.lookupOpener("vehicle/menu", param );  
            if (opener) {
                return opener; 
            } else {
                throw new Exception('No available handler found');
            } 
        }
    ] as TileViewModel;
    
}