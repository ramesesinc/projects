package com.rameses.gov.etracs.rpt.faas.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.util.*;
import com.rameses.common.*;

public class FaasController 
{
    @Binding
    def binding;
    
    @Invoker
    def invoker;
    
    @Service('FAASService')
    def service;
    
    @Service('RPUService')
    def rpuSvc;
    
    @Service('Var')
    def var;
    
    def MODE_CREATE = 'create';
    def MODE_EDIT   = 'edit';
    def MODE_READ   = 'read';
    
    def entity;
    def mode = 'read';
    def rpuopener;
    def callbacks;
    
    def taskstate;
    def assignee; 
    def queryinfo;
    
    //callbacks
    def afterCreate;
    def afterUpdate;
    def closeonsave = false;
    
        
    String entityName = 'faas';
    
    def getEntity(){
        return entity;
    }
    
    void recalc(){
        if (rpuopener.handle)
            rpuopener.handle.recalculate();
    }
    
    public String getRpuOpenerName(){
        return 'rpu' + getEntity().rpu.rputype
    }
    
    public String getTitle(){
        def t = getFaasType();    
        if (mode == MODE_CREATE){
            t += ' (New)';
        }
        else{
            if (getEntity().state == 'CANCELLED'){
                if (getEntity().cancelledbytdnos.indexOf('Cancellation') < 0){
                    t += '  Cancelled By TD No. ' + getEntity().cancelledbytdnos 
                }
                else{
                    t += '  Cancelled By ' + getEntity().cancelledbytdnos 
                }
                t += '  Reason: ' + getEntity().cancelreason 
                t += '  Date: ' + formatDate(getEntity().canceldate); 
            }
        }
        return t;
    }
    
    @FormTitle
    @FormId
    public String getFormId(){
        return 'FAAS : ' + (getEntity().tdno ? getEntity().tdno : getEntity().utdno);
    }
    
    def getOwnerName(){
        if (entity.taxpayer && entity.taxpayer.objid != null)
            return entity.taxpayer.entityno + ' - ' + entity.taxpayer.name 
        return '';
    }
    
    void create(){
        loadRpuOpener();
        mode = MODE_CREATE;
    }
    
    void init(){ 
        open();
        getEntity()._resolve = true;
        mode = MODE_EDIT;
    }
    void doOpen(){
        afterOpen();
        if (taskstate) getEntity().taskstate = taskstate;
        if (assignee) getEntity().assignee = assignee;
        loadRpuOpener();
        clearCacheImageFlag()
        buildQueryInfo();
        mode = MODE_READ;
    }
    void open(){
        getEntity().putAll(service.openFaas(getEntity()));
        doOpen();
    }
    
    void cancel(){
        getEntity().putAll(service.openFaas(getEntity()));
        if (taskstate) getEntity().taskstate = taskstate;
        if (assignee) getEntity().assignee = assignee;
        
        mode = MODE_READ;
    }

    void afterOpen(){}
    
    
    void loadRpuOpener(){
        if (! callbacks){
            callbacks = [
                allowEdit         : allowEditCallback,
                allowEditPinInfo  : allowEditPinInfoCallback,
                allowEditPrevInfo : allowEditPrevInfoCallback,
                allowEditOwner    : allowEditOwnerCallback,
                showActions       : showActionsCallback,
            ]
        }
        rpuopener = InvokerUtil.lookupOpener(rpuOpenerName + ':open', [
                entity    : getEntity(),
                callbacks : callbacks,
        ]);
    }
    
    /*-----------------------------------------------------
     * 
     * DOCUMENT SUPPORT 
     *
     *----------------------------------------------------*/
    void edit(){
        mode = MODE_EDIT;
    }
    
    
    void cancelEdit(){
        getEntity().putAll(service.openFaas(getEntity()));
        if (taskstate) getEntity().taskstate = taskstate;
        if (assignee) getEntity().assignee = assignee;
        mode = MODE_READ;
    }
    
    
    def save(){
        if (mode == MODE_CREATE){
            getEntity().putAll(service.createFaas(getEntity()));
            if (afterCreate) afterCreate(entity);
        }
        else {
            getEntity().putAll(service.updateFaas(getEntity()));
            if (afterUpdate) afterUpdate(entity);
        }
        mode = MODE_READ;
        if (closeonsave)
            return '_close';
        return null;
    }
    
    
    def delete(){
        if (MsgBox.confirm('Delete FAAS?')){
            service.deleteFaas(getEntity());
            return '_close';
        }
        return null;
    }

    def getLookupTaxpayer(){
        return InvokerUtil.lookupOpener('entity:lookup',[
            onselect : { 
                updateOwnershipInfo(it);
            },
            onempty  : { 
                getEntity().taxpayer = null;
                getEntity().owner    = null;
            } 
        ])
    }
    
    void updateOwnershipInfo(taxpayer){
        def address = taxpayer.address.text 
        getEntity().taxpayer = taxpayer;
        getEntity().taxpayer.address = address
        if (getEntity().owner == null || getEntity().owner.name == null || RPTUtil.toBoolean(getEntity().datacapture, true) == false){
            getEntity().owner = [name:taxpayer.name, address:address];
            if (isFormalizeOwnerName(taxpayer)){
                getEntity().owner.name = service.formalizeOwnerInfo(taxpayer.objid);
            }
        }
        if ( RPTUtil.toBoolean(getEntity().datacapture, true)){
            getEntity().prevowner = getEntity().owner.name;
        }
        binding.refresh('entity.taxpayer.*|entity.owner.*|entity.prevowner.*');
    }
    
    boolean isFormalizeOwnerName(taxpayer){
        if (taxpayer.type != 'INDIVIDUAL')
            return false;
        return RPTUtil.isTrue(var.get('faas_formalize_owner_name'));
    }
    
    def popupChangeInfo(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:getEntity()] ).findAll{
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean( vw, [entity:getEntity(), orgid:OsirisContext.env.ORGID] ));
        }
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
    
    
    /*===============================================
     *
     * EDITABILITY SUPPORT
     *
     *===============================================*/
        
    public boolean getAllowEditOwner() {
        return allowEditOwnerCallback();
    }
    
    def allowEditOwnerCallback = {
        return RPTUtil.toBoolean(getEntity().txntype.allowEditOwner, false);
    }
    
    def allowEditCallback = {
        if ( mode == MODE_READ) return false;
        if ( getEntity().state == 'CURRENT' ) return false;
        if ( getEntity().state == 'CANCELLED' ) return false;
        return true;
    }
    
    boolean getAllowEdit(){
        return allowEditCallback();
    }
    
    def allowEditPinInfoCallback = {
        if ( mode == MODE_READ) return false;
        if( rpu.rputype != 'land' ) return false;
        return getAllowEditPin()
    }
    
    boolean getAllowEditPinInfo(){
        return allowEditPinInfoCallback();
    }
    
    def allowEditPrevInfoCallback = {
        if ( mode == MODE_READ) return false;
        if( getEntity().state == 'CURRENT' ) return false
        if( getEntity().state == 'CANCELLED' ) return false
        if( util.toBoolean(getEntity().datacapture, false) == true) return false 
        return true 
    }
    
    def getAllowAssignNewTdNo(){
        if (entity.state != 'CURRENT') return false;
        if (entity.taskstate != 'record') return false;
        if (entity.assignee && entity.assignee.objid != OsirisContext.env.USERID) return false;
        return true;
    }
    
    
    boolean getAllowEditPrevInfo(){
        return getAllowEditPrevInfoCallback();
    }
    
    def getOrgid(){
        return OsirisContext.env.ORGID;
    }
    
    def showActionsCallback = {
        if (getEntity().taskstate && getEntity().taskstate.matches('assign.*')) return false;
        if (getEntity().taskstate && !getEntity().taskstate.matches('examiner|receiver|appraiser|provappraiser|taxmapper|provtaxmapper|recommender')) return false;
        if (getEntity().state.matches('CURRENT|CANCELLED')) return false;
        if (OsirisContext.env.USERID != getEntity().assignee.objid) return false;
        if (mode != MODE_READ) return false;
        return true;
    }
    
    boolean getShowActions(){
        return showActionsCallback();
    }
        
    
    
    /*===============================================
     *
     * Support Methods
     *
     *===============================================*/
     
     List getRestrictions(){
         return LOV.RPT_FAAS_RESTRICTIONS*.key
     }
     
     
     List getQuarters() { 
         return [1,2,3,4];
     }
     
     
     def closeForm(){
         return '_close'
     }
     
    def getShowAnnotation(){
        return getEntity().annotated
    }
    
    def getExemptions(){
        return rpuSvc.getExemptionTypes();
    }
    
    def getFaasType(){
        def t = '';
        if (getEntity().rpu.rputype == 'land')
            t += 'Land FAAS: ';
        else if (getEntity().rpu.rputype == 'bldg')
            t += 'Building FAAS: ';
        else if (getEntity().rpu.rputype == 'mach')
            t += 'Machine FAAS: ';
        else if (getEntity().rpu.rputype == 'planttree')
            t += 'Plant/Tree FAAS: ';
        else
            t += 'Miscellaneous FAAS: ';
        t += ' ' + getEntity().fullpin;
        return t;
    }
     
    
    
    @PropertyChangeListener
    def listener = [
        'entity.administrator.*' : {
            if ( RPTUtil.toBoolean(getEntity().datacapture, true)){
                getEntity().prevadministrator = getEntity().administrator?.name
            }
        },
    ]
    
    
    void refreshForm(){
        binding.refresh('entity.*|rpuopener');
    }
    
    def close(){
        return '_close'
    }
    
    void clearCacheImageFlag(){
        System.getProperties().remove(DBImageUtil.CACHE_IMAGE_KEY);
    }
    
    void buildQueryInfo(){
        queryinfo = null;
        def redflagCount = service.getOpenRedflagCount(getEntity().objid);
        if (redflagCount > 0){
            queryinfo = redflagCount + ' Red Flag' + (redflagCount == 1 ? ' needs' : 's need') + ' to be resolved.';
        }
    }
    
    def getMessagelist(){
        if (queryinfo)
            return [queryinfo];
        return null;
    }
    
    
    def formatDate(dt){
        if (dt == null) return '';
        if (!(dt instanceof Date)){
            try{
                dt = java.sql.Date.valueOf(dt.toString())
            }
            catch(e){
                return '';
            }
        }
        return new java.text.SimpleDateFormat('yyyy-MM-dd').format(dt);
    }

}