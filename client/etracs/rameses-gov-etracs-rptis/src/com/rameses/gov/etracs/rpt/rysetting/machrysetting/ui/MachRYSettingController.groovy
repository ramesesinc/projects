package com.rameses.gov.etracs.rpt.rysetting.machrysetting.ui;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID;
import com.rameses.gov.etracs.rpt.rysetting.ui.RYSettingPageFlowController;

public class MachRYSettingController extends RYSettingPageFlowController
{
    @Service('MachRYSettingService')
    def svc;
    
    public def getService(){ return svc }
    public String getSettingType(){ return 'mach'}
    public String getSettingTitle(){ return 'Machine Revision Setting'}
    public String getPrefixId(){ return 'MRY'}
    String entityName = 'machrysetting';
    
    void loadItems(){
        assessLevels = svc.getAssessLevels( entity.objid )
        forexes      = svc.getForexes( entity.objid )
    }
    
    
    
    /*---------------------------------------------------------------------
    *
    * Forex Support
    *
    ---------------------------------------------------------------------*/
    def selectedForex 
    def forexes = []
    
    def forexListHandler  = [
        createItem : { return createForex() },
        getRows    : { return 100 },
                
        getColumns : { return [
            new Column(name:'year', caption:'Year*', type:'integer', format:'0000', editable:true ),
            new Column(name:'forex', caption:'Rate*', type:'decimal', editable:true, typeHandler:new DecimalColumnHandler(scale:4, format:'#,##0.0000')  ),
        ]},
        
        validate : { li -> 
            def forex = li.item 
            def duplicate = false;
            if (forex.isnew){
                duplicate = forexes.find{it.year == forex.year} != null
            }
            else {
                duplicate = forexes.findAll{it.year == forex.year}.size() > 1
            }
            if (duplicate) throw new Exception('Duplicate year is not allowed.')
            svc.saveForex( forex )
        },
                
        onAddItem  : { item -> forexes.add( item ) },
                
        onRemoveItem : { forex -> 
            if( isLastItem( forexes, forex ) && MsgBox.confirm('Delete selected forex?')) {
                svc.deleteForex( forex ) 
                forexes.remove( forex )
                return true
            }
            else{
                throw new Exception('Only the last item can be deleted.')
            }
            return false;
        },
                
        fetchList    : { return forexes },
    ] as EditorListModel


    

    /*---------------------------------------------------------------------
    *
    * Create Records Support
    *
    ---------------------------------------------------------------------*/
    Map createEntity() {
        return [
            objid   : 'MRY' + new UID(),
            ry      : null,
        ]
    }
    
    Map createAssessLevel() {
        return [ 
            objid           : 'AL' + new UID(),
            machrysettingid : entity.objid,
            fixrate         : true,
            rate            : 0.0,
        ]
    }
    
    Map createAssessLevelRange() {
        return [ 
            objid               : 'ALR' + new UID(),
            machassesslevelid   : selectedAssessLevel.objid,
            machrysettingid     : entity.objid,
            mvfrom              : null,
            mvto                : null,
            rate                : 0.0,
        ]
    }
    
    Map createForex() {
        return [
            objid               : 'FO' + new UID(),
            machrysettingid     : entity.objid,
            isnew               : true,
        ]
    }
    
}
