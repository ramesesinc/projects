package ccom.rameses.enterprise.treasury.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class SimpleLedgerPanelModel extends ComponentBean {
        
    
    def selectedItem;
    def handler;
    def showOption = 1;
    def showOptions = [
        [text:"Show only unpaid items", id:1],
        [text:"Show all items", id:2],
        [text:"Show only paid items", id:3],
    ];
    
    def listHandler = [
        fetchList: { o->
            return handler.fetchList(o); 
        }
    ] as BasicListModel;
    
    
    void addItem() {
        def h = { o->
            handler.addItem(o);
        }
        Modal.show("revenueitem_entry:create", [handler: h ] );
    }
    
    void removeItem() {
        if(!selectedItem) throw new Exception("selectedItem is required");
        handler.removeItem(selectedItem);
    }
}