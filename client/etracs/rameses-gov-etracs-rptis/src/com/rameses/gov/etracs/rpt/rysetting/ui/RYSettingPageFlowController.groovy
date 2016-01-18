package com.rameses.gov.etracs.rpt.rysetting.ui;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID

public abstract class RYSettingPageFlowController extends PageFlowController
{
    @Binding 
    def binding;
    
    @Invoker
    def invoker
            
    //handlers
    def onClose;
    ListModelHandler listModelHandler;
    
    
    def MODE_READ   = 'read';
    def MODE_EDIT   = 'edit';
    def MODE_CREATE = 'create';
    def mode = MODE_READ;
    
    def entity;
    def copyYear;
    
    
    public abstract def getService();
    public abstract String getSettingType();
    public abstract String getSettingTitle();
    public abstract String getPrefixId();
    
    public void loadItems(){}
    public void modeChanged(){}
    
    
    def create(){
        entity = createEntity();
        if (!entity) entity = [:];
        if (!entity.objid) entity.objid = prefixId + (new UID());
        mode = MODE_CREATE;
        if (service.settingExists())
            return super.start("copy");
        else
            return super.start("new");
    }
    
    def open() {
        entity = service.open( entity );
        loadItems();
        mode = MODE_READ;
        modeChanged();
        return super.start("main");
    }
    
    def copy(){
        entity = service.copy(entity.objid, entity.ry)
        return open()
    }
    
    void edit(){
        mode = MODE_EDIT;
        modeChanged();
    }
    
    void saveCreate(){
        entity = service.create(entity)
        mode = MODE_READ;
        modeChanged();
    }
    
    void saveUpdate(){
        entity = service.update(entity)
        mode = MODE_READ;
        modeChanged();
    }
    
    void cancelEdit(){
        entity = service.open(entity);
        loadItems();
        mode = MODE_READ;
    }
    
    void approve(){
        entity = service.approve(entity);
        mode = MODE_READ;
        modeChanged();
    }
    
    void delete(){
        service.delete(entity);
    }
    
    
    void createSetting(){
        entity = service.create(entity);
        loadItems();
        mode = MODE_EDIT;
    }
    
    void copySetting(){
        entity = svc.reviseSettings( entity.ry, copyYear );
        loadItems();
        mode = MODE_EDIT;
    }
    
    
    
    /*---------------------------------------------------------------------
    *
    * LGU Support 
    *
    ----------------------------------------------------------------------*/
    def lgu;
    def selectedLgu
            
    def onselectLgu = {data ->
        def brgy;
        if (data.barangay){
            brgy = entity.lgus.find{it.barangayid == data.barangay.objid}
            if (brgy) throw new Exception('Barangay ' + data.barangay.name + ' has already been added.')
        }
        else if( entity.lgus.find{ it.lguid == data.lgu.objid } != null ) {
            throw new Exception( data.lgu.name + ' has already been added.')
        }
        if (entity.lgus == null )
            entity.lgus =[]
        entity.lgus.add([
            objid       : 'RY' + new java.rmi.server.UID(),
            lguid       : data.lgu.objid,
            barangayid  : data.barangay?.objid,
            lguname     : (data.barangay ? data.lgu.name + ' - ' + data.barangay.name : data.lgu.name),
            settingtype : getSettingType(),
        ])
        lguListHandler.load()
    }
    
    def getLookupLgu() {
        return InvokerUtil.lookupOpener('rysettinglgu:lookup', [
            settingtype:getSettingType(), ry:entity.ry, onselect:onselectLgu
        ])
    }
    
    def lguListHandler = [
        getColumns : { return [
            new Column(name:'lguname', caption:'Name'),
        ]},
        fetchList : { return entity.lgus },
        onRemoveItem : { item ->
            if( MsgBox.confirm('Delete item?')) {
                entity.lgus.remove( item )
                return true;
            }
            return false;
        },
        
    ] as EditorListModel
                
                
                                
    /*==================================================================
    * 
    *  ASSESSLEVEL  SUPPORT
    *
    ==================================================================*/
    def selectedAssessLevel
    def assessLevels = [] 

    def classLookupOpener = InvokerUtil.lookupOpener('propertyclassification:lookup', 
        [
            onselect : { pc ->
                selectedAssessLevel.classification = pc
            },
            onempty : {
                selectedAssessLevel.classification = null
            }
        ]
    )

    def assessLevelListHandler = [
        createItem : { return createAssessLevel() },
                
        getRows    : { return 150 },
                
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:60, editable:true ),
            new Column(name:'name', caption:'Name*', editable:true ),
            new Column(name:'fixrate', caption:'Fix?*', type:'boolean', maxWidth:40, editable:true ),
            new Column(name:'rate', caption:'Rate (%)*', type:'decimal', editable:true ),
            new Column(caption:'Classification', type:'lookup', editable:true, handler: classLookupOpener, expression: '#{item.classification.code}' ),
        ]},
                
        validate : { li -> 
            def al = li.item
            required(al.code, 'Code')
            required(al.name, 'Name')
            validateRate( al.rate )
            required(al.classification, 'Classification')
            checkDuplicateAssessLevel( al )
            svc.saveAssessLevel( al ) 
        },
                
        onRemoveItem   : { item -> 
            if (MsgBox.confirm('Remove selected item?')){
                svc.deleteAssessLevel( selectedAssessLevel )
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
    


    void setSelectedAssessLevel(selectedAssessLevel){
        this.selectedAssessLevel = selectedAssessLevel
        assessLevelRanges.clear()
        if (selectedAssessLevel) {
            assessLevelRanges = svc.getRanges(selectedAssessLevel.objid)
        }
        rangeLevelListHandler.load()
    }
        
    /*==================================================================
    * 
    *  ASSESSLEVELRANGE  SUPPORT
    *
    ==================================================================*/
    def selectedRange
    def assessLevelRanges = []


    def allowRangeColumnEdit = { item ->
        if( ! selectedAssessLevel ) return false
        if( selectedAssessLevel.fixrate == true ) return false
        if( isFirstItem( assessLevelRanges ) ) return true
        if( isNewEntry( item.mvfrom ) ) return true
        return isLastItem( assessLevelRanges, item )
    } as Map
    

    def rangeLevelListHandler = [
        createItem : { return createAssessLevelRange() },
            
        getRows    : { return 50 },
                
        getColumns : { return [
            new Column(name:'mvfrom', caption:'MV greater than or equal', type:'decimal'),
            new Column(name:'mvto', caption:'MV less than', type:'decimal', editable:true, editableWhen:'#{allowRangeColumnEdit[item]}' ),
            new Column(name:'rate', caption:'Rate (%)*', type:'decimal', editable:true),
        ]},
                
        validate     : { li -> 
            def range = li.item

            if (range.mvfrom == null){
                range.mvfrom = 0.0

                def lastRange = null 
                if( assessLevelRanges ) {
                    lastRange = assessLevelRanges.last()
                }

                if (lastRange && range.objid != lastRange.objid ){
                    range.mvfrom = (lastRange ? lastRange.mvto : 0.0)

                    if( range.mvto < range.mvfrom && range.mvto != 0  ) 
                    throw new Exception('MV To must be greater than MV From')    
                }
            }
            validateRate( range.rate )
            svc.saveRange( range )
        },
                
        onAddItem    : { item -> 
            assessLevelRanges.add( item ) 
        },
                
        onRemoveItem   : { range -> 
            if( isLastItem( assessLevelRanges, range ) && MsgBox.confirm('Delete selected item?')) {
                svc.deleteRange( range )
                assessLevelRanges.remove( range )
                return true;
            }
            return false;
        },
                
        fetchList    : { return assessLevelRanges },
    ] as EditorListModel
    

    
    
    /*---------------------------------------------------------------------
    *
    * Common Methods 
    *
    ---------------------------------------------------------------------*/
    void required( value, caption ) {
        if( ! value ) throw new Exception(caption + ' is required.') 
    }
    
    void validateRate( rate ) {
        if( rate < 0.0 ) throw new Exception('Rate must be greater than or equal to zero.')
        if( rate > 100) throw new Exception('Rate must not exceed 100.00')
    }
    
        
    void checkDuplicate( list, caption, field, objid, value ) {
        def data = list.find{ it.objid != objid && it[field] == value }
        if( data ) throw new Exception('Duplicate ' + caption + ' is not allowed.')
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
    
    @Close
    public boolean closeForm() {
        if ( onClose ) onClose()
        return true 
    }
}
