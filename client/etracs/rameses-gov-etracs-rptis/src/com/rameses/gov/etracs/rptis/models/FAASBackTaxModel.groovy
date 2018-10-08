package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*
import java.rmi.server.UID;
import com.rameses.gov.etracs.rptis.util.*;

public class FAASBackTaxModel
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    def svc;
    
    @Service('FAASNewDiscoveryService')
    def ndSvc 
    
    def entity;
    def selectedItem;
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    def mode;
    
    String title = 'Back Taxes'
    
    void init() {
        mode = MODE_READ;
        if( ! entity.backtaxes ) 
            entity.backtaxes = []
        entity._backtaxes = []
    }
    
    void refresh(){
        binding.refresh();
    }
    
    void edit(){
        caller.addMessage([type:'backtax', msg:'Back Taxes section is still in editing mode.']);
        mode = MODE_EDIT;
    }
    
    void save(){
        svc.saveBackTaxes(entity);
        svc.savePreviousFaases(entity);
        caller.clearMessages('backtax');
        mode = MODE_READ;
    }
    
    def listHandler = [
        fetchList   : { return entity.backtaxes },
        createItem  : { return createEntity() },
        getColumns  : { return [
            new Column(name:'ry', caption:'RY', editable:true, type:'integer', format:'0000', width:50, required:true),
            new Column(name:'tdno', caption:'TD No.', editable:true, width:150, required:true),
            new Column(name:'bmv', caption:'Base Market Value', editable:true, type:'decimal', format:'#,##0.00', width:120, required:true),
            new Column(name:'mv', caption:'Market Value', editable:true, type:'decimal', format:'#,##0.00', width:120, required:true),
            new Column(name:'av', caption:'Assessed Value', editable:true, type:'decimal', format:'#,##0.00', width:120, required:true),
            new Column(name:'effectivityyear', caption:'Effectivity', editable:true, type:'integer', format:'0000', width:120, required:true),
            new Column(name:'taxable', caption:'Taxable?', editable:true, type:'boolean', width:80, required:true),
        ]},
        validate   : { li -> doValidate( li.item) },
        onAddItem  : { item -> 
            item.objid = 'BT' + new UID()
            entity.backtaxes.add( item ) 
        },
        onRemoveItem : { item -> 
            if( MsgBox.confirm('Remove item?') ) {
                ndSvc.removeBackTaxItem(item);
                entity.backtaxes.remove( item )
                entity._backtaxes.add( item )
                if (!entity.backtaxes){
                    entity.previousfaases = []
                }
                listHandler.refresh()
            }
        },
    ] as EditorListModel 
    
    def doValidate( item ) {
        RPTUtil.required( 'RY', item.ry );
        RPTUtil.required( 'Base Market Value', item.bmv);
        RPTUtil.required( 'Market Value', item.mv );
        RPTUtil.required( 'Assessed Value', item.ry );
        RPTUtil.required( 'Assessed Value', item.av );
        RPTUtil.required( 'Effectivity', item.effectivityyear );
        if( item.bmv < 0.0 ) throw new Exception('Base Market Value must be greater than or equal to zero.')
        if( item.mv < 0.0 ) throw new Exception('Market Value must be greater than or equal to zero.')
        if( item.av < 0.0 ) throw new Exception('Assessed Value must be greater than or equal to zero.')
        if( item.av > item.mv) throw new Exception('Assessed Value must be less than Market Value.')
        if( item.ry > entity.rpu.ry ) throw new Exception("RY must be less than current FAAS revision year $entity.rpu.ry")
        if( item.effectivityyear < item.ry ) throw new Exception('Effectivity must be greater than or equal to RY')
        validateRySequence( item )
        entity.backtaxyrs = svc.validateAndGetNumberBackTaxYears(entity.effectivityyear, item)
    }
    
    
    void validateRySequence( item ) {
        def list = []
        if( ! item.objid ) {
            //new item 
            if( item.ry > entity.rpu.ry ) throw new Exception('RY must be less than ' + entity.rpu.ry + '.')
            list = entity.backtaxes.findAll{ it.ry < item.ry }
        }
        else {
            def index = entity.backtaxes.findIndexOf{ it.objid == item.objid }
            if( index >= 0 ) {
                def lastindex = index - 1
                if( lastindex >= 0 ){
                    list.addAll( entity.backtaxes[0..index-1].findAll{ it.ry <= item.ry } )
                }
                lastindex = entity.backtaxes.size() - 1
                if( lastindex >= index + 1) {
                    list.addAll( entity.backtaxes[index+1..lastindex].findAll{ it.ry >= item.ry } )
                }
            }
        }
        if( list ) throw new Exception('RY must be arranged in descending order.')
    }
    
    def createEntity(){
        return [taxable:true, faasid:entity.objid, effectivityqtr:1] 
    }
 
    
       
    boolean getShowActions(){
        if (entity.state.matches('CURRENT|CANCELLED')) return false;
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.taskstate && !entity.taskstate.matches('.*appraiser.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        if (mode != MODE_READ) return false;
        return true;
    }
    
    boolean getAllowEdit(){
        return mode != MODE_READ
    }    
    
    void updateBackTaxItem(ndfaas, item){
        item.objid = ndfaas.objid;
        item.ry = ndfaas.rpu.ry;
        item.effectivityyear = ndfaas.effectivityyear;
        item.tdno = ndfaas.tdno;
        item.bmv = ndfaas.rpu.totalbmv;
        item.mv = ndfaas.rpu.totalmv;
        item.av = ndfaas.rpu.totalav;
    }
    
    def afterCreate = {
        def item = createEntity();
        updateBackTaxItem(it, item);
        
        if (!entity.backtaxes){
            updatePrevInfo(entity, it);
            svc.updateFaasPrevInfo(entity);
        }
        else {
            updatePrevInfo(entity.backtaxes.last(), it);
        }
            
        entity.backtaxes << item 
        listHandler.reload();
    }
    
    def afterUpdate = {
        updateBackTaxItem(it, selectedItem);
        if (entity.backtaxes.size() > 0 && entity.backtaxes[0].objid == selectedItem.objid){
            updatePrevInfo(entity, it)
            svc.updateFaasPrevInfo(entity);
        }
        else{
            for(int i=1; i<entity.backtaxes.size(); i++){
                if (entity.backtaxes[i].objid == it.objid){
                    def bt = entity.backtaxes[i-1]
                    updatePrevInfo(bt, it)
                    svc.updateFaasPrevInfo(bt);
                    break;
                }
            }
        }
        listHandler.reload();
        
    }
    
    void updatePrevInfo(faas, prevfaas){
        faas.prevtdno = prevfaas.tdno;
        faas.prevpin = prevfaas.fullpin;
        faas.prevowner = prevfaas.owner.name;
        faas.prevadministrator = prevfaas.administrator?.name;
        faas.prevav = RPTUtil.format('#,##0.00', prevfaas.rpu.totalav);
        faas.prevmv = RPTUtil.format('#,##0.00', prevfaas.rpu.totalmv);
        faas.prevareasqm = RPTUtil.format('#,##0.00', prevfaas.rpu.totalareasqm);
        faas.prevareaha = RPTUtil.format('#,##0.000000', prevfaas.rpu.totalareaha);
        faas.preveffectivity = prevfaas.effectivityyear;

        def pf = null;
        if (!faas.previousfaases) {
            faas.previousfaases = [];
            pf = [objid: 'PF' + new java.rmi.server.UID()];
            faas.previousfaases << pf;
        }

        pf = faas.previousfaases.first();
        pf.faasid  =  faas.objid
        pf.prevfaasid  =  prevfaas.objid
        pf.prevrpuid  =  prevfaas.rpuid 
        pf.prevtdno  =  prevfaas.tdno 
        pf.prevpin  =  prevfaas.fullpin 
        pf.prevowner  =  prevfaas.owner?.name 
        pf.prevadministrator  =  prevfaas.administrator?.name
        pf.prevav  =  prevfaas.rpu.totalav
        pf.prevmv  =  prevfaas.rpu.totalmv 
        pf.prevareasqm  =  prevfaas.rpu.totalareasqm
        pf.prevareaha  =  prevfaas.rpu.totalareaha
        pf.preveffectivity  =  prevfaas.effectivityyear
    }
    
    def addBacktaxItem(){
        def lastry = entity.rpu.ry;
        if (entity.backtaxes){
            lastry = entity.backtaxes.last().ry - 1;
        }
        
        def faas = ndSvc.createBackTaxFaas([objid:entity.objid], lastry)
        return Inv.lookupOpener('faas:capture:newdiscovery', [
            closeonsave : true, 
            afterCreate : afterCreate,
            afterUpdate : afterUpdate, 
            entity      : faas
        ])
    }
    
    def openBacktaxItem(){
        if(!selectedItem) return 
        return Inv.lookupOpener('faas:capture:newdiscovery:open', [
            closeonsave : true, 
            afterCreate : afterCreate,
            afterUpdate : afterUpdate, 
            entity      : ndSvc.openFaas(selectedItem),
        ])
    }
}
