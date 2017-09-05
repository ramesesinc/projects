package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class BankModel extends CrudFormModel {

    @PropertyChangeListener
    def listener = [
        'entity.depository' : {
            if (entity.depository == 1){
                entity.deposittype = 'ON-US'
            }
            else {
                entity.deposittype = null
            }
        }
    ]
    
}