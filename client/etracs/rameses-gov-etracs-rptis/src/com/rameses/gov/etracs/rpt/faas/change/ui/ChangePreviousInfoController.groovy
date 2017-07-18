package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;


public class ChangePreviousInfoController extends ChangeFaasInfoController
{
    String title = 'Modify Superseded Information';
    
    @Service('FAASService')
    def faasSvc;
    
    
    public def getModifiedEntity(){
        return [previousfaases:entity.previousfaases]
    }
    
    public void updateEntityInfo(newinfo){
        entity.previousfaases = newinfo.previousfaases
    }
    
    
    
    def selectedItem;
    
    def listHandler = [
        createItem : {
            def item = [:];
            item.objid  = 'PF' + new java.rmi.server.UID();
            item.faasid = entity.objid;
            item.prevpin = entity.fullpin;
            item.prevowner = entity.owner?.name;
            item.prevadministrator = entity.administrator?.name;
            item.prevmv = 0;
            item.prevav = 0;
            item.prevareasqm = 0;
            item.prevareaha = 0;
            return item;
        },
        
        getRows   : { ( changeinfo.newinfo.previousfaases.size() <= 10 ? 10 : changeinfo.newinfo.previousfaases.size() + 1) },
        
        fetchList : { changeinfo.newinfo.previousfaases },
        
        onColumnUpdate : {item, colname -> 
            if ('prevtdno' == colname)
                buildPrevFaasInfo(item)
            else if ('prevmv' == colname)
                item.prevmv = format('#,##0.00', item.prevmv)
            else if ('prevav' == colname)
                item.prevav = format('#,##0.00', item.prevav)
            else if ('prevareasqm' == colname)
                updateAreaHa(item)
            else if ('prevareaha' == colname)
                updateAreaSqm(item)
        },
        
        onAddItem : {item -> 
            changeinfo.newinfo.previousfaases << item;
            updatePrevTdno();
        },
        
        onRemoveItem : {item ->
            if (MsgBox.confirm('Delete selected item?')){
                changeinfo.newinfo.previousfaases.remove(item);
                if (!entity._previousfaases) 
                    changeinfo.newinfo._previousfaases = [];
                changeinfo.newinfo._previousfaases << item;
                updatePrevTdno();
                return true;
            }
            return false;
        },
        
        validate : {li -> 
            def item = li.item;
            if (!item.prevtdno) throw new Exception('TD No. is required.');
            if (!item.prevpin) throw new Exception('PIN. is required.');
            if (item.prevmv == null) item.prevmv = 0.0;
            if (!item.prevav == null) item.prevav = 0.0;
            if (!item.prevareasqm == null) item.prevareasqm = 0.0;
            if (!item.prevareaha == null) item.prevareaha = 0.0;
            def dup = changeinfo.newinfo.previousfaases.find{it.prevtdno == item.prevtdno && it.objid != item.objid};
            if (dup) throw new Exception('Duplicate TD No. is not allowed.')
        },
        
    ] as EditorListModel
    
    
    void updatePrevTdno(){
        entity.prevtdno = null;
        if (changeinfo.newinfo.previousfaases){
            changeinfo.newinfo.prevtdno = changeinfo.newinfo.previousfaases.prevtdno.join(', ')
        }
    }
    
    def format(pattern, val){
        try{
            def tmp = val;
            if (tmp == null) tmp = '0.0';
            return RPTUtil.format(pattern, RPTUtil.toDecimal(tmp.toString().replace(',','')))
        }
        catch(e){
            return val;
        }
    }
    
    
    void updateAreaHa(item){
        if (item.prevareasqm == null)
            item.prevareasqm = '0.00'
        try{
            def val = RPTUtil.toDecimal(item.prevareasqm.replace(',',''));
            if (val >= 0.0)
                item.prevareaha = format('#,##0.000000', val / 10000.0);
            item.prevareasqm = format('#,##0.00', val);
        }
        catch(e){}
    }
    
    void updateAreaSqm(item){
        if (item.prevareaha == null)
            item.prevareaha = '0.00'
        try{
            def val = RPTUtil.toDecimal(item.prevareaha.replaceAll(',',''));
            if (val >= 0.0)
                item.prevareasqm = format('#,##0.00', val * 10000.0);
            item.prevareaha = format('#,##0.000000', val);
        }
        catch(e){}
    }
    
    void buildPrevFaasInfo(item){
        def faas = faasSvc.getFaasByTdNo(item.prevtdno)
        if (faas){
            item.prevfaasid = faas.objid;
            item.prevrpuid = faas.rpuid;
            item.prevpin = faas.fullpin;
            item.prevowner = faas.owner?.name;
            item.prevadministrator = faas.administrator?.name;
            item.prevav = RPTUtil.format('#,##0.00', faas.rpu.totalav);
            item.prevmv = RPTUtil.format('#,##0.00', faas.rpu.totalmv);
            item.prevareasqm = RPTUtil.format('#,##0.00', faas.rpu.totalareasqm);
            item.prevareaha = RPTUtil.format('#,##0.000000', faas.rpu.totalareaha);
            item.preveffectivity = faas.effectivityyear+'';
        }
        else{
            item.prevfaasid = null;
            item.prevrpuid = null;
            item.prevpin = entity.fullpin;
            item.prevowner = entity.owner?.name;
            item.prevadministrator = entity.administrator?.name;
            item.prevav = '0.00';
            item.prevmv = '0.00';
            item.prevareasqm = '0.00';
            item.prevareaha = '0.00';
            item.preveffectivity = null;
        }
    }
    
    
    
}
       