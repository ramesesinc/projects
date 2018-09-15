package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*; 
import com.rameses.util.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

class EFTPaymentModel extends CrudFormModel  { 

    
    @PropertyChangeListener
    def listener = [
        "entity.bankaccount" : { o->
            entity.fund = o.fund;
            entity.fundid = o.fund.objid;
        }
    ]
} 
