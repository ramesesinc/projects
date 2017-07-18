package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPULandInfoImprovementModel extends SubPageModel
{
    def selectedPlantTree
    
    def getLookupTreeUnitValue(){
        return InvokerUtil.lookupOpener('planttreeunitvalue:lookup', [lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry, onselect:{
            selectedPlantTree.planttreeunitvalue = it;
            selectedPlantTree.planttree = it.planttree;
            selectedPlantTree.unitvalue = it.unitvalue;
        }] );
    }
    
    def getLookupPlantTreeAssessLevel(){
        return InvokerUtil.lookupOpener('planttreeassesslevel:lookup', [lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry] );
    }
    
    
    def planttreeListHandler = [
        createItem  : { 
            return createPlantTree() 
        },
            
        getColumns : { return [
            new Column(caption:'Plant/Tree*', type:'lookup', handler:lookupTreeUnitValue, expression:'#{item.planttree.name}', editable:true, width:200),
            new Column(name:'unitvalue', caption:'Unit Value', width:70, type:'decimal'),
            new Column(name:'actualuse', caption:'Actual Use*', width:90, type:'lookup', handler:'lookupPlantTreeAssessLevel', expression:'#{item.actualuse.code}', editable:true),
            new Column(name:'productive', caption:'Productive*', width:80, type:'decimal', editable:true, format:'#,##0.##'),
            new Column(name:'nonproductive', caption:'NonProductive*', type:'decimal', width:100, editable:true, format:'#,##0.##'),
            new Column(name:'nonproductiveage', caption:'Age', width:150, editable:true),
            new Column(name:'areacovered', caption:'Area Covered (ha)', width:150, type:'decimal', scale:6, editable:true, format:'#,##0.######'),
            new Column(name:'basemarketvalue', caption:'B.M.V.', type:'decimal'),
            new Column(name:'adjustment', caption:'Adjustment', type:'decimal', width:90),
            new Column(name:'marketvalue', caption:'M.V.', type:'decimal'),
            new Column(name:'assessedvalue', caption:'A.V.', type:'decimal'),
        ]},
                
        validate : { li ->
            RPTUtil.required('Plant/Tree', li.item.planttreeunitvalue );
            RPTUtil.required('Actual Use', li.item.actualuse );
            RPTUtil.required('Productive', li.item.productive);
            RPTUtil.required('Non-Productive', li.item.nonproductive);
            li.item.assesslevel = li.item.actualuse.rate;
        },
                
        onAddItem : { item -> 
            item.objid = 'TP' + new java.rmi.server.UID()
            entity.rpu.planttrees.add( item ) 
        },
                
        onRemoveItem : { item -> 
            if( MsgBox.confirm('Delete selected item?') ) {
                entity.rpu.planttrees.remove( item )
                if (!entity.rpu._planttrees) entity.rpu._planttrees = [];
                entity.rpu._planttrees.add(item)
                calculateAssessment()
            }
        },

        fetchList: { 
            return entity.rpu.planttrees 
        },
                
        onCommitItem  : {
            calculateAssessment()
        }
                
    ] as EditorListModel
    
    
    Map createPlantTree() {
        return [
            landrpuid       : entity.rpu.objid, 
            planttreeunitvalue : [:],
            planttree          : [:],
            productive      : 0.0,
            nonproductive   : 0.0, 
            unitvalue       : 0.0,
            areacovered     : 0.0,
            basemarketvalue : 0.0,
            adjustment      : 0.0,
            adjustmentrate  : 0.0,
            marketvalue     : 0.0,
            assesslevel     : 0.0,
            assessedvalue   : 0.0,
        ]   
    }
        
}    
    