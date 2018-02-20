package com.rameses.treasury.common.models;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class AttributePanelModel extends ComponentBean  {

    @Service("PersistenceService")
    def persistenceSvc;

    String lookupHandler;
    boolean editable;
   
    def selectedItem;
   
    def parent;
    String schemaname;
    
    def listHandler = [
        fetchList: { o->
            return getValue();
        }
    ] as BasicListModel;
    
    void addAttribute() {
        def h = { o->
            def itm = [:];
            if( schemaname ) {
                if(!parent) throw new Exception("Parent must not be null");
                def oitm = [_schemaname: schemaname ];
                oitm.attribute = o;
                oitm.parent = parent;
                persistenceSvc.create( oitm );
            }
            itm.attribute = o;
            getValue() << itm;
        };
        Modal.show( lookupHandler, [onselect: h ] );
    }
    
    void removeAttribute() {
        if(!selectedItem ) throw new Exception("Please select an item to remove");
        if( schemaname ) {
            selectedItem._schemaname = schemaname;
            persistenceSvc.removeEntity( selectedItem );
        }
        getValue().remove( selectedItem );
    }
    
}