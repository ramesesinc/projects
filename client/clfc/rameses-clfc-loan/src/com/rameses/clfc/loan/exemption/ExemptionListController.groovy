package com.rameses.clfc.loan.exemption;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class ExemptionListController extends ListController 
{
    String serviceName = 'LoanExemptionService';
    String entityName = 'exemption';
    
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
