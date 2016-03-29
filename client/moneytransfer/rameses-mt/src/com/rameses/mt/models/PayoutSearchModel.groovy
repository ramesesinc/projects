import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.BreakException

class PayoutSearchModel extends PageFlowController {
    
    @Service('PayoutSearchService')
    def svc; 
    
    @Service("QueryService")
    def querySvc;    
        
    @Binding 
    def binding;
    
    def title = 'Payout Search'; 
    def query = [:];
    def entity = [:];
    def results = []; 
    
    @PropertyChangeListener 
    def changelistener = [
        'query.searchtype': {
            if ( query.searchtype=='BY_TXN_NUMBER') {
                binding.focus( 'query.txnno' ); 
            } else if ( query.searchtype=='BY_RECEIVER') { 
                binding.focus( 'query.receiverlastname' ); 
            }
        }
    ]; 
    
    def init() { 
        query.searchtype = 'BY_TXN_NUMBER'; 
        return super.signal('search'); 
    }
    
    void fetchList() {
        results = svc.getList( query ); 
        if ( !results ) throw new Exception('No matching record(s) found'); 
        
        if ( results.size()==1 ) {
            selectedItem = results.first();
        }
    }
    
    void searchByBarcode() {
        results.clear(); 
        
        def barcode = MsgBox.prompt("Enter Barcode");
        if ( !barcode ) throw new BreakException();
        
        def qry = [:];
        qry.findBy = [entityno: barcode];
        qry._schemaname = 'vw_entityindividual_lookup';
        def z = querySvc.findFirst( qry );
        if ( !z ) throw new Exception('Receiver record does not exist');
        
        query.searchtype = 'BY_RECEIVER'; 
        query.receiverlastname = z.lastname;
        query.receiverfirstname = z.firstname; 
        fetchList();
    }
    
    def selectedItem;
    def resulthandler = [
        fetchList: {
            return results; 
        } 
    ] as BasicListModel; 
    
    def opener; 
    void loadHandler() { 
        if ( !selectedItem ) throw new Exception('No available selected item');
        
        def shandler = 'default-payout';
        if ( selectedItem.handler ) {
            shandler = selectedItem.handler; 
        } else {
            selectedItem.handler = shandler;
        }
        
        def invhandler = null; 
        def handlername = shandler +':create';
        try { 
            invhandler = Inv.lookup( handlername ); 
        } catch(Throwable t) {
            //do nothing 
        }
        
        if ( !invhandler ) throw new Exception('failed to find the payout form handler for this record'); 
        
        opener = Inv.lookupOpener( handlername, [entity: selectedItem]); 
    }
    
    void close() {
        binding.fireNavigation('_exit'); 
    }
}
