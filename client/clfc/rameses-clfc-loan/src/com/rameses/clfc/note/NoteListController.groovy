package com.rameses.clfc.note;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class NoteListController extends ListController 
{
    String serviceName = 'NoteService';
    String entityName = 'note';
    
    def options = [
        [name:'PENDING', caption:'Pending'],
        [name:'FOR_APPROVAL', caption:'For Approval'],
        [name:'APPROVED', caption:'Approved'], 
        [name:'CLOSED', caption:'Closed'],
        [name:'REJECTED', caption:'Rejected']  
    ];  
    def selectedOption = options[0]; 
    
    def optionsModel = [
        getDefaultIcon: {
            return 'Tree.closedIcon'; 
        }, 
        fetchList: { 
            return options; 
        },
        onselect: {o-> 
            query.state = (o? o.name: null); 
            reloadAll(); 
        }
    ] as ListPaneModel; 
    
    void beforeGetColumns( Map params ) {        
        if (selectedOption) params.state = selectedOption.name; 
    }
}
