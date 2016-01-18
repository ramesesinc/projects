package com.rameses.gov.etracs.bpls.controller;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;

/************************************************************************
* This abstract class is extended by 
*************************************************************************/
public class LobController  {

    def lobModel = [
       createItem: { 
            throw new Exception("Create Item Not supported");
       },
       fetchList: { o->
            return entity.lobs;
        },
    ] as EditorListModel;


    def addLob() {
        return InvokerUtil.lookupOpener( "lob:lookup", [
            onselect: { o->
                if(entity.lobs.find{ it.lobid == o.objid }!=null) 
                    throw new Exception("Item already added");
                def m = [:];
                m.objid = "BIZLOB"+new UID();
                m.lobid = o.objid;
                m.name = o.name;
                m.classification = o.classification;
                m.assessmenttype = "NEW";
                entity.lobs << m; 
                lobModel.reload();
                lobUpdated = true;
                binding.focus("lob");
            }
        ]);
    }

    void retireLob() {
        if(!selectedLob) return;
        if(selectedLob.assessmenttype == 'NEW') throw new Exception("New cannot be retired");
        selectedLob.prevassessmenttype = selectedLob.assessmenttype; 
        selectedLob.assessmenttype = "RETIRE";
        lobUpdated = true;
    }

    void unretireLob() {
        if(!selectedLob) return;
        selectedLob.assessmenttype = selectedLob.prevassessmenttype;
        lobUpdated = true;
    }

     void removeLob()  {
        if( !selectedLob ) return;
        entity.lobs.remove(selectedLob);
        lobModel.reload();
        binding.focus("lob");
    }


    def reclassifyLob() {
        if(!selectedLob) return;
        return InvokerUtil.lookupOpener( "lob:lookup", [
            onselect: { o->
                if(entity.lobs.find{ it.lobid == o.objid }!=null) 
                    throw new Exception("Item already added");
                selectedLob.lobid = o.objid;
                selectedLob.name = o.name;
                selectedLob.classification = o.classification;
                lobModel.reload();
                lobUpdated = true;
                binding.focus("lob");
            }
        ]);
    }

    def verifyLOB() {
        if(!entity.lobs) 
            throw new Exception("Please specify at least one line of business");
    }
    

}