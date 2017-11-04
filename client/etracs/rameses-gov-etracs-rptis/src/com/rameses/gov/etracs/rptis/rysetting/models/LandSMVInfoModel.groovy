package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class LandSMVInfoModel implements SubPage
{
    @Binding
    def binding 
    
    //LandRYSettingService
    def service;
    
    def entity;
    def mode = 'read';
    
        
    void init(){
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    
    /*==================================================================
    * 
    *  LCUV  SUPPORT
    *
    ==================================================================*/
    def classification 

    List getClassificationList(){
        return service.getPropertyClassifications()
    }


    /*==================================================================
    * 
    *  SPECIFICCLASS  SUPPORT
    *
    ==================================================================*/

    def selectedSpecificClass
    def specificClasses = []
    def areaTypes = ['SQM', 'HA']
    
    def specificClassListHandler = [
        createItem : { return createSpecificClass() },
        getRows    : { return (specificClasses.size() <= 25 ? 25 : specificClasses.size() + 1) },
                
        getColumns : { return [
            new Column(name:'landspecificclass', caption:'Code*', maxWidth:80, editable:true, type:'lookup', handler:'landspecificclass:lookup', expression:'#{item.landspecificclass.code}' ),
            new Column(name:'landspecificclass.name', caption:'Name*', editable:false),
            new Column(name:'areatype', caption:'Area Type*', type:'combo', items:areaTypes , editable:true),
        ]},
                
        validate : { li -> 
            def sc = li.item 
            sc.code = sc.landspecificclass?.code 
            RPTUtil.required( sc.code, 'Code' )
            RPTUtil.required( sc.areatype, 'Area Type' )
            RPTUtil.checkDuplicate( specificClasses, 'Specific Class', 'code', sc.objid, sc.code )
            sc.putAll(service.saveSpecificClass( sc ))
            sc.committed = true;
        },
                
        onAddItem : { item -> specificClasses.add( item )},
                
        onRemoveItem : { item -> 
            if( MsgBox.confirm('Delete selected item?') ) {
                service.deleteSpecificClass( item )
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
        if( !selectedSpecificClass) return false
        if( selectedSpecificClass.committed == false) return false
        return true
    } as Map


    def subClassListHandler = [
        createItem : { return createSubClass() },
        getRows    : { return (subclasses.size() <= 25 ? 25 : subclasses.size() + 1) },
                
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:80, editable:true, editableWhen:'#{allowSubClassColumnEdit[item]}', textCase:'UPPER' ),
            new Column(name:'name', caption:'Name*', editable:true, editableWhen:'#{allowSubClassColumnEdit[item]}', textCase:'UPPER' ),
            new Column(name:'unitvalue', caption:'Unit Value*', type:'decimal', editable:true, editableWhen:'#{allowSubClassColumnEdit[item]}' ),
        ]},
                
        onColumnUpdate : { item, colName -> 
            if (colName=='code'){
                updateSubClassName(item)
            }
        },
                
        validate : { li -> 
            def sc = li.item 
            RPTUtil.required( sc.code, 'Code' )
            RPTUtil.required( sc.name, 'Name' )
            if( sc.unitvalue == null ) 
                throw new Exception('Unit Value is required.')
            if( sc.unitvalue < 0.0 ) 
                throw new Exception('Unit Value must be greater than or equal to zero.')
                
            RPTUtil.checkDuplicate( subclasses, 'Code', 'code', sc.objid, sc.code )
            RPTUtil.checkDuplicate( subclasses, 'Name', 'name', sc.objid, sc.name )

            sc.putAll(service.saveSubClass( sc ));
        },
                
        onAddItem    : { item -> subclasses.add( item ) },
                
        onRemoveItem   : { item -> 
            if( MsgBox.confirm('Delete selected item?') ) {
                service.deleteSubClass( item )
                subclasses.remove( item )
                return true;
            }
            return false;
        },
                
        fetchList    : { return subclasses },
    ] as EditorListModel
    
    
    void updateSubClassName(item){
        if (item.code && item.code.matches('.*[0-9]')){
            def num = item.code.replaceAll('[^0-9]', '')
            if (num){
                if (num.matches('11|12|13')) item.name = num + 'TH CLASS'
                else if (num[-1] == '1') item.name = num + 'ST CLASS'
                else if (num[-1] == '2') item.name = num + 'ND CLASS'
                else if (num[-1] == '3') item.name = num + 'RD CLASS'
                else item.name = num + 'TH CLASS'
            }
        }
    }
    


    /*==================================================================
    * 
    *  STRIPPING  SUPPORT
    *
    ==================================================================*/
    def strippings = []

    def strippingListHandler = [
        createItem : { return createStripping() },
        getRows    : { return (strippings.size() <= 25 ? 25 : strippings.size() + 1) },
                
        getColumns : { return [
            new Column(name:'striplevel', caption:'Stripping Level*', type:'integer', editable:true ),
            new Column(name:'rate', caption:'Rate (%)*', type:'decimal', editable:true ),
        ]},
                
        validate : { li -> 
            def st = li.item 
            RPTUtil.required( st.striplevel, 'Strip Level' )
            RPTUtil.checkDuplicate( strippings, 'Strip Level', 'striplevel', st.objid, st.striplevel )
            validateRate( st.rate )
            st.putAll(service.saveStripping( st ))
        },
                
        onAddItem    : { item -> strippings.add( item ) },
                
        onRemoveItem : { item -> 
            if (MsgBox.confirm('Delete selected item?')) {
                service.deleteStripping( item )
                strippings.remove( item );
                return true;
            }
            return false;
        },
                
        fetchList    : { return strippings  },
    ] as EditorListModel
 
    
    Map createSpecificClass() {
        return [ 
            objid            : 'SPC' + new java.rmi.server.UID(),
            landrysettingid   : entity?.objid,
            classification    : classification,
            areatype          : 'SQM',
            committed         : false,
        ]
    }
    
    Map createSubClass() {
        def code = selectedSpecificClass.landspecificclass.code + '' + (subclasses.size() + 1)
        def item = [ 
            objid               : 'SC' + new java.rmi.server.UID(),
            code                : code, 
            specificclass       : selectedSpecificClass,
            landrysettingid     : entity?.objid,
            unitvalue           : 0.0,
            landrysettingid     : entity?.objid,
        ]
        updateSubClassName(item)
        return item
    }
    
    Map createStripping() {
        return [ 
            objid                   : 'ST' + new java.rmi.server.UID(),
            landrysettingid         : entity?.objid,
            classification          : classification,
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
            specificClasses = service.getSpecificClasses(entity, classification)
            specificClassListHandler.load()
            strippings = service.getStrippings(entity, classification)
            strippingListHandler.load()
        }
    }

    void setSelectedSpecificClass( selectedSpecificClass){
        this.selectedSpecificClass = selectedSpecificClass
        subclasses.clear()
        if( selectedSpecificClass ){
            subclasses = service.getSubClasses( selectedSpecificClass)
            subClassListHandler.load()
        }
    }
 
    void validateRate( rate ) {
        if( rate < 0.0 ) throw new Exception('Rate must be greater than or equal to zero.')
        if( rate > 100) throw new Exception('Rate must not exceed 100.00')
    }    
}