package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class SubdivisionAssistModel
{
    @Caller
    def caller;

    @Binding
    def binding;
    
    @Service('SubdivisionService')
    def svc; 
    
    //subdivision 
    def entity; 
    String title = 'List of Assistants'

    def assignees;
    def items;
    def selectedAssignee;
    def selectedItem;
    def role = 'TAXMAPPER';
    def roles = ['TAXMAPPER', 'APPRAISER'];
    

    @PropertyChangeListener
    def listener = [
        'role': {
            refresh();
        }
    ]

    void init(){
        assignees = svc.getAssignees([objid: entity.objid, role: role]);
    }

    def addTaxmapper() {
        return getAssistanceInvoker('taxmapper');
    }

    def addAppraiser() {
        return getAssistanceInvoker('appraiser');
    }

    def getAssistanceInvoker(taskstate) {
        return Inv.lookupOpener('subdivision:assist:assignee', [
            subdivision: entity,
            taskstate: taskstate,
        ])
    }

    void refresh() {
        init();
        assigneeListHandler?.reload();
    }

    def getItems() {
        items = [];
        if ( selectedAssignee) {
            items = svc.getAssigneeItems(selectedAssignee)
        }
        return items;
    }

    def assigneeListHandler = [
        getRows      : { assignees.size() },
        fetchList    : { assignees },
        onRemoveItem : { 
            if (MsgBox.confirm('Delete selected assignee?')) {
                svc.removeAssignee(it);
                refresh();
            }
        }
    ] as EditorListModel;

    def itemListHandler = [
        getRows      : { items.size() },
        fetchList    : { getItems() },
        onRemoveItem : { 
            if (MsgBox.confirm('Delete selected item?')) {
                svc.removeAssistItem(it);
                refresh();
            }
        }
    ] as EditorListModel;
        
    boolean getShowActions(){
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        return true;
    }
    
}
