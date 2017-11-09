package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUBldgInfoAppraisalModel extends SubPageModel
{
    def rpuSvc;
    

    void calculateAssessment(){
        super.calculateAssessment();
        floor = getFloors()?.find{it.objid == floor?.objid}
        structuralTypeListHandler.load()
        bldgUseListHandler.load()
        floorHandler.load();
        additionalItemHandler.load();
        paramListHandler.load();
        binding.refresh('.*');
    }
    

    /*---------------------------------------------------------
     *
     * BLDG TYPE SUPPORT
     *
     ---------------------------------------------------------*/
    def selectedStructuralType

    def getLookupBldgType(){
        return InvokerUtil.lookupOpener('bldgtype:lookup',[lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry])  
    }
    
    def getLookupBldgKindBucc(){
        return InvokerUtil.lookupOpener('bldgkindbucc:lookup',[bldgtype:selectedStructuralType?.bldgtype])  
    }

    def structuralTypeListHandler = [
        createItem : { return [
            bldgrpuid   : entity.rpu.objid,
            bldguses    : [],
        ]},
            
        fetchList  : { return entity.rpu?.structuraltypes; },
        
        validate   : { li ->
            def stt = li.item;
            RPTUtil.required('Building Type', stt.bldgtype)
            RPTUtil.required('Building Kind', stt.bldgkindbucc)
            validateAndSetBaseValue(stt)
        },
        
        onColumnUpdate : {item, colname -> 
            if (colname == 'bldgtype' ){
                item.bldgkindbucc = null;
                item.basevalue = 0.0;
                item.unitvalue = 0.0;
                validateAndSetBaseValue(item);
            }
            if (colname == 'bldgkindbucc' ){
                validateAndSetBaseValue(item);
            }
        },
                
        onCommitItem : { item ->
            if (item.objid) calculateAssessment()
        },
                
        onAddItem  : { item -> 
            item.objid = RPTUtil.generateId('STT')
            entity.rpu.structuraltypes.add( item ) 
            calculateAssessment()
        },
                
        onRemoveItem : { item ->
            if (MsgBox.confirm('Delete selected item?')) {
                if (! entity.rpu._structuraltypes) entity.rpu._structuraltypes = [];
                def stt = entity.rpu.structuraltypes.find{it.objid == item.objid}
                if (stt){
                    entity.rpu.structuraltypes.remove(stt);
                    entity.rpu._structuraltypes.add(stt);
                    calculateAssessment();
                }
                return true;
            }
            return false;
        },
        
    ] as EditorListModel
    
    void validateAndSetBaseValue(stt){
        if (stt.bldgtype?.basevaluetype == 'fix') {
            if (stt.bldgkindbucc){
                stt.basevalue = stt.bldgkindbucc.basevalue;
                stt.unitvalue = stt.basevalue;            
            }
        }
        else if (stt.bldgtype?.basevaluetype == 'range') {
            if (stt.bldgkindbucc){
                def minvalue = stt.bldgkindbucc.minbasevalue;
                def maxvalue = stt.bldgkindbucc.maxbasevalue;
                if (stt.basevalue == 0.0)
                    stt.basevalue = maxvalue;
                if (stt.basevalue < minvalue || stt.basevalue > maxvalue) {
                    def msg = "Base Value must be between $minvalue and $maxvalue."
                    stt.basevalue = maxvalue;
                    stt.unitvalue = stt.basevalue;            
                    throw new Exception(msg);
                }
            }
        }
        stt.bldguses.each{ bu ->
            bu.basevalue = stt.basevalue;
            bu.floors.each{ bf ->
                bf.basevalue = stt.basevalue;
                bf.unitvalue = stt.basevalue;
            }
        }
        
    }
    
    
    /*---------------------------------------------------------
     *
     * BLDG USE SUPPORT
     *
     ---------------------------------------------------------*/
    def selectedBldgUse;
    
    
    def getLookupBldgActualUse(){
        return InvokerUtil.lookupOpener('bldgactualuse:lookup', [lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry])
    }
    
    
    def bldgUseListHandler = [
        createItem : { return [
            bldgrpuid           : entity.rpu.objid,
            structuraltype      : [objid:selectedStructuralType?.objid],
            basevalue           : selectedStructuralType.basevalue,
            area                : 0.00,
            basemarketvalue     : 0.00,
            depreciation    : 0.00,
            depreciationvalue   : 0.00,
            adjustment      : 0.00,
            marketvalue     : 0.00,
            assesslevel     : 0.00,
            assessedvalue   : 0.00,
            taxable         : true,
            floors              : [],
        ]},
            
        fetchList  : { return selectedStructuralType?.bldguses; },
        
        validate   : { li ->
            def bu = li.item;
            RPTUtil.required('Actual Use', bu.actualuse);
            checkDuplicateActualUse(bu);
        },
                
        onAddItem  : { item -> 
            item.objid = RPTUtil.generateId('BU')
            selectedStructuralType.bldguses.add( item ) 
            binding.refresh('floorHandler ')
        },
                
        onCommitItem : {item -> 
            calculateAssessment();
        },
                
        onRemoveItem : { item ->
            if (MsgBox.confirm('Delete selected item?')) {
                if (! entity.rpu._bldguses) entity.rpu._bldguses = [];
                selectedStructuralType.bldguses.remove(item);
                entity.rpu._bldguses.add(item);
                calculateAssessment();
                return true;
            }
            return false;
        },
        
    ] as EditorListModel
                
                
    void checkDuplicateActualUse(item){
        def dup = selectedStructuralType.bldguses.find{it.actualuse.objid == item.actualuse.objid && it.objid != selectedBldgUse.objid}
        if (dup)
            throw new Exception('Duplicate actual use is not allowed.');
    }
    
    def assessmentListHandler = [
        fetchList : { return entity.rpu?.assessments }
    ] as BasicListModel
    

    void updateFloorAreas(){
        entity.rpu.structuraltypes.each{
            it.totalfloorarea = 0.0;
            if (it.bldguses)
                it.totalfloorarea = it.bldguses.area.sum();
            if (it.totalfloorarea == null )
                it.totalfloorarea = 0.0;
        }
        binding?.refresh("selectedStructuralType");
    }
                
     
    
    /*---------------------------------------------------------
     *
     * BLDG FLOORS
     *
     ---------------------------------------------------------*/   
    
    def selectedFloor
    
    def floorHandler = [
        createItem : { return [
            objid           : 'BF' + new java.rmi.server.UID(),
            bldguseid       : selectedBldgUse.objid,
            bldgrpuid       : entity.rpu.objid, 
            area            : 0.0,
            storeyrate      : 0.0,
            basevalue       : selectedBldgUse.basevalue,
            unitvalue       : 0.0,
            basemarketvalue : 0.0,
            adjustment      : 0.0,
            marketvalue     : 0.0,
            additionals     : [],
        ] },
            
        onAddItem : { floor -> 
            selectedBldgUse.floors.add(floor) 
        },
                
        validate : { li -> 
        },
                
        onCommitItem : {item -> 
            updateBldgUseInfo(); 
            updateFloorCount();
            calculateAssessment();
            binding.refresh("floor");
        },
                
        onRemoveItem : { floor -> 
            if (MsgBox.confirm('Delete selected floor?')){
                selectedBldgUse.floors.remove(floor)
                if (!entity.rpu._floors) 
                    entity.rpu._floors = []
                entity.rpu._floors.add(floor)
                updateFloorCount();
                calculateAssessment();
                return true;
            }
            return false;
        },
                
        fetchList    : { 
            return selectedBldgUse?.floors 
        }
        
    ] as EditorListModel 
    
    
    void updateBldgUseInfo(){
        selectedBldgUse.area = selectedBldgUse.floors.area.sum();
        selectedBldgUse.adjustment = selectedBldgUse.floors.adjustment.sum()
        updateFloorAreas();
    }
    
    void updateFloorCount(){
        entity.rpu.floorcount = 1;
        entity.rpu.structuraltypes.each{st ->
            st.bldguses.each{bu ->
                bu.floors.each{f ->
                    if (f.floorno > entity.rpu.floorcount){
                        entity.rpu.floorcount = Integer.parseInt(f.floorno+'');
                    }
                }
            }
        }
    }
    
    
    
    
        
    
    
    /*---------------------------------------------------------
     *
     * ADDITIONAL ITEMS SUPPORT
     *
     ---------------------------------------------------------*/
    def floor;
    def selectedAdditionalItem;
    

    def getLookupAdditionalItem() {
        return InvokerUtil.lookupOpener('bldgadditionalitem:lookup', [ 
                lguid       : entity.lguid, 
                barangayid  : entity.rp.barangayid,
                ry          : entity.rpu.ry,
                selectedAdditionalItem : selectedAdditionalItem, 
                
                onselect : {
                    setParamsRefId(selectedAdditionalItem, it.params);
                    selectedAdditionalItem.additionalitem = it;
                    selectedAdditionalItem.params  = it.params;
                    selectedAdditionalItem.expr    = it.expr;
                    selectedAdditionalItem.basevalue = selectedStructuralType.basevalue;
                    paramListHandler.load();
                },
            ])
    }
    
    void setParamsRefId(item, params){
         params.each{param ->
            param.objid = RPTUtil.generateId('PRM');
            param.bldgflooradditionalid = item.objid;
            param.bldgrpuid = entity.rpu.objid;
        }
     }    
    
                    
    void checkDuplicate( item ) {
        def items = floor.additionalitems.find{ it.additionalitem.objid == item.additionalitem.objid && it.objid != item.objid }
        if( items) throw new Exception('Duplicate item is not allowed.')    
    }
    
    def getFloors(){
        def list = [];
        entity.rpu.structuraltypes.each{stt ->
            stt.bldguses.each{ bu ->
                bu.floors.each{bf -> 
                    bf.actualusecode = bu.actualuse.code;
                    bf.actualusename = bu.actualuse.name;
                    bf.bldgtypecode = stt.bldgtype.code
                }
                list += bu.floors;
            }
        }
        return list.sort{a, b -> 
            def texta = a.bldgtypecode + '-' + a.floorno + '-' + a.actualusecode;
            def textb = b.bldgtypecode + '-' + b.floorno + '-' + b.actualusecode;
            return texta <=> textb;
        };
    }
    
    def additionalItemHandler = [
        getRows      : { return 50 }, 
            
        fetchList    : { return floor?.additionals },
            
        createItem : { return [
            objid       : RPTUtil.generateId('BFA'),
            bldgfloorid : floor.objid,
            bldgrpuid   : floor.bldgrpuid,
            amount      : 0.0,
            depreciate  : true,
        ]},
        
        validate     : { li -> 
            def item = li.item;
            RPTUtil.required('Code', item.additionalitem)
            checkDuplicate( item )
        },
                
        onAddItem    : { item -> 
            floor.additionals.add(item)
        },
                
        onRemoveItem : { item -> 
            if (MsgBox.confirm('Delete selected item?')){
                floor.additionals.remove(item)
                if (!entity.rpu._additionals) entity.rpu._additionals = [];
                entity.rpu._additionals.add(item);
                calculateAssessment();
                return true;
            }
            return false;
        },
                                
        onCommitItem  : {  
            calculateAssessment();
        }
    ] as EditorListModel 
    
    def getTotalAdditional(){
        def total = 0.0;
        getFloors().each{
            total += (it.additionals ? it.additionals.amount.sum() : 0.0);
        }
        if(total == null)
            return 0.0;
        return total;
    }
    
    
    
    def selectedParam;
                
    def paramListHandler = [
        fetchList : { 
            return selectedAdditionalItem?.params?.each{
                it.value = (it.intvalue != null ? it.intvalue : it.decimalvalue)
            }
        },
        
        validate : { li -> 
            def item = li.item;
            if (item.value == null)
                throw new Exception('Value is required.');
            checkRange(item);
            calculateAssessment();
        }
    ] as EditorListModel
    
    void checkRange(item){
        def value = RPTUtil.toDecimal(item.value);
            
        if (item.param.paramtype.toLowerCase().startsWith('range')){
            if (value < item.param.minvalue || value > item.param.maxvalue){
                throw new Exception('Value must be between ' + item.param.minvalue + ' and ' + item.param.maxvalue + '.')
            }
        }
        
        item.intvalue = null;
        item.decimalvalue = null;
        
        if (item.param.paramtype.toLowerCase().indexOf('integer') >= 0)
           item.intvalue = value;
        else
           item.decimalvalue = value;
    }
    
                
   
}    