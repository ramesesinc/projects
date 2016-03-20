import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LookupReceiverModel implements SimpleLookupDataSource {
    
    @Caller 
    def caller; 
    
    @Service('EntityRelationService') 
    def relationSvc; 
    
    String searchText;
    LookupSelector selector;
    
    def title = 'Receiver Lookup'; 
    def list = [];
    def onselect;
    
    void init() { 
        if ( !sender?.objid ) throw new Exception('Please specify the sender information'); 
        
        loadRelations(); 
    }
    
    def getEntity() { 
        return caller?.entity; 
    } 
    def getSender() { 
        return entity?.sender; 
    } 
        
    def closeForm() {
        return '_close'; 
    }
    def addReceiver() { 
        def params = [ 
            entity: [
                objid: 'IND'+ new java.rmi.server.UID(), 
                entity: [objid: sender?.objid ] 
            ], 
            onselect: { o-> 
                if ( !o.entity ) { 
                    relationSvc.create([ 
                        entity   : [ objid: sender.objid ], 
                        relateto : [ objid: o.objid ] 
                    ]); 
                } 
                
                loadRelations(); 
                receiverListHandler.reload(); 
            }
        ];
        def opener = Inv.lookupOpener('individualentity:create', params);
        opener.target = 'self'; 
        return opener; 
    }
    
    void loadRelations() {
        list = relationSvc.getList([ entity: [objid: sender?.objid]]); 
    }
    
    def selectedReceiver;
    def receiverListHandler = [ 
        fetchList: { o-> 
            return list; 
        }, 
        onOpenItem: { o,name-> 
            if ( !o ) return null; 
            
            return selectReceiverImpl( selectedReceiver ); 
        }
    ] as BasicListModel; 
    
    def selectReceiver() { 
        if ( !selectedReceiver ) return null; 
        
        return selectReceiverImpl( selectedReceiver ); 
    }
    
    def selectReceiverImpl( o ) { 
        if ( !o.relation ) {
            boolean pass = false; 
            def h = { pass = true; } 
            Modal.show('entity:specify-relation', [entity: o, handler: h]); 
            if ( !pass ) return null; 
        }
        
        selector.select( o );  
        return '_close'; 
    } 
}
