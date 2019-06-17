package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.BreakException;
import com.rameses.seti2.models.CrudListModel;

class EntityJuridicalListModel extends CrudListModel {
    
    def confs = [
        'entityno'     : [ width: 120 ],
        'entityname'   : [ visible: false ], 
        'name'         : [ width: 400 ], 
        'address.text' : [ width: 400 ], 
        'orgtype'      : [ width: 100 ]
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
        The number of characters allowed for search text must be greater than 2. 

        Examples: 

        AGR       (Searching names that starts with AGR)
        AGR%CORP  (Searching names that starts with AGR and followed by CORP) 

    ''';
    
    void search() { 
        if ( searchText ) {
            def tmpstr = searchText.toString().trim().replaceAll('%',''); 
            if ( tmpstr.length() <= 2 ) {
                throw new Exception ( search_text_err_msg ); 
            }
        }
        super.search(); 
    } 
    
    boolean beforeSelectNode( node ) {
        if ( searchText ) {
            def tmpstr = searchText.toString().trim().replaceAll('%',''); 
            if ( tmpstr.length() <= 2 ) {
                MsgBox.alert( search_text_err_msg ); 
                return false; 
            }
        } 
        return super.beforeSelectNode( node ); 
    }
}