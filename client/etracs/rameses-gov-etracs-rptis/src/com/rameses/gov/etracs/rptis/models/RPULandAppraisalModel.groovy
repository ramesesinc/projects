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
    
    @Service('LandRYSettingLookupService')
    def settingSvc
    
    def entity;
    def rpuSvc;
    
    def classifications;
    def loaded = false;
    
    void init(){
        loadComboItems();
        loaded = true;
    }
    
    void loadComboItems(){
        classifications = rpuSvc.getClassifications();
        entity.rpu.classification = classifications.find{it.objid == entity.rpu.classification?.objid}
    }
    
    @PropertyChangeListener()
    def listener = [
        'entity.rpu.distanceawr' : {
            def sdawr = entity.rpu.landadjustments.find{it.adjustmenttype.code == 'SDAWR'}
            updateDistance(sdawr, entity.rpu.distanceawr);
            if (loaded)
                calculateAssessment()
        },
        'entity.rpu.distanceltc' : {
            def sdltc = entity.rpu.landadjustments.find{it.adjustmenttype.code == 'SDLTC'}
            updateDistance(sdltc, entity.rpu.distanceawr);
            if (loaded)
                calculateAssessment()
        },
        'entity.rpu.classification' : {
            calculateAssessment()
        }
    ]
    
        
    void calculateAssessment(){
        loadStandardAgriAdjustments()
        super.calculateAssessment();
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
    
    void loadStandardAgriAdjustments(){
        def autoadj = varSvc.get('faas_land_auto_agricultural_adjustment');
        if (!autoadj || autoadj.matches('0|n|no|f|false')) {
            return ;
        }
            
        if (! entity.rpu.landadjustments)
                entity.rpu.landadjustments = []
                
        if ('AGRICULTURAL'.equalsIgnoreCase(entity.rpu.classification?.name)){
            def params = [:]
            params.ry = entity.rpu.ry
            params.lguid = entity.lguid 
            params.barangayid = entity.rp.barangayid
            params.classificationid = entity.rpu.classification.objid 
            def adjustments = settingSvc.lookupAdjustmentTypes(params)
            
            def sdawr = entity.rpu.landadjustments.find{it.adjustmenttype.code == 'SDAWR'}
            if (!sdawr){
                def awr = adjustments.find{it.code == 'SDAWR'}
                if (awr)
                    entity.rpu.landadjustments << buildAdjustment(awr, entity.rpu.distanceawr)
            }
            else{
                updateDistance(sdawr, entity.rpu.distanceawr);
            }
            
            def sdltc = entity.rpu.landadjustments.find{it.adjustmenttype.code == 'SDLTC'}
            if (!sdltc){
                def dlt = adjustments.find{it.code == 'SDLTC'}
                if (dlt)
                    entity.rpu.landadjustments << buildAdjustment(dlt, entity.rpu.distanceltc)
            }
            else{
                updateDistance(sdltc, entity.rpu.distanceltc);
            }
        }
        else {
            if (!entity.rpu._landadjustments ) entity.rpu._landadjustments = []
            entity.rpu._landadjustments = entity.rpu.landadjustments.collect{[objid:it.objid]}
            entity.rpu.landadjustments.clear();
        }
    }
    
    void updateDistance(adj, distance){
        def p = adj.params.find{it.param.objid == 'DISTANCE_KM'}
        if (p) p.value = distance
    }
    
    def buildAdjustment(adjtype, distance){
        def adj = [:]
        adj.objid = 'LA' + new java.rmi.server.UID()
        adj.landrpuid = entity.objid 
        adj.landdetailid = null 
        adj.adjustmenttype = adjtype
        adj.expr = adjtype.expr
        adj.adjustment = 0.0
        adj.basemarketvalue = 0.0 
        adj.marketvalue= 0.0 
        adj.type = 'LV'
        
        adj.params = []
        adj.params << [
            objid : 'LAP' +  new java.rmi.server.UID(),
            landadjustmentid : adj.objid,
            landrpuid : entity.objid, 
            param : [objid:'DISTANCE_KM', name:'DISTANCE_KM', paramtype:'decimal'],
            value : distance
        ]
        return adj
    }
   
}    
    