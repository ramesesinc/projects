package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUMiscInfoAppraisalModel extends SubPageModel
{
    @Service('MiscRPUService')
    def svc;
    
    def rpuSvc;
    
    void init(){
        entity.rpu.dtappraised = entity.appraiser?.dtsigned;
        entity.rpu.classification = classifications.find{it.objid == entity.rpu.classification?.objid}
        entity.rpu.actualuse = actualUses?.find{it.objid == entity.rpu.actualuse?.objid}
    }
    
    void calculateAssessment(){
        super.calculateAssessment();
        listHandler.load();
        binding.refresh('.*');
    }
    
    
    def getClassifications(){
        return rpuSvc.getClassifications();
    }    
    
    def getActualUses(){
        return svc.getMiscAssessLevels([lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry]);
    }
    
            
    def selectedItem;
    
     @PropertyChangeListener
     def listener = [
        'entity.rpu.actualuse|entity.rpu.useswornamount|entity.rpu.swornamount':{ calculateAssessment() }
     ] 
     
     
     
     
     def getLookupMiscItemValue(){
         return InvokerUtil.lookupOpener('miscitemvalue:lookup', [
             lguid : entity.lguid,
             barangayid : entity.rp.barangayid,
             ry    : entity.rpu.ry,
             
             onselect : { miv ->
                setParamsRefId(selectedItem, miv.params)
                selectedItem.miv = miv;
                selectedItem.params = miv.params
                selectedItem.miscitem = miv.miscitem;
                selectedItem.expr = miv.expr;
                selectedItem.basemarketvalue = svc.calculateExpression(selectedItem)
             }
         ])
     }
     
     void setParamsRefId(item, params){
         params.each{param ->
            param.miscrpuitemid = item.objid;
            param.miscrpuid = entity.rpu.objid;
        }
     }
     
     
     def listHandler = [
         getRows : { return (entity.rpu.items.size() < 25 ? 25 : entity.rpu.items.size() + 1) },
             
         fetchList : { return entity.rpu.items },
             
         createItem : { return [
            miscrpuid           : entity.rpu.objid,
            depreciation        : 0.0,
            depreciatedvalue    : 0.0,
            basemarketvalue     : 0.0,
            marketvalue         : 0.0,
            assesslevel         : 0.0,
            assessedvalue       : 0.0,
         ]},
                 
         validate : { li ->
             def item = li.item 
             RPTUtil.required('Misc Item', item.miv)
             RPTUtil.required('Depreciation', item.depreciation)
             checkDuplicate(item)
             if (item.objid){
                calculateAssessment()
             }
         },
                 
         onAddItem : { item ->
            item.objid = RPTUtil.generateId('MI')
            setParamsRefId(item, item.params)
            entity.rpu.items.add(item)
            calculateAssessment()
         },
         
         onRemoveItem : { item ->
             if (MsgBox.confirm('Delete selected item')) {
                 entity.rpu.items.remove(item);
                 if (!entity.rpu._items) entity.rpu._items = [];
                 entity.rpu._items.add(item);
                 calculateAssessment()
                 return true;
             }
             return false;
         }
             
     ] as EditorListModel
     
     
     void checkDuplicate(item){
        def data = entity.rpu.items.find{ (it.objid != item.objid || item.objid == null) && it.miv.objid == item.miv.objid }
        if( data ) throw new Exception( 'Duplicate item is not allowed.' )
     }
                 
                           
                            
     def selectedParam;
     
    def paramListHandler = [
        fetchList : { 
            return selectedItem?.params?.each{
                if (it.decimalvalue != null && it.decimalvalue != 0)
                    it.value = it.decimalvalue
                else 
                    it.value = it.intvalue 
            }
        },
        
        validate : { li -> 
            def item = li.item;
            if (item.value == null)
                throw new Exception('Value is required.');
            checkRange(item);
            selectedItem.basemarketvalue = svc.calculateExpression(selectedItem)
            calculateAssessment();
        }
    ] as EditorListModel
    
    void checkRange(item){
        def value = RPTUtil.toDecimal(item.value);
       
        if (item.param.type.toLowerCase().startsWith('range')){
            if (value < item.param.minvalue || value > item.param.maxvalue){
                throw new Exception('Value must be between ' + item.param.minvalue + ' and ' + item.param.maxvalue + '.')
            }
        }
        
        item.intvalue = null;
        item.decimalvalue = null;
        
        if (item.param.type.toLowerCase().indexOf('integer') >= 0)
           item.intvalue = value;
        else
           item.decimalvalue = value;
    }
                               
    
}    
