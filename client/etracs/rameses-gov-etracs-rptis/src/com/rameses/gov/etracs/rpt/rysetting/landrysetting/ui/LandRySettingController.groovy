package com.rameses.gov.etracs.rpt.rysetting.landrysetting.ui;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID;
import com.rameses.gov.etracs.rpt.rysetting.ui.RYSettingPageFlowController;

public class LandRySettingController extends RYSettingPageFlowController
{
    @Service('LandRYSettingService')
    def svc
            
    String settingType  = 'land'
    String settingTitle = 'Land Revision Setting'
    String prefixId     = 'LD'
    String entityName = 'landrysetting';
    
    public def getService(){ return svc }
    
    public void loadItems(){
        assessLevels    = svc.getAssessLevels( entity.objid )
        landAdjustmentTypes = svc.getLandAdjustmentTypes( entity.objid )
    }
    
    
    /*==================================================================
    * 
    *  LCUV  SUPPORT
    *
    ==================================================================*/
    def classification 

    List getClassificationList(){
        return svc.getPropertyClassifications()
    }


    /*==================================================================
    * 
    *  SPECIFICCLASS  SUPPORT
    *
    ==================================================================*/

    def selectedSpecificClass
    def specificClasses = []
    def areaTypes = ['SQM', 'HA']
    
    
    def getLookupLandSpecificClass(){
        return Inv.lookupOpener('landspecificclass:lookup', [
           onselect : {
               selectedSpecificClass.landspecificclass = it;
               selectedSpecificClass.code = it.code;
               selectedSpecificClass.name = it.name;
           }
        ]);
    }

    def specificClassListHandler = [
        createItem : { return createSpecificClass() },
        getRows    : { return specificClasses.size() + 2 },
                
        getColumns : { return [
            new Column(name:'landspecificclass', caption:'Code*', maxWidth:60, editable:true, type:'lookup', handler:'lookupLandSpecificClass', expression:'#{item.landspecificclass.code}' ),
            new Column(name:'name', caption:'Name*', editable:false),
            new Column(name:'areatype', caption:'Area Type*', type:'combo', items:areaTypes , editable:true),
        ]},
                
        validate : { li -> 
            def sc = li.item 
            required( sc.code, 'Code' )
            required( sc.name, 'Name' )
            required( sc.areatype, 'Area Type' )
            checkDuplicate( specificClasses, 'Code', 'code', sc.objid, sc.code )
            checkDuplicate( specificClasses, 'Name', 'name', sc.objid, sc.name );

            if (!sc.objid) sc.objid = 'SPC' + new UID();
            svc.saveSpecificClass( sc )
        },
                
        onAddItem : { item -> specificClasses.add( item )},
                
        onRemoveItem : { item -> 
            if( MsgBox.confirm('Delete selected item?') ) {
                svc.deleteSpecificClass( item )
                specificClasses.remove( item )
                return true;
            }
            return false;
        },
                
        fetchList : { return specificClasses },
                
    ] as EditorListModel
    


    /*==================================================================
    * 
    *  SUBCLASS  SUPPORT
    *
    ==================================================================*/
    def selectedSubClass
    def subclasses = []

    def allowSubClassColumnEdit = { item ->
        if( !classification ) return false
        if( !selectedSpecificClass.objid ) return false
        return true
    } as Map


    def subClassListHandler = [
        createItem : { return createSubClass() },
        getRows    : { return 50 },
                
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:60, editable:true, editableWhen:'#{allowSubClassColumnEdit[item]}' ),
            new Column(name:'name', caption:'Name*', editable:true, editableWhen:'#{allowSubClassColumnEdit[item]}' ),
            new Column(name:'unitvalue', caption:'Unit Value*', type:'decimal', editable:true, editableWhen:'#{allowSubClassColumnEdit[item]}' ),
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
            def sc = li.item 
            required( sc.code, 'Code' )
            required( sc.name, 'Name' )
            if( sc.unitvalue == null ) 
                throw new Exception('Unit Value is required.')
            if( sc.unitvalue < 0.0 ) 
                throw new Exception('Unit Value must be greater than or equal to zero.')
                
            checkDuplicate( subclasses, 'Code', 'code', sc.objid, sc.code )
            checkDuplicate( subclasses, 'Name', 'name', sc.objid, sc.name )

            svc.saveSubClass( sc )
        },
                
        onAddItem    : { item -> subclasses.add( item ) },
                
        onRemoveItem   : { item -> 
            if( MsgBox.confirm('Delete selected item?') ) {
                svc.deleteSubClass( item )
                subclasses.remove( item )
                return true;
            }
            return false;
        },
                
        fetchList    : { return subclasses },
    ] as EditorListModel
    


    /*==================================================================
    * 
    *  STRIPPING  SUPPORT
    *
    ==================================================================*/
    def strippings = []

    def strippingListHandler = [
        createItem : { return createStripping() },
        getRows    : { return 50 },
                
        getColumns : { return [
            new Column(name:'striplevel', caption:'Stripping Level*', type:'integer', editable:true ),
            new Column(name:'rate', caption:'Rate (%)*', type:'decimal', editable:true ),
        ]},
                
        validate : { li -> 
            def st = li.item 
            required( st.striplevel, 'Strip Level' )
            checkDuplicate( strippings, 'Strip Level', 'striplevel', st.objid, st.striplevel )
            validateRate( st.rate )
            svc.saveStripping( st )
        },
                
        onAddItem    : { item -> strippings.add( item ) },
                
        onRemoveItem : { item -> 
            if (MsgBox.confirm('Delete selected item?')) {
                svc.deleteStripping( item )
                strippings.remove( item );
                return true;
            }
            return false;
        },
                
        fetchList    : { return strippings  },
    ] as EditorListModel
    
    
    

    /*==================================================================
    * 
    *  LANDADJUSTMENTTYPE  SUPPORT
    *
    ==================================================================*/
    def landAdjustmentTypes = []
    def selectedLandAdjustmentType
    
    
    def landAdjustmentTypeListHandler = [
        getRows    : { return landAdjustmentTypes.size()+1},
        getColumns : { return [
            new Column(name:'code', caption:'Code', maxWidth:60),
            new Column(name:'name', caption:'Name'),
            new Column(name:'appliedto', caption:'Applied To', maxWidth:150 ),
            new Column(name:'expr', caption:'Formula', width:200),
            new Column(name:'idx', caption:'Print Order', width:70, type:'integer'),
        ]},
        onRemoveItem   : { item -> return removeLandAdjustmentType( item ) },
        onOpenItem     : { item, colname -> openLandAdjustmentType() },
        fetchList      : { return landAdjustmentTypes },
    ] as BasicListModel 

    
    def addLandAdjustmentTypeHandler = { item -> 
        item.appliedto = item.classifications.classification.code.join(',')
        item.landrysettingid = entity.objid
        svc.saveLandAdjustmentType( item ) 
        if( ! landAdjustmentTypes ) {
            landAdjustmentTypes = []
        }
        landAdjustmentTypes.add( item )
        landAdjustmentTypeListHandler.reload()
    }
    

    def updateLandAdjustmentTypeHandler = { item -> 
        item.appliedto = item.classifications.classification.code.join(',')
        svc.saveLandAdjustmentType( item ) 
        landAdjustmentTypes.set( landAdjustmentTypes.indexOf( selectedLandAdjustmentType ), item )
        binding.refresh('selectedLandAdjustmentType')
    }

    
    def createLandAdjustmentType() {
        return InvokerUtil.lookupOpener('landadjustmenttype:create', [addHandler:addLandAdjustmentTypeHandler, adjustments:landAdjustmentTypes] )
    }

    
    def openLandAdjustmentType() {
        if( selectedLandAdjustmentType ) {
            def adjustment = svc.openLandAdjustmentType( selectedLandAdjustmentType.objid)
            return InvokerUtil.lookupOpener('landadjustmenttype:open', [entity:adjustment, updateHandler:updateLandAdjustmentTypeHandler, adjustments:landAdjustmentTypes] )
        }
    }
    

    void removeLandAdjustmentType() {
        if( selectedLandAdjustmentType ) {
            removeLandAdjustmentType( selectedLandAdjustmentType )
        }
    }
    
    
    def removeLandAdjustmentType( item ) {
        if( MsgBox.confirm('Remove item?') ) {
            svc.deleteLandAdjustmentType( selectedLandAdjustmentType )
            landAdjustmentTypes.remove( item )
            return true 
        }
        return false 
    }
    
    
    
    /*==================================================================
    * 
    *  CREATE ENTITY  SUPPORT
    *
    ==================================================================*/
    Map createEntity() {
        return [
            objid       : 'LRY' + new UID(),
            ry          : null,
            lgus        : [],
        ]
    }
    
    Map createAssessLevel() {
        return [ 
            objid           : 'AL' + new UID(),
            landrysettingid : entity?.objid,
            fixrate         : true,
            rate            : 0.0,
        ]
    }
    
    Map createAssessLevelRange() {
        return [ 
            objid               : 'ALR' + new UID(),
            landassesslevelid   : selectedAssessLevel?.objid,   
            landrysettingid     : entity?.objid,
            mvfrom              : null,
            mvto                : null,
            rate                : 0.0,
        ]
    }
    
    Map createSpecificClass() {
        return [ 
            landrysettingid   : entity?.objid,
            classification    : classification,
            areatype          : 'SQM',
        ]
    }
    
    Map createSubClass() {
        return [ 
            objid               : 'SC' + new UID(),
            specificclass       : selectedSpecificClass,
            landrysettingid     : entity?.objid,
            unitvalue           : 0.0,
            landrysettingid     : entity?.objid,
        ]
    }
    
    Map createStripping() {
        return [ 
            objid                   : 'ST' + new UID(),
            landrysettingid         : entity?.objid,
            classification_objid    : classification?.objid,
            rate                    : 0.0,
        ]
    }
    

    /*==================================================================
    * 
    *  DEPENDENCY SUPPORT
    *
    ==================================================================*/
    void setClassification(classification){
        this.classification = classification;
        specificClasses.clear()
        if (classification){
            specificClasses = svc.getSpecificClasses(entity.objid, classification.objid)
            specificClassListHandler.load()
            strippings = svc.getStrippings(entity.objid, classification.objid)
            strippingListHandler.load()
        }
    }

    void setSelectedSpecificClass( selectedSpecificClass){
        this.selectedSpecificClass = selectedSpecificClass
        subclasses.clear()
        if( selectedSpecificClass ){
            subclasses = svc.getSubClasses( selectedSpecificClass.objid )
            subClassListHandler.load()
        }
    }
    

}

