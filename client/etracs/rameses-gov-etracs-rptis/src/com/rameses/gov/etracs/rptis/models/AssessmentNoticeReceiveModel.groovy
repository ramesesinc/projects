package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;


public class AssessmentNoticeReceiveModel 
{
    def entity;
    def onreceive = {};
    def deliveryTypes;


    def receive() {
        if (MsgBox.confirm('Save delivery information?')) {
            onreceive(entity);
            return '_close';
        }
        return null;
    }
}
