package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityProfileListModel extends CrudListModel {

    def open() {
        if( selectedNode.name == "online") {
            def p = MsgBox.confirm("Accept this record? ");
            if(!p) return null;
        }
        return super.open();
    }
    
}
