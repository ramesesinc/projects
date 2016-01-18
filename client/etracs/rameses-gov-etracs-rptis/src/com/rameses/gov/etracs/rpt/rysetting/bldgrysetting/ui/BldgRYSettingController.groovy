package com.rameses.gov.etracs.rpt.rysetting.bldgrysetting.ui;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;
import com.rameses.gov.etracs.rpt.rysetting.ui.RYSettingPageFlowController;
        
public class BldgRYSettingController extends RYSettingPageFlowController
{
    @Service('BldgRYSettingService')
    def svc 
            
    String settingType  = 'bldg'
    String settingTitle = 'Building Revision Setting'
    String prefixId     = 'BD'
    String entityName = 'bldgrysetting';
    
    @PropertyChangeListener
    def listener = [
        'selectedBldgType.residualrate' : {
            svc.saveBldgType(selectedBldgType);
        }
    ]
    
    public def getService(){ return svc }
    
    void loadItems() {
        assessLevels        = svc.getAssessLevels( entity.objid );
        additionalItems     = svc.getAdditionalItems( entity.objid );
        bldgTypes           = svc.getBldgTypes(entity.objid);
    }
    
        
    /*---------------------------------------------------------------------
    *
    * BldgAdditionalItem Support
    *
    ---------------------------------------------------------------------*/
    def additionalItems = []
    def selectedAdditionalItem
    def searchtext;
    
    void searchAdditionalItems(){
        additionalItems = svc.findAdditionalItems( [objid:entity.objid, searchtext:searchtext] );
        additionalItemListHandler?.reload();
    }
    
    
    def additionalItemListHandler = [
        getRows    : { return additionalItems.size() + 1 },
        getColumns : { return [
            new Column(name:'code', caption:'Code', maxWidth:60),
            new Column(name:'name', caption:'Name'),
            new Column(name:'unit', caption:'Unit', maxWidth:100 ),
            new Column(name:'expr', caption:'Expression'),
            new Column(name:'type', caption:'Type'),
        ]},
        onRemoveItem   : { item -> doRemoveAdditionalItem( item ) },
        fetchList      : { return additionalItems },
    ] as BasicListModel 
    
    
    
    def addAdditionalItemHandler = { item -> 
        item.bldgrysettingid = entity.objid
        svc.saveAdditionalItem( item ) 
        additionalItems.add( item )
        additionalItemListHandler.load()
    }
    

    def updateAdditionalItemHandler = { item -> 
        svc.saveAdditionalItem( item ) 
        additionalItems.set( additionalItems.indexOf( selectedAdditionalItem), item )
        binding.refresh('selectedAdditionalItem')
    }
    

    def createAdditionalItem() {
        return InvokerUtil.lookupOpener('bldgadditionalitem:create', [addHandler:addAdditionalItemHandler] )
    }
    
    def openAdditionalItem() {
        if( selectedAdditionalItem ){
            return InvokerUtil.lookupOpener('bldgadditionalitem:open', [entity:selectedAdditionalItem, updateHandler:updateAdditionalItemHandler] )
        }
    }
    
    void removeAdditionalItem() {
        if( selectedAdditionalItem ) {
            doRemoveAdditionalItem( selectedAdditionalItem )
        }
    }
    
    
    void doRemoveAdditionalItem( item ) {
        if( MsgBox.confirm('Remove item?') ) {
            svc.deleteAdditionalItem( selectedAdditionalItem )
            additionalItems.remove( item )
        }
    }
    
    
    
    
    
    /*---------------------------------------------------------------------
    *
    * BldgType Support
    *
    ---------------------------------------------------------------------*/
    def bldgTypes = [] 
    def selectedBldgType
    
    void setSelectedBldgType( selectedBldgType ) {
        this.selectedBldgType = selectedBldgType
        baseValueType = selectedBldgType?.basevaluetype
        initBaseValueHandler()
        binding.refresh('baseValueType') 

        depreciations       = svc.getDepreciations(selectedBldgType?.objid)
        storeyAdjustments   = svc.getStoreyAdjustments(selectedBldgType?.objid)
        
        depreciationListHandler.load();
        multiStoreyListHandler.load();
    }
    
    def bldgTypeListHandler = [
        createItem : { return createBldgType() },
        getRows    : { return 50 },
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:60, editable:true ),
            new Column(name:'name', caption:'Building Type*', editable:true ),
        ]},
                
        validate       : { li -> 
            def bldgType = li.item
            required( bldgType.code, 'Code' )
            required( bldgType.name, 'Bldg Type' )
            checkDuplicateBldgType( bldgType )
            if ( !bldgType.objid ) bldgType.objid = RPTUtil.generateId('BT')
            svc.saveBldgType( bldgType )
        },
                
        onRemoveItem   : { bldgType -> 
            if( MsgBox.confirm('Remove item?') ) {
                svc.deleteBldgType( bldgType )
                bldgTypes.remove( bldgType )
                return true;
            }
            return false;
        },
                
        onAddItem      : { item -> bldgTypes.add( item ) },
        fetchList      : { return bldgTypes },
    ] as EditorListModel
    
    
    void checkDuplicateBldgType( bldgType ) {
        def item = bldgTypes.find{ it.objid != bldgType.objid && it.code == bldgType.code  }
        if( item ) throw new Exception('Duplicate code is not allowed.')    
        item = bldgTypes.find{ it.objid != bldgType.objid && it.name == bldgType.name }
        if( item ) throw new Exception('Duplicate name is not allowed.')    
    }
    

    /*---------------------------------------------------------------------
    *
    * Multi-Storey Adjustment Support
    *
    ---------------------------------------------------------------------*/
    def selectedStorey;
    def storeyAdjustments = []
            
    def allowStoreyColumnEdit = { item ->
        if( ! selectedBldgType ) return false
        if( ! selectedBldgType.objid ) return false
        if( isFirstItem( storeyAdjustments ) ) return true
        if( isNewEntry( item.floorno ) ) return true
        return isLastItem( storeyAdjustments, item )
    } as Map
    

    def multiStoreyListHandler = [
        createItem : { return createMultiStoreyAdjustment() },
        getRows    : { return 50 },
        getColumns : { return [
            new Column(name:'floorno', caption:'Floor No.', type:'integer'),
            new Column(name:'rate', caption:'Rate*', type:'decimal', editable:true, editableWhen:'#{allowStoreyColumnEdit[item]}' ),
        ]},
                
        validate     : { li -> 
            def item = li.item 
            def lastItem = null 
            if( storeyAdjustments ) {
                lastItem = storeyAdjustments.get( storeyAdjustments.size() - 1 )
            }
            if( ! item.floorno ) item.floorno = (lastItem ? RPTUtil.toInteger(lastItem.floorno) + 1 : 1)

            validateRate( item.rate )
            svc.saveStoreyAdjustment( item )
        },
                
        onAddItem    : { item -> storeyAdjustments.add( item ) },
        
        onRemoveItem : { item -> 
            if(MsgBox.confirm('Delete selected item?') && isLastItem( storeyAdjustments, item )) {
                svc.deleteStoreyAdjustment(item)
                storeyAdjustments.remove( item );
                return true;
            }
            return false;
        },
                
        fetchList    : { return storeyAdjustments  },
    ] as EditorListModel
    
    

    /*---------------------------------------------------------------------
    *
    * Depreciation Support
    *
    ---------------------------------------------------------------------*/
    def selectedDepreciation;
    def depreciations = []

    def allowDepreciationColumnEdit = { item ->
        if( ! selectedBldgType ) return false
        if( ! selectedBldgType.objid ) return false 
        if( isFirstItem( depreciations ) ) return true
        if( isNewEntry( item.agefrom ) ) return true
        return isLastItem( depreciations, item )
    } as Map


    def depreciationListHandler = [
        createItem : { return createDepreciation() },
        getRows    : { return 75 },
        getColumns : { return [
            new Column(name:'agefrom', caption:'Age From', type:'integer'),
            new Column(name:'ageto', caption:'Age To', type:'integer', editable:true, editableWhen:'#{allowDepreciationColumnEdit[item]}' ),
            new Column(name:'rate', caption:'Rate*', type:'decimal', format:'0.000000', editable:true ),
        ]},
                
        validate     : { li -> 
            def item = li.item 
            def lastItem = null 
            if( depreciations ) {
                lastItem = depreciations.get( depreciations.size() - 1 )
            }
            if( ! item.agefrom ) {
                item.agefrom = (lastItem ? lastItem.ageto + 1 : 1)
            }

            if( item.ageto < item.agefrom && item.ageto != 0  ) throw new Exception('Age To must be greater than Age From.')
            validateRate( item.rate )
            svc.saveDepreciation(item)
        },
                
        onAddItem    : { item -> depreciations.add( item )  },
                
        onRemoveItem   : { item -> 
            if(MsgBox.confirm('Delete selected item?')  && isLastItem( depreciations, item )) {
                svc.deleteDepreciation( item )
                depreciations.remove( item );
                return true;
            }
            return false;
        },
                
        fetchList    : { return depreciations  },
    ] as EditorListModel
    
    
    /*---------------------------------------------------------------------
    *
    * BldgKind Support
    *
    ---------------------------------------------------------------------*/
    def baseValueType
    def baseValueHandler
    def selectedBaseValue
    
    void setBaseValueType( baseValueType ) {
        this.baseValueType = baseValueType
        if( selectedBldgType && selectedBldgType.objid ) {
            selectedBldgType.basevaluetype = baseValueType
            svc.saveBldgType( selectedBldgType )
            initBaseValueHandler()
        }
    }
    
    void initBaseValueHandler() {
        baseValueHandler = null
        if( baseValueType ) {
            def openerType = 'basevaluetype:' + baseValueType
            baseValueHandler = InvokerUtil.lookupOpener(openerType, [bldgType:selectedBldgType, mode:mode] )
        }
    }
    
    
    List getBaseValueTypes() {
        return ['fix','range','gap']
    }
    
    
    
    /*---------------------------------------------------------------------
    *
    * Create Records Support
    *
    ---------------------------------------------------------------------*/
    Map createEntity() {
        return [
            objid       : RPTUtil.generateId('BRY'),
            ry          : null,
            predominant : 1,
            depreciatecoreanditemseparately     : 0,
            computedepreciationbasedonschedule  : 0,
            calcbldgagebasedondtoccupied        : 0,
            straightdepreciation                : 1,
        ]
    }
    
    Map createAssessLevel() {
        return [ 
            objid           : RPTUtil.generateId('AL'),
            bldgrysettingid : entity.objid,
            fixrate         : true,
            rate            : 0.0,
            ranges          : [],
        ]
    }
    
    Map createAssessLevelRange() {
        return [ 
            objid               : RPTUtil.generateId('BR'),
            bldgassesslevelid   : selectedAssessLevel?.objid,
            bldgrysettingid     : entity.objid,
            mvfrom              : null,
            mvto                : null,
            rate                : 0.0,
        ]
    }
    
    Map createBldgType() {
        return [
            code            : null,
            name            : null,
            bldgrysettingid : entity.objid,
            basevaluetype   : 'fix', 
            residualrate    : 0.0,
            multistoreyadjustments : [],
            depreciations : [],
        ]
    }
    
    Map createMultiStoreyAdjustment(){
        return [
            objid           : RPTUtil.generateId('MS'),
            bldgtypeid      : selectedBldgType?.objid,
            bldgrysettingid : entity.objid,
            floorno         : null, 
            rate            : 0.0
        ]
    }

    Map createDepreciation(){
        return [
            objid           : RPTUtil.generateId('BD'),
            bldgtypeid      : selectedBldgType?.objid,
            bldgrysettingid : entity.objid,
        ]
    }
    
    
    void modeChanged(){
        if( baseValueHandler ) {
            baseValueHandler.handle.changeMode(mode)
        }
    }
    
    
}


