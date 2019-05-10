package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.BreakException;
import com.rameses.seti2.models.CrudListModel;

class EntityIndividualListModel extends CrudListModel {
    
    def confs = [
        'entityno'     : [ width: 120 ],
        'entityname'   : [ visible: false ], 
        'lastname'     : [ width: 130 ], 
        'firstname'    : [ width: 130 ], 
        'middlename'   : [ width: 130 ], 
        'address.text' : [ width: 300 ], 
        'birthdate'    : [ type: 'date', outputFormat:'yyyy-MM-dd', width:80 ]
    ]; 
    
    boolean autoResize = false; 
    
    public void initColumn( def c ) { 
        
        def conf = confs.get( c.name ); 
        if ( !conf ) return; 
        
        if ( conf.type != null ) c.type = conf.type;
        if ( conf.width != null ) c.width = conf.width; 
        if ( conf.minWidth != null ) c.minWidth = conf.minWidth; 
        if ( conf.maxWidth != null ) c.maxWidth = conf.maxWidth; 
        if ( conf.visible != null ) c.visible = conf.visible; 
        if ( conf.caption != null ) c.caption = conf.caption; 
    } 
    
    private def search_text_err_msg = ''' 
        The number of characters allowed for search text must be greater than 1. 

        Examples: 

        DE        (Searching lastnames that starts with DE)
        CRUZ      (Searching lastnames that starts with CRUZ)
        CRUZ%JUAN (Searching names that starts with CRUZ and followed by JUAN) 

    ''';
    
    void search() { 
        if ( searchText ) {
            def tmpstr = searchText.toString().trim().replaceAll('%',''); 
            if ( tmpstr.length() <= 1 ) {
                throw new Exception ( search_text_err_msg ); 
            }
        }
        super.search(); 
    } 
    
    boolean beforeSelectNode( node ) {
        if ( searchText ) {
            def tmpstr = searchText.toString().trim().replaceAll('%',''); 
            if ( tmpstr.length() <= 1 ) {
                MsgBox.alert( search_text_err_msg ); 
                return false; 
            }
        } 
        return super.beforeSelectNode( node ); 
    }
}