package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class AssessLevelModel implements SubPage
{
    @Binding
    def binding 
    
    def service;
    
    def entity;
    def mode = 'read';
    def selectedLevel;
    def assessLevels = [];
    
    
    void init(){
        assessLevels = service.getAssessLevels(entity);
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    
    def assessLevelListHandler = [
        createItem : { return createAssessLevel() },
                
        getRows    : { return (assessLevels.size() <= 25 ? 25 : assessLevels.size()+1) },

        validate : { li -> 
            def al = li.item;
            RPTUtil.required('Code', al.code);
            RPTUtil.required('Name', al.name);
            validateRate( al.rate );
            RPTUtil.required('Classification', al.classification);
            checkDuplicateAssessLevel( al );
            al.putAll(service.saveAssessLevel( al ));
        },
                
        onRemoveItem   : { item -> 
            if (MsgBox.confirm('Remove selected item?')){
                service.removeAssessLevel( selectedLevel )
                assessLevels.remove( item );
                assessLevelRanges.clear();
                rangeLevelListHandler.load();
                return true;
            }
            return false;
        },
        onAddItem      : { item -> assessLevels.add( item ) },
        fetchList      : { return assessLevels },
    ] as EditorListModel
    

    void checkDuplicateAssessLevel( al ) {
        def item = assessLevels.find{ it.objid != al.objid && (it.code == al.code || it.name == al.name )  }
        if( item ) throw new Exception('Duplicate item is not allowed.')    
    }
    
    
    void setSelectedLevel(selectedLevel){
        this.selectedLevel = selectedLevel
        assessLevelRanges.clear()
        if (selectedLevel) {
            assessLevelRanges = service.getRanges(selectedLevel)
        }
        rangeLevelListHandler.load()
    }
    
    Map createAssessLevel() {
        def al =[:];
        al.objid = 'AL' + new java.rmi.server.UID();
        al[entity.type+'rysettingid']  = entity.objid;
        al.fixrate = true;
        al.rate    = 0.0;
        return al;
    }
    
        
    /*==================================================================
    * 
    *  ASSESSLEVELRANGE  SUPPORT
    *
    ==================================================================*/
    def selectedRange
    def assessLevelRanges = []

    def allowRangeColumnEdit = { item ->
        if( ! selectedLevel ) return false
        if( selectedLevel.fixrate == true ) return false
        if( isFirstItem( assessLevelRanges ) ) return true
        if( isNewEntry( item.mvfrom ) ) return true
        return isLastItem( assessLevelRanges, item )
    } as Map
    

    def rangeLevelListHandler = [
        createItem : { return createAssessLevelRange() },
            
        getRows    : { return (assessLevelRanges.size() <= 25 ? 25 : assessLevelRanges.size() + 1)},
        
        getColumns : { return [
            new Column(name:'mvfrom', caption:'MV greater than or equal', type:'decimal'),
            new Column(name:'mvto', caption:'MV less than', type:'decimal', editable:true, editableWhen:'#{allowRangeColumnEdit[item]}' ),
            new Column(name:'rate', caption:'Rate (%)*', type:'decimal', editable:true),
        ]},
        
        validate     : { li -> 
            def range = li.item
            if( range.mvto < range.mvfrom && range.mvto != 0  ){
                throw new Exception('MV To must be greater than MV From')    
            } 
                
            validateRate( range.rate );
            range.putAll(service.saveRange( range ));
        },
                
        onAddItem    : { item -> 
            assessLevelRanges.add( item ) 
        },
                
        onRemoveItem   : { range -> 
            if( isLastItem( assessLevelRanges, range ) && MsgBox.confirm('Delete selected item?')) {
                service.deleteRange( range )
                assessLevelRanges.remove( range )
                return true;
            }
            return false;
        },
                
        fetchList    : { return assessLevelRanges },
    ] as EditorListModel
    
    Map createAssessLevelRange() {
        def r = [:];
        r.objid       = 'AR' + new java.rmi.server.UID();
        r[entity.type+'assesslevelid'] = selectedLevel.objid;
        r[entity.type+'rysettingid']  = entity.objid;
        r.mvfrom      = 0.0;
        r.mvto        = null;
        r.rate        =  null;
        
        if (assessLevelRanges.size() >= 1){
            def last = assessLevelRanges.last();
            r.mvfrom = last.mvto;
        }
        return r;
    }    
    
    void validateRate( rate){
        if (rate == null) throw new Exception('Rate is required.');
        if (rate < 0.0) throw new Exception('Rate must be greater than or equal to zero.');
    }
    
    def isFirstItem( list ) {
        return list.size() == 0
    }    
    
    def isNewEntry( value ) {
        return value == null 
    }    
    
    def isLastItem( list, item ) {
        def index = list.indexOf( item )
        if( index < 0 ) 
            index = list.size()
        else
            index += 1 
        return list.size() == index 
    }
        
}