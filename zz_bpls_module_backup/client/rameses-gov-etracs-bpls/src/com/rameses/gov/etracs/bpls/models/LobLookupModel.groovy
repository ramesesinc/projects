package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class LobLookupModel extends CrudLookupModel {

    def confs = [
        'classification.objid' : [ width: 150 ],  
        'name' : [ width: 200 ],
        'psic.objid' : [ width: 25 ] 
    ]; 
    
    
    public void initColumn( def c ) { 
        def conf = confs.get( c.name ); 
        if ( !conf ) return; 
        if ( conf.type != null ) c.type = conf.type;
        if ( conf.width != null ) c.width = conf.width; 
        if ( conf.visible != null ) c.visible = conf.visible; 
        if ( conf.caption != null ) c.caption = conf.caption; 
    } 
}   
