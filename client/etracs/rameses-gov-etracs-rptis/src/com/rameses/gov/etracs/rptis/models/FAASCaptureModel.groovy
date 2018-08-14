package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;

public class FAASCaptureModel extends FAASModel
{
    def STATE_INTERIM           = 'INTERIM';
    def STATE_FORAPPROVAL       = 'FORAPPROVAL';
    def STATE_CURRENT           = 'CURRENT';
    def STATE_CANCELLED         = 'CANCELLED';
    
    def afterApprove = {}
    
    
    String getTxntitle(){
        return invoker.caption + ' Transaction'
    }
    
    void submitFaasForApproval(){
        if (MsgBox.confirm('Submit FAAS for Approval?')){
            entity.putAll(service.submitForApproval( entity ));
        }
    }
    
    
    void approveFaas() {
        if (MsgBox.confirm('Approve FAAS?')){
            entity.putAll(service.approveFaas( entity ));
            afterApprove();
        }
    }


    void disapproveFaas() {
        if (MsgBox.confirm('Disapprove FAAS?')){
            entity.putAll(service.disapproveFaas( entity ));
        }
    }
            
}

