package com.rameses.clfc.customer;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CustomerGeneralInfoController 
{
    //feed by the caller 
    def callerContext = [:];
    def entity = [:];
    
    def civilstatusList = OptionTypeUtil.getCivilStatusTypes(); 
    def genderItems = OptionTypeUtil.getGenderTypes();
    
    void init() { 

    }
} 