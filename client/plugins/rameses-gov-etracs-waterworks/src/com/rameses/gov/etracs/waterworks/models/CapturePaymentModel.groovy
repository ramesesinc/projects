package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

class CapturePaymentModel extends PageFlowController  {

    @Service("PaymentService")
    def paymentSvc;
    
    void init() {
        
    }
    
    def consumptionList = [
        fetchList: {
            
        }
    ] as BasicListModel;
    
    def otherFeeList = [
        fetchList: {
            
        }
    ] as BasicListModel;

}