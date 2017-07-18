import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;

public class RealPropertyController
{
    @Binding
    def binding;
          
    @Service('RealPropertyService')
    def svc;

    @Service('Var')
    def varSvc
    
    String getTitle(){
        return 'Real Property'
    }
    
    def entity;
    
    def mode;
    def MODE_READ = 'read';
    def MODE_CREATE = 'create';
    def MODE_EDIT  = 'edit'
    
    boolean allowCreate = true;
    boolean allowDelete = true;
    boolean allowApprove = true;
    boolean allowEdit = true;
    boolean allowEditPinInfo = false;
    boolean allowEditSectionParcel = false;
    
    boolean autoClose = false;
    boolean showClose = true;
    boolean viewOnly = false;
    
    
    def oncreate; //handler 
    def onupdate; //handler 
    def onedit;   //handler
    
    def ryList;
    
    public boolean getAllowEdit(){
        if ( entity.state.toUpperCase().matches('CURRENT|CANCELLED'))
            return false;
        if (mode == MODE_READ)
            return false;
        return allowEdit;
    }
    

    public boolean getShowEditAction(){
        if ( entity.state.toUpperCase().matches('CURRENT|CANCELLED'))
            return false;
        if (mode != MODE_READ)
            return false;
        return allowEdit || allowEditPinInfo;
    }
    
    public boolean getShowDeleteAction(){
        if ( entity.state.toUpperCase().matches('CURRENT|CANCELLED'))
            return false;
        if ( ! entity.state.toUpperCase().matches('FORTAXMAPPING'))
            return false;
        if (mode != MODE_READ)
            return false;
        return allowDelete;
    }
    
        
    public boolean getShowApproveAction(){
        if ( entity.state.toUpperCase().matches('CURRENT|CANCELLED'))
            return false;
        if ( ! entity.state.toUpperCase().matches('FORTAXMAPPING'))
            return false;
        if (mode != MODE_READ)
            return false;
        return allowApprove;
    }
    
        
    @PropertyChangeListener
    def listener = [
        'entity.pintype|entity.isection|entity.iparcel' :{ buildPin() },
    ]
    
                
    void init(){
        ryList = svc.getRyList();
        
        if (!entity){
            entity = svc.init();
            entity.isection = null;
            entity.iparcel = null;
            allowEditPinInfo = true;
        }
        
        if (allowEdit || entity.isection == null || entity.iparcel == null)
            allowEditSectionParcel = true;
        
        entity.ry = ryList.find{it == entity.ry}
        mode = MODE_CREATE;
    }
        
    void open(){
        ryList = svc.getRyList().unique();
        entity.putAll(svc.open(entity));
        
        if (allowEdit || entity.isection == null || entity.iparcel == null)
            allowEditSectionParcel = true;
        
        mode = MODE_READ;

    }
    
    
    def create(){
        updateLguInfo()
        svc.checkDuplicatePin(entity);
        entity = svc.create(entity);
        mode = MODE_READ;
        entity.isnew = false;
        allowEdit = true;
        if (oncreate) oncreate(entity);
    }
    
    def update(){
        updateLguInfo()
        svc.checkDuplicatePin(entity);
        entity = svc.update(entity);
        mode = MODE_READ;
        entity.isnew = false;
        if (onupdate) onupdate(entity);
        if (autoClose)
            return close();
    }
    
    def oldEntity;
    void edit(){
        oldEntity = MapBeanUtils.copy(entity);
        mode = MODE_EDIT;
        if (onedit) onedit(entity);
    }
    
    def cancelEdit(){
        if (MsgBox.confirm('Discard any changes?')){
            entity.putAll(oldEntity);
            oldEntity = null;
            mode = MODE_READ;
        }
        if (onupdate) onupdate(entity);
        if (autoClose)
            return close();
    }
    
    def cancelCreate(){
       if (MsgBox.confirm('Cancel new record?')) {
           return close();
       }
       if (autoClose)
           return close();
       return null;
    }
    
    def delete(){
        if (MsgBox.confirm('Delete record?')){
            svc.removeEntity(entity);
            return close();
        }
    }
                
    void submit(){
        if (MsgBox.confirm('Submit for approval?')) {
            entity = svc.submitForApproval(entity);
        }
    }
        
    void approve(){
        if (MsgBox.confirm('Approve?')){
            entity = svc.approve(entity);
        }
    }
    
    
    @Close
    def close(){
        return '_close';
    }
    
                
    void buildPin(){       
        RPTUtil.buildPin(entity, varSvc);
        binding?.refresh('entity.pin');
    }
    
    
    def getLookupBarangay(){
        return InvokerUtil.lookupOpener('barangay:lookup', [
                onselect : {
                    entity.barangay = it;
                    updateLguInfo()
                    buildPin();
                },
                
                onempty : {
                    entity.barangay = null;
                }
        
                
        ])
    }
    
    
    void updateLguInfo(){
        if ('DISTRICT'.equalsIgnoreCase(entity.barangay?.parent?.orgclass)){
            entity.lguid = entity.barangay.provcity.objid
            entity.lgutype = 'city'
        }
        else {
            entity.lguid = entity.barangay?.munidistrict.objid
            entity.lgutype = 'municipality'
        }
    }
    
    
    def getPinTypes(){
        return ['new','old'];
    }
            
}
