package com.rameses.gov.etracs.rpt.rysetting.bldgrysetting.ui;
        
import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import java.rmi.server.UID

public abstract class BaseValueTypeController 
{
    @Binding
    def binding
            
    def bldgType
    def mode 
    def list = [] 
    def selectedItem
            
            
    public abstract def getService();
    public abstract Column[] getColumns();
    public void validateItem( item ){}
    
    final void init() {
        list = svc.getBldgKindBuccs( bldgType.objid ) 
    }
    
    void changeMode(mode){
        this.mode = mode
        binding.refresh()
    }
    
    /*---------------------------------------------------------------------------------
    *
    * List Support
    *
    *---------------------------------------------------------------------------------*/
    
    def bldgKindLookup = InvokerUtil.lookupOpener( 'bldgkind.lookup', [onselect:{selectedItem.bldgkind = it}])
    
    def listHandler = [
        createItem   : { return createEntity() },
        getRows      : { return list.size() + 1 },
        getColumns   : { return getColumns()},
                
        validate     : { li -> 
            required(li.item.bldgkind, 'Code')
            checkDuplicate(li.item);
            validateItem(li.item);
            svc.saveBldgKindBucc( li.item );
        },
                
        onRemoveItem : { item -> 
            if( MsgBox.confirm('Remove item?' ) ) {
                svc.deleteBldgKindBucc( item )
                list.remove( item )
                return true;
            }
            return false;
        },
                
        onAddItem    : { item ->  list.add( item ) },
        fetchList    : { return list }
    ] as EditorListModel
    
    void checkDuplicate( item ) {
        def data = list.find{ it.objid != item.objid && it.bldgkind.objid == item.bldgkind.objid  }
        if( data ) throw new Exception('Duplicate item ' + item.bldgkind_name + ' is not allowed.')    
    }
    
    void required( value, caption ) {
        if( ! value ) throw new Exception(caption + ' is required.') 
    }
    
    final Map createEntity() {
        return [
            objid           : 'BU' + new UID(),
            bldgrysettingid : bldgType.bldgrysettingid,
            bldgtypeid      : bldgType.objid,
            basevaluetype   : 'fix',
            basevalue       : 0.0,
            minbasevalue    : 0.0,
            maxbasevalue    : 0.0,
            gapvalue        : 0,
            minarea         : 0.0,
            maxarea         : 0.0,
        ]
    }
    
}