package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;
import com.rameses.gov.etracs.rptis.models.*;


public class ChangeAppraisalInfoController extends FAASModel
{
    String title = 'Modify FAAS Appraisal';
    
    @Caller
    def caller 
    
    @Service('FAASChangeInfoService')
    def changeSvc;
    
    def changeinfo = [:]
    
    void init(){
        super.init();
        def previnfo = [isprevious:true];
        previnfo.putAll(entity.rpu);
        changeinfo = [
            objid       : 'CI' + new java.rmi.server.UID(),
            refid       : entity.objid,
            faasid      : entity.objid,
            rpid        : entity.rp.objid,
            rpuid       : entity.rpu.objid,
            action      : invoker.properties.actiontype,
            newinfo     : entity.rpu,
            previnfo    : previnfo,
            redflagid   : entity._redflag?.objid,
        ];
    }
    
    def save(){
        if (MsgBox.confirm('Save and apply changes?')){
            changeSvc.updateInfo(changeinfo);
            getEntity()._resolve = false;
            caller.refreshForm();
        }
        return '_close';
    }
}
       