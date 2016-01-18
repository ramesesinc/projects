package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;


public class ChangeAppraisalInfoController extends com.rameses.gov.etracs.rpt.faas.ui.FaasController
{
    String title = 'Modify FAAS Appraisal';
    
    @Service('FAASChangeInfoService')
    def changeSvc;
    
    def changeinfo = [:]
    
    void init(){
        super.init();
        def previnfo = [isprevious:true];
        previnfo.putAll(entity.rpu);
        changeinfo = [
            objid       : 'CI' + new java.rmi.server.UID(),
            faasid      : entity.objid,
            rpid        : entity.rp.objid,
            rpuid       : entity.rpu.objid,
            action      : invoker.properties.actiontype,
            newinfo     : entity.rpu,
            previnfo    : previnfo,
        ];
    }
    
    void save(){
        if (MsgBox.confirm('Save and apply changes?')){
            changeSvc.updateInfo(changeinfo);
            caller.refreshForm();
            binding.fireNavigation('_close');
        }
    }
}
       