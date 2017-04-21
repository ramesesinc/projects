package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPULandAppraisalModel extends SubPageModel
{
    @Service('Var')
    def varSvc;
    
    def entity;
    def rpuSvc;
    
    def classification;
    def classifications;
    
    void init(){
        loadComboItems();
    }
    
    void loadComboItems(){
        classifications = rpuSvc.getClassifications();
        classification = entity.rpu.classification
    }
    
    
    
    /*---------------------------------------------------------------
    *
    * LandDetail Support
    *
    ---------------------------------------------------------------*/
    def selectedLand;
    def subclass;
    def taxableLand;
    
    def getSelectedLand(){
        return this.selectedLand;
    }
    
    void setSelectedLand(selectedLand){
        this.taxableLand = selectedLand?.taxable;
        this.selectedLand = selectedLand;
    }
    
    def getLookupSubclass(){
        return InvokerUtil.lookupOpener('lcuvsubclass:lookup', [lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry, onselect:{
            selectedLand.subclass = it;
            selectedLand.unitvalue = it.unitvalue;
            selectedLand.basevalue = it.basevalue;
            selectedLand.specificclass = it.specificclass;
            selectedLand.landspecificclass = it.landspecificclass;
            selectedLand.actualuse = null;
        }] )
    }
    
    def getLookupStripping(){
        def classification = selectedLand?.subclass?.classification;
        return InvokerUtil.lookupOpener('lcuvstripping:lookup', [lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry, classificationid:classification?.objid] )
    }
    
    def getLookupAssessLevel() {
        def classification = selectedLand?.subclass?.classification;
        return InvokerUtil.lookupOpener('landassesslevel:lookup', [lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry, classificationid:classification?.objid] )
    }
    
    def getLookupLandSpecificClass(){
        return InvokerUtil.lookupOpener('landspecificclass:lookup', [:] )
    }
    
    boolean allowColumnAVEdit() {
        if ( ! RPTUtil.toBoolean(entity.datacapture, false))
            return false;
        return varSvc.getProperty('faas_datacapture_allow_edit_av', false)
    }
    
    def getLandRpuColumns() {
        def columns = [
            new Column(caption:'SubClass*', type:'lookup', handler:'lookupSubclass',  expression:'#{item.subclass.code}', width:100, editable:true ),
            new Column(name:'landspecificclass', caption:'Specific Class', type:'lookup', handler:'lookupLandSpecificClass', expression:'#{item.landspecificclass.name}', width:100, editable:true, required:true ),
            new Column(name:'taxable', caption:'Tax?', type:'boolean', width:50, editable:true ),
            new Column(name:'actualuse', caption:'Actual Use', type:'lookup', handler:'lookupAssessLevel', expression:'#{item.actualuse.code}', width:100, editable:true, required:true ),
            new Column(name:'stripping', caption:'Strip', type:'lookup', handler:'lookupStripping', expression:'#{item.stripping.striplevel}', width:50, editable:true ),
            new Column(name:'area', caption:'Area*', type:'decimal', editable:true, width:100, typeHandler:new DecimalColumnHandler(scale:6, format:'#,##0.000000')),
            new Column(name:'unitvalue', caption:'Unit Value', type:'decimal', width:100),
            new Column(name:'basemarketvalue', caption:'B.M.V.', type:'decimal', width:100),
            new Column(name:'actualuseadjustment', caption:'Adjustment', type:'decimal', width:100)
        ]
        return columns 
    }
    
    def updateav = false 
    
    def landListHandler = [
        createItem     : { return createLandDetail() },
        getColumns     : { return getLandRpuColumns() },
                
        validate       : { li -> validateSelectedLand()},
                
        onAddItem      : { item -> 
            item.objid = 'LD' + new java.rmi.server.UID()
            entity.rpu.landdetails.add( item ) 
        },
                
        onRemoveItem   : { item -> 
            if( MsgBox.confirm( 'Delete selected item?' ) ) {
                entity.rpu.landdetails.remove( item )
                if(!entity.rpu._landdetails) 
                    entity.rpu._landdetails = [];
                entity.rpu._landdetails << item
                calculateAssessment()
            }
        },
                
        onColumnUpdate : { item, colName -> 
            updateav = false
            if ( colName == 'assessedvalue' ) {
                updateav = true 
            }
            else if (colName == 'taxable' && item.objid  && item.taxable != taxableLand){
                calculateAssessment();
                binding.focus('selectedLand');
            }    
        },
                
        fetchList : { 
            return entity.rpu.landdetails
        },
                
        onCommitItem  : {
            calculateAssessment()
        }
        
    ] as EditorListModel
    
    void validateSelectedLand(){
        RPTUtil.required( 'Subclass', selectedLand.subclass)
        RPTUtil.required( 'Actual Use', selectedLand.actualuse)
        RPTUtil.required( 'Area', selectedLand.area )
        if (selectedLand.area <= 0.0 ) throw new Exception('Area must be greater than zero.')
    }
    
/*----------------------------------------------
     * 
     * Support Methods
     *
     ---------------------------------------------- */
    
     Map createLandDetail() {
        return [
            landrpuid       : entity.rpu.objid, 
            stripping       : [:],
            striprate       : 0.0,
            areasqm         : 0.0,
            areaha          : 0.0,
            basevalue       : 0.0,
            unitvalue       : 0.0,
            taxable         : true,
            basemarketvalue : 0.0,
            adjustment      : 0.0,
            landvalueadjustment : 0.0,
            actualuseadjustment : 0.0,
            marketvalue     : 0.0,
            assesslevel     : 0.0,
            assessedvalue   : 0.0,
            landadjustments : [],
            actualuseadjustments : [],
        ]
    }
    
    
        
    def onupdateLandAdjustment = { 
        calculateAssessment();
    }
                    
    def openActualUseAdjustment() {
        return InvokerUtil.lookupOpener('landactualuseadjustment:open', [ lguid:entity.lguid, barangayid:entity.rp.barangayid, rpu:entity.rpu, landdetail:selectedLand, onupdate:onupdateLandAdjustment  ])
    }
    
    
   
}    
    