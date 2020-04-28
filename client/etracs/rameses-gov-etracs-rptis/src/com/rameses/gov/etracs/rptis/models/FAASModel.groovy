package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.common.*;

public class FAASModel
{
    @Binding
    def binding;
    
    @Invoker
    def invoker;
    
    @Service('FAASService')
    def service;
    
    @Service('Var')
    def var;
    
    def MODE_CREATE = 'create';
    def MODE_EDIT   = 'edit';
    def MODE_READ   = 'read';
    def mode = MODE_READ;
    
    def entity;
    def rpuopener;
    
    def taskstate;
    def assignee; 
    def assistant;
    
    //callbacks
    def afterCreate = {};
    def afterUpdate = {};
    def afterDelete = {};
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
        updateTaskInfo()
        loadRpuOpener();
        clearCacheImageFlag()
        loadExemption()
        mode = MODE_READ;
    }
    
    void open(){
        getEntity().putAll(service.openFaas(getEntity()));
        doOpen();
    }
    
    void cancel(){
        getEntity().putAll(service.openFaas(getEntity()));
        updateTaskInfo();
        mode = MODE_READ;
    }
    
    void updateTaskInfo(){
        if (taskstate) getEntity().taskstate = taskstate;
        if (assignee) getEntity().assignee = assignee;
    }

    void afterOpen(){}
    
    
    void loadRpuOpener(){
        rpuopener = InvokerUtil.lookupOpener(rpuOpenerName + ':open', [entity : getEntity(), faasmodel:this]);
    }
    
    /*-----------------------------------------------------
     * 
     * DOCUMENT SUPPORT 
     *
     *----------------------------------------------------*/
    void edit(){
        binding.focus('entity.tdno');
        mode = MODE_EDIT;
        ((SubPageModel)rpuopener.handle).modeChanged(mode);
    }
    
    void initEdit(){
        loadRpuOpener();
        mode = MODE_EDIT;
    }
    
    
    
    void cancelEdit(){
        getEntity().putAll(service.openFaas(getEntity()));
        updateTaskInfo();
        mode = MODE_READ;
        ((SubPageModel)rpuopener.handle).modeChanged(mode);
    }
    
    
    def save(){
        getEntity().assistant = assistant;
        if (mode == MODE_CREATE){
            getEntity().putAll(service.createFaas(getEntity()));
            if (afterCreate) afterCreate(getEntity());
        }
        else {
            getEntity().putAll(service.updateFaas(getEntity()));
            if (afterUpdate) afterUpdate(getEntity());
        }
        mode = MODE_READ;
        ((SubPageModel)rpuopener.handle).modeChanged(mode);
        if (closeonsave)
            return '_close';
        return null;
    }
    
    
    def delete(){
        if (MsgBox.confirm('Delete FAAS?')){
            service.deleteFaas(getEntity());
            afterDelete();
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
        getEntity()._showOldSketch = getShowOldSketch()
        getEntity()._showNewSketch= !getEntity()._showOldSketch
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
    
    def getShowOldSketch() {
        return service.isOldSketch([objid:entity.objid]) 
    }

    def getShowNewSketch() {
        return !service.isOldSketch([objid:entity.objid]) 
    }

    /*===============================================
     *
     * EDITABILITY SUPPORT
     *
     *===============================================*/
        
    boolean getAllowEdit(){
        if ( getEntity()._modify_ == true) return true;
        if ( getEntity().state == 'CURRENT' ) return false;
        if ( getEntity().state == 'CANCELLED' ) return false;
        if (getEntity().originlguid != getOrgid()) return false;
        if ( mode == MODE_READ) return false;
        return true;
    }

    public boolean getAllowEditOwner() {
        if (!getAllowEdit()) return false;
        return RPTUtil.toBoolean(getEntity().txntype.allowEditOwner, false);
    }
    
    boolean getAllowEditPinInfo(){
        if (!getAllowEdit()) return false;
        if ( mode == MODE_READ) return false;
        if( rpu.rputype != 'land' ) return false;
        return getAllowEditPin()
    }
    
    def getAllowAssignNewTdNo(){
        if (!getAllowEdit()) return false;
        if (entity.state != 'CURRENT') return false;
        if (entity.taskstate != 'record') return false;
        if (entity.assignee && entity.assignee.objid != OsirisContext.env.USERID) return false;
        return true;
    }
    
    boolean getAllowEditPrevInfo(){
        if (!getAllowEdit()) return false;
        if ( mode == MODE_READ) return false;
        if( getEntity().state == 'CURRENT' ) return false
        if( getEntity().state == 'CANCELLED' ) return false
        if( util.toBoolean(getEntity().datacapture, false) == true) return false 
        return true 
    }
    
    def getOrgid(){
        return OsirisContext.env.ORGID;
    }
    
    boolean getShowActions(){
        def taskstate = getEntity()?.taskstate
        if (getEntity().state.matches('CURRENT|CANCELLED')) return false;
        if (taskstate && taskstate.matches('assign.*')) return false;
        if ('city'.equalsIgnoreCase(OsirisContext.env.ORGCLASS)){
            if (taskstate && !taskstate.matches('receiver|appraiser.*|taxmapper.*|recommender')) 
                return false;
        }
        else {
            if (taskstate && !taskstate.matches('receiver|appraiser|provappraiser|taxmapper|provtaxmapper|recommender')) 
                return false;
        }
        if (!isAssignee()) return false;
        if (mode != MODE_READ) return false;
        return true;
    }

    def isAssignee() {
        if (OsirisContext.env.USERID == getEntity().assignee.objid || assistant) {
            return true;
        }
        return false;
    }
        
    /*===============================================
     *
     * Support Methods
     *
     *===============================================*/
     
     List getRestrictions(){
         return service.getRestrictionTypes()
     }
     
     List getQuarters() { 
         return [1,2,3,4];
     }
     
     def closeForm(){
         return '_close'
     }
     
    def _exemptions
     
    def getExemptions(){
        if (!_exemptions)
            _exemptions = service.getExemptionTypes();
        return _exemptions;
    }
    
    def getFaasType(){
        def t = '';
        if (getEntity().rpu.rputype == 'land')
            t += 'Land: ';
        else if (getEntity().rpu.rputype == 'bldg')
            t += 'Building: ';
        else if (getEntity().rpu.rputype == 'mach')
            t += 'Machine: ';
        else if (getEntity().rpu.rputype == 'planttree')
            t += 'Plant/Tree: ';
        else
            t += 'Miscellaneous: ';
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
        mode = MODE_READ;
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
        return getEntity().messagelist;
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
    
    void loadExemption(){
        entity.rpu.exemptiontype = exemptions.find{it.objid == entity.rpu.exemptiontype?.objid}
    }


    def popupActions(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = Inv.lookupOpeners( inv.properties.category, [entity: entity] ).findAll{
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean(vw, [mode:mode, entity: entity]));
        }
        list.sort{a,b -> a.caption <=> b.caption }
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }

}