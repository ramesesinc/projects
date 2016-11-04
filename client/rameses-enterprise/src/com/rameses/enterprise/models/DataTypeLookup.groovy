package com.rameses.enterprise.models;

import com.rameses.rcp.common.*;
import com.rameses.osiris2.themes.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.control.*;
        
public class DataTypeLookup implements SimpleLookupDataSource {
            
    def selector;
    def searchText;
    def list;
    def selectedEntity;
    def onselect;

    public void setSearchText( String s ) {
        this.searchText = s;
    }

    public void setSelector(LookupSelector s) {
        this.selector = s;
    }

    def doSelect() { 
        if ( !selectedEntity ) throw new Exception("Please select an item"); 
        if ( onselect ) onselect( selectedEntity.name );
        else if ( selector ) selector.select( selectedEntity.name );
        return "_close"; 
    } 

    def doCancel() {
        return "_close";
    }

    def listHandler = [
        getColumnList: {
            return [
                [name:'name', caption:'Name']
            ];
        },
        fetchList: { o->
            list = [];
            list << [ name:'string_array' ];
            FormControlUtil.instance.controlsIndex.each {k,v->
                if( k.matches(searchText+".*")) {
                    list << [name: k];
                }
            }                
            return list;
        }
    ] as BasicListModel;
        
}