package com.rameses.clfc.treasury.fund;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class FundRequestListController extends ListController 
{
    String serviceName = 'FundRequestService';
    String entityName = 'fundrequest';
    boolean allowCreate = false;
    
    def options = [
        [name:'PENDING', caption:'Pending'], 
        [name:'APPROVED', caption:'Approved'], 
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
