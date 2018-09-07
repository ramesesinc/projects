package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;

class RedFlagListModel extends CrudListModel {

    def confs = [
        'caseno'           : [ width:80 ],
        'businessid'       : [ visible:false ],  
        'blockaction'      : [ caption: 'Block Action', width:100 ], 
        'effectivedate'    : [ caption: 'Effectivity Date', type:'date', width:100 ], 
        'resolved'         : [ caption: 'Resolved', type:'boolean', width:60 ], 
        'resolvedby.objid' : [ visible: false ], 
        'dtfiled'          : [ type: 'date', outputFormat:'yyyy-MM-dd HH:mm:ss' ], 
        'business.businessname' : [ width: 150 ], 
        'business.owner.name'   : [ width: 150 ] 
    ]; 
    
    boolean autoResize = false;
    
    
    public void initColumn( def c ) { 
        def conf = confs.get( c.name ); 
        if ( !conf ) return; 
        
        if ( conf.type != null ) c.type = conf.type;
        if ( conf.width != null ) c.width = conf.width; 
        if ( conf.visible != null ) c.visible = conf.visible; 
        if ( conf.caption != null ) c.caption = conf.caption; 
    } 
} 