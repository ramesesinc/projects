package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;


public class ChangePreviousInfoController extends ChangeFaasInfoController
{
    String title = 'Modify Superseded Information';
    
    
    public def getModifiedEntity(){
        return [
            prevtdno : entity.prevtdno,
            prevpin : entity.prevpin,
            prevowner : entity.prevowner,
            prevav : entity.prevav,
            prevmv : entity.prevmv,
            prevadministrator : entity.prevadministrator,
            prevareaha : entity.prevareaha,
            prevareasqm : entity.prevareasqm,
            preveffectivity : entity.preveffectivity,
        ]
    }
    
    public void updateEntityInfo(newinfo){
        entity.prevtdno  = newinfo.prevtdno
        entity.prevpin  = newinfo.prevpin
        entity.prevowner  = newinfo.prevowner
        entity.prevav  = newinfo.prevav
        entity.prevmv  = newinfo.prevmv
        entity.prevadministrator  = newinfo.prevadministrator
        entity.prevareaha  = newinfo.prevareaha
        entity.prevareasqm  = newinfo.prevareasqm
        entity.preveffectivity  = newinfo.preveffectivity
    }
    
}
       