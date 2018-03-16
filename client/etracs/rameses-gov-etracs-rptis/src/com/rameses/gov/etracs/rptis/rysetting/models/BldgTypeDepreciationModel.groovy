package com.rameses.gov.etracs.rptis.rysetting.models;
        
import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import java.rmi.server.UID
import com.rameses.gov.etracs.rptis.util.*

public class BldgTypeDepreciationModel 
{
    @Binding
    def binding
            
    def service
    def bldgType
    def mode 
    def selectedDepreciation
    def depreciations = []

    final void init() {
        depreciations = service.getDepreciations(bldgType)
    }
    
    void changeMode(mode){
        this.mode = mode
        binding.refresh()
    }
    
    def allowDepreciationColumnEdit = { item ->
        if( ! bldgType ) return false
        if( ! bldgType.objid ) return false 
        if( item.isnew ) return true
        if( isFirstItem( depreciations ) ) return true
        return isLastItem( depreciations, item )
    } as Map


    def depreciationListHandler = [
        createItem : { return createDepreciation() },
        getRows    : { return (depreciations.size() <= 10 ? 10 : depreciations.size()+1)},
        getColumns : { getColumns() },
                
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
            service.saveDepreciation(item)
        },
                
        onAddItem    : { item -> depreciations.add( item )  },
                
        onRemoveItem   : { item -> 
            if(MsgBox.confirm('Delete selected item?')  && isLastItem( depreciations, item )) {
                service.deleteDepreciation( item )
                depreciations.remove( item );
                return true;
            }
            return false;
        },
                
        fetchList    : { return depreciations  },
    ] as EditorListModel
    
    
    def getColumns(){
        def cols = [];
        cols << new Column(name:'agefrom', caption:'Age From', type:'integer')
        cols << new Column(name:'ageto', caption:'Age To', type:'integer', editable:true, editableWhen:'#{allowDepreciationColumnEdit[item]}' )
        if ( ! RPTUtil.isTrue(bldgType?.usecdu)){
            cols << new Column(name:'rate', caption:'Rate*', type:'decimal', format:'0.0000', editable:true )
        }
        else {
            cols << new Column(name:'excellent', caption:'Excellent', type:'decimal', format:'0.0000', editable:true )
            cols << new Column(name:'verygood', caption:'Very Good', type:'decimal', format:'0.0000', editable:true )
            cols << new Column(name:'good', caption:'Good', type:'decimal', format:'0.0000', editable:true, width:80 )
            cols << new Column(name:'average', caption:'Average', type:'decimal', format:'0.0000', editable:true )
            cols << new Column(name:'fair', caption:'Fair', type:'decimal', format:'0.0000', editable:true, width:80 )
            cols << new Column(name:'poor', caption:'Poor', type:'decimal', format:'0.0000', editable:true, width:80 )
            cols << new Column(name:'verypoor', caption:'Very Poor', type:'decimal', format:'0.0000', editable:true )
            cols << new Column(name:'unsound', caption:'Unsound', type:'decimal', format:'0.0000', editable:true, width:80 )
        }
        return cols;
    }
 
    Map createDepreciation(){
        return [
            objid           : RPTUtil.generateId('BD'),
            bldgtypeid      : bldgType?.objid,
            bldgrysettingid : bldgType?.bldgrysettingid,
            rate            : 0,
            excellent       : 0,
            verygood        : 0,
            good            : 0,
            average         : 0,
            fair            : 0,
            poor            : 0,
            verypoor        : 0,
            unsound         : 0,
            isnew           : true,
        ]
    }
        
    def isFirstItem( list ) {
        return list.size() == 0
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