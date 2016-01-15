package com.rameses.clfc.treasury.checkpayment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class CheckPaymentListController extends ListController 
{
    @Service('CheckPaymentService')
    def svc;
    
    String serviceName = 'CheckPaymentService';
    String entityName = 'checkpayment';
    boolean allowCreate = false;
    
    /*
    def options = [
        [name:'FOR_CLEARING', caption:'For Clearing'],
        [name:'CLEARED', caption:'Cleared'],
        [name:'REJECTED', caption:'Rejected']  
    ];  
    def selectedOption = options[0]; 
    */
    def selectedOption;
    def optionsModel = [
        getDefaultIcon: {
            return 'Tree.closedIcon'; 
        }, 
        fetchList: { 
            return svc.getStates();
            //return options; 
        },
        onselect: {o-> 
            //query.state = (o? o.name: null); 
            query.state = o?.state;
            reloadAll(); 
        }
    ] as ListPaneModel; 
    
    void beforeGetColumns( Map params ) {        
        //if (selectedOption) params.state = selectedOption.name; 
        params.state = selectedOption?.state;
    }
}
