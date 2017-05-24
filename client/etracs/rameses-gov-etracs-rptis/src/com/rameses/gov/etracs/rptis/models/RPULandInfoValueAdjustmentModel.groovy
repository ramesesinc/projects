package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPULandInfoValueAdjustmentModel extends SubPageModel
{
    def selectedItem 
    
    void init(){
        calculateAssessment();
    }
    
    void calculateAssessment() {
        rpumodel.calculateAssessment();
        listHandler.load();
        paramsListHandler.load();
    }
    
    def onupdateHandler = { la ->
        calculateAssessment()
    }
    
    def getLookupAdjustment() {
        return InvokerUtil.lookupOpener('landadjustmenttype:lookup', [lguid:entity.lguid, classificationid:'%', ry:entity.rpu.ry, barangayid:entity.rp.barangayid] )
    }
    
    def listHandler = [
            
        createItem : { return [adjustment:0.0, type:'LV'] },
            
        getColumns : { return [
            new Column(name:'adjustmenttype', caption:'Adjustment', type:'lookup', handler:'lookupAdjustment',  expression:'#{item.adjustmenttype.name}', width:350, editable:true, editableWhen:'#{item.objid==null}'),
            new Column(name:'adjustment', caption:'Amount', type:'decimal', width:120, format:'#,##0.00' ),
        ]},
                
        validate  : { li -> 
            RPTUtil.required( 'Code', li.item.adjustmenttype)
            checkDuplicate( li.item )
            if ( li.item.objid ){
                calculateAssessment()
            }
        },
                
        onAddItem : { item -> 
            item.objid = 'LDA' + new java.rmi.server.UID()
            item.expr = item.adjustmenttype.expr;
            item.type = 'LV'
            item.adjustmenttype.params.each{
                it.objid = 'ADJ' + new java.rmi.server.UID();
                it.landadjustmentid = item.objid;
            }
            item.params = item.adjustmenttype.params;
            entity.rpu.landadjustments.add( item ) 
            calculateAssessment();
        },
                
        onRemoveItem  : { item -> 
            if( MsgBox.confirm( 'Delete selected item?' ) ) {
                entity.rpu.landadjustments.remove( item )
                if (!entity.rpu._landadjustments) 
                    entity.rpu._landadjustments = []
                entity.rpu._landadjustments << [objid:item.objid]
                entity.rpu.landdetails.each{
                    def lda = it.landadjustments.find{ it.adjustmenttype.objid == item.adjustmenttype.objid }
                    if (lda){
                        it.landadjustments.remove(lda)
                        if (!it._landadjustments) it._landadjustments = [];
                        it._landadjustments << [objid:lda.objid]
                    }
                }
                calculateAssessment()
            }
        },
                
        fetchList : { return entity.rpu.landadjustments  },
                
    ] as EditorListModel
    
    
    void checkDuplicate( item ) {
        def data = entity.rpu.landadjustments.find{ it.adjustmenttype.objid == item.adjustmenttype.objid }
        if( data ) throw new Exception('Duplicate Adjustment is not allowed.')    
    }
    
    
    
    
    
    def selectedParam;
    
    def paramsListHandler = [
        fetchList : { return selectedItem?.params },
        
        validate  : { li -> 
            def item = li.item;
            if (item.value == null)
                throw new Exception('Value is required.');
            checkRange(item);
            updateDistance(item);
            calculateAssessment();
        }
    ] as EditorListModel    
    

    void checkRange(item){
        def value = null;
        if (item.param.paramtype.toLowerCase().indexOf('integer') >= 0) 
            value = RPTUtil.toInteger(item.value)
        else
            value = RPTUtil.toDecimal(item.value)
            
        if (item.param.paramtype.toLowerCase().startsWith('range')){
            if (value < item.param.minvalue || value > item.param.maxvalue){
                throw new Exception('Value must be between ' + item.param.minvalue + ' and ' + item.param.maxvalue + '.')
            }
        }
    }    
    
    void updateDistance(param){
        if(selectedItem.adjustmenttype.code == 'SDAWR')
            entity.rpu.distanceawr = param.value
        else if(selectedItem.adjustmenttype.code == 'SDLTC')
            entity.rpu.distanceltc = param.value
    }
    
    
    def getTotalAdjustment(){
        if (entity.rpu.landadjustments)
            return entity.rpu.landadjustments.adjustment.sum();
        return 0.0;
    }
    
   
}    
    