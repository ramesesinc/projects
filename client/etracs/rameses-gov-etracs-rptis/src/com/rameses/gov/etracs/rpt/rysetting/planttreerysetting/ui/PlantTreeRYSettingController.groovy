package com.rameses.gov.etracs.rpt.rysetting.planttreerysetting.ui;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;
import com.rameses.gov.etracs.rpt.rysetting.ui.RYSettingPageFlowController;

        

public class PlantTreeRYSettingController  extends RYSettingPageFlowController
{
    @Service('PlantTreeRYSettingService')
    def svc
    
        
    public def getService(){ return svc; }
    public String getSettingType(){ return 'planttree'; }
    public String getSettingTitle(){ return 'Plant/Tree Revision Setting'; }
    public String getPrefixId(){ return 'PTRY'; }
    String entityName = 'planttreerysetting';
    
    
    public void loadItems(){
        assessLevels = svc.getAssessLevels(entity.objid);
        plantTrees   = svc.getPlantTrees();
    }
    
    
    /*---------------------------------------------------------------------
    *
    * PlantTreeUnit Support
    *
    ---------------------------------------------------------------------*/
    def plantTrees = [] 
    def selectedPlantTree
    def selectedValue
    def unitValueTitle 
    
    void setSelectedPlantTree( selectedPlantTree ) {
        this.selectedPlantTree = selectedPlantTree
        unitValues.clear()
        unitValueTitle = 'Please select a Plant or Tree.'
        if( selectedPlantTree ) {
            unitValues = svc.getUnitValues( entity.objid, selectedPlantTree.objid )
            unitValueTitle = selectedPlantTree.name 
        }
        unitvalueListHandler.load()
    }
    
    
    def planttreeListHandler  = [
        getRows    : { return 150 },
        getColumns : { return [
            new Column( name:'code', caption:'Code', maxWidth:80 ),
            new Column( name:'name', caption:'Plant/Tree' ),
        ]},
        fetchList    : { return plantTrees },
    ] as BasicListModel
    
    
    /*---------------------------------------------------------------------
    *
    * PlantTreeUnitValue Support
    *
    ---------------------------------------------------------------------*/
    List unitValues = []
    
    def unitvalueListHandler = [
        createItem : { return createUnitValue() },
        getRows    : { return 25 },
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:60, editable:true ),
            new Column(name:'name', caption:'Name*', editable:true ),
            new Column(name:'unitvalue', caption:'Unit Value*', type:'decimal', editable:true ),
        ]},
    
        onColumnUpdate : { item, colName -> 
            if (colName=='code'){
                if (item.code && item.code.matches('.*[0-9]')){
                    def num = item.code[-1];
                    if (num == '1') item.name = '1ST CLASS'
                    else if (num == '2') item.name = '2ND CLASS'
                    else if (num == '3') item.name = '3RD CLASS'
                    else item.name = num + 'TH CLASS'
                }
            }
        },
                
        validate : { li -> 
            def uv = li.item 
            required(uv.code, 'Code')
            required(uv.name, 'Name')
            checkDuplicate( unitValues, 'Code', 'code', uv.objid, uv.code )
            checkDuplicate( unitValues, 'Name', 'name', uv.objid, uv.name )
            svc.saveUnitValue( uv )
        },
                
        onRemoveItem   : { uv ->
            if( MsgBox.confirm('Remove item?' ) ) {
                svc.deleteUnitValue( uv ) 
                unitValues.remove( uv )
                return true;
            }
            return false;
        },
                
        onAddItem      : { item -> unitValues.add( item ) },
        fetchList      : { return unitValues },
    ] as EditorListModel
    
    
    /*---------------------------------------------------------------------
    *
    * Create Records Support
    *
    ---------------------------------------------------------------------*/
    Map createEntity() {
        return [
            objid       : RPTUtil.generateId('TRY'),
            state       : 'DRAFT',
            ry          : null,
            applyagriadjustment : 0,
        ]
    }
    
    Map createAssessLevel() {
        return [ 
            objid     : RPTUtil.generateId('AL'),
            planttreerysettingid  : entity.objid,
            code      : null,
            name      : null,
            rate      : 0.0,
        ]
    }
    
    Map createUnitValue() {
        return [
            objid                   : RPTUtil.generateId('BT'),
            planttreerysettingid    : entity.objid, 
            planttree_objid         : selectedPlantTree.objid,
            code                    : null,
            name                    : null,
            unitvalue               : 0.0,
        ]
    }
    
}